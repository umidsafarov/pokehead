package com.umidsafarov.pokehead.data.local.entitites

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ability_effects")
data class AbilityEffectEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int?,
    @ColumnInfo(name = "ability_id") val abilityId: Int,
    @ColumnInfo(name = "short_description") val shortDescription: String,
    @ColumnInfo(name = "full_description") val fullDescription: String,
)