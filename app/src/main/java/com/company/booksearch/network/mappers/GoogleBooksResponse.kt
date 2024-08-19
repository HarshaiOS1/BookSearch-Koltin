package com.company.booksearch.network.mappers
import kotlinx.serialization.Serializable

@Serializable
data class GoogleBooksResponse(
    val items: List<BookItem>?
)

@Serializable
data class BookItem(
    val id: String,
    val volumeInfo: VolumeInfo
)

@Serializable
data class VolumeInfo(
    val title: String,
    val authors: List<String>?,
    val description: String?,
    val imageLinks: ImageLinks?
)

@Serializable
data class ImageLinks(
    val thumbnail: String
)
