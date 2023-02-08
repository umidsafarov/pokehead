package com.umidsafarov.pokehead.presentation.screen.pokemons_list

import com.umidsafarov.pokehead.presentation.screen.pokemons_list.model.PokemonItemUIModel

class PokemonsListContract {

    data class State(
        val pokemons: List<PokemonItemUIModel>? = null,
        val isLoading: Boolean = false,
        val isRefreshing:Boolean = false,
        val aboutShown:Boolean = false,
        val errorMessage: String? = null,
        val pokemonIdToNavigate: Int? = null
    )

    sealed class Event {
        class PokemonDetailsToggle(val pokemonId: Int) : Event()
        class PokemonChosen(val pokemonId: Int) : Event()
        object LoadNext : Event()
        object Refresh : Event()
        object ShowAbout : Event()
        object HideAbout: Event()
        object ErrorShown : Event()
        object NavigationDone : Event()
    }

}