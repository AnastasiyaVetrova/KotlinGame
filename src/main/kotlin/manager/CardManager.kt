package com.project.manager

import com.project.manager.context.CardForGame
import com.project.manager.strategy.BlockStrategy
import com.project.manager.strategy.ActionTypeStrategy
import com.project.manager.strategy.DoubleDownStrategy
import com.project.manager.strategy.NoneStrategy
import com.project.manager.strategy.StealStrategy
import com.project.model.Card
import com.project.model.enums.CardType
import com.project.utils.GameConstant.SIZE_DECK
import kotlin.random.Random

class CardManager {
    companion object {
        private const val MIN_RANDOM: Int = 10
        private const val MIDDLE_RANDOM: Int = 40
        private const val MAX_RANDOM: Int = 60
        private const val STEAL_CHANCE: Int = 23
        private const val DOUBLE_DOWN_CHANCE: Int = 34
        private const val CARD_PERCENT: Int = 60
        private const val POINT_PERCENT: Int = 70
        private const val STRATEGY_PERCENT: Int = 100
    }

    fun generateRandomDeck(cards: MutableList<Card>): MutableList<CardForGame> {
        val pointsCard = cards.filter { it.type == CardType.POINTS_CARD }
        val actionCard = cards.filter { it.type == CardType.ACTION_CARD }

        return MutableList(SIZE_DECK) { index ->
            if (randomValue(CARD_PERCENT) < MIDDLE_RANDOM) generateCards(pointsCard.random(), index + 1)
            else generateCards(actionCard.random(), index + 1)
        }
    }

    private fun generateCards(card: Card, index: Int): CardForGame {
        val cardTypeStrategy = if (card.type == CardType.POINTS_CARD) NoneStrategy() else generateStrategy()
        val point: Int = randomValue(POINT_PERCENT) / 10
        return CardForGame(
            "${card.type} - ${cardTypeStrategy.getType().name}",
            point,
            index,
            cardTypeStrategy
        )
    }

    private fun generateStrategy(): ActionTypeStrategy {
        val value = randomValue(STRATEGY_PERCENT)
        return when {
            value < STEAL_CHANCE -> BlockStrategy()
            value in STEAL_CHANCE..DOUBLE_DOWN_CHANCE -> StealStrategy()
            else -> DoubleDownStrategy()
        }
    }

    private fun randomValue(percent: Int): Int {
        return if (Random.nextInt(0, 100) <= percent)
            Random.nextInt(MIN_RANDOM, MIDDLE_RANDOM) else Random.nextInt(MIDDLE_RANDOM, MAX_RANDOM)
    }
}