package br.com.curso.portalrickandmorty.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.curso.portalrickandmorty.data.dao.CharacterDao
import br.com.curso.portalrickandmorty.data.dao.EpisodeDao
import br.com.curso.portalrickandmorty.data.dao.LocationDao
import br.com.curso.portalrickandmorty.data.dao.UserDao
import br.com.curso.portalrickandmorty.data.entity.CharacterEntity
import br.com.curso.portalrickandmorty.data.entity.EpisodeEntity
import br.com.curso.portalrickandmorty.data.entity.LocationEntity
import br.com.curso.portalrickandmorty.data.entity.UserEntity

@Database(
    entities = [
        CharacterEntity::class,
        LocationEntity::class,
        EpisodeEntity::class,
        UserEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun characterDao(): CharacterDao
    abstract fun locationDao(): LocationDao
    abstract fun episodeDao(): EpisodeDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "rick_and_morty_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}