package com.umidsafarov.pokehead.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PokemonDTO(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("types") val elementTypes: List<ElementType>,
    @SerializedName("abilities") val abilities: List<Ability>,
    @SerializedName("sprites") val spriteUrls: SpriteUrls,
) {
    data class ElementType(
        @SerializedName("type") val type: ResourceDTO,
    )

    data class Ability(
        @SerializedName("ability") val ability: ResourceDTO,
    )

    data class SpriteUrls(
        @SerializedName("front_default") val frontDefaultMale: String?,
        @SerializedName("front_default_female") val frontDefaultFemale: String?,
        @SerializedName("back_default") val backDefaultMale: String?,
        @SerializedName("back_default_female") val backDefaultFemale: String?,
        @SerializedName("front_shiny") val frontShinyMale: String?,
        @SerializedName("front_shiny_female") val frontShinyFemale: String?,
        @SerializedName("back_shiny") val backShinyMale: String?,
        @SerializedName("back_shiny_female") val backShinyFemale: String?,
    )
}