package com.example.stylesnapai.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.stylesnapai.model.StyledImageResult
import com.example.stylesnapai.ui.CameraViewModel
import com.example.stylesnapai.ui.Screen

@Composable
fun ResultsScreen(navController: NavController, cameraViewModel: CameraViewModel) {
    val uiState by cameraViewModel.uiState.collectAsState()
    val result = uiState.latestResult ?: return

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Original", style = MaterialTheme.typography.titleMedium)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .padding(vertical = 8.dp)
        ) {
            AsyncImage(
                model = result.original,
                contentDescription = "Original",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text("Styled results", style = MaterialTheme.typography.titleMedium)
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(result.styled) { item ->
                StyledResultItem(item) {
                    navController.navigate(
                        "${Screen.Detail.route}?uri=${java.net.URLEncoder.encode(item.uri.toString(), "UTF-8")}&label=${java.net.URLEncoder.encode(item.styleLabel, "UTF-8")}&id=${item.id ?: -1}&original=${java.net.URLEncoder.encode(result.original.toString(), "UTF-8")}"
                    )
                }
            }
        }
    }
}

@Composable
private fun StyledResultItem(item: StyledImageResult, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clickable { onClick() }
    ) {
        Column {
            AsyncImage(
                model = item.uri,
                contentDescription = item.styleLabel,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                contentScale = ContentScale.Crop
            )
            Text(
                text = item.styleLabel,
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
