package com.project.service

import com.project.model.Game
import com.project.model.enums.StatusGame

interface GameService {

    fun saveFinishedGame(gameId: Long, userId: Long)

    fun updateStatus(gameId: Long, status: StatusGame)

    fun saveGame(game: Game) : Game
}