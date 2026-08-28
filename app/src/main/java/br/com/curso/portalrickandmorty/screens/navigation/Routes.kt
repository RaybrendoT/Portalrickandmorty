package br.com.curso.portalrickandmorty.screens.navigation

sealed class Routes(val route: String) {
    object Login : Routes("login")
    object Characters : Routes("characters")
    object CharacterDetail : Routes("character/{id}") {
        fun createRoute(id: Int) = "character/$id"
    }
    object Locations : Routes("locations")
    object LocationDetail : Routes("location/{id}") {
        fun createRoute(id: Int) = "location/$id"
    }
    object Episodes : Routes("episodes")
    object EpisodeDetail : Routes("episode/{id}") {
        fun createRoute(id: Int) = "episode/$id"
    }
    object MyPortal : Routes("myPortal")
}