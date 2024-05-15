package dev.tonholo.marktdown.domain

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

interface MarktdownMetadata {
    val title: String
    val authors: List<Author>
    val publishedDateTime: LocalDateTime
    val description: String? get() = null
    val crossPost: MarktdownLink? get() = null
    val tags: List<MarktdownTag> get() = emptyList()
    val lastUpdateDateTime: LocalDateTime get() = publishedDateTime
    val postThumbnail: MarktdownLink? get() = null
}

class MarktdownMetadataMap(
    map: Map<String, Any>,
) : MarktdownMetadata {
    override val title: String by map
    override val authors: List<Author> by lazy {
        requireNotNull(map["authors"] as? List<*>).map { authors ->
            val authorMap = requireNotNull(authors as? Map<*, *>)
                .mapKeys { (key, _) -> key.toString() }
            Author(map = authorMap)
        }
    }
    override val publishedDateTime: LocalDateTime by map
    override val description: String? by lazy {
        map["description"]?.toString()
    }
    override val crossPost: MarktdownLink? by lazy {
        map["crossPost"]?.let { MarktdownLink(it.toString()) }
    }
    override val tags: List<MarktdownTag> by lazy {
        (map["tags"] as? List<*>)?.map {
            MarktdownTag(it.toString())
        } ?: emptyList()
    }
    override val lastUpdateDateTime: LocalDateTime by lazy {
        map["lastUpdateDateTime"] as? LocalDateTime ?: publishedDateTime
    }
    override val postThumbnail: MarktdownLink? by lazy {
        map["postThumbnail"]?.let { MarktdownLink(it.toString()) }
    }
}

data class MarktdownMetadataImpl(
    override val title: String,
    override val authors: List<Author>,
    override val publishedDateTime: LocalDateTime,
    override val description: String? = null,
    override val crossPost: MarktdownLink? = null,
    override val tags: List<MarktdownTag> = emptyList(),
    override val lastUpdateDateTime: LocalDateTime = publishedDateTime,
    override val postThumbnail: MarktdownLink? = null,
) : MarktdownMetadata {
    companion object {
        operator fun invoke(): MarktdownMetadataImpl = MarktdownMetadataImpl(
            title = "",
            authors = emptyList(),
            publishedDateTime = Clock.System.now().toLocalDateTime(TimeZone.UTC)
        )
    }
}

data class Author(
    val name: String,
    val links: List<MarktdownLink> = emptyList(),
    val avatar: MarktdownLink? = null,
) {
    companion object {
        operator fun invoke(map: Map<String, Any?>): Author {
            val name = requireNotNull(map["name"]?.toString())
            val links = (map["links"] as? List<*>)?.map { MarktdownLink(it.toString()) }.orEmpty()
            val avatar = map["avatar"]?.toString()?.let(::MarktdownLink)
            return Author(
                name = name,
                links = links,
                avatar = avatar,
            )
        }
    }
}
