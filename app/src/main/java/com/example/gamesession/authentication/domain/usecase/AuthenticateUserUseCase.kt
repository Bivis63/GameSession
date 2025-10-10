package com.example.gamesession.authentication.domain.usecase

import com.example.gamesession.authentication.domain.model.User
import com.example.gamesession.authentication.domain.repository.UserRepository

class AuthenticateUserUseCase(private val userRepository: UserRepository) {

    suspend operator fun invoke(login: String, password: String): User? {
        return userRepository.getUserByLoginAndPassword(login, password)
    }
}