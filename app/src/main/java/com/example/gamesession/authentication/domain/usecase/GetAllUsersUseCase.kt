package com.example.gamesession.authentication.domain.usecase

import com.example.gamesession.authentication.domain.model.User
import com.example.gamesession.authentication.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class GetAllUsersUseCase(private val userRepository: UserRepository) {

    operator fun invoke(): Flow<List<User>> {
        return userRepository.getAllUsers()
    }
}