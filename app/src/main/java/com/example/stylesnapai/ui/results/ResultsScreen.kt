package com.example.stylesnapai.ui.results

import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.stylesnapai.CameraViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage

@Composable
fun ResultsScreen(
    navController: NavController,
    viewModel: CameraViewModel,
    onOpenDetail: (String, String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Column(modifier = Modifier.padding(16.dp)) {
        uiState.originalUri?.let {
            Text(text = "Original", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            AsyncImage(
                model = it,
                contentDescription = "Original photo",
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }
        LazyVerticalGrid(
            columns = GridCells.Adaptive(140.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(uiState.generatedImages) { styled ->
                Card(
                    modifier = Modifier.padding(8.dp),
                    onClick = { onOpenDetail(styled.sourceUri.toString(), styled.label) }
                ) {
                    Image(
                        bitmap = styled.bitmap.asImageBitmap(),
                        contentDescription = styled.label,
                        modifier = Modifier.padding(8.dp)
                    )
                    Text(
                        text = styled.label,
                        modifier = Modifier.padding(8.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
