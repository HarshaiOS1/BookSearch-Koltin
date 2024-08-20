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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.company.booksearch.utils.NetworkObserver
import com.company.booksearch.viewModel.BookViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(navController: NavController, viewModel: BookViewModel) {
    val context = LocalContext.current
    val networkObserver = remember { NetworkObserver(context) }
    val isConnected by networkObserver.isConnected

    var searchQuery by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    val scaffoldState = rememberBottomSheetScaffoldState(
        SheetState(
            skipHiddenState = true,
            skipPartiallyExpanded = false,
            initialValue = SheetValue.PartiallyExpanded
        )
    )
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
            HomeTopBar()
        },
    ) {
        Column {
            SearchSection(
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it },
                onSearchClick = {
                    viewModel.searchBooks(searchQuery)
                    focusManager.clearFocus()
                },
                onToggleFavourites = { viewModel.toggleShowFavourites() },
                showFavourites = viewModel.showFavourites,
                isConnected = isConnected
            )
            if (!isConnected) {
                Text(
                    text = "No Internet..!",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    textAlign = TextAlign.Left
                )
            }
            Divider(modifier = Modifier.height(2.dp))

            if (viewModel.error.isNotEmpty()) {
                ErrorSection(viewModel.error)
            } else {
                BookListSection(
                    navController = navController,
                    viewModel = viewModel
                )
            }
        }
    }
}

/**
 * Composable for the Top Bar of the Home Page.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar() {
    TopAppBar(
        title = {
            Text(
                text = "Books",
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
                modifier = Modifier.fillMaxWidth()
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = Color.White
        ),
        windowInsets = TopAppBarDefaults.windowInsets
    )
}

/**
 * Composable for the search section which includes the search input and filter buttons.
 */
@Composable
fun SearchSection(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearchClick: () -> Unit,
    onToggleFavourites: () -> Unit,
    showFavourites: Boolean,
    isConnected: Boolean
) {
    val focusManager = LocalFocusManager.current

    Column {
        TextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            label = { Text("Search Books Online..") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions { focusManager.clearFocus() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp)
        )
        Row {
            Button(
                onClick = onSearchClick,
                enabled = (isConnected && searchQuery.length > 2),
                modifier = Modifier
                    .padding(5.dp)
                    .weight(1f)

            ) {
                Text("Search")
            }
            Button(
                onClick = onToggleFavourites,
                modifier = Modifier
                    .padding(5.dp)
                    .weight(1f)
            ) {
                Text(if (showFavourites) "Show All Books" else "Show Favourite Books")
            }
        }
    }
}


/**
 * Composable to display error messages if the search or data loading fails.
 */
@Composable
fun ErrorSection(errorMessage: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = errorMessage,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error
        )
    }
}

/**
 * Composable to display the list of books along with a header separating the fresh search results and previous results.
 */
@Composable
fun BookListSection(navController: NavController, viewModel: BookViewModel) {
    val top10Books = viewModel.filteredBooks.take(10)
    val previousBooks = viewModel.filteredBooks.drop(10)

    LazyColumn(
        modifier = Modifier
            .padding(bottom = 56.dp)
    ) {
        // Display top 10 fresh results
        items(top10Books) { book ->
            BookItem(book, onClick = {
                viewModel.selectBook(book.id)
                navController.navigate("detail")
            })
        }

        // Display header if there are previous search results
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

            // Display previous search results
            items(previousBooks) { book ->
                BookItem(book, onClick = {
                    viewModel.selectBook(book.id)
                    navController.navigate("detail")
                })
            }
        }
    }
}