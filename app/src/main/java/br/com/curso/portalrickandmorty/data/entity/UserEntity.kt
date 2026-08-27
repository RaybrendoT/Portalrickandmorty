package br.com.curso.portalrickandmorty.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: String, // email or uuid
    val name: String,
    val email: String
)