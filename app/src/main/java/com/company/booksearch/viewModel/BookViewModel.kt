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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
The BookViewModel manages the data and handles UI-related logic for book-related operations.
This ViewModel interacts with the local database via the BookDao and also fetches data from an external Google Books API.*/
@HiltViewModel
class BookViewModel @Inject constructor(
    private val database: BookDatabase
) : ViewModel() {
    //Provides access to the DAO for performing database operations.
    private val bookDao: BookDao = database.bookDao()

    //An instance of the API service used to fetch data from the Google Books API.
    private val googleBooksApi = GoogleBooksApi.create()

    //Holds the list of books currently available in the ViewModel.
    var books by mutableStateOf(listOf<Book>())
        private set

    //Holds the currently selected book based on user interactions.
    var selectedBook: Book? by mutableStateOf(null)
        private set

    //Controls whether the UI should show only favorite books
    var showFavourites by mutableStateOf(false)
        private set

    //Stores any error messages that may arise during data operations.
    var error by mutableStateOf("")
        private set

    //Dynamically filters books based on the showFavourites flag and the search query (filterSearchQuery).
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

    // Holds the search query used to filter books in the UI.
    var filterSearchQuery by mutableStateOf("")

    // The init block initializes the ViewModel by loading the books from the local database when the ViewModel is created.
    init {
        loadBooksFromDatabase()
    }

    /** Loads all books from the local database into the books list.
     * Sets the error property in case of any issues during data loading*/
    private fun loadBooksFromDatabase() {
        viewModelScope.launch {
            try {
                books = bookDao.getAllBooks()
            } catch (e: Exception) {
                error = "Error loading books from database"
            }
        }
    }

    /** Fetches books from the Google Books API based on the provided search query.
    Maps the API response to the Book model, then inserts the fetched books into the local database.
    Reloads the books from the database after insertion.
    Handles errors by setting the error property.
     */
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

    //    Toggles the showFavourites flag, which determines whether only favorite books should be displayed.
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
