package com.umidsafarov.pokehead.presentation.screen.pokemons_list

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import com.umidsafarov.pokehead.BuildConfig
import com.umidsafarov.pokehead.R
import com.umidsafarov.pokehead.common.Config
import com.umidsafarov.pokehead.presentation.common.composables.OnBottomReached
import com.umidsafarov.pokehead.presentation.screen.pokemons_list.model.PokemonItemUIModel
import com.umidsafarov.pokehead.presentation.screen.pokemons_list.ui_component.PokemonItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PokemonsListScreen(
    state: PokemonsListContract.State,
    sendEvent: (event: PokemonsListContract.Event) -> Unit,
    onNavigateToPokemon: (pokemonId: Int) -> Unit
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.isRefreshing,
        onRefresh = { sendEvent(PokemonsListContract.Event.Refresh) }
    )

    if (state.pokemonIdToNavigate != null) {
        onNavigateToPokemon(state.pokemonIdToNavigate)
        sendEvent(PokemonsListContract.Event.NavigationDone)
    }

    LaunchedEffect(key1 = state.errorMessage) {
        if (state.errorMessage != null) {
            sendEvent(PokemonsListContract.Event.ErrorShown)
            scope.launch { snackbarHostState.showSnackbar(state.errorMessage) }
        }
    }

    ErrorMessage(snackbarHostState)

    if (state.aboutShown)
        AboutDialog(onDismiss = { sendEvent(PokemonsListContract.Event.HideAbout) })

    Column(modifier = Modifier.fillMaxSize()) {
        Header(onClick = { sendEvent(PokemonsListContract.Event.ShowAbout) })
        Box(
            modifier = Modifier
                .weight(1f)
                .pullRefresh(pullRefreshState)
        ) {
            Crossfade(targetState = !state.pokemons.isNullOrEmpty()) { pokemonsExist ->
                if (pokemonsExist) {
                    PokemonsList(
                        pokemons = state.pokemons!!,
                        isLoading = state.isLoading,
                        onItemClick = { sendEvent(PokemonsListContract.Event.PokemonDetailsToggle(it.id)) },
                        onItemDetailsClick = { sendEvent(PokemonsListContract.Event.PokemonChosen(it.id)) },
                        onBottomReached = { sendEvent(PokemonsListContract.Event.LoadNext) }
                    )
                } else {
                    EmptyMessage(
                        state = state,
                        onClick = { sendEvent(PokemonsListContract.Event.Refresh) }
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
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp)
                .clickable {
                    onClick()
                }
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
    isLoading: Boolean,
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
        itemsIndexed(items = pokemons, key = { _, item -> item.id }) { i, pokemon ->
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