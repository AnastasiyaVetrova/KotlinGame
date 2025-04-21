package com.project.manager


import com.project.manager.context.CardForGame
import com.project.manager.context.Player
import com.project.model.Card
import com.project.model.enums.StatusGame

class SessionGame(
    val partyNumber: Long,
    var status: StatusGame = StatusGame.WAIT_FOR_PLAYER,
    var players: ArrayDeque<Player> = ArrayDeque(),
    var deckOfCard: MutableList<CardForGame> = mutableListOf(),
    var turnNumber: Int = 0
) {
    fun addPlayer(player: Player): Boolean {
        if (players.size >= 4 || players.any { it.id == player.id }) return false
        players.addLast(player)
        return true
    }

    fun nextPlayer(): Player {
        val last = players.removeFirst()
        players.addLast(last)

        val current = players.first()

        if (current.skipNextTurn) {
            current.skipNextTurn = false
            return nextPlayer()
        }
        return current
    }

    fun currentPlayer(): Player {
        return players.first()
    }

    fun playerTakeCard(): CardForGame? {
        return deckOfCard.getOrNull(turnNumber)
    }

    fun nextTurn(): Int {
        return ++turnNumber
    }

    fun generateCard(sampleCards: MutableList<Card>) {
        val cardManager = CardManager()
        deckOfCard = cardManager.generateRandomDeck(sampleCards)
    }

    fun hasValidPlayerCount(): Boolean {
        return players.size in 2..4
    }
}
