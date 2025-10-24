package com.example.gamesession.authentication.presentation.session

import com.example.gamesession.authentication.domain.model.User
import com.example.gamesession.authentication.domain.model.Computer
import com.example.gamesession.authentication.domain.model.SessionTariff
import com.example.gamesession.authentication.domain.model.SessionStatus
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable

interface AdminSessionComponent {
    val model: StateFlow<Model>

    fun onLogout()
    fun onAddSessionClicked()
    fun onDismissCreateSession()
    fun onDateChanged(value: String)
    fun onTimeChanged(value: String)
    fun onToggleUser(userId: Int)
    fun onComputerSelected(computerId: Int)
    fun onTariffSelected(tariffId: Int)
    fun onShowDatePicker()
    fun onShowTimePicker()
    fun onDismissDatePicker()
    fun onDismissTimePicker()
    fun onCreateSession()
    fun onAddComputerClicked()
    fun onDismissAddComputer()
    fun onConfirmAddComputer()
    fun onDeleteComputer(computerId: Int)
    fun onStartSession(sessionId: Int)
    fun onPauseSession(sessionId: Int)
    fun onResumeSession(sessionId: Int)
    fun onFinishSession(sessionId: Int)

    @Serializable
    data class Model(
        val currentUser: User,
        val showCreateDialog: Boolean = false,
        val date: String = "",
        val time: String = "",
        val allUsers: List<User> = emptyList(),
        val selectedUserIds: Set<Int> = emptySet(),
        val availableComputers: List<Computer> = emptyList(),
        val selectedComputerId: Int? = null,
        val availableTariffs: List<SessionTariff> = emptyList(),
        val selectedTariffId: Int? = null,
        val availableTimeSlots: List<String> = emptyList(),
        val showDatePicker: Boolean = false,
        val showTimePicker: Boolean = false,
        val isComputerAvailable: Boolean = true,
        val errorMessage: String? = null,
        val sessions: List<SessionItem> = emptyList(),
        val showAddComputerDialog: Boolean = false,
        val nextComputerCode: String = ""
    )

    @Serializable
    data class SessionItem(
        val id: Int,
        val headerText: String,
        val participants: List<User>,
        val computerName: String,
        val duration: Double,
        val status: SessionStatus = SessionStatus.SCHEDULED,
        val actualDuration: String = "0м",
        val cost: Int = 0,
        val startTime: Long = 0L,
        val durationHours: Double = 0.0
    )
}


