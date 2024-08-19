package com.company.booksearch.views

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.company.booksearch.viewModel.BookViewModel

@Composable
fun BookApp(viewModel: BookViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomePage(navController, viewModel)
        }
        composable("detail") {
            BookDetailPage(viewModel, navController)
        }
    }
}