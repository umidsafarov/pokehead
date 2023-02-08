package com.umidsafarov.pokehead.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PokemonsListDTO(
    @SerializedName("next") val next:String?,
    @SerializedName("results") val pokemons: List<ResourceDTO>?,
)