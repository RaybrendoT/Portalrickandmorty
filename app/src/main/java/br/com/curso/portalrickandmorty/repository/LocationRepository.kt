package br.com.curso.portalrickandmorty.repository

import br.com.curso.portalrickandmorty.data.local.dao.LocationDao
import br.com.curso.portalrickandmorty.data.local.entity.LocationEntity
import br.com.curso.portalrickandmorty.domain.model.Location
import br.com.curso.portalrickandmorty.remote.RetrofitInstance
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocationRepository(private val dao: LocationDao) {

    private val api = RetrofitInstance.api

    fun getAllLocations(): Flow<List<Location>> {
        return dao.getAllLocations().map { entities ->
            entities.map { entity ->
                Location(
                    id = entity.id,
                    name = entity.name,
                    type = entity.type,
                    dimension = entity.dimension
                )
            }
        }
    }

    suspend fun syncLocations() {
        try {
            val response = api.getLocations()
            val entities = response.results.map { dto ->
                LocationEntity(
                    id = dto.id,
                    name = dto.name,
                    type = dto.type,
                    dimension = dto.dimension
                )
            }
            dao.insertLocations(entities)
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getLocationById(id: Int): Location? {
        val entity = dao.getLocationById(id)
        return entity?.let {
            Location(
                id = it.id,
                name = it.name,
                type = it.type,
                dimension = it.dimension
            )
        }
    }
}