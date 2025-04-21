package com.project.service.impl

import com.project.dto.PlayTurnResponse
import com.project.dto.StartGameResponse

import com.project.manager.SessionGame
import com.project.manager.context.CardForGame
import com.project.manager.context.Player

import com.project.model.Card
import com.project.model.Game
import com.project.model.enums.StatusGame
import com.project.repository.CardRepository
import com.project.repository.UserRepository
import com.project.service.GameService
import com.project.service.PlayGameService
import com.project.utils.GameConstant.MAX_SCORE
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.concurrent.ConcurrentHashMap

@Service
class PlayGameServiceImpl(
    private val turnService: TurnServiceImpl,
    private val gameService: GameService,
    private val cardRepository: CardRepository,
    private val userRepository: UserRepository,
    private val games: ConcurrentHashMap<Long, SessionGame> = ConcurrentHashMap()
) : PlayGameService {


    override fun createGame(userId: Long): Long {

        val game = gameService.saveGame(Game())

        val gameId = game.id ?: throw ResponseStatusException(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Создание игры не удалось, попробуйте позже"
        )

        val sessionGame = SessionGame(gameId)
        sessionGame.addPlayer(Player(userId))

        games[gameId] = sessionGame
        return gameId
    }

    override fun joinGame(gameId: Long, userId: Long): Int {
        val sessionGame = getSession(gameId)

        if (!sessionGame.addPlayer(Player(userId))) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Игрок уже добавлен или нет мест"
            )
        }
        return sessionGame.players.size
    }

    override fun startGame(gameId: Long): StartGameResponse {
        val sessionGame = getSession(gameId)

        if (!sessionGame.hasValidPlayerCount()) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Недостаточно игроков")
        }

        sessionGame.status = StatusGame.IN_PROGRESS

        gameService.updateStatus(gameId, sessionGame.status)

        val sampleCards: MutableList<Card> = cardRepository.findAll()
        sessionGame.generateCard(sampleCards)

        return StartGameResponse(gameId, sessionGame.currentPlayer().id)
    }

    override fun play(gameId: Long, userId: Long): PlayTurnResponse {
        val sessionGame = getSession(gameId)
        val player = getCurrentPlayer(sessionGame, userId)
        val card = getCurrentCard(sessionGame)

        val actionResult = card.action.performAction(player.score, card.point)
        player.setNewScore(actionResult.scope)
        player.skipNextTurn = actionResult.skipNextTurn

        if (player.score == MAX_SCORE) {
            actionResult.description += " Вы выиграли!"
            sessionGame.status = StatusGame.FINISHED
            gameService.saveFinishedGame(gameId, userId)
        }

        turnService.saveTurn(gameId, userId, sessionGame.turnNumber, card)

        val response = PlayTurnResponse(
            actionResult.description,
            sessionGame.turnNumber,
            player.id,
            player.id,
            scope = player.score
        )

        if (actionResult.changeNextTurn) {
            response.nextPlayerId = sessionGame.nextPlayer().id
            sessionGame.nextTurn()
        }

        return response
    }

    override fun stealPointsFromPlayer(gameId: Long, userId: Long, victimUserId: Long): PlayTurnResponse {
        val sessionGame = getSession(gameId)
        val thiefPlayer = getCurrentPlayer(sessionGame, userId)
        val victimPlayer = getVictimPlayer(sessionGame, victimUserId)
        val card = getCurrentCard(sessionGame)

        victimPlayer.setNewScore(victimPlayer.score - card.point)
        thiefPlayer.setNewScore(thiefPlayer.score + card.point)

        val response = PlayTurnResponse(
            "Игрок ${thiefPlayer.id} украл очки у игрока ${victimPlayer.id}",
            sessionGame.turnNumber,
            thiefPlayer.id,
            sessionGame.nextPlayer().id,
            scope = thiefPlayer.score,
            victimPlayer.id,
            victimPlayer.score
        )

        if (thiefPlayer.score == MAX_SCORE) {
            response.description += " Вы выиграли!"
            sessionGame.status = StatusGame.FINISHED
            gameService.saveFinishedGame(gameId, userId)
        }

        turnService.updateTurn(gameId, sessionGame.turnNumber, victimUserId, thiefPlayer.score)

        sessionGame.nextTurn()
        return response
    }

    private fun getSession(gameId: Long): SessionGame {
        val sessionGame = games[gameId] ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Игры с ID - $gameId не существует"
        )
        if (sessionGame.status == StatusGame.FINISHED) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Сессия уже завершена")
        }
        return sessionGame
    }

    private fun getCurrentPlayer(sessionGame: SessionGame, userId: Long): Player {
        val player = sessionGame.currentPlayer()
        if (player.id != userId) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Ходит другой игрок")
        }
        return player
    }

    private fun getVictimPlayer(sessionGame: SessionGame, victimUserId: Long): Player {
        if (!userRepository.existsById(victimUserId)) {
            throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Игрок с ID - $victimUserId не существует"
            )
        }
        val victimPlayer = sessionGame.players.find { p -> p.id == victimUserId } ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Игрок с ID - $victimUserId не найден в сессии игры"
        )
        return victimPlayer
    }

    private fun getCurrentCard(sessionGame: SessionGame): CardForGame {
        return sessionGame.playerTakeCard() ?: throw ResponseStatusException(
            HttpStatus.BAD_REQUEST,
            "Карты закончились"
        )
    }

    fun getGamesForTest(): Map<Long, SessionGame> = games
}