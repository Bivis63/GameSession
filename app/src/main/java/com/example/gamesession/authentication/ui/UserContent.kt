package com.example.gamesession.authentication.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gamesession.R
import com.example.gamesession.authentication.domain.model.SessionStatus
import com.example.gamesession.authentication.presentation.user.UserComponent
import com.example.gamesession.utils.SessionUtils

@Composable
fun UserContent(
    component: UserComponent,
) {
    val model by component.model.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Добро пожаловать!",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Text(
                        text = model.currentUser.nickName,
                        color = Color.Green,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                
                IconButton(onClick = component::onLogout) {
                    Icon(
                        painter = painterResource(R.drawable.ic_logout),
                        contentDescription = "Выйти",
                        tint = Color.Red,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Text(
                text = "Ваши сессии:",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
            )

            if (model.userSessions.isEmpty()) {
                Text(
                    text = "У вас пока нет сессий",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(model.userSessions) { session ->
                        UserSessionCard(session = session)
                    }
                }
            }
        }
    }
}

@Composable
private fun UserSessionCard(
    session: UserComponent.UserSessionItem
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF121212))
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${session.date} ${session.time}",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = when(session.status) {
                        SessionStatus.RUNNING -> session.actualDuration
                        SessionStatus.PAUSED -> session.actualDuration
                        SessionStatus.FINISHED -> session.actualDuration
                        else -> "${session.duration}ч"
                    },
                    color = when(session.status) {
                        SessionStatus.RUNNING -> Color.Green
                        SessionStatus.PAUSED -> Color.Blue
                        SessionStatus.FINISHED -> Color.Gray
                        else -> Color.Green
                    },
                    fontSize = 12.sp
                )
            }
            
            Text(
                text = "ПК: ${session.computerName}",
                color = Color.Gray,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
            
            Text(
                text = "Статус: ${SessionUtils.getStatusText(session.status)}",
                color = when(session.status) {
                    SessionStatus.SCHEDULED -> Color.Yellow
                    SessionStatus.RUNNING -> if (SessionUtils.isUserSessionTimeAlmostExpired(session)) Color.Red else Color.Green
                    SessionStatus.PAUSED -> Color.Blue
                    SessionStatus.FINISHED -> Color.Gray
                },
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 2.dp)
            )
            
            if (session.price > 0) {
                Text(
                    text = "Стоимость: ${session.price} руб",
                    color = Color.Cyan,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}




