package com.umidsafarov.pokehead.data.local.entitites

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon_details")
data class PokemonDetailsEntity(
    @PrimaryKey @ColumnInfo(name = "pokemon_id") val pokemonId: Int,
    @ColumnInfo(name = "element_types") val elementTypes: List<String>,
    @ColumnInfo(name = "front_default") val frontDefaultMale: String?,
    @ColumnInfo(name = "front_default_female") val frontDefaultFemale: String?,
    @ColumnInfo(name = "back_default") val backDefaultMale: String?,
    @ColumnInfo(name = "back_default_female") val backDefaultFemale: String?,
    @ColumnInfo(name = "front_shiny") val frontShinyMale: String?,
    @ColumnInfo(name = "front_shiny_female") val frontShinyFemale: String?,
    @ColumnInfo(name = "back_shiny") val backShinyMale: String?,
    @ColumnInfo(name = "back_shiny_female") val backShinyFemale: String?,
)