package com.project.repository

import com.project.model.Turn
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TurnRepository : JpaRepository<Turn, Long> {

    fun findByGameIdAndTurnNumber(gameId: Long, turnNumber: Int): Turn?
}