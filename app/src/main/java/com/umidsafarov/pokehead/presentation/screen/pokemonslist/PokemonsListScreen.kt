package com.umidsafarov.pokehead.presentation.screen.pokemonslist

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import com.umidsafarov.pokehead.BuildConfig
import com.umidsafarov.pokehead.R
import com.umidsafarov.pokehead.common.Config
import com.umidsafarov.pokehead.presentation.common.composables.OnBottomReached
import com.umidsafarov.pokehead.presentation.screen.pokemonslist.model.PokemonItemUIModel
import com.umidsafarov.pokehead.presentation.screen.pokemonslist.uicomponent.PokemonItem
import com.umidsafarov.pokehead.presentation.theme.PokeheadAppTheme
import de.palm.composestateevents.EventEffect

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PokemonsListScreen(
    state: PokemonsListContract.State,
    sendEvent: (event: PokemonsListContract.UIEvent) -> Unit,
    onNavigateToPokemon: (pokemonId: Int) -> Unit
) {
    //context
    val context = LocalContext.current

    //states
    val snackbarHostState = remember { SnackbarHostState() }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.isRefreshing,
        onRefresh = { sendEvent(PokemonsListContract.UIEvent.Refresh) }
    )
    val pokemonsExist = remember(key1 = state.pokemons) {
        !state.pokemons.isNullOrEmpty()
    }
    val aboutShownState = remember { mutableStateOf(false) }

    //single time vents
    EventEffect(event = state.navigateToPokemonEvent, onConsumed = {
        sendEvent(PokemonsListContract.UIEvent.NavigationEventConsumed)
    }) { pokemonIdToNavigate ->
        onNavigateToPokemon(pokemonIdToNavigate)
    }

    EventEffect(
        event = state.errorMessageEvent,
        onConsumed = { sendEvent(PokemonsListContract.UIEvent.ErrorEventConsumed) }) { errorMessage ->
        snackbarHostState.showSnackbar(errorMessage ?: context.getString(R.string.error_unknown))
    }

    //messages
    ErrorMessage(snackbarHostState)

    //dialogs
    if (aboutShownState.value)
        AboutDialog(onDismiss = { aboutShownState.value = false })

    //interface
    Column(modifier = Modifier.fillMaxSize()) {
        Header(onClick = { aboutShownState.value = true })
        Box(
            modifier = Modifier
                .weight(1f)
                .pullRefresh(pullRefreshState)
        ) {
            Crossfade(targetState = pokemonsExist) { pokemonsExist ->
                if (pokemonsExist) {
                    PokemonsList(
                        pokemons = state.pokemons!!,
                        onItemClick = {
                            sendEvent(
                                PokemonsListContract.UIEvent.PokemonDetailsToggle(
                                    it.id
                                )
                            )
                        },
                        onItemDetailsClick = {
                            sendEvent(
                                PokemonsListContract.UIEvent.PokemonChosen(
                                    it.id
                                )
                            )
                        },
                        onBottomReached = { sendEvent(PokemonsListContract.UIEvent.LoadNext) }
                    )
                } else {
                    EmptyMessage(
                        state = state,
                        onClick = { sendEvent(PokemonsListContract.UIEvent.Refresh) }
                    )
                }
            }
            PullRefresh(
                state.isLoading,
                pullRefreshState,
                Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

@Composable
private fun ErrorMessage(snackbarHostState: SnackbarHostState) {
    Box(modifier = Modifier.fillMaxSize()) {
        SnackbarHost(
            hostState = snackbarHostState,
            snackbar = { Snackbar(it) },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun Header(onClick: () -> Unit) {
    Surface(
        shape = MaterialTheme.shapes.large, elevation = 10.dp,
        modifier = Modifier
            .fillMaxWidth()
            .zIndex(1f)
            .clickable {
                onClick()
            }
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp)
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun PullRefresh(
    isLoading: Boolean,
    pullRefreshState: PullRefreshState,
    modifier: Modifier
) {
    PullRefreshIndicator(
        isLoading,
        pullRefreshState,
        modifier = modifier,
    )
}

@Composable
private fun AboutDialog(onDismiss: () -> Unit) {
    val context = LocalContext.current
    Dialog(onDismissRequest = { onDismiss() }) {
        Card {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(20.dp)
            ) {
                Image(painter = painterResource(id = R.drawable.logo), contentDescription = null)
                Text(text = stringResource(id = R.string.about_version, BuildConfig.VERSION_NAME))
                Spacer(modifier = Modifier.height(30.dp))
                Text(
                    text = stringResource(id = R.string.about_source, Config.GIT_REPO_URL),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            context.startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(Config.GIT_REPO_URL)
                                )
                            )
                        })
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = stringResource(id = R.string.about_api, Config.API_URL),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            context.startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(Config.API_URL)
                                )
                            )
                        })
                Spacer(modifier = Modifier.height(30.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Button(
                        onClick = { onDismiss() },
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        Text(text = stringResource(id = R.string.close))
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyMessage(
    state: PokemonsListContract.State,
    onClick: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(id = if (state.isLoading) R.string.loading else R.string.no_pokemons),
            style = MaterialTheme.typography.h6,
            modifier = Modifier
                .align(Alignment.Center)
                .clickable { onClick() },
        )
    }
}

@Composable
private fun PokemonsList(
    pokemons: List<PokemonItemUIModel>,
    onItemClick: (pokemon: PokemonItemUIModel) -> Unit,
    onItemDetailsClick: (pokemon: PokemonItemUIModel) -> Unit,
    onBottomReached: () -> Unit,
) {
    val listState = rememberLazyListState()
    listState.OnBottomReached(onLoadMore = onBottomReached)

    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(10.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        modifier = Modifier
            .padding(0.dp, 2.dp, 0.dp, 0.dp)
            .fillMaxSize()
    ) {
        itemsIndexed(items = pokemons, key = { _, item -> item.id }) { _, pokemon ->
            PokemonItem(
                pokemonItemUIModel = pokemon,
                onItemClick = { onItemClick(pokemon) },
                onDetailsClick = { onItemDetailsClick(pokemon) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            )
        }
        item {
            Text(
                text = stringResource(id = R.string.loading),
                style = MaterialTheme.typography.h6,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    PokeheadAppTheme {
        PokemonsListScreen(
            state = PokemonsListContract.State(
                pokemons = listOf(
                    PokemonItemUIModel(1, "Pokemon one", null, false),
                    PokemonItemUIModel(2, "Pokemon two", null, true),
                    PokemonItemUIModel(3, "Pokemon three", null, false),
                ),
                isLoading = true,
                isRefreshing = false,
            ),
            sendEvent = {},
            onNavigateToPokemon = {}
        )
    }
}