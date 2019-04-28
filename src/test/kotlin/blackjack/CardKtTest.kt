package blackjack

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import java.util.stream.Stream.of

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class CardKtTest {

    @Test
    fun `A full set of cards is generated`() {

        val cards = createDeckOfCards()

        assertEquals(52, cards.size)
        assertEquals(13, cards.filter { card -> card.suit == Suit.CLUB }.size)
        assertEquals(13, cards.filter { card -> card.suit == Suit.SPADE }.size)
        assertEquals(13, cards.filter { card -> card.suit == Suit.HEART }.size)
        assertEquals(13, cards.filter { card -> card.suit == Suit.DIAMOND }.size)
    }

    @Test
    fun `The correct card is dealt and the deck is adjusted`() {

        val blackjack = Blackjack(createDeckOfCards())

        val expectedCard = blackjack.deckOfCards[0]
        val actualCard = blackjack.dealCard()

        assertEquals(expectedCard, actualCard)
        assertEquals(51, blackjack.deckOfCards.size)
    }

    @ParameterizedTest
    @MethodSource("cardProvider")
    fun `The all possible scores have been accounted for`(testData: Pair<Int, List<Card>>) {
        assertEquals(testData.first, testData.second.getHandScore(false))
    }

    fun cardProvider(): Stream<Pair<Int, List<Card>>> {
        return of(
            Pair(14, listOf(Card(Suit.CLUB, Ranking.TEN), Card(Suit.DIAMOND, Ranking.FOUR))),
            Pair(11, listOf(Card(Suit.CLUB, Ranking.TEN), Card(Suit.SPADE, Ranking.ACE))),
            Pair(11, listOf(Card(Suit.DIAMOND, Ranking.FOUR), Card(Suit.HEART, Ranking.SEVEN))),
            Pair(17, listOf(Card(Suit.HEART, Ranking.EIGHT), Card(Suit.CLUB, Ranking.NINE))),
            Pair(18, listOf(Card(Suit.CLUB, Ranking.NINE), Card(Suit.DIAMOND, Ranking.NINE)))
        )
    }
}
