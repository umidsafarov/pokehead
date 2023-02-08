package com.umidsafarov.pokehead.presentation.common.model

import com.umidsafarov.pokehead.R
import com.umidsafarov.pokehead.domain.model.Pokemon

fun Pokemon.ElementType.toDrawableResource(): Int {
    return when (this) {
        Pokemon.ElementType.NORMAL -> {
            R.drawable.ic_type_normal
        }
        Pokemon.ElementType.FIGHTING -> {
            R.drawable.ic_type_fighting
        }
        Pokemon.ElementType.FLYING -> {
            R.drawable.ic_type_flying
        }
        Pokemon.ElementType.POISON -> {
            R.drawable.ic_type_poison
        }
        Pokemon.ElementType.GROUND -> {
            R.drawable.ic_type_ground
        }
        Pokemon.ElementType.ROCK -> {
            R.drawable.ic_type_rock
        }
        Pokemon.ElementType.BUG -> {
            R.drawable.ic_type_bug
        }
        Pokemon.ElementType.GHOST -> {
            R.drawable.ic_type_ghost
        }
        Pokemon.ElementType.FIRE -> {
            R.drawable.ic_type_fire
        }
        Pokemon.ElementType.STEEL -> {
            R.drawable.ic_type_steel
        }
        Pokemon.ElementType.WATER -> {
            R.drawable.ic_type_water
        }
        Pokemon.ElementType.GRASS -> {
            R.drawable.ic_type_grass
        }
        Pokemon.ElementType.ELECTRIC -> {
            R.drawable.ic_type_electric
        }
        Pokemon.ElementType.PSYCHIC -> {
            R.drawable.ic_type_psychic
        }
        Pokemon.ElementType.ICE -> {
            R.drawable.ic_type_ice
        }
        Pokemon.ElementType.DRAGON -> {
            R.drawable.ic_type_dragon
        }
        Pokemon.ElementType.DARK -> {
            R.drawable.ic_type_dark
        }
        Pokemon.ElementType.FAIRY -> {
            R.drawable.ic_type_fairy
        }
        Pokemon.ElementType.UNKNOWN -> {
            R.drawable.ic_type_unknown
        }
        Pokemon.ElementType.SHADOW -> {
            R.drawable.ic_type_shadow
        }
    }
}

fun Pokemon.Details.SpriteUrls.availableSpritesList(): List<String> {
    val result = mutableListOf<String>()
    frontDefaultMale?.let { result.add(it) }
    frontDefaultFemale?.let { result.add(it) }
    backDefaultMale?.let { result.add(it) }
    backDefaultFemale?.let { result.add(it) }
    frontShinyMale?.let { result.add(it) }
    frontShinyFemale?.let { result.add(it) }
    backShinyMale?.let { result.add(it) }
    backShinyFemale?.let { result.add(it) }
    return result
}