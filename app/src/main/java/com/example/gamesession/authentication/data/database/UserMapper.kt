package com.example.gamesession.authentication.data.database

import com.example.gamesession.authentication.domain.model.User
import kotlin.math.log

object UserMapper {

    fun toDomain(entity: UserEntity): User {
        return User(
            id = entity.id,
            login = entity.login,
            password = entity.password,
            phoneNumber = entity.phoneNumber,
            nickName = entity.nickName,
            rule = entity.rule,
            isBlocked = entity.isBlocked
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
            isBlocked = user.isBlocked
        )
    }

    fun toDomainList(entities: List<UserEntity>): List<User> {
        return entities.map { toDomain(it) }
    }

    fun toEntitiesList(users: List<User>): List<UserEntity> {
        return users.map { toEntity(it) }
    }
}