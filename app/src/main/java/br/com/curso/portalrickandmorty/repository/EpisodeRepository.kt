package br.com.curso.portalrickandmorty.repository

import br.com.curso.portalrickandmorty.data.dao.EpisodeDao
import br.com.curso.portalrickandmorty.data.entity.EpisodeEntity
import br.com.curso.portalrickandmorty.domain.model.Episode
import br.com.curso.portalrickandmorty.remote.RetrofitInstance
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class EpisodeRepository(private val dao: EpisodeDao) {

    private val api = RetrofitInstance.api

    fun getAllEpisodes(): Flow<List<Episode>> {
        return dao.getAllEpisodes().map { entities ->
            entities.map { entity ->
                Episode(
                    id = entity.id,
                    name = entity.name,
                    airDate = entity.airDate,
                    episode = entity.episode
                )
            }
        }
    }

    suspend fun syncEpisodes() {
        try {
            val response = api.getEpisodes()
            val entities = response.results.map { dto ->
                EpisodeEntity(
                    id = dto.id,
                    name = dto.name,
                    airDate = dto.airDate,
                    episode = dto.episode
                )
            }
            dao.insertEpisodes(entities)
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getEpisodeById(id: Int): Episode? {
        val entity = dao.getEpisodeById(id)
        return entity?.let {
            Episode(
                id = it.id,
                name = it.name,
                airDate = it.airDate,
                episode = it.episode
            )
        }
    }
}