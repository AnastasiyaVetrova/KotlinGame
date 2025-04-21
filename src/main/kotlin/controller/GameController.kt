package com.project.controller

import com.project.dto.PlayTurnResponse
import com.project.dto.StartGameResponse
import com.project.jwt.JwtUtil
import com.project.service.PlayGameService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/game")
class GameController(
    private val jwtUtil: JwtUtil,
    private val playGameService: PlayGameService
) {

    @PostMapping("/create")
    fun create(@RequestHeader("Authorization") header: String): ResponseEntity<Long> {
        val userId = jwtUtil.getIdFromToken(header)
        return ResponseEntity.ok(playGameService.createGame(userId))
    }

    @PostMapping("{gameId}/join")
    fun join(@RequestHeader("Authorization") header: String, @PathVariable gameId: Long): ResponseEntity<String> {
        val userId = jwtUtil.getIdFromToken(header)
        val countPlayer = playGameService.joinGame(gameId, userId)
        return ResponseEntity.ok("Количество игроков: $countPlayer")
    }

    @PostMapping("{gameId}/start")
    fun start(@PathVariable gameId: Long): ResponseEntity<StartGameResponse> {
        return ResponseEntity.ok(playGameService.startGame(gameId))
    }

    @PostMapping("{gameId}/play")
    fun play(
        @RequestHeader("Authorization") header: String,
        @PathVariable gameId: Long
    ): ResponseEntity<PlayTurnResponse> {
        val userId = jwtUtil.getIdFromToken(header)
        return ResponseEntity.ok(playGameService.play(gameId, userId))
    }

    @PostMapping("{gameId}/steal-points/from-player/{victimUserId}")
    fun stealPoints(
        @RequestHeader("Authorization") header: String,
        @PathVariable gameId: Long,
        @PathVariable victimUserId: Long
    ): ResponseEntity<PlayTurnResponse> {
        val userId = jwtUtil.getIdFromToken(header)
        return ResponseEntity.ok(playGameService.stealPointsFromPlayer(gameId, userId, victimUserId))
    }
}
