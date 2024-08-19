package com.company.booksearch.model

/**
 * Book Data model class used to parse data from api
 * */
data class Book(
    val id: Int,
    val title: String,
    val author: String,
    val description: String,
    val imageUrl: String,
    var isFavorite: Boolean = false
)