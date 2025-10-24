package com.example.gamesession.authentication.presentation.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.example.gamesession.authentication.domain.model.RuleSettings
import com.example.gamesession.authentication.domain.model.User
import com.example.gamesession.authentication.presentation.admin.DefaultAdminPanelComponent
import com.example.gamesession.authentication.presentation.login.DefaultLoginComponent
import com.example.gamesession.authentication.presentation.session.DefaultAdminSessionComponent
import com.example.gamesession.authentication.presentation.user.DefaultAdminComponent
import com.example.gamesession.authentication.presentation.user.DefaultUserComponent
import com.example.gamesession.utils.AppDependencies
import kotlinx.serialization.Serializable


class DefaultRootComponent(
    componentContext: ComponentContext,
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
                    when (user.rule) {
                        RuleSettings.ADMIN -> navigateToAdminPanel(user)
                        RuleSettings.USER -> navigateToUser(user)
                    }
                }
                RootComponent.Child.Login(component)
            }

            is Config.AdminPanel -> {
                val adminPanelComponent = DefaultAdminPanelComponent(
                    componentContext = componentContext,
                    user = config.user,
                    onLogoutCallback = { navigateToLogin() }
                )

                val adminComponent = DefaultAdminComponent(
                    componentContext = componentContext,
                    onLogoutCallback = { navigateToLogin() }
                )

                val adminSessionComponent = DefaultAdminSessionComponent(
                    componentContext = componentContext,
                    user = config.user,
                    onLogoutCallback = { navigateToLogin() }
                )

                RootComponent.Child.AdminPanel(
                    adminPanelComponent,
                    adminComponent,
                    adminSessionComponent
                )
            }

            is Config.Users -> {
                val component = DefaultUserComponent(
                    componentContext = componentContext,
                    user = config.user,
                    onLogoutCallback = { navigateToLogin() }
                )
                RootComponent.Child.User(component)
            }
        }
    }

    fun navigateToAdminPanel(user: User) {
        navigation.push(Config.AdminPanel(user))
    }

    fun navigateToUser(user: User) {
        navigation.push(Config.Users(user))
    }

    fun navigateToLogin() {
        navigation.pop()
    }

    @Serializable
    private sealed interface Config {
        @Serializable
        object Login : Config

        @Serializable
        data class AdminPanel(
            val user: User
        ) : Config

        @Serializable
        data class Users(
            val user: User
        ) : Config
    }
}
