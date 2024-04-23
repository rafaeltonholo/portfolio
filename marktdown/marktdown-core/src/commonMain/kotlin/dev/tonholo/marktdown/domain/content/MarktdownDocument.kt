package dev.tonholo.marktdown.domain.content

import dev.tonholo.marktdown.domain.MarktdownMetadata

data class MarktdownDocument(
    val metadata: MarktdownMetadata?,
    val children: List<MarktdownElement>,
    val linkDefinitions: List<LinkDefinition> = emptyList(),
) : MarktdownRenderer
