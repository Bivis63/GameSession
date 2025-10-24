package com.example.gamesession.authentication.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gamesession.R
import com.example.gamesession.authentication.domain.model.Computer
import com.example.gamesession.authentication.domain.model.SessionTariff
import com.example.gamesession.authentication.domain.model.SessionStatus
import com.example.gamesession.authentication.domain.model.User
import com.example.gamesession.authentication.presentation.session.AdminSessionComponent
import com.example.gamesession.utils.SessionUtils
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.TimePickerDefaults
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun AdminSessionContent(
    component: AdminSessionComponent,
    selectedIndex: Int,
    onUserClick: () -> Unit,
    onSessionClick: () -> Unit,
) {
    val model by component.model.collectAsState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        bottomBar = {
            BottomNavigationBar(
                selectedIndex = selectedIndex,
                onUserClick = onUserClick,
                onSessionClick = onSessionClick
            )
        },
        containerColor = Color.Black
    ) { paddingValues ->
        SessionsScreenContent(
            sessions = model.sessions,
            onAddSessionClicked = component::onAddSessionClicked,
            onStartSession = component::onStartSession,
            onPauseSession = component::onPauseSession,
            onResumeSession = component::onResumeSession,
            onFinishSession = component::onFinishSession,
            modifier = Modifier.padding(paddingValues)
        )
    }

    SessionDialogs(
        model = model,
        component = component
    )
}

@Composable
private fun SessionsScreenContent(
    sessions: List<AdminSessionComponent.SessionItem>,
    onAddSessionClicked: () -> Unit,
    onStartSession: (Int) -> Unit,
    onPauseSession: (Int) -> Unit,
    onResumeSession: (Int) -> Unit,
    onFinishSession: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        SessionsHeader(onAddSessionClicked = onAddSessionClicked)
        
        if (sessions.isEmpty()) {
            EmptySessionsPlaceholder()
        } else {
            SessionsList(
                sessions = sessions,
                onStartSession = onStartSession,
                onPauseSession = onPauseSession,
                onResumeSession = onResumeSession,
                onFinishSession = onFinishSession
            )
        }
    }
}

@Composable
private fun SessionsHeader(onAddSessionClicked: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Сессии",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        IconButton(onClick = onAddSessionClicked) {
            Icon(
                painter = painterResource(id = R.drawable.outline_add_circle_24),
                contentDescription = "Добавить сессию",
                tint = Color.White
            )
        }
    }
}

@Composable
private fun EmptySessionsPlaceholder() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Сессий пока нет",
            color = Color.Gray,
            fontSize = 16.sp
        )
    }
}

@Composable
private fun SessionsList(
    sessions: List<AdminSessionComponent.SessionItem>,
    onStartSession: (Int) -> Unit,
    onPauseSession: (Int) -> Unit,
    onResumeSession: (Int) -> Unit,
    onFinishSession: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(sessions) { session ->
            SessionCard(
                session = session,
                onStart = { onStartSession(session.id) },
                onPause = { onPauseSession(session.id) },
                onResume = { onResumeSession(session.id) },
                onFinish = { onFinishSession(session.id) }
            )
        }
    }
}

@Composable
private fun SessionDialogs(
    model: AdminSessionComponent.Model,
    component: AdminSessionComponent
) {
    if (model.showCreateDialog) {
        CreateSessionDialog(
            date = model.date,
            time = model.time,
            onTimeChange = component::onTimeChanged,
            users = model.allUsers,
            selectedIds = model.selectedUserIds,
            onToggleUser = component::onToggleUser,
            computers = model.availableComputers,
            selectedComputerId = model.selectedComputerId,
            onComputerSelected = component::onComputerSelected,
            tariffs = model.availableTariffs,
            selectedTariffId = model.selectedTariffId,
            onTariffSelected = component::onTariffSelected,
            availableTimeSlots = model.availableTimeSlots,
            onShowDatePicker = component::onShowDatePicker,
            onDismiss = component::onDismissCreateSession,
            onCreate = component::onCreateSession,
            onAddComputer = component::onAddComputerClicked,
            onDeleteComputer = component::onDeleteComputer
        )
    }
    
    if (model.showAddComputerDialog) {
        AddComputerDialog(
            nextCode = model.nextComputerCode,
            onDismiss = component::onDismissAddComputer,
            onConfirm = component::onConfirmAddComputer
        )
    }
    
    DatePickerDialog(
        showDialog = model.showDatePicker,
        onDismiss = component::onDismissDatePicker,
        onDateSelected = { date ->
            component.onDateChanged(date)
            component.onDismissDatePicker()
        }
    )

    TimePickerDialog(
        showDialog = model.showTimePicker,
        onDismiss = component::onDismissTimePicker,
        onTimeSelected = { time ->
            component.onTimeChanged(time)
            component.onDismissTimePicker()
        }
    )
}

@Composable
private fun AddComputerDialog(
    nextCode: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val number = nextCode.removePrefix("PC-").toIntOrNull() ?: 0
    val name = "ПК-$number"
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Добавить компьютер", color = Color.White) },
        text = {
            Column {
                Text(
                    text = "Код: $nextCode",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Название: $name",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text("Добавить", color = Color.White)
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF757575))
            ) {
                Text("Отмена", color = Color.White)
            }
        }
    )
}


@Composable
private fun SessionCard(
    session: AdminSessionComponent.SessionItem,
    onStart: () -> Unit,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onFinish: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF121212))
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = session.headerText, color = Color.White, fontSize = 12.sp)
                Text(text = "ПК: ${session.computerName}", color = Color.Gray, fontSize = 10.sp)
                Text(text = "Время: ${session.actualDuration}", color = Color.Gray, fontSize = 10.sp)
                Text(
                    text = "Статус: ${SessionUtils.getStatusText(session.status)}",
                    color = when(session.status) {
                        SessionStatus.SCHEDULED -> Color.Yellow
                        SessionStatus.RUNNING -> if (SessionUtils.isAdminSessionTimeAlmostExpired(session)) Color.Red else Color.Green
                        SessionStatus.PAUSED -> Color.Blue
                        SessionStatus.FINISHED -> Color.Gray
                    },
                    fontSize = 10.sp
                )
                if (session.cost > 0) {
                    Text(text = "Стоимость: ${session.cost} руб.", color = Color.Cyan, fontSize = 10.sp)
                }
            }
        }

        session.participants.forEach { user ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(generateAvatarColor(user.id)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = user.nickName.first().uppercase(),
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(text = user.nickName, color = Color.White)
                }
                Text(
                    text = if (user.status) "Онлайн" else "Оффлайн",
                    color = if (user.status) Color.Green else Color.Gray,
                    fontSize = 12.sp
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            when (session.status) {
                SessionStatus.SCHEDULED -> {
                    Button(
                        onClick = onStart,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Старт", color = Color.White, fontSize = 12.sp)
                    }
                }
                SessionStatus.RUNNING -> {
                    Button(
                        onClick = onPause,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Пауза", color = Color.White, fontSize = 12.sp)
                    }
                    Button(
                        onClick = onFinish,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Завершить", color = Color.White, fontSize = 12.sp)
                    }
                }
                SessionStatus.PAUSED -> {
                    Button(
                        onClick = onResume,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Продолжить", color = Color.White, fontSize = 12.sp)
                    }
                    Button(
                        onClick = onFinish,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Завершить", color = Color.White, fontSize = 12.sp)
                    }
                }
                SessionStatus.FINISHED -> {}
            }
        }
    }
}

@Composable
fun DatePickerDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onDateSelected: (String) -> Unit
) {
    val dialogState = rememberMaterialDialogState()
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    LaunchedEffect(showDialog) {
        if (showDialog) dialogState.show()
    }

    MaterialDialog(
        dialogState = dialogState,
        buttons = {
            positiveButton("OK") {
                val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
                onDateSelected(selectedDate.format(formatter))
                dialogState.hide()
                onDismiss()
            }
            negativeButton("Отмена") {
                dialogState.hide()
                onDismiss()
            }
        }
    ) {
        datepicker(
            initialDate = selectedDate,
            onDateChange = { selectedDate = it },
            colors = DatePickerDefaults.colors(
                headerBackgroundColor = Color(0xFF2A2A2A),
                headerTextColor = Color.White,
                calendarHeaderTextColor = Color.White,
                dateActiveBackgroundColor = Color(0xFF4CAF50),
                dateActiveTextColor = Color.White,
                dateInactiveTextColor = Color.Gray
            )
        )
    }
}

@Composable
fun TimePickerDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onTimeSelected: (String) -> Unit
) {
    val dialogState = rememberMaterialDialogState()
    var selectedTime by remember { mutableStateOf(LocalTime.now()) }

    LaunchedEffect(showDialog) {
        if (showDialog) dialogState.show()
    }

    MaterialDialog(
        dialogState = dialogState,
        buttons = {
            positiveButton("OK") {
                val formatter = DateTimeFormatter.ofPattern("HH:mm")
                onTimeSelected(selectedTime.format(formatter))
                dialogState.hide()
                onDismiss()
            }
            negativeButton("Отмена") {
                dialogState.hide()
                onDismiss()
            }
        }
    ) {
        timepicker(
            initialTime = selectedTime,
            onTimeChange = { selectedTime = it },
            colors = TimePickerDefaults.colors(
                activeBackgroundColor = Color(0xFF4CAF50),
                activeTextColor = Color.White,
                inactiveBackgroundColor = Color(0xFF2A2A2A),
                inactiveTextColor = Color.Gray
            )
        )
    }
}


@Composable
private fun CreateSessionDialog(
    date: String,
    time: String,
    onTimeChange: (String) -> Unit,
    users: List<User>,
    selectedIds: Set<Int>,
    onToggleUser: (Int) -> Unit,
    computers: List<Computer>,
    selectedComputerId: Int?,
    onComputerSelected: (Int) -> Unit,
    tariffs: List<SessionTariff>,
    selectedTariffId: Int?,
    onTariffSelected: (Int) -> Unit,
    availableTimeSlots: List<String>,
    onShowDatePicker: () -> Unit,
    onDismiss: () -> Unit,
    onCreate: () -> Unit,
    onAddComputer: () -> Unit,
    onDeleteComputer: (Int) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            val enabled =
                date.isNotBlank() && time.isNotBlank() && selectedIds.isNotEmpty() &&
                        selectedComputerId != null && selectedTariffId != null
            Button(
                onClick = onCreate,
                enabled = enabled,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) { Text("СОЗДАТЬ", color = Color.White) }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF757575))
            ) { Text("Отмена", color = Color.White) }
        },
        title = { Text("Создание сессии", color = Color.White) },
        text = {
            Column(
                modifier = Modifier
                    .height(400.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                DatePickerButton(date = date, onClick = onShowDatePicker)
                TimeSlotsList(
                    time = time,
                    availableTimeSlots = availableTimeSlots,
                    onTimeSelected = onTimeChange,
                    date = date,
                    selectedComputerId = selectedComputerId,
                    selectedTariffId = selectedTariffId
                )
                TariffsList(tariffs, selectedTariffId, onTariffSelected)
                ComputersList(
                    computers = computers,
                    selectedComputerId = selectedComputerId,
                    onComputerSelected = onComputerSelected,
                    onAddComputer = onAddComputer,
                    onDeleteComputer = onDeleteComputer
                )
                UsersList(users, selectedIds, onToggleUser)
            }
        }
    )
}

@Composable
private fun DatePickerButton(date: String, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = Color.White,
            containerColor = Color(0xFF2A2A2A)
        )
    ) {
        Text(
            text = if (date.isBlank()) "Выберите дату" else date,
            color = if (date.isBlank()) Color.Gray else Color.White
        )
    }
}

@Composable
private fun TimeSlotsList(
    time: String,
    availableTimeSlots: List<String>,
    onTimeSelected: (String) -> Unit,
    date: String,
    selectedComputerId: Int?,
    selectedTariffId: Int?
) {
    if (availableTimeSlots.isNotEmpty()) {
        Text(
            text = "Доступное время:",
            color = Color.White,
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
        )
        LazyColumn(
            modifier = Modifier.height(150.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(availableTimeSlots) { timeSlot ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onTimeSelected(timeSlot) },
                    colors = CardDefaults.cardColors(
                        containerColor = if (time == timeSlot) Color(0xFF4CAF50) else Color(0xFF2A2A2A)
                    )
                ) {
                    Text(
                        text = timeSlot,
                        color = Color.White,
                        modifier = Modifier.padding(8.dp),
                        fontSize = 12.sp
                    )
                }
            }
        }
    } else if (selectedComputerId != null && selectedTariffId != null && date.isNotBlank()) {
        Text(
            text = "Нет доступного времени на выбранную дату",
            color = Color.Red,
            fontSize = 12.sp,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}

@Composable
private fun TariffsList(
    tariffs: List<SessionTariff>,
    selectedTariffId: Int?,
    onTariffSelected: (Int) -> Unit
) {
    Text(
        text = "Тарифы",
        color = Color.White,
        modifier = Modifier.padding(top = 12.dp, bottom = 6.dp)
    )
    tariffs.forEach { tariff ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = tariff.name, color = Color.White, fontSize = 14.sp)
                Text(text = "${tariff.price} руб", color = Color.Gray, fontSize = 12.sp)
            }
            RadioButton(
                selected = selectedTariffId == tariff.id,
                onClick = { onTariffSelected(tariff.id) },
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun ComputersList(
    computers: List<Computer>,
    selectedComputerId: Int?,
    onComputerSelected: (Int) -> Unit,
    onAddComputer: () -> Unit,
    onDeleteComputer: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Компьютеры",
            color = Color.White
        )
        OutlinedButton(
            onClick = onAddComputer,
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color.Green,
                containerColor = Color.Transparent
            ),
            modifier = Modifier.height(32.dp)
        ) {
            Text("➕ Добавить ПК", fontSize = 12.sp)
        }
    }
    
    computers.forEach { computer ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedComputerId == computer.id,
                    onClick = { onComputerSelected(computer.id) },
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "${computer.name} (${computer.code})",
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            IconButton(
                onClick = { onDeleteComputer(computer.id) },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_delete),
                    contentDescription = "Удалить",
                    tint = Color.Red,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
private fun UsersList(
    users: List<User>,
    selectedIds: Set<Int>,
    onToggleUser: (Int) -> Unit
) {
    Text(
        text = "Пользователи",
        color = Color.White,
        modifier = Modifier.padding(top = 12.dp, bottom = 6.dp)
    )
    users.forEach { user ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(generateAvatarColor(user.id)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = user.nickName.first().uppercase(),
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column(modifier = Modifier.padding(start = 8.dp)) {
                    Text(text = user.nickName, color = Color.White, fontSize = 14.sp)
                    Text(
                        text = if (user.status) "Онлайн" else "Оффлайн",
                        color = if (user.status) Color.Green else Color.Gray,
                        fontSize = 11.sp
                    )
                }
            }
            Checkbox(
                checked = selectedIds.contains(user.id),
                onCheckedChange = { onToggleUser(user.id) },
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

private fun generateAvatarColor(userId: Int): Color {
    val colors = listOf(
        Color(0xFF607D8B),
        Color(0xFFFFC107),
        Color(0xFF2196F3),
        Color(0xFF9C27B0),
        Color(0xFF4CAF50),
        Color(0xFFFF9800),
        Color(0xFFE91E63),
        Color(0xFF795548)
    )
    return colors[userId % colors.size]
}