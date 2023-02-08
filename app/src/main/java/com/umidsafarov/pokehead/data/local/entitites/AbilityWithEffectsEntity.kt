package com.umidsafarov.pokehead.data.local.entitites

import androidx.room.Embedded
import androidx.room.Relation

data class AbilityWithEffectsEntity(
    @Embedded val ability: AbilityEntity,
    @Relation(parentColumn = "id", entityColumn = "ability_id")
    val effects: List<AbilityEffectEntity>?
)