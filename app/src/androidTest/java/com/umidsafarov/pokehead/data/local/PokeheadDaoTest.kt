package com.umidsafarov.pokehead.data.local

import androidx.test.filters.SmallTest
import com.google.common.truth.Truth
import com.umidsafarov.pokehead.data.local.entitites.AbilityEffectEntity
import com.umidsafarov.pokehead.data.local.entitites.AbilityEntity
import com.umidsafarov.pokehead.data.local.entitites.PokemonDetailsEntity
import com.umidsafarov.pokehead.data.local.entitites.PokemonEntity
import com.umidsafarov.pokehead.domain.model.Pokemon
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@OptIn(ExperimentalCoroutinesApi::class)
@SmallTest
@HiltAndroidTest
class PokeheadDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)


    @Inject
    @Named("test_db")
    lateinit var database: PokeheadDatabase
    private lateinit var dao: PokeheadDAO

    private val testPokemonId = 1
    private val testPokemonAbilityId = 1

    private val pokemonEntity = PokemonEntity(testPokemonId, "TestName")
    private val pokemonDetailsEntity = PokemonDetailsEntity(
        testPokemonId,
        listOf(Pokemon.ElementType.DARK.name),
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null
    )
    private val pokemonAbilityEntity = AbilityEntity(null, 1, testPokemonId, "TestAbilityName")
    private val pokemonAbilityEffectEntity = AbilityEffectEntity(
        null,
        testPokemonAbilityId,
        "TestShortDescription",
        "TestFullDescription"
    )

    @Before
    fun setUp() {
        hiltRule.inject()
        dao = database.pokeheadDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertPokemon() = runTest {
        dao.insertPokemons(listOf(pokemonEntity))
        val databaseEntity = dao.getPokemon(testPokemonId)
        Truth.assertThat(databaseEntity.pokemonEntity.id).isEqualTo(testPokemonId)
    }

    @Test
    fun insertPokemonWithDetails() = runTest {
        dao.insertPokemons(listOf(pokemonEntity))
        dao.insertPokemonDetails(pokemonDetailsEntity)

        val databaseEntity = dao.getPokemon(testPokemonId)
        Truth.assertThat(databaseEntity.pokemonDetailsEntity?.pokemonId)
            .isEqualTo(testPokemonId)
    }

    @Test
    fun insertPokemonWithAbilities() = runTest {
        dao.insertPokemons(listOf(pokemonEntity))
        dao.insertAbilities(listOf(pokemonAbilityEntity))

        val databaseEntity = dao.getPokemon(testPokemonId)
        Truth.assertThat(databaseEntity.abilitiesEntity?.get(0)?.ability?.pokemonId)
            .isEqualTo(testPokemonId)
    }

    @Test
    fun insertPokemonAbility() = runTest {
        dao.insertAbilities(listOf(pokemonAbilityEntity))

        val databaseEntity = dao.getAbility(testPokemonAbilityId)
        Truth.assertThat(databaseEntity.ability.abilityId).isEqualTo(testPokemonAbilityId)
    }

    @Test
    fun insertPokemonAbilityWithEffects() = runTest {
        dao.insertAbilities(listOf(pokemonAbilityEntity))
        dao.insertAbilityEffects(listOf(pokemonAbilityEffectEntity))

        val databaseEntity = dao.getAbility(testPokemonAbilityId)
        Truth.assertThat(databaseEntity.effects?.get(0)?.abilityId)
            .isEqualTo(testPokemonAbilityId)
    }

    @Test
    fun clearPokemons() {
        dao.insertPokemons(listOf(pokemonEntity))
        dao.clearPokemons()

        val databaseEntities = dao.getPokemons(1, 0)
        Truth.assertThat(databaseEntities).isEmpty()
    }

    @Test
    fun clearAbilities() {
        dao.insertAbilities(listOf(pokemonAbilityEntity))
        dao.clearAbilities()

        val databaseEntities = dao.getAbilities(testPokemonId)
        Truth.assertThat(databaseEntities).isEmpty()
    }

    @Test
    fun clearDatabase() {
        dao.insertPokemons(listOf(pokemonEntity))
        dao.insertPokemonDetails(pokemonDetailsEntity)
        dao.insertAbilities(listOf(pokemonAbilityEntity))
        dao.insertAbilityEffects(listOf(pokemonAbilityEffectEntity))
        dao.clearAll()

        val databasePokemonEntities = dao.getPokemons(1, 0)
        Truth.assertThat(databasePokemonEntities).isEmpty()
        val databaseAbilityEntities = dao.getAbilities(testPokemonId)
        Truth.assertThat(databaseAbilityEntities).isEmpty()
    }


    @Test
    fun getPokemons() {
        dao.insertPokemons(
            listOf(
                pokemonEntity,
                pokemonEntity.copy(id = pokemonEntity.id + 1)
            )
        )

        val databaseEntities = dao.getPokemons(10, 0)
        Truth.assertThat(databaseEntities).hasSize(2)
    }
}