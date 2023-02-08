package com.umidsafarov.pokehead.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.umidsafarov.pokehead.data.local.entitites.AbilityEffectEntity
import com.umidsafarov.pokehead.data.local.entitites.AbilityEntity
import com.umidsafarov.pokehead.data.local.entitites.PokemonDetailsEntity
import com.umidsafarov.pokehead.data.local.entitites.PokemonEntity
import com.umidsafarov.pokehead.data.local.helpers.Converter

@Database(
    entities = [PokemonEntity::class, PokemonDetailsEntity::class, AbilityEntity::class, AbilityEffectEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converter::class)
abstract class PokeheadDatabase : RoomDatabase() {
    abstract fun pokeheadDao(): PokeheadDAO
}