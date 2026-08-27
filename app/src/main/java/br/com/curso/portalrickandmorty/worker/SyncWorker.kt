package br.com.curso.portalrickandmorty.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import br.com.curso.portalrickandmorty.PortalApplication
import br.com.curso.portalrickandmorty.repository.CharacterRepository
import br.com.curso.portalrickandmorty.repository.EpisodeRepository
import br.com.curso.portalrickandmorty.repository.LocationRepository

class SyncWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val application = applicationContext as PortalApplication
        val database = application.database

        val characterRepository = CharacterRepository(database.characterDao())
        val locationRepository = LocationRepository(database.locationDao())
        val episodeRepository = EpisodeRepository(database.episodeDao())

        return try {
            characterRepository.syncCharacters()
            locationRepository.syncLocations()
            episodeRepository.syncEpisodes()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}