package com.company.booksearch.views

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.company.booksearch.viewModel.BookViewModel

/**
 * The main composable function for the BookApp, which sets up the navigation
 * structure of the application using Jetpack Compose's Navigation component.
 *
 * @param viewModel The [BookViewModel] instance used to provide data and manage
 * UI logic across the screens.
 */
@Composable
fun BookApp(viewModel: BookViewModel) {
    val navController = rememberNavController()

    /**
     * Sets up the navigation graph for the application.
     * The starting destination is the "home" screen.
     */
    NavHost(navController = navController, startDestination = "home") {
        // Defines the "home" route that loads the HomePage composable.
        composable("home") {
            HomePage(navController, viewModel)
        }
        // Defines the "detail" route that loads the BookDetailPage composable.
        composable("detail") {
            BookDetailPage(viewModel, navController)
        }
    }
}