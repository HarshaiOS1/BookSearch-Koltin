package com.company.booksearch.viewModel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.booksearch.data.BookDatabase
import com.company.booksearch.model.Book
import com.company.booksearch.model.dao.BookDao
import com.company.booksearch.network.apis.GoogleBooksApi
import kotlinx.coroutines.launch

/**
 * BooksVieModel holding books data which drives the ui
 * this also has multiple functions to get perform operation
 * will be updated as it grows, lets test with dummy book
 * */
class BookViewModel(application: Application) : AndroidViewModel(application) {

    private val bookDao: BookDao = BookDatabase.getDatabase(application).bookDao()
    private val googleBooksApi = GoogleBooksApi.create()
    var books by mutableStateOf(listOf<Book>())
        private set
    var selectedBook: Book? by mutableStateOf(null)
        private set
    var showFavourites by mutableStateOf(false)
        private set
    var error by mutableStateOf("")
        private set
    val filteredBooks: List<Book>
        get() = if (showFavourites) {
            books.filter {
                it.isFavorite &&
                        (it.title.contains(filterSearchQuery, ignoreCase = true) ||
                                it.author.contains(filterSearchQuery, ignoreCase = true))
            }
        } else {
            books.filter {
                it.title.contains(filterSearchQuery, ignoreCase = true) ||
                        it.author.contains(filterSearchQuery, ignoreCase = true)
            }
        }
    var filterSearchQuery by mutableStateOf("")

    init {
        loadBooksFromDatabase()
    }

    private fun loadBooksFromDatabase() {
        viewModelScope.launch {
            try {
                books = bookDao.getAllBooks()
            } catch (e: Exception) {
                error = "Error loading books from database"
            }
        }
    }

    fun searchBooks(query: String) {
        viewModelScope.launch {
            try {
                val response = googleBooksApi.searchBooks(query)
                val newBooks = response.items?.map { item ->
                    Book(
                        id = item.id,
                        title = item.volumeInfo.title,
                        author = item.volumeInfo.authors?.joinToString(", ") ?: "Unknown",
                        imageUrl = item.volumeInfo.fixedThumbnailUrl ?: "",
                        description = item.volumeInfo.description ?: "No description available",
                        isFavorite = false
                    )
                } ?: emptyList()

                bookDao.insertBooks(newBooks)
                loadBooksFromDatabase()
            } catch (e: Exception) {
                error = e.localizedMessage ?: "Error Fetching Books"
            }
        }
    }

    // Function to select a book by its ID
    fun selectBook(bookId: String) {
        viewModelScope.launch {
            selectedBook = bookDao.getBookById(bookId)
        }
    }

    fun toggleShowFavourites() {
        showFavourites = !showFavourites
    }

    // Function to toggle the favorite status of a book
    fun toggleFavorite(book: Book) {
        viewModelScope.launch {
            book.isFavorite = !book.isFavorite
            bookDao.updateBook(book)
            books = books.map { if (it.id == book.id) book else it }
        }
    }
}
