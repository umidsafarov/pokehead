package com.umidsafarov.pokehead.presentation.screen.pokemons_list.model

data class PokemonItemUIModel(
    val id: Int,
    val name: String,
    val avatarUrl: String? = null,
    val expanded: Boolean = false,
)