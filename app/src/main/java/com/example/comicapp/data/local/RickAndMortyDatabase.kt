package com.example.comicapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.comicapp.data.local.dao.CharacterDao
import com.example.comicapp.data.local.dao.RemoteKeyDao
import com.example.comicapp.data.local.entity.CharacterEntity
import com.example.comicapp.data.local.entity.RemoteKeyEntity

@Database(
    entities = [CharacterEntity::class, RemoteKeyEntity::class],
    version = 1,
    exportSchema = false
)
abstract class RickAndMortyDatabase : RoomDatabase() {
    abstract val characterDao: CharacterDao
    abstract val remoteKeyDao: RemoteKeyDao
}
