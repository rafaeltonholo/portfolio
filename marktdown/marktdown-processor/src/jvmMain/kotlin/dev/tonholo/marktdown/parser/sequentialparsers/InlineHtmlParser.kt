//package dev.tonholo.marktdown.parser.sequentialparsers
//
//import org.intellij.markdown.MarkdownTokenTypes
//import org.intellij.markdown.parser.sequentialparsers.RangesListBuilder
//import org.intellij.markdown.parser.sequentialparsers.SequentialParser
//import org.intellij.markdown.parser.sequentialparsers.TokensCache
//
//class InlineHtmlParser : SequentialParser {
//    override fun parse(tokens: TokensCache, rangesToGlue: List<IntRange>): SequentialParser.ParsingResult {
//        var result = SequentialParser.ParsingResultBuilder()
//        val delegateIndices = RangesListBuilder()
//        var iterator: TokensCache.Iterator = tokens.RangesListIterator(rangesToGlue)
//
//        while (iterator.type != null) {
//            if (iterator.type == MarkdownTokenTypes.LT && iterator.rawLookup(steps = 1) == MarkdownTokenTypes.HTML_TAG) {
//                val start = iterator.index
//                while ()
//            }
//        }
//
//        return result.withFurtherProcessing(delegateIndices.get())
//    }
//}
