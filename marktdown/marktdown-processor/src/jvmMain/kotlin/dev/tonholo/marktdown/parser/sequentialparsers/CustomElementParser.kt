package dev.tonholo.marktdown.parser.sequentialparsers

import dev.tonholo.marktdown.flavours.MarktdownElementTypes
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.parser.sequentialparsers.RangesListBuilder
import org.intellij.markdown.parser.sequentialparsers.SequentialParser
import org.intellij.markdown.parser.sequentialparsers.TokensCache

class CustomElementParser : SequentialParser {
    override fun parse(tokens: TokensCache, rangesToGlue: List<IntRange>): SequentialParser.ParsingResult {
        val result = SequentialParser.ParsingResultBuilder()
        val delegateIndices = RangesListBuilder()
        var iterator: TokensCache.Iterator = tokens.RangesListIterator(rangesToGlue)

        while (iterator.type != null) {
            if (iterator.type == MarkdownTokenTypes.LT &&
                iterator.rawLookup(steps = 1) == MarkdownTokenTypes.LPAREN
            ) {
                val startIndex = iterator.index
                iterator = iterator.advance() // Skip the opening bracket
                val endIterator = findOfSize(iterator.advance())
                if (endIterator != null) {
                    if (iterator.length == 1) {
                        result.withNode(
                            SequentialParser.Node(
                                range = startIndex..endIterator.index + 1,
                                type = MarktdownElementTypes.CUSTOM_ELEMENT,
                            )
                        )
                    } else {
                        result.withNode(
                            SequentialParser.Node(
                                range = startIndex..endIterator.index + 1,
                                type = MarktdownElementTypes.CUSTOM_ELEMENT,
                            )
                        )
                        iterator = endIterator.advance()
                        continue
                    }
                }
            }

            delegateIndices.put(iterator.index)
            iterator = iterator.advance()
        }

        return result.withFurtherProcessing(delegateIndices.get())
    }

    private fun findOfSize(it: TokensCache.Iterator): TokensCache.Iterator? {
        var iterator = it
        while (iterator.type != null) {
            if (iterator.type == MarkdownTokenTypes.RPAREN &&
                iterator.charLookup(steps = 1) == '/' &&
                iterator.rawLookup(steps = 2) == MarkdownTokenTypes.GT
            ) {
                // Advance 1 to skip '/'
                // Advance 2 to skip '>'
                return iterator.advance().advance()
            }
            iterator = iterator.advance()
        }
        return null
    }
}
