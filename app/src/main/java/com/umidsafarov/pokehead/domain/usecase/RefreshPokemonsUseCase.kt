package com.umidsafarov.pokehead.domain.usecase

import com.umidsafarov.pokehead.common.Resource
import com.umidsafarov.pokehead.domain.model.Pokemon
import com.umidsafarov.pokehead.domain.repository.PokeheadRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RefreshPokemonsUseCase @Inject constructor(
    private val repository: PokeheadRepository,
) {
    operator fun invoke(pageSize: Int): Flow<Resource<List<Pokemon>>> {
        return repository.refreshPokemons(pageSize)
    }
}