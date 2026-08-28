package br.com.curso.portalrickandmorty.screens.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Animation
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.com.curso.portalrickandmorty.data.local.AppDatabase
import br.com.curso.portalrickandmorty.location.LocationManager
import br.com.curso.portalrickandmorty.screens.characters.CharacterDetailScreen
import br.com.curso.portalrickandmorty.screens.characters.CharacterViewModel
import br.com.curso.portalrickandmorty.screens.characters.CharactersScreen
import br.com.curso.portalrickandmorty.screens.episodes.EpisodeDetailScreen
import br.com.curso.portalrickandmorty.screens.episodes.EpisodeViewModel
import br.com.curso.portalrickandmorty.screens.episodes.EpisodesScreen
import br.com.curso.portalrickandmorty.screens.locations.LocationDetailScreen
import br.com.curso.portalrickandmorty.screens.locations.LocationViewModel
import br.com.curso.portalrickandmorty.screens.locations.LocationsScreen
import br.com.curso.portalrickandmorty.screens.login.LoginScreen
import br.com.curso.portalrickandmorty.screens.login.LoginViewModel
import br.com.curso.portalrickandmorty.screens.portal.MyPortalScreen
import br.com.curso.portalrickandmorty.screens.portal.PortalViewModel

@Composable
fun AppNavigation(database: AppDatabase) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val locationManager = remember { LocationManager(context) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showBottomBar = currentDestination?.route != Routes.Login.route

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    val items = listOf(
                        Routes.Characters to "Characters",
                        Routes.Locations to "Locations",
                        Routes.Episodes to "Episodes",
                        Routes.MyPortal to "Portal"
                    )
                    val icons = listOf(
                        Icons.Default.List,
                        Icons.Default.LocationOn,
                        Icons.Default.Movie,
                        Icons.Default.Animation
                    )

                    items.forEachIndexed { index, item ->
                        NavigationBarItem(
                            icon = { Icon(icons[index], contentDescription = item.second) },
                            label = { Text(item.second) },
                            selected = currentDestination?.hierarchy?.any { it.route == item.first.route } == true,
                            onClick = {
                                navController.navigate(item.first.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.Login.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.Login.route) {
                val loginViewModel: LoginViewModel = viewModel(
                    factory = object : ViewModelProvider.Factory {
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return LoginViewModel(database.userDao()) as T
                        }
                    }
                )
                LoginScreen(
                    viewModel = loginViewModel,
                    onLoginSuccess = {
                        navController.navigate(Routes.Characters.route) {
                            popUpTo(Routes.Login.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(Routes.Characters.route) {
                val characterViewModel: CharacterViewModel = viewModel(
                    factory = object : ViewModelProvider.Factory {
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return CharacterViewModel(database.characterDao()) as T
                        }
                    }
                )
                CharactersScreen(
                    viewModel = characterViewModel,
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
                val characterViewModel: CharacterViewModel = viewModel(
                    factory = object : ViewModelProvider.Factory {
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return CharacterViewModel(database.characterDao()) as T
                        }
                    }
                )
                CharacterDetailScreen(
                    id = id,
                    viewModel = characterViewModel,
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable(Routes.Locations.route) {
                val locationViewModel: LocationViewModel = viewModel(
                    factory = object : ViewModelProvider.Factory {
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return LocationViewModel(database.locationDao()) as T
                        }
                    }
                )
                LocationsScreen(
                    viewModel = locationViewModel
                )
            }

            composable(
                route = Routes.LocationDetail.route,
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getInt("id") ?: 0
                val locationViewModel: LocationViewModel = viewModel(
                    factory = object : ViewModelProvider.Factory {
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return LocationViewModel(database.locationDao()) as T
                        }
                    }
                )
                LocationDetailScreen(
                    id = id,
                    viewModel = locationViewModel,
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable(Routes.Episodes.route) {
                val episodeViewModel: EpisodeViewModel = viewModel(
                    factory = object : ViewModelProvider.Factory {
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return EpisodeViewModel(database.episodeDao()) as T
                        }
                    }
                )
                EpisodesScreen(
                    viewModel = episodeViewModel
                )
            }

            composable(
                route = Routes.EpisodeDetail.route,
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getInt("id") ?: 0
                val episodeViewModel: EpisodeViewModel = viewModel(
                    factory = object : ViewModelProvider.Factory {
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return EpisodeViewModel(database.episodeDao()) as T
                        }
                    }
                )
                EpisodeDetailScreen(
                    id = id,
                    viewModel = episodeViewModel,
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable(Routes.MyPortal.route) {
                val portalViewModel: PortalViewModel = viewModel(
                    factory = object : ViewModelProvider.Factory {
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return PortalViewModel(locationManager) as T
                        }
                    }
                )
                MyPortalScreen(
                    viewModel = portalViewModel
                )
            }
        }
    }
}