package com.example.gamesession.authentication.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gamesession.R
import com.example.gamesession.authentication.domain.model.User
import com.example.gamesession.authentication.presentation.user.AdminComponent
import com.example.gamesession.ui.theme.BottomNavItemSelectedBackground
import com.example.gamesession.ui.theme.CursorColor
import com.example.gamesession.ui.theme.IndicatorColor
import com.example.gamesession.ui.theme.InputTextColor
import com.example.gamesession.ui.theme.LabelColor
import com.example.gamesession.ui.theme.TextFieldContainerColor

@Composable
fun AdminContent(
    component: AdminComponent,
) {
    val model by component.model.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .statusBarsPadding()
            .padding(8.dp),

        ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            Text(
                text = "Пользователи",
                color = Color.White,
                fontSize = 20.sp,
                modifier = Modifier.weight(1f)
            )

            Row {
                IconButton(onClick = { component.onAddUserClicked() }) {
                    Icon(
                        painter = painterResource(R.drawable.outline_add_circle_24),
                        contentDescription = "Добавить пользователя",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                IconButton(onClick = { component.onLogout() }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_logout),
                        contentDescription = "Выйти",
                        tint = Color.Red,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        SearchTextField(
            value = model.searchQuery,
            onValueChange = component::onSearchQueryChanged
        )

        Spacer(modifier = Modifier.height(10.dp))

        if (model.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        } else {

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(model.filteredUsers) { user ->
                    UserItem(
                        user = user,
                        onToggleBlock = { component.onToggleUserBlock(user.id) },
                        onEdit = { component.onEditUserClicked(user.id) },
                        onDelete = { component.onDeleteUserClicked(user.id) }
                    )
                }
            }
        }

        BottomNavigationBar(0, {}, {})
    }

    model.errorMessage?.let { errorMessage ->
        AlertDialog(
            onDismissRequest = component::onClearError,
            title = { Text("Ошибка") },
            text = { Text(errorMessage) },
            confirmButton = {
                Button(onClick = component::onClearError) {
                    Text("OK")
                }
            }
        )
    }

    if (model.showAddUserDialog) {
        AddUserDialog(
            onDismiss = component::onAddUserDialogDismissed,
            onAddUser = component::onAddUser
        )
    }

    if (model.showEditUserDialog && model.editingUser != null) {
        EditUserDialog(
            user = model.editingUser!!,
            onDismiss = component::onAddUserDialogDismissed,
            onEditUser = component::onEditUser
        )
    }
}

@Composable
fun UserItem(
    user: User,
    onToggleBlock: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    val nameColor = if (user.isBlocked) Color.Red else Color.White
    val avatarColor = generateAvatarColor(user.id)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF121212)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF121212)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(1f)
                    .clickable { onEdit() }
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(avatarColor),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = user.nickName.first().uppercase(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = user.nickName,
                        color = nameColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = user.phoneNumber,
                        color = Color.Gray,
                        fontSize = 13.sp
                    )
                    Text(
                        text = statusUser(user),
                        color = colorStatusUser(user),
                        fontSize = 12.sp
                    )
                }
            }

            Row {
                IconButton(onClick = onToggleBlock) {
                    Icon(
                        painter = painterResource(
                            if (user.isBlocked)
                                R.drawable.button_unblocked
                            else
                                R.drawable.button_block
                        ),
                        contentDescription = if (user.isBlocked) "Разблокировать" else "Заблокировать",
                        tint = Color.Unspecified
                    )
                }

                IconButton(onClick = onDelete) {
                    Icon(
                        painter = painterResource(R.drawable.ic_delete),
                        contentDescription = "Удалить",
                        tint = Color.Red
                    )
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    selectedIndex: Int,
    onUserClick: () -> Unit,
    onSessionClick: () -> Unit,
) {
    val items = listOf(
        Triple("Пользователь", R.drawable.outline_supervised_user_circle_24, onUserClick),
        Triple("Сессии", R.drawable.baseline_access_time_24, onSessionClick)

    )
    NavigationBar(
        modifier = Modifier.padding(6.dp),
        containerColor = Color.Black
    ) {
        items.forEachIndexed { index, (titleRes, iconRes, onClick) ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = null
                    )
                },
                label = { Text(titleRes) },
                selected = selectedIndex == index,
                onClick = onClick,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    unselectedIconColor = Color.Gray,
                    selectedTextColor = Color.White,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = Color.Transparent
                ),
                modifier = Modifier
                    .height(53.dp)
                    .border(
                        width = 0.6.dp,
                        color = Color.White,
                        shape = RoundedCornerShape(3.6.dp)
                    )
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (selectedIndex == index) BottomNavItemSelectedBackground else Color.Transparent)
            )
        }
    }
}

@Composable
private fun SearchTextField(
    value: String,
    onValueChange: (String) -> Unit,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Поиск по нику или телефону") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text
        ),
        modifier = Modifier.fillMaxWidth(),
        colors = TextFieldDefaults.colors(
            focusedTextColor = InputTextColor,
            unfocusedTextColor = InputTextColor,
            cursorColor = CursorColor,
            unfocusedLabelColor = LabelColor,
            focusedLabelColor = LabelColor,
            unfocusedIndicatorColor = IndicatorColor,
            focusedContainerColor = TextFieldContainerColor,
            unfocusedContainerColor = TextFieldContainerColor
        )
    )
}

@Composable
fun UserTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = Color.White) },
        modifier = modifier.fillMaxWidth(),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            cursorColor = Color.White,
            focusedLabelColor = Color.White,
            unfocusedLabelColor = Color.Gray,
            focusedContainerColor = Color(0xFF2A2A2A),
            unfocusedContainerColor = Color(0xFF2A2A2A)
        )
    )
}

@Composable
fun UserFields(
    login: String,
    onLoginChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    nickName: String,
    onNickChange: (String) -> Unit,
    phoneNumber: String,
    onPhoneChange: (String) -> Unit,
) {
    Column {
        UserTextField(label = "Логин", value = login, onValueChange = onLoginChange)
        Spacer(modifier = Modifier.height(8.dp))
        UserTextField(
            label = "Пароль",
            value = password,
            onValueChange = onPasswordChange,
            keyboardType = KeyboardType.Password,
            isPassword = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        UserTextField(label = "Ник", value = nickName, onValueChange = onNickChange)
        Spacer(modifier = Modifier.height(8.dp))
        UserTextField(
            label = "Телефон",
            value = phoneNumber,
            onValueChange = onPhoneChange,
            keyboardType = KeyboardType.Phone
        )
    }
}

@Composable
fun AddUserDialog(
    onDismiss: () -> Unit,
    onAddUser: (String, String, String, String) -> Unit,
) {
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var nickName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Добавить пользователя", color = Color.White) },
        text = {
            UserFields(
                login = login,
                onLoginChange = { login = it },
                password = password,
                onPasswordChange = { password = it },
                nickName = nickName,
                onNickChange = { nickName = it },
                phoneNumber = phoneNumber,
                onPhoneChange = { phoneNumber = it }
            )
        },
        confirmButton = {
            Button(
                onClick = { onAddUser(login, password, nickName, phoneNumber) },
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
fun EditUserDialog(
    user: User,
    onDismiss: () -> Unit,
    onEditUser: (Int, String, String, String, String) -> Unit,
) {
    var login by remember { mutableStateOf(user.login) }
    var password by remember { mutableStateOf(user.password) }
    var nickName by remember { mutableStateOf(user.nickName) }
    var phoneNumber by remember { mutableStateOf(user.phoneNumber) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Редактировать пользователя", color = Color.White) },
        text = {
            UserFields(
                login = login,
                onLoginChange = { login = it },
                password = password,
                onPasswordChange = { password = it },
                nickName = nickName,
                onNickChange = { nickName = it },
                phoneNumber = phoneNumber,
                onPhoneChange = { phoneNumber = it }
            )
        },
        confirmButton = {
            Button(
                onClick = { onEditUser(user.id, login, password, nickName, phoneNumber) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text("Сохранить", color = Color.White)
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

private fun statusUser(user: User): String {
    return if (user.status == true) "online" else "offline"
}

private fun colorStatusUser(user: User): Color {
    return if (user.status == true) Color.Green else Color.Gray
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
