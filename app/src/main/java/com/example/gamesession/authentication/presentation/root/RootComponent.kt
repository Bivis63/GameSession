package com.example.gamesession.authentication.presentation.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.example.gamesession.authentication.presentation.login.LoginComponent
import com.example.gamesession.authentication.presentation.user.AdminComponent

interface RootComponent {

    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {
        class Login(val component: LoginComponent) : Child
        class Admin(val component: AdminComponent) : Child
    }
}