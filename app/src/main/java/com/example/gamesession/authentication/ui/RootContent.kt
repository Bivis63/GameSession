package com.example.gamesession.authentication.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.gamesession.ui.theme.GameSessionTheme
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.example.gamesession.authentication.presentation.root.RootComponent

@Composable
fun RootContent(
    component: RootComponent
) {
    GameSessionTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {

            Children(
                stack = component.stack
            ) {
                when (val instance = it.instance) {
                    is RootComponent.Child.Login -> {
                        LoginContent(component = instance.component)
                    }

                    is RootComponent.Child.AdminPanel -> {
                        AdminPanelContent(
                            component = instance.adminPanelComponent,
                            adminComponent = instance.adminComponent,
                            adminSessionComponent = instance.adminSessionComponent
                        )
                    }

                    is RootComponent.Child.User -> {
                        UserContent(component = instance.component)
                    }
                }
            }
        }
    }
}
