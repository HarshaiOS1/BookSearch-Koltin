package com.company.booksearch.views

import androidx.compose.runtime.Composable
import com.company.booksearch.viewModel.BookViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

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