package br.com.curso.portalrickandmorty.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.curso.portalrickandmorty.data.entity.EpisodeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EpisodeDao {

    @Query("SELECT * FROM episodes")
    fun getAllEpisodes(): Flow<List<EpisodeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEpisodes(episodes: List<EpisodeEntity>)

    @Query("SELECT * FROM episodes WHERE id = :id")
    suspend fun getEpisodeById(id: Int): EpisodeEntity?

    @Query("DELETE FROM episodes")
    suspend fun deleteAllEpisodes()
}