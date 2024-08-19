package com.company.booksearch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Surface
import com.company.booksearch.ui.theme.BookSearchTheme
import com.company.booksearch.viewModel.BookViewModel
import com.company.booksearch.views.BookApp


class MainActivity : ComponentActivity() {
    private val viewModel: BookViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BookSearchTheme {
                Surface {
                    BookApp(viewModel)
                }
            }
        }
    }
}