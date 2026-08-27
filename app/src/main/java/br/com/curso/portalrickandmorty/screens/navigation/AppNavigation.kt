package br.com.curso.portalrickandmorty.screens.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.com.curso.portalrickandmorty.data.local.AppDatabase
import br.com.curso.portalrickandmorty.screens.characters.CharacterDetailScreen
import br.com.curso.portalrickandmorty.screens.characters.CharacterViewModel
import br.com.curso.portalrickandmorty.screens.characters.CharactersScreen
import br.com.curso.portalrickandmorty.screens.login.LoginScreen
import br.com.curso.portalrickandmorty.screens.login.LoginViewModel
import br.com.curso.portalrickandmorty.screens.episodes.EpisodeDetailScreen
import br.com.curso.portalrickandmorty.screens.episodes.EpisodeViewModel
import br.com.curso.portalrickandmorty.screens.episodes.EpisodesScreen
import br.com.curso.portalrickandmorty.screens.locations.LocationDetailScreen
import br.com.curso.portalrickandmorty.screens.locations.LocationViewModel
import br.com.curso.portalrickandmorty.screens.locations.LocationsScreen
import br.com.curso.portalrickandmorty.screens.portal.MyPortalScreen
import br.com.curso.portalrickandmorty.screens.portal.PortalViewModel
import br.com.curso.portalrickandmorty.location.LocationManager
import androidx.compose.ui.platform.LocalContext

@Composable
fun AppNavigation(database: AppDatabase) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val locationManager = remember { LocationManager(context) }

    NavHost(
        navController = navController,
        startDestination = Routes.Login.route
    ) {
        // ... (existing)
        composable(Routes.Login.route) {
            LoginScreen(
                viewModel = LoginViewModel(database.userDao()),
                onLoginSuccess = {
                    navController.navigate(Routes.Characters.route) {
                        popUpTo(Routes.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.Characters.route) {
            CharactersScreen(
                viewModel = CharacterViewModel(database.characterDao()),
                onCharacterClick = { id ->
                    navController.navigate(Routes.CharacterDetail.createRoute(id))
                },
                onPortalClick = {
                    navController.navigate(Routes.MyPortal.route)
                }
            )
        }

        composable(
            route = Routes.CharacterDetail.route,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 0
            CharacterDetailScreen(
                id = id,
                viewModel = CharacterViewModel(database.characterDao()),
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Routes.Locations.route) {
            LocationsScreen(
                viewModel = LocationViewModel(database.locationDao())
            )
        }

        composable(
            route = Routes.LocationDetail.route,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 0
            LocationDetailScreen(
                id = id,
                viewModel = LocationViewModel(database.locationDao()),
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Routes.Episodes.route) {
            EpisodesScreen(
                viewModel = EpisodeViewModel(database.episodeDao())
            )
        }

        composable(
            route = Routes.EpisodeDetail.route,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 0
            EpisodeDetailScreen(
                id = id,
                viewModel = EpisodeViewModel(database.episodeDao()),
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Routes.MyPortal.route) {
            MyPortalScreen(
                viewModel = PortalViewModel(locationManager)
            )
        }
    }
}