package com.example.comicapp.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun NetworkErrorDialog(
    onDismiss: () -> Unit,
    onRetry: () -> Unit,
    errorMessage: String
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Network Error") },
        text = { Text(text = errorMessage) },
        confirmButton = {
            TextButton(onClick = {
                onRetry()
                onDismiss()
            }) {
                Text("Retry")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Dismiss")
            }
        }
    )
}
