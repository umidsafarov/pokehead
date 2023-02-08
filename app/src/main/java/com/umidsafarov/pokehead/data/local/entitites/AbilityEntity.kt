package com.umidsafarov.pokehead.data.local.entitites

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "abilities")
data class AbilityEntity(
    @PrimaryKey @ColumnInfo("id") val id: Int?,
    @ColumnInfo(name = "abilityId") val abilityId: Int,
    @ColumnInfo(name = "pokemon_id") val pokemonId: Int,
    @ColumnInfo(name = "name") val name: String,
)
