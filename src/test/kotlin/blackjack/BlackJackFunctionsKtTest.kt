package blackjack

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class BlackJackFunctionsKtTest {

    @Test
    fun `21 is recognised as blackjack`() =
        assertEquals(GameState.BLACKJACK, getGameStateFromPotentialScores(21))

    @Test
    fun `anything greater than 21 is recognised as bust`() =
        assertEquals(GameState.BUST, getGameStateFromPotentialScores(22))

    @Test
    fun `anything less than 21 is recognised as continue`() =
        assertEquals(GameState.CONTINUE, getGameStateFromPotentialScores(19))

    @ParameterizedTest
    @ValueSource(ints = [17, 18, 19, 20, 21])
    fun `house winning scores are recognised correctly`(score: Int) {
        assertTrue(houseWinningHand(score))
    }

    @Test
    fun `blackjack score recognised`() = assertTrue(isBlackjack(21))

    @Test
    fun `bust recognised`() = assertTrue(isBust(22))

    @ParameterizedTest
    @MethodSource("cardProvider")
    fun `correct score possibilities are generated`(testData: Pair<Int, List<Card>>) {
        assertEquals(testData.first, getAllScores(testData.second).size)
    }

    fun cardProvider(): Stream<Pair<Int, List<Card>>> {
        return Stream.of(
            Pair(1, listOf(Card(Suit.CLUB, Ranking.TEN), Card(Suit.DIAMOND, Ranking.FOUR))),
            Pair(2, listOf(Card(Suit.CLUB, Ranking.TEN), Card(Suit.SPADE, Ranking.ACE))),
            Pair(1, listOf(Card(Suit.DIAMOND, Ranking.FOUR), Card(Suit.HEART, Ranking.SEVEN))),
            Pair(1, listOf(Card(Suit.HEART, Ranking.EIGHT), Card(Suit.CLUB, Ranking.NINE))),
            Pair(2, listOf(Card(Suit.CLUB, Ranking.NINE), Card(Suit.DIAMOND, Ranking.ACE))),
            Pair(2, listOf(Card(Suit.CLUB, Ranking.NINE), Card(Suit.DIAMOND, Ranking.ACE), Card(Suit.DIAMOND, Ranking.KING)))
        )
    }

    @ParameterizedTest
    @MethodSource("cardProviderForConsoleOut")
    fun `correct score possibilities are outputed correctly`(testData: Pair<Int, List<Card>>) {
        assertEquals(testData.first, createScoreConsoleOutput(getAllScores(testData.second).toList()))
    }

    fun cardProviderForConsoleOut(): Stream<Pair<String, List<Card>>> {
        return Stream.of(
            Pair("14", listOf(Card(Suit.CLUB, Ranking.TEN), Card(Suit.DIAMOND, Ranking.FOUR))),
            Pair("11/21", listOf(Card(Suit.CLUB, Ranking.TEN), Card(Suit.SPADE, Ranking.ACE))),
            Pair("11", listOf(Card(Suit.DIAMOND, Ranking.FOUR), Card(Suit.HEART, Ranking.SEVEN))),
            Pair("17", listOf(Card(Suit.HEART, Ranking.EIGHT), Card(Suit.CLUB, Ranking.NINE))),
            Pair("10/20", listOf(Card(Suit.CLUB, Ranking.NINE), Card(Suit.DIAMOND, Ranking.ACE))),
            Pair("20", listOf(Card(Suit.CLUB, Ranking.NINE), Card(Suit.DIAMOND, Ranking.ACE), Card(Suit.DIAMOND, Ranking.KING)))
        )
    }

    @ParameterizedTest
    @MethodSource("cardProviderForWinners")
    fun `The winner is determined successfully`(testData: Pair<GameResult, Pair<Int, Int>>) {

        val expectedGameResult = testData.first
        val (playerScore, houseScore) = testData.second

        assertEquals(expectedGameResult.name, determineWinner(playerScore, houseScore))
    }

    fun cardProviderForWinners(): Stream<Pair<GameResult, Pair<Int, Int>>> {
        return Stream.of(
            Pair(GameResult.HOUSE_WINS, Pair(10, 19)),
            Pair(GameResult.TIE, Pair(15, 15)),
            Pair(GameResult.PLAYER_WINS, Pair(18, 17)),
            Pair(GameResult.PLAYER_WINS, Pair(21, 25)),
            Pair(GameResult.TIE, Pair(21, 21))
        )
    }

    @ParameterizedTest
    @MethodSource("scorePairings")
    fun `determineScore returns the correct value we expect from a list of integer scores`(testData: Pair<Int, Pair<Int, Int>>) {
        val (highScore, lowScore) = testData.second
        assertEquals(testData.first, determineCorrectScoreToTake(highScore, lowScore))
    }

    fun scorePairings(): Stream<Pair<Int, Pair<Int, Int>>> {
        return Stream.of(
            Pair(18, Pair(28, 18)),
            Pair(21, Pair(21, 11)),
            Pair(21, Pair(21, 3)),
            Pair(19, Pair(19, 9)),
            Pair(16, Pair(16, 6)),
            Pair(15, Pair(15, 5)),
            Pair(14, Pair(14, 4)),
            Pair(9, Pair(9, 7))
        )
    }

    @ParameterizedTest
    @MethodSource("handPairings")
    fun `getHandscore returns the correct score based of a list of possible scores`(testData: Pair<Int, Pair<Int, Int>>) {
        val (scoreOne, scoreTwo) = testData.second
        val scoresList = listOf(scoreOne, scoreTwo)

        assertEquals(testData.first, getHandScore(scoresList))
    }

    fun handPairings(): Stream<Pair<Int, Pair<Int, Int>>> {
        return Stream.of(
            Pair(11, Pair(10, 11)),
            Pair(11, Pair(11, 10)),
            Pair(9, Pair(9, 9)),
            Pair(10, Pair(10, 10)),
            Pair(8, Pair(8, 8)),
            Pair(15, Pair(15, 5)),
            Pair(14, Pair(14, 4)),
            Pair(9, Pair(9, 7))
        )
    }
}
