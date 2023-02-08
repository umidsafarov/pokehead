package com.umidsafarov.pokehead.domain.use_case

import com.umidsafarov.pokehead.common.Resource
import com.umidsafarov.pokehead.domain.model.Pokemon
import com.umidsafarov.pokehead.domain.repository.PokeheadRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPokemonsListUseCase @Inject constructor(
    private val repository: PokeheadRepository,
) {
    operator fun invoke(pageSize: Int, pageIndex: Int): Flow<Resource<List<Pokemon>>> {
        return repository.getPokemons(pageSize, pageIndex)
    }
}