package com.example.stylesnapai.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.stylesnapai.model.CustomStyle
import com.example.stylesnapai.ui.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val customStyles by viewModel.customStyles.collectAsState()
    val apiKey by viewModel.apiKey.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var editingStyle by remember { mutableStateOf<CustomStyle?>(null) }

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("Settings") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Custom styles", style = MaterialTheme.typography.titleMedium)
            Button(onClick = {
                editingStyle = null
                showDialog = true
            }) { Icon(Icons.Default.Add, contentDescription = null); Spacer(Modifier.width(8.dp)); Text("Add style") }
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(customStyles) { style ->
                    ListItem(
                        headlineContent = { Text(style.name) },
                        supportingContent = { Text(style.prompt) },
                        trailingContent = {
                            Row {
                                IconButton(onClick = { editingStyle = style; showDialog = true }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Edit")
                                }
                                IconButton(onClick = { viewModel.deleteStyle(style) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                                }
                            }
                        }
                    )
                }
            }

            Divider()
            Text("OpenAI API key", style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(
                value = apiKey,
                onValueChange = { viewModel.setApiKey(it) },
                placeholder = { Text("Paste your key (mock)") },
                modifier = Modifier.fillMaxWidth()
            )

            Divider()
            Text("About", style = MaterialTheme.typography.titleMedium)
            Text("StyleSnap AI v1.0")
            Text("Built with Jetpack Compose, CameraX, and Material 3")
        }
    }

    if (showDialog) {
        StyleDialog(
            style = editingStyle,
            onDismiss = { showDialog = false },
            onSave = { name, prompt ->
                if (editingStyle == null) viewModel.addStyle(name, prompt)
                else viewModel.updateStyle(editingStyle!!.copy(name = name, prompt = prompt))
                showDialog = false
            }
        )
    }
}

@Composable
private fun StyleDialog(style: CustomStyle?, onDismiss: () -> Unit, onSave: (String, String) -> Unit) {
    var name by remember { mutableStateOf(TextFieldValue(style?.name ?: "")) }
    var prompt by remember { mutableStateOf(TextFieldValue(style?.prompt ?: "")) }
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = { onSave(name.text, prompt.text) }) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
        title = { Text(if (style == null) "New style" else "Edit style") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
                OutlinedTextField(value = prompt, onValueChange = { prompt = it }, label = { Text("Prompt") })
            }
        }
    )
}
