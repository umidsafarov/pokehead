package com.umidsafarov.pokehead.presentation.screen.pokemondetails

import androidx.lifecycle.SavedStateHandle
import com.google.common.truth.Truth
import com.umidsafarov.pokehead.domain.usecase.GetAbilityUseCase
import com.umidsafarov.pokehead.domain.usecase.GetPokemonUseCase
import com.umidsafarov.pokehead.presentation.navigation.NavigationKeys
import com.umidsafarov.pokehead.repositories.FakePokeheadRepository
import com.umidsafarov.pokehead.rules.MainDispatcherRule
import de.palm.composestateevents.StateEvent
import de.palm.composestateevents.StateEventWithContentConsumed
import de.palm.composestateevents.StateEventWithContentTriggered
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PokemonDetailsViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val pokemonId = 1

    private lateinit var repository: FakePokeheadRepository
    private lateinit var viewModel: PokemonDetailsViewModel

    @Before
    fun setUp() {
        repository = FakePokeheadRepository()
        viewModel = PokemonDetailsViewModel(
            SavedStateHandle(
                mapOf(NavigationKeys.Arg.POKEMON_ID to pokemonId)
            ),
            GetPokemonUseCase(repository),
            GetAbilityUseCase(repository)
        )
    }

    @Test
    fun `loads correct pokemon on initialize`() {
        Truth.assertThat(viewModel.state.pokemon?.id).isEqualTo(pokemonId)
    }

    @Test
    fun `loads pokemon details on initialize`() {
        Truth.assertThat(viewModel.state.pokemon?.details?.spriteUrls?.backDefaultMale).isNotNull()
        Truth.assertThat(viewModel.state.pokemon?.details?.spriteUrls?.frontShinyFemale).isNotNull()
    }

    @Test
    fun `navigating to ability screen on ability choose`() {
        val abilityId = 1
        viewModel.handleUIEvent(PokemonDetailsContract.UIEvent.AbilityChosen(abilityId))
        Truth.assertThat(
            (viewModel.state.navigateToAbilityEvent as StateEventWithContentTriggered).content
        )
            .isEqualTo(abilityId)
    }

    @Test
    fun `navigating event resets on navigation consumed`() {
        viewModel.handleUIEvent(PokemonDetailsContract.UIEvent.AbilityChosen(1))
        viewModel.handleUIEvent(PokemonDetailsContract.UIEvent.NavigationEventConsumed)
        Truth.assertThat(viewModel.state.navigateToAbilityEvent)
            .isInstanceOf(StateEventWithContentConsumed::class.java)
    }

    @Test
    fun `navigating up on close`() {
        viewModel.handleUIEvent(PokemonDetailsContract.UIEvent.Close)
        Truth.assertThat(viewModel.state.navigateUpEvent).isEqualTo(StateEvent.Triggered)
    }

    @Test
    fun `navigating up resets on navigation consumed`() {
        viewModel.handleUIEvent(PokemonDetailsContract.UIEvent.Close)
        viewModel.handleUIEvent(PokemonDetailsContract.UIEvent.NavigationEventConsumed)
        Truth.assertThat(viewModel.state.navigateUpEvent).isEqualTo(StateEvent.Consumed)
    }

    @Test
    fun `shows error message on repository error`() {
        repository.setShouldReturnError()
        viewModel.handleUIEvent(PokemonDetailsContract.UIEvent.AbilityDetailsToggle(1))
        Truth.assertThat(viewModel.state.errorMessageEvent)
            .isInstanceOf(StateEventWithContentTriggered::class.java)
    }

    @Test
    fun `error message resets on error consume`() {
        repository.setShouldReturnError()
        viewModel.handleUIEvent(PokemonDetailsContract.UIEvent.AbilityDetailsToggle(1))
        viewModel.handleUIEvent(PokemonDetailsContract.UIEvent.ErrorEventConsumed)
        Truth.assertThat(viewModel.state.errorMessageEvent)
            .isInstanceOf(StateEventWithContentConsumed::class.java)
    }
}