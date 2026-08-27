package br.com.curso.portalrickandmorty.screens.navigation

sealed class Routes(val route: String) {
    object Login : Routes("login")
    object Characters : Routes("characters")
    object CharacterDetail : Routes("character_detail/{id}") {
        fun createRoute(id: Int) = "character_detail/$id"
    }
    object Locations : Routes("locations")
    object LocationDetail : Routes("location_detail/{id}") {
        fun createRoute(id: Int) = "location_detail/$id"
    }
    object Episodes : Routes("episodes")
    object EpisodeDetail : Routes("episode_detail/{id}") {
        fun createRoute(id: Int) = "episode_detail/$id"
    }
    object MyPortal : Routes("my_portal")
}