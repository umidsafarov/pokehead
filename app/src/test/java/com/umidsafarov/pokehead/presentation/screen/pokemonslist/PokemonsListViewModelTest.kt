package com.umidsafarov.pokehead.presentation.screen.pokemonslist

import com.google.common.truth.Truth
import com.umidsafarov.pokehead.domain.usecase.GetPokemonUseCase
import com.umidsafarov.pokehead.domain.usecase.GetPokemonsListUseCase
import com.umidsafarov.pokehead.domain.usecase.RefreshPokemonsUseCase
import com.umidsafarov.pokehead.repositories.FakePokeheadRepository
import com.umidsafarov.pokehead.rules.MainDispatcherRule
import de.palm.composestateevents.StateEventWithContentConsumed
import de.palm.composestateevents.StateEventWithContentTriggered
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PokemonsListViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: FakePokeheadRepository
    private lateinit var viewModel: PokemonsListViewModel

    @Before
    fun setUp() {
        repository = FakePokeheadRepository()
        viewModel = PokemonsListViewModel(
            GetPokemonsListUseCase(repository),
            RefreshPokemonsUseCase(repository),
            GetPokemonUseCase(repository),
        )
    }

    @Test
    fun `loads items on initialize`() {
        Truth.assertThat(viewModel.state.pokemons).isNotEmpty()
    }

    @Test
    fun `loads items on refresh`() {
        viewModel.handleUIEvent(PokemonsListContract.UIEvent.Refresh)
        Truth.assertThat(viewModel.state.pokemons).isNotEmpty()
    }

    @Test
    fun `loads avatar on pokemon details toggle`() {
        val pokemonId = 1
        viewModel.handleUIEvent(PokemonsListContract.UIEvent.PokemonDetailsToggle(pokemonId))
        Truth.assertThat(viewModel.state.pokemons?.find { it.id == pokemonId }?.avatarUrl)
            .isNotNull()
    }

    @Test
    fun `navigating to pokemon details screen on pokemon choose`() {
        val pokemonId = 1
        viewModel.handleUIEvent(PokemonsListContract.UIEvent.PokemonChosen(pokemonId))
        Truth.assertThat(
            (viewModel.state.navigateToPokemonEvent as StateEventWithContentTriggered).content
        )
            .isEqualTo(pokemonId)
    }

    @Test
    fun `navigating event resets on navigation consumed`() {
        viewModel.handleUIEvent(PokemonsListContract.UIEvent.PokemonChosen(1))
        viewModel.handleUIEvent(PokemonsListContract.UIEvent.NavigationEventConsumed)
        Truth.assertThat(viewModel.state.navigateToPokemonEvent)
            .isInstanceOf(StateEventWithContentConsumed::class.java)
    }

    @Test
    fun `shows error message on repository error`() {
        repository.setShouldReturnError()
        viewModel.handleUIEvent(PokemonsListContract.UIEvent.Refresh)
        Truth.assertThat(viewModel.state.errorMessageEvent)
            .isInstanceOf(StateEventWithContentTriggered::class.java)
    }

    @Test
    fun `error message resets on error consume`() {
        repository.setShouldReturnError()
        viewModel.handleUIEvent(PokemonsListContract.UIEvent.Refresh)
        viewModel.handleUIEvent(PokemonsListContract.UIEvent.ErrorEventConsumed)
        Truth.assertThat(viewModel.state.errorMessageEvent)
            .isInstanceOf(StateEventWithContentConsumed::class.java)
    }
}