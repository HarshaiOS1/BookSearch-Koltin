package com.company.booksearch.model.dao

import androidx.room.*
import com.company.booksearch.model.Book

@Dao
interface BookDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooks(books: List<Book>)

//    @Query("SELECT * FROM books WHERE id = :id LIMIT 1")
//    suspend fun getBookById(id: String): Book?
//
//    @Query("SELECT * FROM books")
//    suspend fun getAllBooks(): List<Book>
//
//    @Update
//    suspend fun updateBook(book: Book)
}