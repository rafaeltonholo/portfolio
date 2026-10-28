package dev.tonholo.marktdown.domain

data class MarktdownTableOfContent(
    val items: Set<MarktdownTableOfContentItem> = emptySet(),
)

data class MarktdownTableOfContentItem(
    val id: String,
    val title: String,
    val children: Set<MarktdownTableOfContentItem> = emptySet(),
)
