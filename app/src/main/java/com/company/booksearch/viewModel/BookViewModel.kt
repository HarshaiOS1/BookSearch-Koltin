package com.company.booksearch.viewModel
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.company.booksearch.model.Book

/**
 * BooksVieModel holding books data which drives the ui
 * this also has multiple functions to get perform operation
 * will be updated as it grows, lets test with dummy book
 * */
class BookViewModel : ViewModel() {

    private val _books = mutableStateListOf<Book>()
    val books: List<Book> = _books

    // Selected books for the detail screen
    var selectedBook by mutableStateOf<Book?>(null)
        private set

    init {
        // Dummy data for the books
        _books.addAll(
            listOf(
                Book(1, "Book One", "Author A", "This is a description for Book One."),
                Book(2, "Book Two", "Author B", "This is a description for Book Two."),
                Book(3, "Book Three", "Author C", "This is a description for Book Three.")
            )
        )
    }

    fun selectBook(bookId: Int) {
        selectedBook = _books.find { it.id == bookId }
    }
}
