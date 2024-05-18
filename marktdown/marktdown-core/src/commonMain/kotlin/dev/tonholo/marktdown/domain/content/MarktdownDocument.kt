package dev.tonholo.marktdown.domain.content

import dev.tonholo.marktdown.domain.MarktdownMetadata
import dev.tonholo.marktdown.domain.MarktdownTableOfContent

data class MarktdownDocument(
    val metadata: MarktdownMetadata?,
    val children: List<MarktdownElement>,
    val tableOfContent: MarktdownTableOfContent = MarktdownTableOfContent(),
    val linkDefinitions: List<LinkDefinition> = emptyList(),
)
