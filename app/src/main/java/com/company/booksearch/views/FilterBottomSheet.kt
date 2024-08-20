package com.company.booksearch.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**FilterBottomSheet
 * A composable function that provides a user interface to filter books by a search query. It includes a text field for entering a query and a button to clear the filter.
 * @param:
 * filterSearchQuery: String: The current value of the search query.
 * onSearchQueryChange: (String) -> Unit: A lambda function that handles updates to the search query when the user types in the text field or clicks the clear button.
 * Usage:
 * This function is typically used within a bottom sheet or a modal where users can filter books based on their title or author. */
@Composable
fun FilterBottomSheet(
    filterSearchQuery: String,
    onSearchQueryChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        Text(
            "Filter",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        TextField(
            value = filterSearchQuery,
            onValueChange = { onSearchQueryChange(it) },
            label = { Text("Search by title or author") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        )
        Button(
            onClick = { onSearchQueryChange("") },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(
                text = "Clear Filter",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}