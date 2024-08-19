package com.company.booksearch.viewModel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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

    fun searchBooks(query: String) {
        viewModelScope.launch {
            try {
                val response = googleBooksApi.searchBooks(query)
                books = response.items?.map { item ->
                    Book(
                        id = item.id.toIntOrNull() ?: 0,
                        title = item.volumeInfo.title,
                        author = item.volumeInfo.authors?.joinToString(", ") ?: "Unknown",
                        imageUrl = item.volumeInfo.imageLinks?.thumbnail ?: "",
                        description = item.volumeInfo.description ?: "No description available",
                        isFavorite = false
                    )
                } ?: emptyList()
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    // Function to select a book by its ID
    fun selectBook(bookId: Int) {
        selectedBook = books.find { it.id == bookId }
    }

    // Function to toggle the favorite status of a book
    fun toggleFavorite(book: Book) {
        book.isFavorite = !book.isFavorite
        books = books.map { if (it.id == book.id) book else it }
    }
}
