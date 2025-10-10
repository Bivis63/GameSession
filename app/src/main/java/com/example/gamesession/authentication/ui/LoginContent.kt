package com.example.gamesession.authentication.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gamesession.R
import com.example.gamesession.authentication.presentation.login.LoginComponent
import com.example.gamesession.ui.theme.ButtonColorDisabled
import com.example.gamesession.ui.theme.ButtonColorEnabled
import com.example.gamesession.ui.theme.ButtonTextColorDisabled
import com.example.gamesession.ui.theme.ButtonTextColorEnabled
import com.example.gamesession.ui.theme.CursorColor
import com.example.gamesession.ui.theme.GameSessionTheme
import com.example.gamesession.ui.theme.IndicatorColor
import com.example.gamesession.ui.theme.InputTextColor
import com.example.gamesession.ui.theme.LabelColor
import com.example.gamesession.ui.theme.RedError
import com.example.gamesession.ui.theme.TextFieldContainerColor
import com.example.gamesession.ui.theme.TitleColor
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun LoginContent(
    component: LoginComponent,
) {
    val model by component.model.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.height(60.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.app_name),
                    color = TitleColor,
                    fontSize = 28.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Image(
                    modifier = Modifier.size(48.dp),
                    painter = painterResource(R.drawable.ic_progress_bar),
                    contentDescription = null
                )
            }

            Spacer(modifier = Modifier.height(178.dp))

            LoginTextField(
                value = model.login,
                onValueChange = component::onLoginChanged,
                isError = model.isLoginError,
                isLoginFailed = model.loginFailed,
                errorMessage = stringResource(R.string.language_validation)
            )

            PasswordTextField(
                value = model.password,
                onValueChange = component::onPasswordChanged,
                isError = model.isPasswordError
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 12.dp),
                horizontalArrangement = Arrangement.End,
            ) {
                Button(
                    onClick = component::onSubmit,
                    enabled = model.isButtonEnabled,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ButtonColorEnabled,
                        contentColor = ButtonTextColorEnabled,
                        disabledContainerColor = ButtonColorDisabled,
                        disabledContentColor = ButtonTextColorDisabled
                    ),
                    shape = RoundedCornerShape(4.dp),
                ) {
                    Text(
                        text = stringResource(R.string.textEnterButton),
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun LoginTextField(
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    isLoginFailed: Boolean,
    errorMessage: String,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(stringResource(R.string.login)) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password
        ),
        colors = TextFieldDefaults.colors(
            focusedTextColor = InputTextColor,
            unfocusedTextColor = InputTextColor,
            cursorColor = CursorColor,
            unfocusedLabelColor = LabelColor,
            focusedLabelColor = LabelColor,
            focusedIndicatorColor = if (isError) RedError else IndicatorColor,
            unfocusedIndicatorColor = IndicatorColor,
            focusedContainerColor = TextFieldContainerColor,
            unfocusedContainerColor = TextFieldContainerColor
        ),
        supportingText = {
            if (isError) Text(errorMessage, color = RedError)
            if (isLoginFailed) {
                Text(
                    text = stringResource(R.string.invalid_login),
                    color = RedError,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    )
}

@Composable
private fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
) {
    var passwordVisible by remember { mutableStateOf(false) }

    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(stringResource(R.string.password)) },
        singleLine = true,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password
        ),
        trailingIcon = {
            if (value.isNotEmpty()) {
                val image = if (passwordVisible)
                    painterResource(id = R.drawable.ic_visibility)
                else painterResource(id = R.drawable.ic_visibility_off)

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        painter = image,
                        contentDescription = if (passwordVisible) stringResource(R.string.hide_password) else stringResource(R.string.show_password),
                        tint = Color.White
                    )
                }
            }
        },
        modifier = Modifier.fillMaxWidth(),
        colors = TextFieldDefaults.colors(
            focusedTextColor = InputTextColor,
            unfocusedTextColor = InputTextColor,
            cursorColor = CursorColor,
            unfocusedLabelColor = LabelColor,
            focusedLabelColor = LabelColor,
            focusedIndicatorColor = if (isError) RedError else IndicatorColor,
            unfocusedIndicatorColor = IndicatorColor,
            focusedContainerColor = TextFieldContainerColor,
            unfocusedContainerColor = TextFieldContainerColor
        ),
        supportingText = {
            if (isError) {
                when {
                    value.length < 6 -> Text(stringResource(R.string.error_password_too_short), color = RedError)
                    !value.any { it.isDigit() } -> Text(stringResource(R.string.error_password_no_digit), color = RedError)
                    !value.any { it.isLetter() && it.code < 128 } -> Text(stringResource(R.string.error_password_no_latin), color = RedError)
                }
            }
        }
    )
}

