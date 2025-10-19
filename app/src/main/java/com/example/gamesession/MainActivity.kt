package com.example.gamesession

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.arkivanov.decompose.defaultComponentContext
import com.example.gamesession.authentication.presentation.root.DefaultRootComponent
import com.example.gamesession.authentication.ui.RootContent
import com.example.gamesession.ui.theme.GameSessionTheme
import com.example.gamesession.utils.AppDependencies
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        GlobalScope.launch {
            AppDependencies.initializeUsersDataUseCase()
        }

        val rootComponent = DefaultRootComponent(
            componentContext = defaultComponentContext()
        )
        
        setContent {
            GameSessionTheme {
                RootContent(component = rootComponent)
            }
        }
    }
}