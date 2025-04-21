package com.project.service

import com.project.dto.PlayTurnResponse
import com.project.dto.StartGameResponse

interface PlayGameService {

    fun createGame(userId: Long): Long

    fun joinGame(gameId: Long, userId: Long): Int

    fun startGame(gameId: Long): StartGameResponse

    fun play(gameId: Long, userId: Long): PlayTurnResponse

    fun stealPointsFromPlayer(gameId: Long, userId: Long, playerId: Long): PlayTurnResponse
}