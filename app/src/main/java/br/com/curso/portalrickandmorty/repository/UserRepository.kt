package br.com.curso.portalrickandmorty.repository

import br.com.curso.portalrickandmorty.data.dao.UserDao
import br.com.curso.portalrickandmorty.data.entity.UserEntity
import kotlinx.coroutines.flow.Flow

class UserRepository(private val dao: UserDao) {

    fun getUser(): Flow<UserEntity?> = dao.getUser()

    suspend fun login(name: String, email: String) {
        val user = UserEntity(
            id = email, // Using email as ID for simplicity
            name = name,
            email = email
        )
        dao.insertUser(user)
    }

    suspend fun logout() {
        dao.deleteUser()
    }
}