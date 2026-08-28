package br.com.curso.portalrickandmorty.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.curso.portalrickandmorty.data.local.dao.CharacterDao
import br.com.curso.portalrickandmorty.data.local.dao.EpisodeDao
import br.com.curso.portalrickandmorty.data.local.dao.LocationDao
import br.com.curso.portalrickandmorty.data.local.dao.UserDao
import br.com.curso.portalrickandmorty.data.local.entity.CharacterEntity
import br.com.curso.portalrickandmorty.data.local.entity.EpisodeEntity
import br.com.curso.portalrickandmorty.data.local.entity.LocationEntity
import br.com.curso.portalrickandmorty.data.local.entity.UserEntity
import androidx.room.TypeConverters

@Database(
    entities = [
        CharacterEntity::class,
        LocationEntity::class,
        EpisodeEntity::class,
        UserEntity::class
    ],
    version = 2, // Incremented version because of schema change
    exportSchema = false
)
@TypeConverters(Converters::class)
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
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}