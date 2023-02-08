package com.umidsafarov.pokehead.presentation.navigation

object NavigationKeys {
    object Arg {
        const val POKEMON_ID = "pokemon_id"
    }

    object Route {
        const val POKEMONS_LIST = "pokemons_list"
        const val POKEMON_DETAILS = "${POKEMONS_LIST}/{${Arg.POKEMON_ID}}"
    }
}