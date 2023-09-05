package com.umidsafarov.pokehead.presentation.screen.pokemonslist.model

data class PokemonItemUIModel(
    val id: Int,
    val name: String,
    val avatarUrl: String? = null,
    val expanded: Boolean = false,
)