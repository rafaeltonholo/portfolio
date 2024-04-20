package dev.tonholo.marktdown.domain

import kotlinx.datetime.LocalDateTime

data class MarktdownMetadata(
    val documentTitle: String,
    val authors: List<Author>,
    val publishedDateTime: LocalDateTime,
    val documentDescription: String? = null,
    val crossPost: MarktdownLink? = null,
    val tags: List<MarktdownTag> = emptyList(),
    val lastUpdateDateTime: LocalDateTime = publishedDateTime,
    val postThumbnail: MarktdownLink? = null,
)

data class Author(
    val name: String,
    val links: List<MarktdownLink> = emptyList(),
    val avatar: MarktdownLink? = null,
)
