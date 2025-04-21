package com.project.service.impl

import com.project.model.Game
import com.project.model.User
import com.project.model.enums.StatusGame
import com.project.repository.GameRepository
import com.project.repository.UserRepository
import com.project.service.GameService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime

@Service
@Transactional
class GameServiceImpl(
    private val gameRepository: GameRepository,
    private val userRepository: UserRepository,
) : GameService {

    override fun saveFinishedGame(gameId: Long, userId: Long) {

        val game: Game = gameRepository.findByIdOrNull(gameId) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Игры с ID - $gameId не существует"
        )
        val user: User = userRepository.findByIdOrNull(userId) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Игрока с ID $userId не существует"
        )

        game.finishedDate = LocalDateTime.now()
        game.status = StatusGame.FINISHED
        game.winner = user

        gameRepository.save(game)
    }

    override fun updateStatus(gameId: Long, status: StatusGame) {
        gameRepository.updateStatusById(gameId, status)
    }

    override fun saveGame(game: Game): Game {
        return gameRepository.save(game)
    }
}