package com.umidsafarov.pokehead.presentation.screen.pokemons_list

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umidsafarov.pokehead.R
import com.umidsafarov.pokehead.common.Resource
import com.umidsafarov.pokehead.domain.model.Pokemon
import com.umidsafarov.pokehead.domain.use_case.GetPokemonUseCase
import com.umidsafarov.pokehead.domain.use_case.GetPokemonsListUseCase
import com.umidsafarov.pokehead.domain.use_case.RefreshPokemonsUseCase
import com.umidsafarov.pokehead.presentation.common.PresenterConfig
import com.umidsafarov.pokehead.presentation.screen.pokemons_list.model.PokemonItemUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
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
    @ApplicationContext private val context: Context,
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

    fun handleInputEvent(event: PokemonsListContract.Event) {
        when (event) {
            is PokemonsListContract.Event.PokemonDetailsToggle -> {
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
            is PokemonsListContract.Event.PokemonChosen -> {
                navigateToPokemon(event.pokemonId)
            }
            is PokemonsListContract.Event.LoadNext -> {
                getNextPokemonsPage()
            }
            is PokemonsListContract.Event.Refresh -> {
                refresh()
            }
            is PokemonsListContract.Event.ShowAbout -> {
                state = state.copy(aboutShown = true)
            }
            is PokemonsListContract.Event.HideAbout -> {
                state = state.copy(aboutShown = false)
            }
            is PokemonsListContract.Event.ErrorShown -> {
                state = state.copy(errorMessage = null)
            }
            is PokemonsListContract.Event.NavigationDone -> {
                navigationDone()
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
                                errorMessage = response.message
                                    ?: context.getString(R.string.error_unknown)
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
                    state = state.copy(errorMessage = t.message)
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
                                errorMessage = response.message
                                    ?: context.getString(R.string.error_unknown)
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
                    state = state.copy(errorMessage = t.message)
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun navigateToPokemon(pokemonId: Int) {
        state = state.copy(pokemonIdToNavigate = pokemonId)
    }

    private fun navigationDone() {
        state = state.copy(pokemonIdToNavigate = null)
    }
}