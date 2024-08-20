package com.company.booksearch.model.dao

import androidx.room.*
import com.company.booksearch.model.Book

@Dao

interface BookDao {
    /**
     * Retrieves all books from the database, ordered by the timestamp in descending order.
     *
     * @return A list of all books.
     */
    @Query("SELECT * FROM books ORDER BY timestamp DESC")
    suspend fun getAllBooks(): List<Book>

    /**
     * Retrieves a specific book by its ID.
     *
     * @param bookId The ID of the book to retrieve.
     * @return The book with the specified ID, or null if not found.
     */
    @Query("SELECT * FROM books WHERE id = :bookId")
    suspend fun getBookById(bookId: String): Book?

    /**
     * Inserts a list of books into the database. If a conflict occurs, the existing book is replaced.
     *
     * @param books The list of books to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooks(books: List<Book>)

    /**
     * Updates the details of an existing book in the database.
     *
     * @param book The book to update.
     */
    @Update
    suspend fun updateBook(book: Book)
}