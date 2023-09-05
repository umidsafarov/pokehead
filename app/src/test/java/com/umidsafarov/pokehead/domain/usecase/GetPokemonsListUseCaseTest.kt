package com.umidsafarov.pokehead.domain.usecase

import com.google.common.truth.Truth
import com.umidsafarov.pokehead.common.Resource
import com.umidsafarov.pokehead.repositories.FakePokeheadRepository
import com.umidsafarov.pokehead.rules.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetPokemonsListUseCaseTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: FakePokeheadRepository
    private lateinit var useCase: GetPokemonsListUseCase

    @Before
    fun setUp() {
        repository = FakePokeheadRepository()
        useCase = GetPokemonsListUseCase(repository)
    }

    @Test
    fun `loads correct amount of items`() = runTest {
        Truth.assertThat(useCase(2, 0).first { it is Resource.Success }.data).hasSize(2)
    }

    @Test
    fun `loads nothing on passing zero count`() = runTest {
        Truth.assertThat(useCase(0, 0).first { it is Resource.Success }.data).hasSize(0)
    }

    @Test
    fun `loads items starting from given index`() = runTest {
        val firstItems = useCase(2, 0).first { it is Resource.Success }.data
        val secondItems = useCase(1, 1).first { it is Resource.Success }.data
        Truth.assertThat(firstItems?.get(1)?.id).isEqualTo(secondItems?.get(0)?.id)
    }

    @Test
    fun `pokemon has no details and abilities on load`() = runTest {
        Truth.assertThat(
            useCase(0, 0).first { it is Resource.Success }
                .data?.find { it.details != null || it.abilities == null }).isNull()
    }

    @Test
    fun `error on negative count`() = runTest {
        Truth.assertThat(useCase(-1, 0).filterNot { it is Resource.Loading }.first())
            .isInstanceOf(Resource.Error::class.java)
    }

    @Test
    fun `error on negative page index`() = runTest {
        Truth.assertThat(useCase(1, -1).filterNot { it is Resource.Loading }.first())
            .isInstanceOf(Resource.Error::class.java)
    }

    @Test
    fun `error on repository error`() = runTest {
        repository.setShouldReturnError()
        Truth.assertThat(useCase(1, 0).filterNot { it is Resource.Loading }.first())
            .isInstanceOf(Resource.Error::class.java)
    }
}