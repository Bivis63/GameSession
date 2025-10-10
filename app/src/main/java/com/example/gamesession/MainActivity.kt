package com.example.gamesession

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.defaultComponentContext
import com.example.gamesession.authentication.data.datasource.UserDataSource
import com.example.gamesession.authentication.presentation.login.DefaultLoginComponent
import com.example.gamesession.authentication.presentation.login.LoginComponent
import com.example.gamesession.authentication.ui.LoginContent
import com.example.gamesession.ui.theme.GameSessionTheme
import com.example.gamesession.utils.AppDependencies

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GameSessionTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                ) { innerPadding ->
                    LoginContent(
                        DefaultLoginComponent(
                            componentContext = defaultComponentContext(),
                            AppDependencies.authenticateUserUseCase,
                            AppDependencies.initializeUsersDataUseCase
                        )
                    )
                }
            }
        }
    }
}