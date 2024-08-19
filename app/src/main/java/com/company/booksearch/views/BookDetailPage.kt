package com.company.booksearch.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import com.company.booksearch.viewModel.BookViewModel
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailPage(viewModel: BookViewModel, navController: NavHostController) {
    val book = viewModel.selectedBook

    if (book != null) {
        var isFavorite by remember { mutableStateOf(book.isFavorite) }
        LaunchedEffect(book.isFavorite) {
            isFavorite = book.isFavorite
        }
        Column {
            TopAppBar(
                title = {
                    Text(
                        text = "Book Details",
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Blue,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White,
                    navigationIconContentColor = Color.White
                ),
                windowInsets = TopAppBarDefaults.windowInsets
            )
            Row(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .height(270.dp)
            ) {
                AsyncImage(
                    model = book.imageUrl,
                    contentDescription = "Book Cover",
                    modifier = Modifier
                        .fillMaxWidth(fraction = 0.5f)
                        .fillMaxHeight()
                        .padding(2.dp)
                )

                Spacer(modifier = Modifier.width(5.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.5f)
                    ) {
                        Text(
                            text = "Title: ${book.title}",
                            style = MaterialTheme.typography.titleLarge,
                            maxLines = 3
                        )
                    }
                    Divider(
                        modifier = Modifier
                            .height(2.dp)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.2f)
                    ) {
                        Text(
                            text = "Author: ${book.author}",
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 2,
                        )
                    }
                    Text(
                        text = if (isFavorite) "❤️ Favorite" else "",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Red,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    Button(
                        onClick = {
                            viewModel.toggleFavorite(book)
                            isFavorite = book.isFavorite // Update the UI after toggling
                        },
                        modifier = Modifier
                            .padding(top = 2.dp)
                            .weight(0.2f)
                    ) {
                        Text(text = if (isFavorite) "Unmark Favorite" else "Mark Favorite")
                    }
                }
            }

            Divider(
                modifier = Modifier
                    .height(5.dp)
            )
            Text(text = "Description: ${book.description}")
        }
    } else {
        Text(text = "No book selected")
    }
}
