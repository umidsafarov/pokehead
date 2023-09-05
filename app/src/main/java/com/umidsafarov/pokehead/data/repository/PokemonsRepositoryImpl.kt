package com.umidsafarov.pokehead.data.repository

import com.umidsafarov.pokehead.common.Resource
import com.umidsafarov.pokehead.data.local.PokeheadDAO
import com.umidsafarov.pokehead.data.local.entitites.AbilityEffectEntity
import com.umidsafarov.pokehead.data.remote.PokeheadService
import com.umidsafarov.pokehead.data.toAbility
import com.umidsafarov.pokehead.data.toPokemon
import com.umidsafarov.pokehead.data.toPokemonEntity
import com.umidsafarov.pokehead.data.toPokemonWithDetailsEntity
import com.umidsafarov.pokehead.domain.model.Ability
import com.umidsafarov.pokehead.domain.model.Pokemon
import com.umidsafarov.pokehead.domain.repository.PokeheadRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PokemonsRepositoryImpl @Inject constructor(
    private val api: PokeheadService,
    private val dao: PokeheadDAO,
) : PokeheadRepository {

    override fun refreshPokemons(count: Int): Flow<Resource<List<Pokemon>>> {
        return flow {
            emit(Resource.Loading(true))

            val remoteData = api.getPokemonsList(count, 0)
            if (remoteData.pokemons == null) {
                emit(Resource.Error())
                emit(Resource.Loading(false))
                return@flow
            }

            dao.clearAll()
            dao.insertPokemons(remoteData.pokemons.map { it.toPokemonEntity() })
            val localData = dao.getPokemons(count, 0)
            emit(Resource.Success(localData.map { it.toPokemon() }))
            emit(Resource.Loading(false))
        }.flowOn(Dispatchers.IO)
    }

    override fun getPokemons(count: Int, offset: Int): Flow<Resource<List<Pokemon>>> {
        return flow {
            emit(Resource.Loading(true))

            val localData = dao.getPokemons(count, offset)

            val dataCached = localData.isNotEmpty() && localData.size == count
            if (dataCached) {
                emit(Resource.Success(localData.map { it.toPokemon() }))
            } else {
                val remoteData = api.getPokemonsList(count, offset)
                if (remoteData.pokemons == null) {
                    emit(Resource.Error())
                } else {
                    dao.insertPokemons(remoteData.pokemons.map { it.toPokemonEntity() })
                    val updatedLocalData = dao.getPokemons(count, offset)
                    emit(Resource.Success(updatedLocalData.map { it.toPokemon() }))
                }
            }

            emit(Resource.Loading(false))
        }.flowOn(Dispatchers.IO)
    }

    override fun getPokemon(pokemonId: Int): Flow<Resource<Pokemon>> {
        return flow {
            emit(Resource.Loading(true))

            val localData = dao.getPokemon(pokemonId)
            emit(Resource.Success(localData.toPokemon()))

            if (localData.pokemonDetailsEntity == null || localData.abilitiesEntity == null) {
                val remoteData = try {
                    api.getPokemonDetails(pokemonId)
                } catch (t: Throwable) {
                    t.printStackTrace()
                    emit(Resource.Error(t.message))
                    null
                }
                remoteData?.let { dto ->
                    dao.insertPokemonDetailsAndAbilities(
                        dto.toPokemonWithDetailsEntity()
                    )
                    val updatedData = dao.getPokemon(pokemonId)
                    emit(Resource.Success(updatedData.toPokemon()))
                }
            }

            emit(Resource.Loading(false))
        }.flowOn(Dispatchers.IO)
    }

    override fun getAbility(abilityId: Int): Flow<Resource<Ability>> {
        return flow {
            emit(Resource.Loading(true))

            val localData = dao.getAbility(abilityId)
            emit(Resource.Success(localData.toAbility()))

            if (localData.effects == null) {
                val remoteData = try {
                    api.getAbilityDetails(abilityId)
                } catch (t: Throwable) {
                    t.printStackTrace()
                    emit(Resource.Error(t.message))
                    null
                }
                remoteData?.let { dto ->
                    dao.insertAbilityEffects(
                        dto.effects.map {
                            AbilityEffectEntity(
                                null,
                                abilityId,
                                it.shortDescription,
                                it.fullDescription
                            )
                        }
                    )
                    val updatedData = dao.getAbility(abilityId)
                    emit(Resource.Success(updatedData.toAbility()))
                }
            }

            emit(Resource.Loading(false))
        }.flowOn(Dispatchers.IO)
    }

}