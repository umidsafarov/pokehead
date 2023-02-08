package com.umidsafarov.pokehead.presentation.screen.pokemon_details

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umidsafarov.pokehead.R
import com.umidsafarov.pokehead.common.Resource
import com.umidsafarov.pokehead.domain.use_case.GetAbilityUseCase
import com.umidsafarov.pokehead.domain.use_case.GetPokemonUseCase
import com.umidsafarov.pokehead.presentation.navigation.NavigationKeys
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    @ApplicationContext private val context: Context,
    private val getPokemonUseCase: GetPokemonUseCase,
    private val getPokemonAbilityUseCase: GetAbilityUseCase,
) : ViewModel() {

    private val pokemonId = savedStateHandle.get<Int>(NavigationKeys.Arg.POKEMON_ID)

    var state by mutableStateOf(PokemonDetailsContract.State())
        private set

    init {
        viewModelScope.launch {
            if (pokemonId == null) {
                state = state.copy(errorMessage = context.getString(R.string.error_unknown_pokemon))
                return@launch
            }

            getPokemon()
        }
    }

    private fun getPokemon() {
        getPokemonUseCase(pokemonId!!).onEach {
            when (it) {
                is Resource.Success -> state = state.copy(pokemon = it.data)
                is Resource.Loading -> state = state.copy(isLoading = it.isLoading)
                is Resource.Error -> state =
                    state.copy(
                        pokemon = it.data ?: state.pokemon,
                        isLoading = false,
                        errorMessage = it.message
                    )
            }
        }.launchIn(viewModelScope)
    }

    fun handleInputEvent(event: PokemonDetailsContract.Event) {
        viewModelScope.launch {
            when (event) {
                is PokemonDetailsContract.Event.AbilityChosen -> {
                    state = state.copy(abilityIdToNavigate = event.abilityId)
                }
                is PokemonDetailsContract.Event.Close -> {
                    state = state.copy(navigateUp = true)
                }
                is PokemonDetailsContract.Event.NavigationDone -> {
                    state = state.copy(abilityIdToNavigate = null, navigateUp = false)
                }
            }
        }
    }
}