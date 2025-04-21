package manager

import com.project.manager.CardManager

import com.project.model.Card
import com.project.model.enums.CardType
import com.project.utils.GameConstant.SIZE_DECK
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CardManagerTest {
    private val cardManager = CardManager()
    private lateinit var cards: MutableList<Card>

    @Test
    fun generateRandomDeckTest_returnsDeckOfExpectedSize() {
        val cards = mutableListOf(
            Card(1, "CardPoints", CardType.POINTS_CARD),
            Card(2, "CardAction", CardType.ACTION_CARD),
        )

        val deck = cardManager.generateRandomDeck(cards)

        assertEquals(SIZE_DECK, deck.size)
    }
}