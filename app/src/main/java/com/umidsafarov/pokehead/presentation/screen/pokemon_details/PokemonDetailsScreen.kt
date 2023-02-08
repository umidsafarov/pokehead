package com.umidsafarov.pokehead.presentation.screen.pokemon_details

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.umidsafarov.pokehead.R
import com.umidsafarov.pokehead.domain.model.Ability
import com.umidsafarov.pokehead.domain.model.Pokemon
import com.umidsafarov.pokehead.presentation.common.model.availableSpritesList
import com.umidsafarov.pokehead.presentation.common.model.toDrawableResource
import com.umidsafarov.pokehead.presentation.common.resources.ResourcesGetter

@Composable
fun PokemonDetailsScreen(
    state: PokemonDetailsContract.State,
    sendEvent: (event: PokemonDetailsContract.Event) -> Unit,
    navigateUp: () -> Unit
) {
    if (state.navigateUp) {
        navigateUp()
        sendEvent(PokemonDetailsContract.Event.NavigationDone)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .scrollable(state = rememberScrollState(), orientation = Orientation.Vertical)
    ) {
        Header(
            name = state.pokemon?.name,
            state.pokemon?.details?.spriteUrls?.frontDefaultMale,
            state.pokemon?.details?.elementTypes
        )
        Spacer(modifier = Modifier.height(5.dp))
        Abilites(abilities = state.pokemon?.abilities)
        Spacer(modifier = Modifier.height(10.dp))
        Sprites(sprites = state.pokemon?.details?.spriteUrls?.availableSpritesList())
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun Header(name: String?, imageUrl: String?, elementTypes: List<Pokemon.ElementType>?) {
    val avatarExpanded = remember { mutableStateOf(false) }
    val avatarSize = remember { Animatable(100f) }
    LaunchedEffect(avatarExpanded.value) {
        avatarSize.animateTo(if (avatarExpanded.value) 180f else 100f)
    }

    Surface(elevation = 10.dp, modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = imageUrl,
                placeholder = ResourcesGetter.loadingPlaceholder(),
                error = ResourcesGetter.errorPlaceholder(),
                contentDescription = null,
                modifier = Modifier
                    .size(avatarSize.value.dp)
                    .clickable { avatarExpanded.value = !avatarExpanded.value },
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(20.dp)
            ) {
                Text(
                    text = (name
                        ?: stringResource(id = R.string.error_unknown_pokemon)).capitalize(Locale.current),
                    style = MaterialTheme.typography.h6,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(4.dp))
                ElementTypes(elementTypes = elementTypes)
            }
        }
    }
}

@Composable
private fun ElementTypes(elementTypes: List<Pokemon.ElementType>?) {
    if (elementTypes == null) {
        Text(text = stringResource(id = R.string.loading), modifier = Modifier.fillMaxWidth())
    } else {
        Row(modifier = Modifier.fillMaxWidth()) {
            elementTypes.forEach {
                Image(
                    painter = painterResource(id = it.toDrawableResource()),
                    contentDescription = null,
                    modifier = Modifier
                        .size(28.dp)
                        .padding(3.dp, 0.dp)
                )
            }
        }
    }
}

@Composable
private fun Abilites(abilities: List<Ability>?) {
    Crossfade(abilities != null, modifier = Modifier.fillMaxWidth()) { abilitiesExist ->
        if (abilitiesExist) {
            Card(modifier = Modifier.fillMaxWidth()) {
                if (!abilities.isNullOrEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.abilities_header),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        for (i in abilities.indices) {
                            Text(
                                text = abilities[i].name.capitalize(Locale.current),
                                style = MaterialTheme.typography.subtitle1,
                                modifier = Modifier.padding(8.dp, 0.dp)
                            )
                            if (i < abilities.size - 1)
                                Divider(modifier = Modifier.padding(30.dp, 15.dp))
                        }
                    }
                }
            }
        } else {
            Text(
                text = stringResource(id = R.string.loading),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h6,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun Sprites(sprites: List<String>?) {
    Surface(elevation = 10.dp, modifier = Modifier.fillMaxWidth()) {
        if (sprites == null) {
            Text(
                text = stringResource(id = R.string.loading),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h6,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(100.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                items(items = sprites) { sprite ->
                    val isExpanded = remember { mutableStateOf(false) }
                    val size = remember { Animatable(100f) }

                    LaunchedEffect(isExpanded.value) {
                        size.animateTo(if (isExpanded.value) 200f else 100f)
                    }

                    Card(
                        elevation = 4.dp,
                        onClick = { isExpanded.value = !isExpanded.value },
                        modifier = Modifier
                            .padding(5.dp)
                    ) {
                        AsyncImage(
                            model = sprite,
                            placeholder = ResourcesGetter.loadingPlaceholder(),
                            error = ResourcesGetter.errorPlaceholder(),
                            contentDescription = null,
                            modifier = Modifier
                                .requiredSize(size.value.dp),
                        )
                    }
                }
            }
        }
    }
}