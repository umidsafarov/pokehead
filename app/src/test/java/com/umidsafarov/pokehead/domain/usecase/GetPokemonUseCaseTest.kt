package com.umidsafarov.pokehead.domain.usecase

import com.google.common.truth.Truth
import com.umidsafarov.pokehead.common.Resource
import com.umidsafarov.pokehead.repositories.FakePokeheadRepository
import com.umidsafarov.pokehead.rules.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetPokemonUseCaseTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val pokemonId = 1

    private lateinit var repository: FakePokeheadRepository
    private lateinit var useCase: GetPokemonUseCase

    @Before
    fun setUp() {
        repository = FakePokeheadRepository()
        useCase = GetPokemonUseCase(repository)
    }

    @Test
    fun `loads correct pokemon`() = runTest {
        Truth.assertThat(useCase(pokemonId).firstOrNull { it is Resource.Success }?.data?.id)
            .isEqualTo(pokemonId)
    }

    @Test
    fun `error on loading not existing pokemon`() = runTest {
        Truth.assertThat(useCase(-1).firstOrNull() { it is Resource.Error }).isNotNull()
    }

    @Test
    fun `pokemon has details and abilities on load`() = runTest {
        val list = useCase(1).filter { it is Resource.Success }.toList()
        Truth.assertThat(list[0].data?.details).isNull()
        Truth.assertThat(list[0].data?.abilities).isNull()
        Truth.assertThat(
            Truth.assertThat(list[1].data?.details)
        ).isNotNull()
        Truth.assertThat(
            Truth.assertThat(list[1].data?.abilities)
        ).isNotNull()
    }

    @Test
    fun `error on repository error`() = runTest {
        repository.setShouldReturnError()
        Truth.assertThat(useCase(1).firstOrNull { it is Resource.Error })
            .isInstanceOf(Resource.Error::class.java)
    }
}