package com.umidsafarov.pokehead.presentation.screen.pokemondetails

import com.umidsafarov.pokehead.domain.model.Pokemon
import de.palm.composestateevents.StateEvent
import de.palm.composestateevents.StateEventWithContent
import de.palm.composestateevents.consumed

class PokemonDetailsContract {

    data class State(
        val pokemon: Pokemon? = null,
        val isLoading: Boolean = false,
        val errorMessageEvent: StateEventWithContent<String?> = consumed(),
        val navigateUpEvent: StateEvent = consumed,
        val navigateToAbilityEvent: StateEventWithContent<Int> = consumed()
    )

    sealed class UIEvent {
        class AbilityDetailsToggle(val abilityId: Int) : UIEvent()
        class AbilityChosen(val abilityId: Int) : UIEvent()
        object Close : UIEvent()
        object ErrorEventConsumed : UIEvent()
        object NavigationEventConsumed : UIEvent()
    }
}