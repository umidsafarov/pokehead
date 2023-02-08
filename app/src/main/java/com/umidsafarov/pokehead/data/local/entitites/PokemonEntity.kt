package com.umidsafarov.pokehead.data.local.entitites

import androidx.room.*

@Entity(tableName = "pokemons")
data class PokemonEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "name") val name: String,
)