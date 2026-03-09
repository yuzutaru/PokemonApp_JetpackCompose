package com.yustar.dashboard.presentation.screen

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import com.yustar.core.ui.PokeApp_JetpackComposeTheme
import com.yustar.core.ui.Turquoise25
import com.yustar.dashboard.data.local.PokemonEntity
import com.yustar.dashboard.presentation.viewmodel.MenuViewModel
import org.koin.androidx.compose.koinViewModel

/**
 * Created by Yustar Pramudana on 08/03/26.
 */

@Composable
fun MenuScreen(paddingValues: PaddingValues, viewModel: MenuViewModel = koinViewModel()) {
    val pokemonPagingItems = viewModel.pokemonPagingData.collectAsLazyPagingItems()

    MenuContent(
        paddingValues = paddingValues,
        pokemonPagingItems = pokemonPagingItems
    )
}

@Composable
fun MenuContent(
    paddingValues: PaddingValues,
    pokemonPagingItems: LazyPagingItems<PokemonEntity>
) {
    MenuContentInternal(
        paddingValues = paddingValues,
        itemCount = pokemonPagingItems.itemCount,
        pokemonAtIndex = { index -> pokemonPagingItems[index] },
        itemKey = pokemonPagingItems.itemKey { it.name },
        refreshState = pokemonPagingItems.loadState.refresh,
        appendState = pokemonPagingItems.loadState.append
    )
}

@Composable
private fun MenuContentInternal(
    paddingValues: PaddingValues,
    itemCount: Int,
    pokemonAtIndex: (Int) -> PokemonEntity?,
    itemKey: (Int) -> Any,
    refreshState: LoadState,
    appendState: LoadState
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(paddingValues)
            .safeDrawingPadding()
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(
                count = itemCount,
                key = itemKey
            ) { index ->
                val pokemon = pokemonAtIndex(index)
                if (pokemon != null) {
                    PokemonItem(pokemon = pokemon)
                }
            }

            when {
                refreshState is LoadState.Loading -> {
                    item(span = { GridItemSpan(2) }) {
                        LoadingIndicator(modifier = Modifier.fillMaxWidth().padding(32.dp))
                    }
                }
                appendState is LoadState.Loading -> {
                    item(span = { GridItemSpan(2) }) {
                        LoadingIndicator(modifier = Modifier.fillMaxWidth().padding(16.dp))
                    }
                }
                refreshState is LoadState.Error -> {
                    val e = refreshState as LoadState.Error
                    item(span = { GridItemSpan(2) }) {
                        Text(
                            text = e.error.localizedMessage ?: "Unknown Error",
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LoadingIndicator(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = Turquoise25)
    }
}

@Composable
fun PokemonItem(pokemon: PokemonEntity) {
    val id = pokemon.url.split("/").filter { it.isNotEmpty() }.lastOrNull() ?: "1"
    val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"
    
    // Background colors similar to the image
    val colors = listOf(
        Color(0xFFFFB6C1), // Soft Pink (Jigglypuff)
        Color(0xFFA1E3A1), // Soft Green (Bulbasaur)
        Color(0xFFFFE082), // Soft Yellow (Pikachu)
        Color(0xFF81D4FA)  // Soft Blue (Polywag)
    )
    val backgroundColor = try {
        colors[id.toInt() % colors.size]
    } catch (e: Exception) {
        colors[0]
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.85f),
        shape = RoundedCornerShape(24.dp),
        color = backgroundColor,
        shadowElevation = 4.dp
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            ) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = pokemon.name,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentScale = ContentScale.Fit
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = pokemon.name.replaceFirstChar { it.uppercase() },
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.weight(1f)
                    )
                    
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, device = Devices.PIXEL_4, showBackground = true)
@Composable
fun LightModePreviewMenuContent() {
    val pokemonList = listOf(
        PokemonEntity("bulbasaur", "https://pokeapi.co/api/v2/pokemon/1/", 1),
        PokemonEntity("ivysaur", "https://pokeapi.co/api/v2/pokemon/2/", 1),
        PokemonEntity("venusaur", "https://pokeapi.co/api/v2/pokemon/3/", 1),
        PokemonEntity("charmander", "https://pokeapi.co/api/v2/pokemon/4/", 1),
        PokemonEntity("charmeleon", "https://pokeapi.co/api/v2/pokemon/5/", 1),
        PokemonEntity("charizard", "https://pokeapi.co/api/v2/pokemon/6/", 1)
    )

    PokeApp_JetpackComposeTheme {
        MenuContentInternal(
            paddingValues = PaddingValues(0.dp),
            itemCount = pokemonList.size,
            pokemonAtIndex = { pokemonList[it] },
            itemKey = { pokemonList[it].name },
            refreshState = LoadState.NotLoading(false),
            appendState = LoadState.NotLoading(false)
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, device = Devices.PIXEL_4, showBackground = true)
@Composable
fun NightModePreviewMenuContent() {
    val pokemonList = listOf(
        PokemonEntity("bulbasaur", "https://pokeapi.co/api/v2/pokemon/1/", 1),
        PokemonEntity("ivysaur", "https://pokeapi.co/api/v2/pokemon/2/", 1),
        PokemonEntity("venusaur", "https://pokeapi.co/api/v2/pokemon/3/", 1),
        PokemonEntity("charmander", "https://pokeapi.co/api/v2/pokemon/4/", 1)
    )

    PokeApp_JetpackComposeTheme {
        MenuContentInternal(
            paddingValues = PaddingValues(0.dp),
            itemCount = pokemonList.size,
            pokemonAtIndex = { pokemonList[it] },
            itemKey = { pokemonList[it].name },
            refreshState = LoadState.NotLoading(false),
            appendState = LoadState.NotLoading(false)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPokemonItem() {
    PokeApp_JetpackComposeTheme {
        Box(modifier = Modifier.padding(16.dp).size(200.dp)) {
            PokemonItem(PokemonEntity("bulbasaur", "https://pokeapi.co/api/v2/pokemon/1/", 0))
        }
    }
}
