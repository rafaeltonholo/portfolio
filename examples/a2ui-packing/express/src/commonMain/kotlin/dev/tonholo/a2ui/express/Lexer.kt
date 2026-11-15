package dev.tonholo.a2ui.express

internal enum class TokenType {
    Identifier, Text, Number, Path,
    OpenParen, CloseParen, OpenBracket, CloseBracket, OpenBrace, CloseBrace,
    Comma, Colon, Equals, NewLine, End,
}

internal data class Token(val type: TokenType, val text: String, val line: Int)

private val punctuation = mapOf(
    '(' to TokenType.OpenParen, ')' to TokenType.CloseParen,
    '[' to TokenType.OpenBracket, ']' to TokenType.CloseBracket,
    '{' to TokenType.OpenBrace, '}' to TokenType.CloseBrace,
    ',' to TokenType.Comma, ':' to TokenType.Colon, '=' to TokenType.Equals,
)
private val opening = setOf(TokenType.OpenParen, TokenType.OpenBracket, TokenType.OpenBrace)
private val closing = setOf(TokenType.CloseParen, TokenType.CloseBracket, TokenType.CloseBrace)
private val escapes = mapOf('n' to '\n', 't' to '\t', '\\' to '\\', '"' to '"')

/**
 * Splits an Express block into tokens. Newlines end a statement only outside brackets, so a single assignment
 * can span several lines.
 */
internal class Lexer(private val source: String) {
    private var position = 0
    private var line = 1
    private var depth = 0
    private val tokens = mutableListOf<Token>()

    fun tokens(): List<Token> {
        while (position < source.length) readNext()
        tokens += Token(TokenType.NewLine, "", line)
        tokens += Token(TokenType.End, "", line)
        return tokens
    }

    private fun readNext() {
        val char = source[position]
        when {
            char == '\n' -> readNewLine()
            char.isWhitespace() -> position++
            char == '"' -> readText()
            char == '$' -> readPath()
            char.isDigit() || char == '.' || (char in "+-" && peek(1)?.isDigit() == true) -> readNumber()
            char.isLetter() || char == '_' -> readIdentifier()
            char in punctuation -> readPunctuation(punctuation.getValue(char))
            else -> throw ExpressException(line, "Unexpected character '$char'")
        }
    }

    private fun peek(offset: Int): Char? = source.getOrNull(position + offset)

    private fun readNewLine() {
        if (depth == 0) tokens += Token(TokenType.NewLine, "", line)
        line++
        position++
    }

    private fun readPunctuation(type: TokenType) {
        if (type in opening) depth++
        if (type in closing) depth = (depth - 1).coerceAtLeast(0)
        tokens += Token(type, source[position].toString(), line)
        position++
    }

    private fun readText() {
        val startLine = line
        val text = StringBuilder()
        position++
        while (true) {
            val char = source.getOrNull(position) ?: throw ExpressException(startLine, "Unterminated string")
            position++
            when (char) {
                '"' -> break
                '\\' -> text.append(readEscape(startLine))
                else -> text.append(char).also { if (char == '\n') line++ }
            }
        }
        tokens += Token(TokenType.Text, text.toString(), startLine)
    }

    private fun readEscape(startLine: Int): Char {
        val escaped = source.getOrNull(position) ?: throw ExpressException(startLine, "Unterminated string")
        position++
        return escapes[escaped] ?: throw ExpressException(line, "Unknown escape \\$escaped")
    }

    private fun readPath() {
        position++
        val path = takeWhile { it.isLetterOrDigit() || it in "_/-." }
        if (path.isEmpty()) throw ExpressException(line, "Expected a data path after $")
        tokens += Token(TokenType.Path, path, line)
    }

    private fun readNumber() {
        val literal = takeWhile { it.isDigit() || it in "+-.eE" }
        if (literal.toDoubleOrNull() == null) throw ExpressException(line, "Invalid number $literal")
        tokens += Token(TokenType.Number, literal, line)
    }

    private fun readIdentifier() {
        tokens += Token(TokenType.Identifier, takeWhile { it.isLetterOrDigit() || it == '_' }, line)
    }

    private inline fun takeWhile(predicate: (Char) -> Boolean): String {
        val start = position
        while (position < source.length && predicate(source[position])) position++
        return source.substring(start, position)
    }
}
