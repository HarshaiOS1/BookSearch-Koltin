package com.company.booksearch.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/** The BookViewModelFactory class provides a way to create instances of the BookViewModel,
 * ensuring that the application context is passed correctly.*/
class BookViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookViewModel::class.java)) {
            return BookViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
