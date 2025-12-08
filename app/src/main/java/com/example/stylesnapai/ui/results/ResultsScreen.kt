package com.example.stylesnapai.ui.results

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.stylesnapai.CameraViewModel
import com.example.stylesnapai.model.StyledImageResult

@Composable
fun ResultsScreen(
    navController: NavController,
    viewModel: CameraViewModel,
    onOpenDetail: (String, String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val result = uiState.latestResult ?: return

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Original", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        AsyncImage(
            model = result.original,
            contentDescription = "Original photo",
            modifier = Modifier.padding(bottom = 12.dp)
        )
        LazyVerticalGrid(
            columns = GridCells.Adaptive(140.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(result.styled) { styled ->
                StyledCard(styled) { onOpenDetail(styled.uri.toString(), styled.styleLabel) }
            }
        }
    }
}

@Composable
private fun StyledCard(styled: StyledImageResult, onClick: () -> Unit) {
    Card(
        modifier = Modifier.padding(8.dp),
        onClick = onClick
    ) {
        AsyncImage(
            model = styled.uri,
            contentDescription = styled.styleLabel,
            modifier = Modifier.padding(8.dp)
        )
        Text(
            text = styled.styleLabel,
            modifier = Modifier.padding(8.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
