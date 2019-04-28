package blackjack

enum class Suit {
    DIAMOND,
    HEART,
    SPADE,
    CLUB
}

enum class Ranking {
    ACE,
    TWO,
    THREE,
    FOUR,
    FIVE,
    SIX,
    SEVEN,
    EIGHT,
    NINE,
    TEN,
    JACK,
    QUEEN,
    KING

}

data class Card(val suit: Suit, val rank: Ranking) {
    override fun toString(): String {
        return "${this.rank} - ${this.suit}"
    }
}

fun createDeckOfCards(): List<Card> =
    Suit.values().flatMap { suit ->
        Ranking.values().map { rank -> Card(suit, rank) }
    }

fun List<Card>.containsAce()= this.any { it.rank == Ranking.ACE }

fun List<Card>.getHandScore(aceHighCard: Boolean): Int {
    var score = 0

    this.forEach { card ->
        val cardScore = when (card.rank) {
            Ranking.ACE -> {
                if(aceHighCard)
                    11
                else
                    1
            }
            Ranking.TWO -> 2
            Ranking.THREE -> 3
            Ranking.FOUR -> 4
            Ranking.FIVE -> 5
            Ranking.SIX -> 6
            Ranking.SEVEN -> 7
            Ranking.EIGHT -> 8
            Ranking.NINE -> 9
            Ranking.TEN, Ranking.JACK, Ranking.QUEEN, Ranking.KING -> 10
        }

        score += cardScore
    }

    return score
}
