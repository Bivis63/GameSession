package com.example.gamesession.authentication.presentation.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.example.gamesession.authentication.domain.model.User
import com.example.gamesession.authentication.presentation.login.DefaultLoginComponent
import com.example.gamesession.authentication.presentation.user.DefaultAdminComponent
import com.example.gamesession.utils.AppDependencies
import kotlinx.serialization.Serializable

class DefaultRootComponent(
    componentContext: ComponentContext
) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = Config.Login,
        handleBackButton = true,
        childFactory = ::child
    )

    private fun child(
        config: Config,
        componentContext: ComponentContext,
    ): RootComponent.Child {
        return when (config) {
            Config.Login -> {
                val component = DefaultLoginComponent(
                    componentContext = componentContext,
                    authenticateUser = AppDependencies.authenticateUserUseCase
                )
                component.setOnLoginSuccess { user ->
                    navigateToAdmin(user)
                }
                RootComponent.Child.Login(component)
            }

            is Config.Admin -> {
                val component = DefaultAdminComponent(
                    componentContext = componentContext,
                    onLogoutCallback = { navigateToLogin() }
                )
                RootComponent.Child.Admin(component)
            }
        }
    }

    fun navigateToAdmin(user: User) {
        navigation.push(Config.Admin(user))
    }

    fun navigateToLogin() {
        navigation.pop()
    }

    @Serializable
    private sealed interface Config {
        @Serializable
        object Login : Config

        @Serializable
        data class Admin(val user: User) : Config
    }
}