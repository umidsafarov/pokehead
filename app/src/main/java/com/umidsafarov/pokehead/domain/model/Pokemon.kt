package com.umidsafarov.pokehead.domain.model

data class Pokemon(
    val id: Int,
    val name: String,
    val details: Details?,
    val abilities: List<Ability>?
) {
    enum class ElementType {
        NORMAL,
        FIGHTING,
        FLYING,
        POISON,
        GROUND,
        ROCK,
        BUG,
        GHOST,
        STEEL,
        FIRE,
        WATER,
        GRASS,
        ELECTRIC,
        PSYCHIC,
        ICE,
        DRAGON,
        DARK,
        FAIRY,
        UNKNOWN,
        SHADOW,
    }

    data class Details(
        val elementTypes: List<ElementType>,
        val spriteUrls: SpriteUrls,
    ) {
        data class SpriteUrls(
            val frontDefaultMale: String?,
            val frontDefaultFemale: String?,
            val backDefaultMale: String?,
            val backDefaultFemale: String?,
            val frontShinyMale: String?,
            val frontShinyFemale: String?,
            val backShinyMale: String?,
            val backShinyFemale: String?,
        )
    }
}
