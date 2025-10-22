package com.example.gamesession.authentication.presentation.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.example.gamesession.authentication.presentation.admin.AdminPanelComponent
import com.example.gamesession.authentication.presentation.login.LoginComponent
import com.example.gamesession.authentication.presentation.session.AdminSessionComponent
import com.example.gamesession.authentication.presentation.user.AdminComponent
import com.example.gamesession.authentication.presentation.user.UserComponent

interface RootComponent {

    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {
        class Login(val component: LoginComponent) : Child
        class AdminPanel(
            val adminPanelComponent: AdminPanelComponent,
            val adminComponent: AdminComponent,
            val adminSessionComponent: AdminSessionComponent
        ) : Child
        class User(val component: UserComponent) : Child
    }
}