package br.com.curso.portalrickandmorty.screens.characters

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.curso.portalrickandmorty.domain.model.Character
import coil.compose.AsyncImage

@Composable
fun CharactersScreen(
    viewModel: CharacterViewModel,
    onCharacterClick: (Int) -> Unit,
    onPortalClick: () -> Unit
) {

    val characters by viewModel.characters.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            if (error != null && characters.isNotEmpty()) {
                OfflineWarningBanner()
            }

            when {
                isLoading && characters.isEmpty() -> {
                    LoadingState()
                }

                error != null && characters.isEmpty() -> {
                    ErrorState(error = error)
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        item {
                            Button(
                                onClick = onPortalClick,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Text("Ver Meu Portal (GPS)")
                            }
                        }

                        items(
                            items = characters
                        ) { character ->
                            CharacterItem(
                                character = character,
                                onClick = { onCharacterClick(character.id) }
                            )
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OfflineWarningBanner() {
    Text(
        text = "Sem conexão - exibindo dados salvos",
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Gray.copy(alpha = 0.8f))
            .padding(8.dp),
        color = Color.White,
        fontSize = 12.sp,
        textAlign = TextAlign.Center
    )
}

@Composable
fun CharacterItem(
    character: Character,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        AsyncImage(
            model = character.image,
            contentDescription = character.name,
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier.padding(start = 16.dp)
        ) {

            Text(
                text = character.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "${character.species} - ${character.status}"
            )
        }
    }
}

@Composable
fun LoadingState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorState(error: String?) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = error ?: "Erro desconhecido"
        )
    }
}