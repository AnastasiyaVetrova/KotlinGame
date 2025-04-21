package com.project.service.impl

import com.project.manager.context.CardForGame
import com.project.model.Game
import com.project.model.Turn
import com.project.model.User
import com.project.repository.GameRepository
import com.project.repository.TurnRepository
import com.project.repository.UserRepository
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.CompletableFuture

@Service
class TurnServiceImpl(
    private val userRepository: UserRepository,
    private val gameRepository: GameRepository,
    private val turnRepository: TurnRepository
) {

    @Async
    @Transactional
    fun saveTurn(gameId: Long, userId: Long, turnNumber: Int, card: CardForGame): CompletableFuture<String> {
        val game: Game =
            gameRepository.findById(gameId).orElseThrow { NoSuchElementException("Игра id= $gameId не найдена") }
        val user: User =
            userRepository.findById(userId).orElseThrow { NoSuchElementException("Игрок id= $userId не найден") }

        var turn = Turn(
            null,
            game,
            user,
            null,
            card.name,
            turnNumber = turnNumber,
            point = card.point
        )
        turnRepository.save(turn)

        //Можно настроить обработку результата
        return CompletableFuture.completedFuture("Сохранено")
    }

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun updateTurn(gameId: Long, turnNumber: Int, victimUserId: Long, thiefScore: Int): CompletableFuture<String> {

        var turn = turnRepository.findByGameIdAndTurnNumber(gameId, turnNumber)
            ?: throw NoSuchElementException("Ход не найден: gameId=$gameId, turnNumber=$turnNumber")

        val victimUser = userRepository.findById(victimUserId)
            .orElseThrow { NoSuchElementException("Игрок id= $victimUserId не найден") }

        turn.targetUser = victimUser

        turnRepository.save(turn)

        //Можно настроить обработку результата
        return CompletableFuture.completedFuture("Сохранено")
    }
}