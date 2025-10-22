package com.example.gamesession.authentication.presentation.session

import com.arkivanov.decompose.ComponentContext
import com.example.gamesession.authentication.domain.model.Computer
import com.example.gamesession.authentication.domain.model.GameSession
import com.example.gamesession.authentication.domain.model.User
import com.example.gamesession.authentication.domain.usecase.CheckComputerAvailabilityUseCase
import com.example.gamesession.authentication.domain.usecase.DeleteComputerUseCase
import com.example.gamesession.authentication.domain.usecase.GetActiveTariffsUseCase
import com.example.gamesession.authentication.domain.usecase.GetAllComputersUseCase
import com.example.gamesession.authentication.domain.usecase.GetAllGameSessionsUseCase
import com.example.gamesession.authentication.domain.usecase.GetAllUsersUseCase
import com.example.gamesession.authentication.domain.usecase.GetAvailableComputersUseCase
import com.example.gamesession.authentication.domain.usecase.GetAvailableTimeSlotsUseCase
import com.example.gamesession.authentication.domain.usecase.GetNextComputerCodeUseCase
import com.example.gamesession.authentication.domain.usecase.InsertComputerUseCase
import com.example.gamesession.authentication.domain.usecase.InsertGameSessionUseCase
import com.example.gamesession.authentication.domain.usecase.UpdateUserUseCase
import com.example.gamesession.utils.AppDependencies
import com.example.gamesession.utils.componentScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class DefaultAdminSessionComponent(
    componentContext: ComponentContext,
    private val user: User,
    private val onLogoutCallback: () -> Unit,
    private val updateUserUseCase: UpdateUserUseCase = AppDependencies.updateUserUseCase,
    private val getAllUsersUseCase: GetAllUsersUseCase = AppDependencies.getAllUsersUseCase,
    private val getAvailableComputersUseCase: GetAvailableComputersUseCase = AppDependencies.getAvailableComputersUseCase,
    private val getAllComputersUseCase: GetAllComputersUseCase = AppDependencies.getAllComputersUseCase,
    private val getAllGameSessionsUseCase: GetAllGameSessionsUseCase = AppDependencies.getAllGameSessionsUseCase,
    private val getActiveTariffsUseCase: GetActiveTariffsUseCase = AppDependencies.getActiveTariffsUseCase,
    private val checkComputerAvailabilityUseCase: CheckComputerAvailabilityUseCase = AppDependencies.checkComputerAvailabilityUseCase,
    private val getAvailableTimeSlotsUseCase: GetAvailableTimeSlotsUseCase = AppDependencies.getAvailableTimeSlotsUseCase,
    private val insertGameSessionUseCase: InsertGameSessionUseCase = AppDependencies.insertGameSessionUseCase,
    private val insertComputerUseCase: InsertComputerUseCase = AppDependencies.insertComputerUseCase,
    private val deleteComputerUseCase: DeleteComputerUseCase = AppDependencies.deleteComputerUseCase,
    private val getNextComputerCodeUseCase: GetNextComputerCodeUseCase = AppDependencies.getNextComputerCodeUseCase
) : AdminSessionComponent, ComponentContext by componentContext {

    companion object {
        private const val KEY = "AdminSessionComponent"
    }

    private val scope = componentScope()

    private val _model = MutableStateFlow(
        stateKeeper.consume(KEY, strategy = AdminSessionComponent.Model.serializer())
            ?: AdminSessionComponent.Model(currentUser = user)
    )

    init {
        stateKeeper.register(KEY, strategy = AdminSessionComponent.Model.serializer()) { model.value }
        loadSessions()
    }
    
    private fun loadSessions() {
        scope.launch {
            val computers = getAllComputersUseCase().first()
            val gameSessions = getAllGameSessionsUseCase().first()
            val users = getAllUsersUseCase().first()
            
            val sortedSessions = gameSessions.sortedWith(compareBy<GameSession> { session ->
                val dateParts = session.date.split(".")
                if (dateParts.size == 3) {
                    "${dateParts[2]}-${dateParts[1]}-${dateParts[0]}"
                } else {
                    session.date
                }
            }.thenBy { session ->
                session.time
            })
            
            val sessionItems = sortedSessions.map { session ->
                val computer = computers.find { it.id == session.computerId }
                val user = users.find { it.id == session.userId }
                AdminSessionComponent.SessionItem(
                    id = session.id,
                    headerText = "${session.date} ${session.time}",
                    participants = user?.let { listOf(it) } ?: emptyList(),
                    computerName = computer?.name ?: "Неизвестный ПК",
                    duration = session.durationHours
                )
            }
            
            _model.value = _model.value.copy(sessions = sessionItems)
        }
    }

    override val model: StateFlow<AdminSessionComponent.Model>
        get() = _model.asStateFlow()

    override fun onAddSessionClicked() {
        _model.value = _model.value.copy(
            showCreateDialog = true,
            date = "",
            time = "",
            selectedUserIds = emptySet(),
            selectedComputerId = null,
            selectedTariffId = null,
            availableTimeSlots = emptyList()
        )
        getAllUsersUseCase()
            .onEach { users ->
                val availableUsers = users.filter { !it.isBlocked }
                _model.value = _model.value.copy(allUsers = availableUsers)
            }
            .launchIn(scope)
            
        getAvailableComputersUseCase()
            .onEach { computers ->
                _model.value = _model.value.copy(availableComputers = computers)
            }
            .launchIn(scope)
            
        getActiveTariffsUseCase()
            .onEach { tariffs ->
                _model.value = _model.value.copy(availableTariffs = tariffs)
            }
            .launchIn(scope)
    }

    override fun onDismissCreateSession() {
        _model.value = _model.value.copy(
            showCreateDialog = false,
            date = "",
            time = "",
            selectedUserIds = emptySet(),
            selectedComputerId = null,
            selectedTariffId = null
        )
    }

    override fun onDateChanged(value: String) {
        _model.value = _model.value.copy(date = value)
        if (value.isNotBlank()) {
            loadAvailableTimeSlots()
        }
    }
    override fun onTimeChanged(value: String) { _model.value = _model.value.copy(time = value) }

    override fun onToggleUser(userId: Int) {
        val set = _model.value.selectedUserIds.toMutableSet()
        if (!set.add(userId)) set.remove(userId)
        _model.value = _model.value.copy(selectedUserIds = set)
    }

    override fun onComputerSelected(computerId: Int) {
        _model.value = _model.value.copy(selectedComputerId = computerId)
        loadAvailableTimeSlots()
    }

    override fun onTariffSelected(tariffId: Int) {
        _model.value = _model.value.copy(selectedTariffId = tariffId)
        loadAvailableTimeSlots()
    }

    override fun onShowDatePicker() {
        _model.value = _model.value.copy(showDatePicker = true)
    }

    override fun onShowTimePicker() {
        _model.value = _model.value.copy(showTimePicker = true)
    }

    override fun onDismissDatePicker() {
        _model.value = _model.value.copy(showDatePicker = false)
    }

    override fun onDismissTimePicker() {
        _model.value = _model.value.copy(showTimePicker = false)
    }

    private fun loadAvailableTimeSlots() {
        val model = _model.value
        if (model.selectedComputerId != null && model.selectedTariffId != null && model.date.isNotBlank()) {
            scope.launch {
                try {
                    val selectedTariff = model.availableTariffs.find { it.id == model.selectedTariffId }
                    if (selectedTariff != null) {
                        val timeSlots = getAvailableTimeSlotsUseCase(
                            model.selectedComputerId!!,
                            model.date,
                            selectedTariff.durationHours
                        )
                        _model.value = _model.value.copy(availableTimeSlots = timeSlots)
                    }
                } catch (e: Exception) {
                    _model.value = _model.value.copy(errorMessage = "Ошибка загрузки временных слотов")
                }
            }
        }
    }

    override fun onCreateSession() {
        val model = _model.value
        if (model.date.isNotBlank() && model.time.isNotBlank() && 
            model.selectedUserIds.isNotEmpty() && 
            model.selectedComputerId != null && model.selectedTariffId != null) {
            
            scope.launch {
                try {
                    val selectedTariff = model.availableTariffs.find { it.id == model.selectedTariffId }
                    if (selectedTariff != null) {
                        val isAvailable = checkComputerAvailabilityUseCase(
                            model.selectedComputerId!!,
                            model.date,
                            model.time,
                            selectedTariff.durationHours
                        )
                        
                        if (isAvailable) {
                            model.selectedUserIds.forEach { userId ->
                                val gameSession = GameSession(
                                    date = model.date,
                                    time = model.time,
                                    durationHours = selectedTariff.durationHours,
                                    computerId = model.selectedComputerId!!,
                                    userId = userId
                                )
                                insertGameSessionUseCase(gameSession)
                            }
                            _model.value = _model.value.copy(
                                showCreateDialog = false,
                                errorMessage = null
                            )
                            loadSessions()
                        } else {
                            _model.value = _model.value.copy(
                                errorMessage = "Компьютер занят в выбранное время. Выберите другое время."
                            )
                        }
                    }
                } catch (e: Exception) {
                    _model.value = _model.value.copy(errorMessage = "Ошибка создания сессии: ${e.message}")
                }
            }
        }
    }


    override fun onAddComputerClicked() {
        scope.launch {
            val nextCode = getNextComputerCodeUseCase()
            _model.value = _model.value.copy(
                showAddComputerDialog = true,
                nextComputerCode = nextCode
            )
        }
    }

    override fun onDismissAddComputer() {
        _model.value = _model.value.copy(showAddComputerDialog = false, nextComputerCode = "")
    }

    override fun onConfirmAddComputer() {
        scope.launch {
            try {
                val code = _model.value.nextComputerCode
                val number = code.removePrefix("PC-").toIntOrNull() ?: return@launch
                val name = "ПК-$number"
                
                val newComputer = Computer(
                    code = code,
                    name = name,
                    isAvailable = true
                )
                
                insertComputerUseCase(newComputer)
                _model.value = _model.value.copy(showAddComputerDialog = false, nextComputerCode = "")

                getAllComputersUseCase()
                    .onEach { computers ->
                        _model.value = _model.value.copy(availableComputers = computers)
                    }
                    .launchIn(scope)
            } catch (e: Exception) {
                _model.value = _model.value.copy(errorMessage = "Ошибка добавления ПК: ${e.message}")
            }
        }
    }

    override fun onDeleteComputer(computerId: Int) {
        scope.launch {
            try {
                val computer = _model.value.availableComputers.find { it.id == computerId }
                computer?.let {
                    deleteComputerUseCase(it)

                    getAllComputersUseCase()
                        .onEach { computers ->
                            _model.value = _model.value.copy(availableComputers = computers)
                        }
                        .launchIn(scope)
                }
            } catch (e: Exception) {
                _model.value = _model.value.copy(errorMessage = "Ошибка удаления ПК: ${e.message}")
            }
        }
    }

    override fun onLogout() {
        scope.launch {
            val updatedUser = user.copy(status = false)
            updateUserUseCase(updatedUser)
        }
        onLogoutCallback()
    }
}


