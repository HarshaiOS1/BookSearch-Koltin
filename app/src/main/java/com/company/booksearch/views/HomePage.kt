package com.company.booksearch.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.company.booksearch.viewModel.BookViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun HomePage(navController: NavController, viewModel: BookViewModel) {
    var searchQuery by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val scaffoldState = rememberBottomSheetScaffoldState()

    BottomSheetScaffold(
        scaffoldState = scaffoldState,

        sheetContent = {
            FilterBottomSheet(
                filterSearchQuery = viewModel.filterSearchQuery,
                onSearchQueryChange = { query ->
                    viewModel.filterSearchQuery = query
                }
            )
        },
        sheetPeekHeight = 56.dp,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Books",
                        textAlign = TextAlign.Center,
                        fontSize = 24.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                modifier = Modifier,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White,
                    navigationIconContentColor = Color.White
                ),
                windowInsets = TopAppBarDefaults.windowInsets
            )
        },
    ) {
        Column {
            Column {
                TextField(
                    value = searchQuery,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions {
                        focusManager.clearFocus()
                    },
                    onValueChange = { searchQuery = it },
                    label = { Text("Search Books Online..") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp)
                )
                Row {
                    Button(
                        onClick = {
                            viewModel.searchBooks(searchQuery)
                            focusManager.clearFocus()
                        },
                        modifier = Modifier
                            .padding(5.dp)
                            .weight(1f)
                    ) {
                        Text("Search")
                    }
                    Button(
                        onClick = { viewModel.toggleShowFavourites() },
                        modifier = Modifier
                            .padding(5.dp)
                            .weight(1f),
                    ) {
                        Text(text = if (viewModel.showFavourites) "Show All Books" else "Show Favourite Books")
                    }
                }
            }

            Divider(
                modifier = Modifier
                    .height(2.dp)
            )
            if (viewModel.error.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = viewModel.error,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            } else {
                val recentSearchedBooks = viewModel.filteredBooks.take(10)
                val previousBooks = viewModel.filteredBooks.drop(10)
                LazyColumn {
                    items(recentSearchedBooks) { book ->
                        BookItem(book, onClick = {
                            viewModel.selectBook(book.id)
                            navController.navigate("detail")
                        })
                    }

                    if (previousBooks.isNotEmpty()) {
                        item {
                            Text(
                                text = "Previous Search Results",
                                style = MaterialTheme.typography.headlineSmall,
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }
                        items(previousBooks) { book ->
                            BookItem(book, onClick = {
                                viewModel.selectBook(book.id)
                                navController.navigate("detail")
                            })
                        }
                    }
                }
            }
        }
    }
}