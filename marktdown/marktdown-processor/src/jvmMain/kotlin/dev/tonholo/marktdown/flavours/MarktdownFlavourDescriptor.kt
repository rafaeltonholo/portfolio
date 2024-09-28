package dev.tonholo.marktdown.flavours

import dev.tonholo.marktdown.parser.sequentialparsers.CustomElementParser
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.flavours.gfm.GFMFlavourDescriptor
import org.intellij.markdown.parser.sequentialparsers.EmphasisLikeParser
import org.intellij.markdown.parser.sequentialparsers.SequentialParser
import org.intellij.markdown.parser.sequentialparsers.SequentialParserManager
import org.intellij.markdown.parser.sequentialparsers.impl.AutolinkParser
import org.intellij.markdown.parser.sequentialparsers.impl.BacktickParser
import org.intellij.markdown.parser.sequentialparsers.impl.EmphStrongDelimiterParser
import org.intellij.markdown.parser.sequentialparsers.impl.ImageParser
import org.intellij.markdown.parser.sequentialparsers.impl.InlineLinkParser
import org.intellij.markdown.parser.sequentialparsers.impl.ReferenceLinkParser

class MarktdownFlavourDescriptor : GFMFlavourDescriptor() {
    override val sequentialParserManager = object : SequentialParserManager() {
        override fun getParserSequence(): List<SequentialParser> = listOf(
            CustomElementParser(),
            AutolinkParser(listOf(MarkdownTokenTypes.AUTOLINK)),
            BacktickParser(),
            ImageParser(),
            InlineLinkParser(),
            ReferenceLinkParser(),
            EmphasisLikeParser(EmphStrongDelimiterParser()),
//            InlineHtmlParser(),
        )
    }
}
