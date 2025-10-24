package com.example.gamesession.authentication.data.database.mapper

import com.example.gamesession.authentication.data.database.entity.UserEntity
import com.example.gamesession.authentication.domain.model.User

object UserMapper {

    fun toDomain(entity: UserEntity): User {
        return User(
            id = entity.id,
            login = entity.login,
            password = entity.password,
            phoneNumber = entity.phoneNumber,
            nickName = entity.nickName,
            rule = entity.rule,
            isBlocked = entity.isBlocked,
            status = entity.status
        )
    }

    fun toEntity(user: User): UserEntity {
        return UserEntity(
            id = user.id,
            login = user.login,
            password = user.password,
            phoneNumber = user.phoneNumber,
            nickName = user.nickName,
            rule = user.rule,
            isBlocked = user.isBlocked,
            status = user.status
        )
    }

    fun toDomainList(entities: List<UserEntity>): List<User> {
        return entities.map { toDomain(it) }
    }
}