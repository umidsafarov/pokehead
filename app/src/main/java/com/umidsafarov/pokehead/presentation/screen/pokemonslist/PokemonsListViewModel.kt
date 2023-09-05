package com.umidsafarov.pokehead.presentation.screen.pokemonslist

import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umidsafarov.pokehead.common.Resource
import com.umidsafarov.pokehead.domain.model.Pokemon
import com.umidsafarov.pokehead.domain.usecase.GetPokemonUseCase
import com.umidsafarov.pokehead.domain.usecase.GetPokemonsListUseCase
import com.umidsafarov.pokehead.domain.usecase.RefreshPokemonsUseCase
import com.umidsafarov.pokehead.presentation.common.PresenterConfig
import com.umidsafarov.pokehead.presentation.screen.pokemonslist.model.PokemonItemUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import de.palm.composestateevents.consumed
import de.palm.composestateevents.triggered
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class PokemonsListViewModel @Inject constructor(
    private val getPokemonsListUseCase: GetPokemonsListUseCase,
    private val refreshPokemonsUseCase: RefreshPokemonsUseCase,
    private val getPokemonUseCase: GetPokemonUseCase,
) : ViewModel() {

    var state by mutableStateOf(PokemonsListContract.State(isLoading = true))
        private set

    private var pokemonsList = mutableListOf<PokemonItemUIModel>()
    private var getPokemonsJob: Job? = null

    init {
        getNextPokemonsPage()
    }

    fun handleUIEvent(event: PokemonsListContract.UIEvent) {
        when (event) {
            is PokemonsListContract.UIEvent.PokemonDetailsToggle -> {
                val pokemonIndex = pokemonsList.indexOfFirst { it.id == event.pokemonId }
                if (pokemonIndex == -1)
                    return

                val pokemonToToggle = pokemonsList[pokemonIndex]
                pokemonsList[pokemonIndex] =
                    pokemonToToggle.copy(expanded = !pokemonToToggle.expanded)
                state = state.copy(pokemons = pokemonsList.toList())

                if (pokemonToToggle.avatarUrl == null)
                    loadPokemonDetails(event.pokemonId)
            }
            is PokemonsListContract.UIEvent.PokemonChosen -> {
                navigateToPokemon(event.pokemonId)
            }
            is PokemonsListContract.UIEvent.LoadNext -> {
                getNextPokemonsPage()
            }
            is PokemonsListContract.UIEvent.Refresh -> {
                refresh()
            }
            is PokemonsListContract.UIEvent.ErrorEventConsumed -> {
                state = state.copy(errorMessageEvent = consumed())
            }
            is PokemonsListContract.UIEvent.NavigationEventConsumed -> {
                state = state.copy(navigateToPokemonEvent = consumed())
            }
        }
    }

    private fun getNextPokemonsPage() {
        if (getPokemonsJob != null && getPokemonsJob!!.isActive) return
        handlePokemons(
            request = {
                getPokemonsListUseCase(
                    PresenterConfig.POKEMONS_PAGE_COUNT,
                    pokemonsList.size
                )
            },
            onLoadingChanged = { state = state.copy(isLoading = it) },
        )
    }

    private fun refresh() {
        pokemonsList.clear()
        handlePokemons(
            request = { refreshPokemonsUseCase(PresenterConfig.POKEMONS_PAGE_COUNT) },
            onLoadingChanged = { state = state.copy(isRefreshing = it) },
        )
    }

    private fun handlePokemons(
        request: () -> Flow<Resource<List<Pokemon>>>,
        onSuccess: (() -> Unit)? = null,
        onLoadingChanged: ((isLoading: Boolean) -> Unit)? = null,
    ) {
        getPokemonsJob?.cancel()
        getPokemonsJob = viewModelScope.launch {
            var failed = false
            request.invoke().cancellable().onEach { response ->
                try {
                    when (response) {
                        is Resource.Loading -> {
                            onLoadingChanged?.invoke(response.isLoading)
                            if (!response.isLoading && !failed)
                                onSuccess?.invoke()
                        }
                        is Resource.Error -> {
                            state = state.copy(
                                errorMessageEvent = triggered(response.message)
                            )
                            failed = true
                        }
                        is Resource.Success -> {
                            if (response.data != null) {
                                response.data.forEach { pokemon ->
                                    pokemonsList.add(
                                        PokemonItemUIModel(
                                            pokemon.id,
                                            pokemon.name,
                                            pokemon.details?.spriteUrls?.frontDefaultMale,
                                            false
                                        )
                                    )
                                }
                                state = state.copy(
                                    pokemons = pokemonsList.toList()
                                )
                            }
                        }
                    }
                } catch (t: Throwable) {
                    state = state.copy(errorMessageEvent = triggered(t.message))
                }
            }.launchIn(viewModelScope)
        }

    }

    private fun loadPokemonDetails(pokemonId: Int) {
        viewModelScope.launch {
            getPokemonUseCase(pokemonId = pokemonId).onEach { response ->
                try {
                    when (response) {
                        is Resource.Loading -> Unit
                        is Resource.Error -> {
                            state = state.copy(
                                errorMessageEvent = triggered(response.message)
                            )
                        }
                        is Resource.Success -> {
                            if (response.data != null) {
                                val pokemonIndex = pokemonsList.indexOfFirst { it.id == pokemonId }
                                pokemonsList[pokemonIndex] = PokemonItemUIModel(
                                    response.data.id,
                                    response.data.name,
                                    response.data.details?.spriteUrls?.frontDefaultMale,
                                    pokemonsList[pokemonIndex].expanded
                                )
                                state = state.copy(pokemons = pokemonsList.toList())
                            }
                        }
                    }
                } catch (t: Throwable) {
                    state = state.copy(errorMessageEvent = triggered(t.message))
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun navigateToPokemon(pokemonId: Int) {
        state = state.copy(navigateToPokemonEvent = triggered(pokemonId))
    }
}