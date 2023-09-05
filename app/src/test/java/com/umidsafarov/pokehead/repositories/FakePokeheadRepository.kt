package com.umidsafarov.pokehead.repositories

import com.umidsafarov.pokehead.common.Resource
import com.umidsafarov.pokehead.domain.model.Ability
import com.umidsafarov.pokehead.domain.model.Pokemon
import com.umidsafarov.pokehead.domain.repository.PokeheadRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakePokeheadRepository : PokeheadRepository {

    private val pokemons = mutableListOf(
        Pokemon(1, "First pokemon", null, null),
        Pokemon(2, "Second pokemon", null, null),
        Pokemon(3, "Third pokemon", null, null),
        Pokemon(4, "Fourth pokemon", null, null),
        Pokemon(5, "Fifth pokemon", null, null),
        Pokemon(6, "Sixth pokemon", null, null),
    )
    private val abilities = mutableListOf(
        Ability(
            1,
            "First ability",
            listOf(Ability.Effect("Short description", "Full description"))
        ),
        Ability(
            2,
            "Second ability",
            listOf(Ability.Effect("Short description", "Full description"))
        ),
        Ability(
            3,
            "Third ability",
            listOf(Ability.Effect("Short description", "Full description"))
        ),
        Ability(
            4,
            "Fourth ability",
            listOf(Ability.Effect("Short description", "Full description"))
        ),
    )

    private var shouldReturnError = false

    fun setShouldReturnError() {
        shouldReturnError = true
    }

    override fun refreshPokemons(count: Int): Flow<Resource<List<Pokemon>>> {
        if (shouldReturnError) return flow {
            emit(Resource.Loading(true))
            emit(Resource.Error("Test error", null))
            emit(Resource.Loading(false))
        }

        if (count < 0) return flow {
            emit(Resource.Loading(true))
            emit(Resource.Error("Invalid count value", null))
            emit(Resource.Loading(false))
        }

        val maxIndex = (pokemons.size).coerceAtMost(count)
        return flow {
            emit(Resource.Loading(true))
            emit(Resource.Success(pokemons.subList(0, maxIndex)))
            emit(Resource.Loading(false))
        }
    }

    override fun getPokemons(count: Int, offset: Int): Flow<Resource<List<Pokemon>>> {
        if (shouldReturnError) return flow {
            emit(Resource.Loading(true))
            emit(Resource.Error("Test error", null))
            emit(Resource.Loading(false))
        }

        if (count < 0 || offset < 0) return flow {
            emit(Resource.Loading(true))
            emit(Resource.Error("Invalid count or offset value", null))
            emit(Resource.Loading(false))
        }

        val maxIndex = (pokemons.size).coerceAtMost(offset + count)
        return flow {
            emit(Resource.Loading(true))
            emit(Resource.Success(pokemons.subList(offset, maxIndex)))
            emit(Resource.Loading(false))
        }
    }

    override fun getPokemon(pokemonId: Int): Flow<Resource<Pokemon>> {
        if (shouldReturnError) return flow {
            emit(Resource.Loading(true))
            emit(Resource.Error("Test error", null))
            emit(Resource.Loading(false))
        }

        val pokemon = pokemons.find { it.id == pokemonId }
        return flow {
            emit(Resource.Loading(true))
            if (pokemon == null) {
                emit(Resource.Error(
                    "Pokemon with id $pokemonId does not exist", null
                ))
            } else {
                emit(Resource.Success(pokemon))
                val pokemonWithDetails = Pokemon(
                    pokemon.id, pokemon.name,
                    Pokemon.Details(
                        listOf(Pokemon.ElementType.GRASS, Pokemon.ElementType.DARK),
                        Pokemon.Details.SpriteUrls(
                            "test url",
                            "test url",
                            "test url",
                            "test url",
                            "test url",
                            "test url",
                            "test url",
                            "test url",
                        )
                    ),
                    listOf(abilities[0], abilities[1])
                )
                emit(Resource.Success(pokemonWithDetails))
            }
            emit(Resource.Loading(false))
        }
    }

    override fun getAbility(abilityId: Int): Flow<Resource<Ability>> {
        if (shouldReturnError) return flow {
            emit(Resource.Loading(true))
            emit(Resource.Error("Test error", null))
            emit(Resource.Loading(false))
        }

        val ability = abilities.find { it.id == abilityId }
        return flow {
            emit(Resource.Loading(true))
            if (ability == null) {
                Resource.Error(
                    "Ability with id $abilityId does not exist", null
                )
            } else {
                Resource.Success(ability)
            }
            emit(Resource.Loading(false))
        }
    }
}