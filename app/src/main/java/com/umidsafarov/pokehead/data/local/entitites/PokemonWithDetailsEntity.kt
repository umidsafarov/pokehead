package com.umidsafarov.pokehead.data.local.entitites

import androidx.room.Embedded
import androidx.room.Relation

data class PokemonWithDetailsEntity(
    @Embedded val pokemonEntity: PokemonEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "pokemon_id",
    )
    val pokemonDetailsEntity: PokemonDetailsEntity?,
    @Relation(
        entity = AbilityEntity::class,
        parentColumn = "id",
        entityColumn = "pokemon_id",
    )
    val abilitiesEntity: List<AbilityWithEffectsEntity>?,
)