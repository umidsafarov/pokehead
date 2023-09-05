package com.umidsafarov.pokehead.presentation.screen.pokemondetails

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umidsafarov.pokehead.common.Resource
import com.umidsafarov.pokehead.domain.usecase.GetAbilityUseCase
import com.umidsafarov.pokehead.domain.usecase.GetPokemonUseCase
import com.umidsafarov.pokehead.presentation.navigation.NavigationKeys
import dagger.hilt.android.lifecycle.HiltViewModel
import de.palm.composestateevents.consumed
import de.palm.composestateevents.triggered
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getPokemonUseCase: GetPokemonUseCase,
    private val getPokemonAbilityUseCase: GetAbilityUseCase,
) : ViewModel() {

    private val pokemonId = savedStateHandle.get<Int>(NavigationKeys.Arg.POKEMON_ID)

    var state by mutableStateOf(PokemonDetailsContract.State())
        private set

    init {
        viewModelScope.launch {
            if (pokemonId == null) {
                state = state.copy(errorMessageEvent = triggered(null))
                return@launch
            }

            getPokemon()
        }
    }

    private fun getPokemon() {
        getPokemonUseCase(pokemonId!!).onEach {
            state = when (it) {
                is Resource.Success -> state.copy(pokemon = it.data)
                is Resource.Loading -> state.copy(isLoading = it.isLoading)
                is Resource.Error -> state.copy(
                    pokemon = it.data ?: state.pokemon,
                    isLoading = false,
                    errorMessageEvent = triggered(it.message)
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun getAbility(abilityId: Int) {
        getPokemonAbilityUseCase(abilityId).onEach {
            when (it) {
                is Resource.Success -> Unit
                is Resource.Loading -> {
                    state = state.copy(isLoading = it.isLoading)
                }
                is Resource.Error -> state = state.copy(
                    isLoading = false,
                    errorMessageEvent = triggered(it.message)
                )
            }
        }.launchIn(viewModelScope)
    }

    fun handleUIEvent(event: PokemonDetailsContract.UIEvent) {
        viewModelScope.launch {
            when (event) {
                is PokemonDetailsContract.UIEvent.AbilityDetailsToggle -> {
                    getAbility(event.abilityId)
                }
                is PokemonDetailsContract.UIEvent.AbilityChosen -> {
                    state = state.copy(navigateToAbilityEvent = triggered(event.abilityId))
                }
                is PokemonDetailsContract.UIEvent.Close -> {
                    state = state.copy(navigateUpEvent = triggered)
                }
                is PokemonDetailsContract.UIEvent.ErrorEventConsumed -> {
                    state = state.copy(errorMessageEvent = consumed())
                }
                is PokemonDetailsContract.UIEvent.NavigationEventConsumed -> {
                    state =
                        state.copy(navigateUpEvent = consumed, navigateToAbilityEvent = consumed())
                }
            }
        }
    }
}