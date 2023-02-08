package com.umidsafarov.pokehead.domain.repository

import com.umidsafarov.pokehead.common.Resource
import com.umidsafarov.pokehead.domain.model.Ability
import com.umidsafarov.pokehead.domain.model.Pokemon
import kotlinx.coroutines.flow.Flow

interface PokeheadRepository {
    fun refreshPokemons(count: Int): Flow<Resource<List<Pokemon>>>
    fun getPokemons(count: Int, offset: Int): Flow<Resource<List<Pokemon>>>
    fun getPokemon(pokemonId: Int): Flow<Resource<Pokemon>>
    fun getAbility(abilityId: Int): Flow<Resource<Ability>>
}