package br.com.curso.portalrickandmorty.repository

import br.com.curso.portalrickandmorty.data.local.dao.CharacterDao
import br.com.curso.portalrickandmorty.data.local.entity.CharacterEntity
import br.com.curso.portalrickandmorty.domain.model.Character
import br.com.curso.portalrickandmorty.remote.RetrofitInstance
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CharacterRepository(private val dao: CharacterDao) {

    private val api = RetrofitInstance.api

    fun getAllCharacters(): Flow<List<Character>> {
        return dao.getAllCharacters().map { entities ->
            entities.map { entity ->
                Character(
                    id = entity.id,
                    name = entity.name,
                    status = entity.status,
                    species = entity.species,
                    image = entity.image,
                    episodes = entity.episodes
                )
            }
        }
    }

    suspend fun syncCharacters() {
        try {
            val response = api.getCharacters()
            val entities = response.results.map { dto ->
                CharacterEntity(
                    id = dto.id,
                    name = dto.name,
                    status = dto.status,
                    species = dto.species,
                    image = dto.image,
                    episodes = dto.episodes
                )
            }
            dao.insertCharacters(entities)
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getCharacterById(id: Int): Character? {
        val local = dao.getCharacterById(id)
        if (local != null) {
            return Character(
                id = local.id,
                name = local.name,
                status = local.status,
                species = local.species,
                image = local.image,
                episodes = local.episodes
            )
        }

        return try {
            val dto = api.getCharacterById(id)
            Character(
                id = dto.id,
                name = dto.name,
                status = dto.status,
                species = dto.species,
                image = dto.image,
                episodes = dto.episodes
            )
        } catch (e: Exception) {
            null
        }
    }
}