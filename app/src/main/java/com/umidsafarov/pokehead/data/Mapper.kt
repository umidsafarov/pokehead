package com.umidsafarov.pokehead.data

import com.umidsafarov.pokehead.data.local.entitites.*
import com.umidsafarov.pokehead.data.remote.dto.AbilityDTO
import com.umidsafarov.pokehead.data.remote.dto.PokemonDTO
import com.umidsafarov.pokehead.data.remote.dto.ResourceDTO
import com.umidsafarov.pokehead.domain.model.Ability
import com.umidsafarov.pokehead.domain.model.Pokemon

private const val ELEMENT_TYPE_NORMAL = "normal"
private const val ELEMENT_TYPE_FIGHTING = "fighting"
private const val ELEMENT_TYPE_FLYING = "flying"
private const val ELEMENT_TYPE_POISON = "poison"
private const val ELEMENT_TYPE_GROUND = "ground"
private const val ELEMENT_TYPE_ROCK = "rock"
private const val ELEMENT_TYPE_BUG = "bug"
private const val ELEMENT_TYPE_GHOST = "ghost"
private const val ELEMENT_TYPE_FIRE = "fire"
private const val ELEMENT_TYPE_STEEL = "steel"
private const val ELEMENT_TYPE_WATER = "water"
private const val ELEMENT_TYPE_GRASS = "grass"
private const val ELEMENT_TYPE_ELECTRIC = "electric"
private const val ELEMENT_TYPE_PSYCHIC = "psychic"
private const val ELEMENT_TYPE_ICE = "ice"
private const val ELEMENT_TYPE_DRAGON = "dragon"
private const val ELEMENT_TYPE_DARK = "dark"
private const val ELEMENT_TYPE_FAIRY = "fairy"
private const val ELEMENT_TYPE_UNKNOWN = "unknown"
private const val ELEMENT_TYPE_SHADOW = "shadow"

fun ResourceDTO.toPokemonEntity() =
    PokemonEntity(getIdFromUrl(url), name)

fun ResourceDTO.toAbilityEntity(pokemonId: Int) =
    AbilityEntity(null, getIdFromUrl(url), pokemonId, name)

fun ResourceDTO.toAbilityWithEffectsEntry(pokemonId: Int) =
    AbilityWithEffectsEntity(AbilityEntity(null, getIdFromUrl(url), pokemonId, name), null)

fun PokemonDTO.toPokemonWithDetailsEntity() = PokemonWithDetailsEntity(
    PokemonEntity(id, name),
    PokemonDetailsEntity(
        id,
        elementTypes.map { it.type.name },
        spriteUrls.frontDefaultMale,
        spriteUrls.frontDefaultFemale,
        spriteUrls.backDefaultMale,
        spriteUrls.backDefaultFemale,
        spriteUrls.frontShinyMale,
        spriteUrls.frontShinyFemale,
        spriteUrls.backShinyMale,
        spriteUrls.backShinyFemale,
    ),
    abilities.map { it.ability.toAbilityWithEffectsEntry(id) },
)

fun AbilityDTO.toAbilityWithEffectsEntity(pokemonId: Int) = AbilityWithEffectsEntity(
    AbilityEntity(null, id, pokemonId, name),
    effects.map { AbilityEffectEntity(null, id, it.shortDescription, it.fullDescription) })


fun PokemonEntity.toPokemon() =
    Pokemon(id, name, null, null)

fun PokemonWithDetailsEntity.toPokemon() = Pokemon(
    pokemonEntity.id, pokemonEntity.name,
    if (pokemonDetailsEntity == null) {
        null
    } else {
        Pokemon.Details(
            pokemonDetailsEntity.elementTypes.map { elementTypeFromString(it) },
            Pokemon.Details.SpriteUrls(
                pokemonDetailsEntity.frontDefaultMale,
                pokemonDetailsEntity.frontDefaultFemale,
                pokemonDetailsEntity.backDefaultMale,
                pokemonDetailsEntity.backDefaultFemale,
                pokemonDetailsEntity.frontShinyMale,
                pokemonDetailsEntity.frontShinyFemale,
                pokemonDetailsEntity.backShinyMale,
                pokemonDetailsEntity.backShinyFemale,
            )
        )
    },
    abilitiesEntity?.map { it.toAbility() }
)

fun AbilityEntity.toAbility() =
    Ability(abilityId, name, null)

fun AbilityWithEffectsEntity.toAbility() = Ability(ability.abilityId, ability.name,
    effects?.map { Ability.Effect(it.shortDescription, it.fullDescription) })


private fun getIdFromUrl(url: String): Int {
    if (url.isEmpty())
        throw Throwable("Can not parse ID from empty URL")

    val id = if (url.last() == '/') {
        url.substring(url.lastIndexOf("/", url.length - 2) + 1 until url.length - 1)
    } else {
        url.substring(url.lastIndexOf("/") + 1)
    }.toIntOrNull()
    id ?: throw Throwable("Can not parse ID from given Url")

    return id
}

private fun elementTypeFromString(str: String): Pokemon.ElementType {
    return when (str) {
        ELEMENT_TYPE_NORMAL -> {
            Pokemon.ElementType.NORMAL
        }
        ELEMENT_TYPE_FIGHTING -> {
            Pokemon.ElementType.FIGHTING
        }
        ELEMENT_TYPE_FLYING -> {
            Pokemon.ElementType.FLYING
        }
        ELEMENT_TYPE_POISON -> {
            Pokemon.ElementType.POISON
        }
        ELEMENT_TYPE_GROUND -> {
            Pokemon.ElementType.GROUND
        }
        ELEMENT_TYPE_ROCK -> {
            Pokemon.ElementType.ROCK
        }
        ELEMENT_TYPE_BUG -> {
            Pokemon.ElementType.BUG
        }
        ELEMENT_TYPE_GHOST -> {
            Pokemon.ElementType.GHOST
        }
        ELEMENT_TYPE_FIRE -> {
            Pokemon.ElementType.FIRE
        }
        ELEMENT_TYPE_STEEL -> {
            Pokemon.ElementType.STEEL
        }
        ELEMENT_TYPE_WATER -> {
            Pokemon.ElementType.WATER
        }
        ELEMENT_TYPE_GRASS -> {
            Pokemon.ElementType.GRASS
        }
        ELEMENT_TYPE_ELECTRIC -> {
            Pokemon.ElementType.ELECTRIC
        }
        ELEMENT_TYPE_PSYCHIC -> {
            Pokemon.ElementType.PSYCHIC
        }
        ELEMENT_TYPE_ICE -> {
            Pokemon.ElementType.ICE
        }
        ELEMENT_TYPE_DRAGON -> {
            Pokemon.ElementType.DRAGON
        }
        ELEMENT_TYPE_DARK -> {
            Pokemon.ElementType.DARK
        }
        ELEMENT_TYPE_FAIRY -> {
            Pokemon.ElementType.FAIRY
        }
        ELEMENT_TYPE_UNKNOWN -> {
            Pokemon.ElementType.UNKNOWN
        }
        ELEMENT_TYPE_SHADOW -> {
            Pokemon.ElementType.SHADOW
        }
        else -> {
            Pokemon.ElementType.UNKNOWN
        }
    }
}