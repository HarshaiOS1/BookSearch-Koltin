package com.company.booksearch.model.dao

import androidx.room.*
import com.company.booksearch.model.Book

@Dao

interface BookDao {
    @Query("SELECT * FROM books ORDER BY timestamp DESC")
    suspend fun getAllBooks(): List<Book>

    @Query("SELECT * FROM books WHERE id = :bookId")
    suspend fun getBookById(bookId: String): Book?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooks(books: List<Book>)

    @Update
    suspend fun updateBook(book: Book)
}