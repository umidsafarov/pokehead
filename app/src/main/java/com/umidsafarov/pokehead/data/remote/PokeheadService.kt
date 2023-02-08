package com.umidsafarov.pokehead.data.remote

import com.umidsafarov.pokehead.data.remote.dto.AbilityDTO
import com.umidsafarov.pokehead.data.remote.dto.PokemonDTO
import com.umidsafarov.pokehead.data.remote.dto.PokemonsListDTO
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeheadService {
    @GET("pokemon/")
    suspend fun getPokemonsList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int?,
    ): PokemonsListDTO

    @GET("pokemon/{pokemonId}/")
    suspend fun getPokemonDetails(
        @Path("pokemonId") pokemonId: Int
    ): PokemonDTO

    @GET("ability/{abilityId}/")
    suspend fun getAbilityDetails(
        @Path("abilityId") abilityId: Int
    ): AbilityDTO
}
