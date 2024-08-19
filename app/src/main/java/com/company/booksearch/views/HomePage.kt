package com.company.booksearch.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.company.booksearch.model.Book
import com.company.booksearch.viewModel.BookViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(navController: NavController, viewModel: BookViewModel) {
    var searchQuery by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Column {
        TopAppBar(
            title = {
                Text(
                    text = "Search Book",
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            actions = {
                IconButton(onClick = { onFilterClick(navController) }) {
                    Icon(imageVector = Icons.Default.List, contentDescription = "Filter Books")
                }
            },
            modifier = Modifier,
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Blue,
                titleContentColor = Color.White,
                actionIconContentColor = Color.White,
                navigationIconContentColor = Color.White
            ),
            windowInsets = TopAppBarDefaults.windowInsets
        )
        Column {
            TextField(
                value = searchQuery,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions {
                    focusManager.clearFocus()
                },
                onValueChange = { searchQuery = it },
                label = { Text("Search Books") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp)
            )
            Button(
                onClick = {
                    viewModel.searchBooks(searchQuery)
                    focusManager.clearFocus()
                },
                modifier = Modifier
                    .padding(2.dp)
                    .fillMaxWidth()
            ) {
                Text("Search")
            }
        }
//            LazyColumn(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp)
//        ) {
//            items(viewModel.books) { book ->
//                BookItem(book) {
//                    viewModel.selectBook(book.id)
//                    navController.navigate("detail")
//                }
//                Spacer(modifier = Modifier.height(16.dp))
//            }
//        }

        LazyColumn {
            items(viewModel.books) { book ->
                BookItem(book, onClick = {
                    viewModel.selectBook(book.id)
                    navController.navigate("detail")
                })

            }
        }
    }
}

fun onFilterClick(navController: NavController) {

//    navController.navigate("filter")
}

