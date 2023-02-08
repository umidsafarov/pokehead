package com.umidsafarov.pokehead.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.umidsafarov.pokehead.presentation.screen.pokemon_details.PokemonDetailsScreen
import com.umidsafarov.pokehead.presentation.screen.pokemon_details.PokemonDetailsViewModel
import com.umidsafarov.pokehead.presentation.screen.pokemons_list.PokemonsListScreen
import com.umidsafarov.pokehead.presentation.screen.pokemons_list.PokemonsListViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = NavigationKeys.Route.POKEMONS_LIST) {
        composable(route = NavigationKeys.Route.POKEMONS_LIST) {
            PokemonsListDestination(navController = navController)
        }
        composable(
            route = NavigationKeys.Route.POKEMON_DETAILS,
            arguments = listOf(navArgument(NavigationKeys.Arg.POKEMON_ID) {
                type = NavType.IntType
            })
        ) {
            PokemonDetailsDestination(navController = navController)
        }
    }
}

@Composable
private fun PokemonsListDestination(navController: NavController) {
    val viewModel: PokemonsListViewModel = hiltViewModel()
    PokemonsListScreen(
        state = viewModel.state,
        sendEvent = { viewModel.handleInputEvent(it) },
        onNavigateToPokemon = { pokemonId ->
            navController.navigate("${NavigationKeys.Route.POKEMONS_LIST}/${pokemonId}")
        }
    )
}

@Composable
private fun PokemonDetailsDestination(navController: NavController) {
    val viewModel: PokemonDetailsViewModel = hiltViewModel()
    PokemonDetailsScreen(
        state = viewModel.state,
        sendEvent = { viewModel.handleInputEvent(it) },
        navigateUp = { navController.popBackStack() }
    )
}