package br.com.curso.portalrickandmorty.remote

import br.com.curso.portalrickandmorty.remote.dto.CharacterDto
import br.com.curso.portalrickandmorty.remote.dto.CharacterResponse
import br.com.curso.portalrickandmorty.remote.dto.EpisodeResponse
import br.com.curso.portalrickandmorty.remote.dto.LocationResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface RickAndMortyApi {

    @GET("character")
    suspend fun getCharacters(): CharacterResponse

    @GET("character/{id}")
    suspend fun getCharacterById(
        @Path("id") id: Int
    ): CharacterDto

    @GET("location")
    suspend fun getLocations(): LocationResponse

    @GET("episode")
    suspend fun getEpisodes(): EpisodeResponse
}