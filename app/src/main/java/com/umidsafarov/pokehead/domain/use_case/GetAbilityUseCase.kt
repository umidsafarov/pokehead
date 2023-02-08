package com.umidsafarov.pokehead.domain.use_case

import com.umidsafarov.pokehead.common.Resource
import com.umidsafarov.pokehead.domain.model.Ability
import com.umidsafarov.pokehead.domain.repository.PokeheadRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAbilityUseCase @Inject constructor(
    private val repository: PokeheadRepository,
) {
    operator fun invoke(
        abilityId: Int
    ): Flow<Resource<Ability>> {
        return repository.getAbility(abilityId)
    }
}