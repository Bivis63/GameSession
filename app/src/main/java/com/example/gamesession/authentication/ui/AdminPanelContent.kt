package com.example.gamesession.authentication.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.gamesession.authentication.presentation.admin.AdminPanelComponent
import com.example.gamesession.authentication.presentation.admin.AdminTab
import com.example.gamesession.authentication.presentation.session.AdminSessionComponent
import com.example.gamesession.authentication.presentation.user.AdminComponent

@Composable
fun AdminPanelContent(
    component: AdminPanelComponent,
    adminComponent: AdminComponent,
    adminSessionComponent: AdminSessionComponent,
) {
    val model by component.model.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            when (AdminTab.fromIndex(model.selectedTabIndex)) {
                AdminTab.USERS -> AdminContent(
                    component = adminComponent,
                    selectedIndex = model.selectedTabIndex,
                    onUserClick = component::onUsersTabClicked,
                    onSessionClick = component::onSessionsTabClicked
                )
                AdminTab.SESSIONS -> AdminSessionContent(
                    component = adminSessionComponent,
                    selectedIndex = model.selectedTabIndex,
                    onUserClick = component::onUsersTabClicked,
                    onSessionClick = component::onSessionsTabClicked
                )
            }
        }
    }
}


