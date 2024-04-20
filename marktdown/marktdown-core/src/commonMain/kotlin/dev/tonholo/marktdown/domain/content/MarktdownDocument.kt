package dev.tonholo.marktdown.domain.content

import dev.tonholo.marktdown.domain.MarktdownMetadata

data class MarktdownDocument(
    val metadata: MarktdownMetadata,
    val children: List<MarktdownElement>,
) : MarktdownRenderer

data class RootElement(
    override val children: List<MarktdownElement>,
) : MarktdownParent<MarktdownElement>

internal fun RootElement.toDocument(
    metadata: MarktdownMetadata,
): MarktdownDocument =
    MarktdownDocument(
        metadata = metadata,
        children = children.toList(),
    )

