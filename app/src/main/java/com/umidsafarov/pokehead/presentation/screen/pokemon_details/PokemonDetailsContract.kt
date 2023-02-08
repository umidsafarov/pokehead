package com.umidsafarov.pokehead.presentation.screen.pokemon_details

import com.umidsafarov.pokehead.domain.model.Pokemon

class PokemonDetailsContract {

    data class State(
        val pokemon: Pokemon? = null,
        val isLoading: Boolean = false,
        val errorMessage: String? = null,
        val navigateUp: Boolean = false,
        val abilityIdToNavigate: Int? = null
    )

    sealed class Event {
        class AbilityChosen(val abilityId: Int) : Event()
        object Close : Event()
        object NavigationDone : Event()
    }
}