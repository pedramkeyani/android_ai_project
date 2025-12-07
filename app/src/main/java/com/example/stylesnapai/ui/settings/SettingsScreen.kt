package com.example.stylesnapai.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.stylesnapai.SettingsViewModel

@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val customStyles by viewModel.customStyles.collectAsStateWithLifecycle()
    var name by remember { mutableStateOf("") }
    var prompt by remember { mutableStateOf("") }
    var apiKey by remember { mutableStateOf("") }

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        item {
            Text(text = "Custom Styles", modifier = Modifier.padding(bottom = 8.dp))
        }
        items(customStyles) { style ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = style.name)
                    Text(text = style.prompt)
                }
                IconButton(onClick = { viewModel.deleteStyle(style) }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                }
            }
        }
        item {
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Style name") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = prompt, onValueChange = { prompt = it }, label = { Text("Prompt") }, modifier = Modifier.fillMaxWidth())
            Button(onClick = { viewModel.addStyle(name, prompt); name = ""; prompt = "" }, modifier = Modifier.padding(top = 8.dp)) {
                Text("Add Style")
            }
        }
        item {
            Text(text = "OpenAI API Key", modifier = Modifier.padding(top = 16.dp))
            OutlinedTextField(
                value = apiKey,
                onValueChange = { apiKey = it },
                label = { Text("API Key") },
                modifier = Modifier.fillMaxWidth(),
                supportingText = { Text("TODO: Store this securely and send with requests") }
            )
        }
        item {
            Text(text = "App: StyleSnap AI v1.0", modifier = Modifier.padding(top = 16.dp))
        }
    }
}
