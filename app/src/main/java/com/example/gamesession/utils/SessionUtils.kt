package com.example.gamesession.utils

import com.example.gamesession.authentication.domain.model.GameSession
import com.example.gamesession.authentication.domain.model.SessionStatus
import com.example.gamesession.authentication.domain.model.SessionTariff
import kotlin.math.ceil

object SessionUtils {

    private const val BILLING_STEP_MINUTES = 30

    fun calculateActualDurationMinutes(session: GameSession): Int {
        if (session.startTime == 0L) return 0

        return when (session.status) {
            SessionStatus.RUNNING -> {
                val currentTime = System.currentTimeMillis()
                val currentElapsed = currentTime - session.startTime
                val totalActiveTime = session.pausedTime + currentElapsed
                if (totalActiveTime > 0) (totalActiveTime / 60000L).toInt() else 0
            }
            SessionStatus.PAUSED -> {
                if (session.pausedTime > 0) (session.pausedTime / 60000L).toInt() else 0
            }
            SessionStatus.FINISHED -> session.actualDurationMinutes
            SessionStatus.SCHEDULED -> 0
        }
    }

    fun calculateBilledMinutes(actualMinutes: Int): Int {
        if (actualMinutes <= 0) return 0
        return ceil(actualMinutes.toDouble() / BILLING_STEP_MINUTES).toInt() * BILLING_STEP_MINUTES
    }


    fun calculateSessionCost(session: GameSession, tariff: SessionTariff): Int {
        val actualMinutes = calculateActualDurationMinutes(session)
        val billedMinutes = calculateBilledMinutes(actualMinutes)
        val costPerMinute = tariff.price / (tariff.durationHours * 60)
        return (billedMinutes * costPerMinute).toInt()
    }

    fun formatDuration(minutes: Int): String {
        val hours = minutes / 60
        val remainingMinutes = minutes % 60
        return if (hours > 0) "${hours}ч ${remainingMinutes}м" else "${remainingMinutes}м"
    }


    fun getStatusText(status: SessionStatus): String {
        return when (status) {
            SessionStatus.SCHEDULED -> "Запланирована"
            SessionStatus.RUNNING -> "Идет"
            SessionStatus.PAUSED -> "На паузе"
            SessionStatus.FINISHED -> "Завершена"
        }
    }

    fun isSessionTimeExpired(session: GameSession): Boolean {
        if (session.status != SessionStatus.RUNNING) return false
        if (session.startTime == 0L) return false
        
        val actualMinutes = calculateActualDurationMinutes(session)
        val plannedMinutes = (session.durationHours * 60).toInt()
        
        return actualMinutes >= plannedMinutes
    }


    fun isUserSessionTimeAlmostExpired(session: com.example.gamesession.authentication.presentation.user.UserComponent.UserSessionItem): Boolean {
        if (session.status != SessionStatus.RUNNING) return false
        if (session.startTime == 0L) return false
        
        val currentTime = System.currentTimeMillis()
        val currentElapsed = currentTime - session.startTime
        val actualMinutes = if (currentElapsed > 0) (currentElapsed / 60000L).toInt() else 0
        val plannedMinutes = (session.durationHours * 60).toInt()
        val remainingMinutes = plannedMinutes - actualMinutes
        
        return remainingMinutes <= 5 && remainingMinutes > 0
    }

    fun isAdminSessionTimeAlmostExpired(session: com.example.gamesession.authentication.presentation.session.AdminSessionComponent.SessionItem): Boolean {
        if (session.status != SessionStatus.RUNNING) return false
        if (session.startTime == 0L) return false
        
        val currentTime = System.currentTimeMillis()
        val currentElapsed = currentTime - session.startTime
        val actualMinutes = if (currentElapsed > 0) (currentElapsed / 60000L).toInt() else 0
        val plannedMinutes = (session.durationHours * 60).toInt()
        val remainingMinutes = plannedMinutes - actualMinutes
        
        return remainingMinutes <= 5 && remainingMinutes > 0
    }
}