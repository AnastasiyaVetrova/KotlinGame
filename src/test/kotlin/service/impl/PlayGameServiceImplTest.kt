package service.impl

import com.project.manager.SessionGame
import com.project.manager.context.CardForGame
import com.project.manager.context.Player
import com.project.manager.strategy.NoneStrategy
import com.project.model.Game
import com.project.model.enums.StatusGame
import com.project.repository.CardRepository
import com.project.repository.UserRepository
import com.project.service.GameService
import com.project.service.impl.PlayGameServiceImpl
import com.project.service.impl.TurnServiceImpl
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import java.lang.reflect.Field
import java.util.concurrent.ConcurrentHashMap
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExtendWith(MockitoExtension::class)
class PlayGameServiceImplTest {
    @Mock
    lateinit var turnService: TurnServiceImpl

    @Mock
    lateinit var gameService: GameService

    @Mock
    lateinit var cardRepository: CardRepository

    @Mock
    lateinit var userRepository: UserRepository

    lateinit var playGameService: PlayGameServiceImpl

    @BeforeEach
    fun setUp() {
        playGameService = PlayGameServiceImpl(
            turnService,
            gameService,
            cardRepository,
            userRepository
        )
    }

    @Test
    fun createGameAndReturnIdGame() {
        val gameId: Long = 7
        val userId: Long = 5
        val saveGame = Game().apply { id = gameId }

        whenever(gameService.saveGame(any<Game>())).thenReturn(saveGame)

        val result = playGameService.createGame(userId)

        assertEquals(gameId, result)
        assertEquals(1, playGameService.getGamesForTest().size)
        assertTrue(playGameService.getGamesForTest().containsKey(gameId))
    }

    @Test
    fun createGameAndReturnThrow() {
        val userId: Long = 5
        val saveGame = Game()

        whenever(gameService.saveGame(any<Game>())).thenReturn(saveGame)

        val exception = assertThrows<ResponseStatusException> { playGameService.createGame(userId) }

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.statusCode)
        assertEquals("Создание игры не удалось, попробуйте позже", exception.reason)
    }

    @Test
    fun joinGameAndReturnSize() {
        val gameId: Long = 7
        val firstUserId: Long = 5
        val secondUserId: Long = 3
        val saveGame = Game().apply { id = gameId }

        whenever(gameService.saveGame(any<Game>())).thenReturn(saveGame)
        playGameService.createGame(firstUserId)

        val playerCount = playGameService.joinGame(gameId, secondUserId)

        assertEquals(2, playerCount)
    }

    @Test
    fun joinGameAndReturnThrow() {
        val gameId: Long = 7
        val userId: Long = 5
        val saveGame = Game().apply { id = gameId }

        whenever(gameService.saveGame(any<Game>())).thenReturn(saveGame)
        playGameService.createGame(userId)

        val exception = assertThrows<ResponseStatusException> { playGameService.joinGame(gameId, userId) }

        assertEquals(HttpStatus.BAD_REQUEST, exception.statusCode)
        assertEquals("Игрок уже добавлен или нет мест", exception.reason)
    }


    @Test
    fun play() {
        val gameId: Long = 7
        val firstUserId: Long = 5
        val secondUserId: Long = 3

        val field: Field = PlayGameServiceImpl::class.java.getDeclaredField("games")
        field.isAccessible = true
        val gamesMap = field.get(playGameService) as ConcurrentHashMap<Long, SessionGame>

        gamesMap[gameId] = getMockSessionGame(gameId, firstUserId, secondUserId)

        val response = playGameService.play(gameId, firstUserId)

        assertNotNull(response)
        assertTrue(response.description.contains("Вам добавлены очки:"))
        assertEquals(secondUserId, response.nextPlayerId)
        assertEquals(1, response.scope)
        assertEquals(firstUserId, response.currentPlayerId)
    }

    private fun getMockSessionGame(gameId: Long, firstUserId: Long, secondUserId: Long): SessionGame {

        val players: ArrayDeque<Player> = ArrayDeque()
        players.addLast(Player(firstUserId))
        players.addLast(Player(secondUserId))

        val deck: MutableList<CardForGame> = mutableListOf()
        deck.add(
            CardForGame(
                "NONE",
                1,
                1,
                NoneStrategy()
            )
        )

        return SessionGame(
            gameId,
            StatusGame.IN_PROGRESS,
            players,
            deck
        )
    }
}