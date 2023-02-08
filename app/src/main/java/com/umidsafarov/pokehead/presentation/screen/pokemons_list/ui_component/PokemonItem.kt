package com.umidsafarov.pokehead.presentation.screen.pokemons_list.ui_component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.umidsafarov.pokehead.R
import com.umidsafarov.pokehead.presentation.common.resources.ResourcesGetter
import com.umidsafarov.pokehead.presentation.screen.pokemons_list.model.PokemonItemUIModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PokemonItem(
    pokemonItemUIModel: PokemonItemUIModel,
    onItemClick: (pokemonItem: PokemonItemUIModel) -> Unit,
    onDetailsClick: (pokemonItem: PokemonItemUIModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        elevation = 2.dp,
        onClick = { onItemClick(pokemonItemUIModel) },
    ) {
        Column(
            modifier = modifier
                .padding(5.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = pokemonItemUIModel.name.capitalize(Locale.current),
                    style = MaterialTheme.typography.h6,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier
                        .weight(1f),
                )
                Button(onClick = { onDetailsClick(pokemonItemUIModel) }) {
                    Text(text = stringResource(id = R.string.pokemon_details))
                }
            }
            AnimatedVisibility(pokemonItemUIModel.expanded, modifier = Modifier.fillMaxWidth()) {
                if (pokemonItemUIModel.avatarUrl != null) {
                    AsyncImage(
                        model = pokemonItemUIModel.avatarUrl,
                        placeholder = ResourcesGetter.loadingPlaceholder(),
                        error = ResourcesGetter.errorPlaceholder(),
                        contentDescription = null,
                        modifier = Modifier
                            .size(96.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                } else {
                    Image(
                        painter = ResourcesGetter.loadingPlaceholder(),
                        contentDescription = null,
                        modifier = Modifier
                            .size(96.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}