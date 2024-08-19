package com.company.booksearch.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.booksearch.model.Book
import com.company.booksearch.network.apis.GoogleBooksApi
import kotlinx.coroutines.launch

/**
 * BooksVieModel holding books data which drives the ui
 * this also has multiple functions to get perform operation
 * will be updated as it grows, lets test with dummy book
 * */
class BookViewModel : ViewModel() {
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
            books.filter { it.isFavorite &&
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

    fun searchBooks(query: String) {
        viewModelScope.launch {
            try {
                val response = googleBooksApi.searchBooks(query)
                books = response.items?.map { item ->
                    Book(
                        id = item.id,
                        title = item.volumeInfo.title,
                        author = item.volumeInfo.authors?.joinToString(", ") ?: "Unknown",
                        imageUrl = item.volumeInfo.fixedThumbnailUrl ?: "",
                        description = item.volumeInfo.description ?: "No description available",
                        isFavorite = false
                    )
                } ?: emptyList()
            } catch (e: Exception) {
                error = e.localizedMessage ?: "Error Fetching Books"
            }
        }
    }

    // Function to select a book by its ID
    fun selectBook(bookId: String) {
        selectedBook = books.find { it.id == bookId }
    }

    fun toggleShowFavourites() {
        showFavourites = !showFavourites
    }

    // Function to toggle the favorite status of a book
    fun toggleFavorite(book: Book) {
        book.isFavorite = !book.isFavorite
        books = books.map { if (it.id == book.id) book else it }
    }
}
