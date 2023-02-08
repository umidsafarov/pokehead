package com.umidsafarov.pokehead.data.local

import androidx.room.*
import com.umidsafarov.pokehead.data.local.entitites.*
import javax.inject.Singleton

@Singleton
@Dao
interface PokeheadDAO {

    @Transaction
    @Query("SELECT * FROM pokemons ORDER BY id LIMIT :count OFFSET :offset")
    fun getPokemons(count: Int, offset: Int): List<PokemonWithDetailsEntity>

    @Transaction
    @Query("SELECT * FROM pokemons WHERE id = :pokemonId")
    fun getPokemon(pokemonId: Int): PokemonWithDetailsEntity

    @Transaction
    @Query("SELECT * FROM abilities WHERE pokemon_id = :pokemonId")
    fun getAbilities(pokemonId: Int): List<AbilityWithEffectsEntity>

    @Transaction
    @Query("SELECT * FROM abilities WHERE id = :abilityId")
    fun getAbility(abilityId: Int): AbilityWithEffectsEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPokemons(pokemons: List<PokemonEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPokemonDetails(details: PokemonDetailsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAbilities(abilities: List<AbilityEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAbilityEffects(effects: List<AbilityEffectEntity>)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPokemonDetailsAndAbilities(
        pokemonWithDetails: PokemonWithDetailsEntity,
    ) {
        insertPokemonDetails(pokemonWithDetails.pokemonDetailsEntity!!)
        insertAbilities(pokemonWithDetails.abilitiesEntity!!.map { it.ability })
    }

    @Query("DELETE FROM pokemons")
    fun clearPokemons()

    @Query("DELETE FROM pokemon_details")
    fun clearPokemonDetails()

    @Query("DELETE FROM abilities")
    fun clearAbilities()

    @Query("DELETE FROM ability_effects")
    fun clearAbilityEffects()

    @Transaction
    fun clearAll() {
        clearPokemons()
        clearPokemonDetails()
        clearAbilities()
        clearAbilityEffects()
    }
}