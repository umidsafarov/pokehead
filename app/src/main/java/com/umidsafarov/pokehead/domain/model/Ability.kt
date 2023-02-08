package com.umidsafarov.pokehead.domain.model

data class Ability(
    val id: Int,
    val name: String,
    val effects: List<Effect>?
) {
    data class Effect(
        val shortDescription: String,
        val fullDescription: String,
    )
}
