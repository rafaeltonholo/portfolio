package dev.tonholo.a2ui.express

private val keywordLiterals = setOf("true", "false", "null", "_")

/** Turns Express tokens into assignment statements. */
internal class Parser(private val tokens: List<Token>) {
    private var position = 0

    fun statements(): List<Statement> = buildList {
        while (current.type != TokenType.End) {
            if (current.type == TokenType.NewLine) advance() else add(statement())
        }
    }

    private val current: Token get() = tokens[position]

    private fun advance(): Token = tokens[position].also { position++ }

    private fun expect(type: TokenType, description: String): Token {
        if (current.type != type) throw ExpressException(current.line, "Expected $description")
        return advance()
    }

    private fun statement(): Statement {
        val target = advance()
        if (current.type != TokenType.Equals) {
            throw ExpressException(target.line, "Expected an assignment such as name = Component(...)")
        }
        advance()
        val value = expression()
        expect(TokenType.NewLine, "a new line after the assignment")
        return when (target.type) {
            TokenType.Identifier -> Statement.Component(target.text, value, target.line)
            TokenType.Path if target.text.startsWith("/") -> Statement.Data(target.text, value, target.line)
            else -> throw ExpressException(target.line, "Only names and absolute $/paths can be assigned")
        }
    }

    private fun expression(): Expression {
        val token = advance()
        return when (token.type) {
            TokenType.Text -> Expression.Text(token.text, token.line)
            TokenType.Number -> Expression.Number(token.text, token.line)
            TokenType.Path -> Expression.Path(token.text, token.line)
            TokenType.OpenBracket -> Expression.ListOf(items(TokenType.CloseBracket) { expression() }, token.line)
            TokenType.OpenBrace -> Expression.MapOf(items(TokenType.CloseBrace) { entry() }, token.line)
            TokenType.Identifier -> identifier(token)
            else -> throw ExpressException(token.line, "Expected a value")
        }
    }

    private fun identifier(token: Token): Expression = when {
        token.text in keywordLiterals -> keywordLiteral(token)
        current.type == TokenType.OpenParen -> call(token)
        else -> Expression.Reference(token.text, token.line)
    }

    private fun keywordLiteral(token: Token): Expression = when (token.text) {
        "true" -> Expression.Bool(value = true, line = token.line)
        "false" -> Expression.Bool(value = false, line = token.line)
        "null" -> Expression.Null(token.line)
        else -> Expression.Skip(token.line)
    }

    private fun call(name: Token): Expression.Call {
        advance()
        val arguments = items(TokenType.CloseParen) { argument() }
        return Expression.Call(
            name = name.text,
            positional = arguments.filter { it.first == null }.map { it.second },
            keywords = arguments.mapNotNull { (keyword, value) -> keyword?.let { it to value } },
            line = name.line,
        )
    }

    private fun argument(): Pair<String?, Expression> {
        val isKeyword = current.type == TokenType.Identifier && tokens[position + 1].type == TokenType.Equals
        if (!isKeyword) return null to expression()
        val keyword = advance().text
        advance()
        return keyword to expression()
    }

    private fun entry(): Pair<String, Expression> {
        val key = advance()
        if (key.type != TokenType.Identifier && key.type != TokenType.Text) {
            throw ExpressException(key.line, "Expected a map key")
        }
        expect(TokenType.Colon, "':' after the map key ${key.text}")
        return key.text to expression()
    }

    private fun <T> items(closing: TokenType, item: () -> T): List<T> = buildList {
        while (current.type != closing) {
            add(item())
            if (current.type != TokenType.Comma) break
            advance()
        }
        expect(closing, "a closing ${closingSymbol(closing)}")
    }

    private fun closingSymbol(type: TokenType): String = when (type) {
        TokenType.CloseParen -> ")"
        TokenType.CloseBracket -> "]"
        else -> "}"
    }
}
