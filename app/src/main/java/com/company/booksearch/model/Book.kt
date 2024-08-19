package com.company.booksearch.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Book Data model class used to parse data from api
 * */
@Entity(tableName = "books")
data class Book(
    @PrimaryKey val id: String,
    val title: String,
    val author: String,
    val description: String,
    val imageUrl: String,
    var isFavorite: Boolean = false
)