package com.umidsafarov.pokehead.presentation.screen.pokemonslist

import com.umidsafarov.pokehead.presentation.screen.pokemonslist.model.PokemonItemUIModel
import de.palm.composestateevents.StateEventWithContent
import de.palm.composestateevents.consumed

class PokemonsListContract {

    data class State(
        val pokemons: List<PokemonItemUIModel>? = null,
        val isLoading: Boolean = false,
        val isRefreshing:Boolean = false,
        val errorMessageEvent: StateEventWithContent<String?> = consumed(),
        val navigateToPokemonEvent: StateEventWithContent<Int> = consumed()
    )

    sealed class UIEvent {
        class PokemonDetailsToggle(val pokemonId: Int) : UIEvent()
        class PokemonChosen(val pokemonId: Int) : UIEvent()
        object LoadNext : UIEvent()
        object Refresh : UIEvent()
        object ErrorEventConsumed : UIEvent()
        object NavigationEventConsumed : UIEvent()
    }

}