package com.yustar.dashboard.presentation.screen

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.yustar.core.ui.PokeApp_JetpackComposeTheme
import com.yustar.core.ui.PokemonColors
import com.yustar.core.ui.Turquoise25
import com.yustar.dashboard.data.remote.model.AbilityDto
import com.yustar.dashboard.data.remote.model.AbilitySlotDto
import com.yustar.dashboard.data.remote.model.OfficialArtworkDto
import com.yustar.dashboard.data.remote.model.OtherSpritesDto
import com.yustar.dashboard.data.remote.model.PokemonDetailResponse
import com.yustar.dashboard.data.remote.model.SpritesDto
import com.yustar.dashboard.data.remote.model.TypeDto
import com.yustar.dashboard.data.remote.model.TypeSlotDto
import com.yustar.dashboard.presentation.viewmodel.DetailViewModel
import org.koin.androidx.compose.koinViewModel

/**
 * Created by Yustar Pramudana on 10/03/26.
 */

@Composable
fun DetailScreen(
    pokemonName: String,
    viewModel: DetailViewModel = koinViewModel(),
    onBackClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(pokemonName) {
        viewModel.getPokemonDetail(pokemonName)
    }

    DetailContent(
        isLoading = uiState.isLoading,
        pokemon = uiState.pokemon,
        error = uiState.error,
        onBackClick = onBackClick
    )
}

@Composable
fun DetailContent(
    isLoading: Boolean,
    pokemon: PokemonDetailResponse?,
    error: String,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Gray
                )
            }
        }

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize().weight(1f), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Turquoise25)
            }
        } else if (error.isNotEmpty()) {
            Box(modifier = Modifier.fillMaxSize().weight(1f), contentAlignment = Alignment.Center) {
                Text(text = error, color = MaterialTheme.colorScheme.error)
            }
        } else if (pokemon != null) {
            val idStr = pokemon.id.toString().padStart(3, '0')
            val imageUrl = pokemon.sprites.other?.officialArtwork?.frontDefault ?: pokemon.sprites.frontDefault ?: ""
            val backgroundColor = try {
                PokemonColors[pokemon.id % PokemonColors.size]
            } catch (e: Exception) {
                PokemonColors[0]
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "#$idStr",
                        color = Color(0xFFE91E63),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = pokemon.name.replaceFirstChar { it.uppercase() },
                        color = Color.Black,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Pokemon Image Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .clip(RoundedCornerShape(32.dp))
                        .background(backgroundColor),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = pokemon.name,
                        modifier = Modifier.fillMaxSize(0.8f),
                        contentScale = ContentScale.Fit
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Types
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    pokemon.types.forEach { typeSlot ->
                        TypeBadge(type = typeSlot.type.name, modifier = Modifier.weight(1f))
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Stats Section
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(topStart = 48.dp, topEnd = 48.dp))
                        .background(backgroundColor)
                        .padding(32.dp)
                ) {
                    Column {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            StatItem(
                                label = "Altura",
                                value = "${pokemon.height / 10.0} m",
                                modifier = Modifier.weight(1f)
                            )
                            StatItem(
                                label = "Categoría",
                                value = "Llama", // Hardcoded as it's not in the basic detail API
                                modifier = Modifier.weight(1f)
                            )
                            StatItem(
                                label = "Debilidad",
                                value = "", // Requires another API call or mapping
                                modifier = Modifier.weight(1f),
                                isWeakness = true
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Row(modifier = Modifier.fillMaxWidth()) {
                            StatItem(
                                label = "Peso",
                                value = "${pokemon.weight / 10.0} kg",
                                modifier = Modifier.weight(1f)
                            )
                            StatItem(
                                label = "Sexo",
                                value = "♀♂",
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.weight(1f))
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "Habilidades",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = pokemon.abilities.joinToString(", ") { it.ability.name.replaceFirstChar { char -> char.uppercase() } },
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TypeBadge(type: String, modifier: Modifier = Modifier) {
    val color = when (type.lowercase()) {
        "fire" -> Color(0xFFF08030)
        "water" -> Color(0xFF6890F0)
        "grass" -> Color(0xFF78C850)
        "electric" -> Color(0xFFF8D030)
        "ice" -> Color(0xFF98D8D8)
        "fighting" -> Color(0xFFC03028)
        "poison" -> Color(0xFFA040A0)
        "ground" -> Color(0xFFE0C068)
        "flying" -> Color(0xFFA890F0)
        "psychic" -> Color(0xFFF85888)
        "bug" -> Color(0xFFA8B820)
        "rock" -> Color(0xFFB8A038)
        "ghost" -> Color(0xFF705898)
        "dragon" -> Color(0xFF7038F8)
        "dark" -> Color(0xFF705848)
        "steel" -> Color(0xFFB8B8D0)
        "fairy" -> Color(0xFFEE99AC)
        else -> Color.Gray
    }

    Surface(
        modifier = modifier.height(40.dp),
        shape = RoundedCornerShape(12.dp),
        color = color,
        shadowElevation = 4.dp
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = type.replaceFirstChar { it.uppercase() },
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun StatItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    isWeakness: Boolean = false
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        if (isWeakness) {
            WeaknessItem(color = Color(0xFF2196F3), label = "Agua")
            WeaknessItem(color = Color(0xFFFFFF00), label = "Electricidad")
            WeaknessItem(color = Color(0xFF8B4513), label = "Roca")
        } else {
            Text(
                text = value,
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun WeaknessItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.8f),
            fontSize = 12.sp
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, device = Devices.PIXEL_4, showBackground = true)
@Composable
fun NightModePreviewDetailScreen() {
    val mockPokemon = PokemonDetailResponse(
        id = 6,
        name = "charizard",
        height = 17,
        weight = 905,
        sprites = SpritesDto(
            frontDefault = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/6.png",
            other = OtherSpritesDto(
                officialArtwork = OfficialArtworkDto(
                    frontDefault = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/6.png"
                )
            )
        ),
        types = listOf(
            TypeSlotDto(1, TypeDto("fire")),
            TypeSlotDto(2, TypeDto("flying"))
        ),
        abilities = listOf(
            AbilitySlotDto(AbilityDto("mar llamas"))
        )
    )
    PokeApp_JetpackComposeTheme {
        DetailContent(isLoading = false, pokemon = mockPokemon, error = "", onBackClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun LightModePreviewDetailScreen() {
    val mockPokemon = PokemonDetailResponse(
        id = 6,
        name = "charizard",
        height = 17,
        weight = 905,
        sprites = SpritesDto(
            frontDefault = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/6.png",
            other = OtherSpritesDto(
                officialArtwork = OfficialArtworkDto(
                    frontDefault = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/6.png"
                )
            )
        ),
        types = listOf(
            TypeSlotDto(1, TypeDto("fire")),
            TypeSlotDto(2, TypeDto("flying"))
        ),
        abilities = listOf(
            AbilitySlotDto(AbilityDto("mar llamas"))
        )
    )
    PokeApp_JetpackComposeTheme {
        DetailContent(isLoading = false, pokemon = mockPokemon, error = "", onBackClick = {})
    }
}
