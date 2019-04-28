package blackjack

import com.github.ajalt.clikt.output.TermUi

enum class GameState {
    BUST,
    BLACKJACK,
    CONTINUE
}

data class Blackjack(var deckOfCards: List<Card>) {
    fun dealCard(): Card {
        val card = deckOfCards[0]
        deckOfCards = deckOfCards.slice(IntRange(1, deckOfCards.size - 1))
        return card
    }
}

fun playHand(hand: MutableList<Card>, blackJack: Blackjack, house: Boolean): Int {
    var gameInProgress = true
    var totalScore = 0

    while (gameInProgress) {
        val scores = getAllScores(hand)
        val output = createScoreConsoleOutput(scores)

        TermUi.echo(output)
        TermUi.echo("$hand")
        TermUi.echo("Score:")

        when {
            scores.size == 2 -> {
                if (isBust(scores[0]) && isBust(scores[1])) {
                    TermUi.echo("Bust!")
                    gameInProgress = false
                }

                if (isBlackjack(scores[0]) || isBlackjack(scores[1])) {
                    TermUi.echo("Blackjack!")
                    gameInProgress = false
                }

                if (house && (houseWinningHand(scores[0]) || houseWinningHand(scores[1]))) {
                    gameInProgress = false
                }

                if (scores[0] > scores[1]) {
                    totalScore = scores[0]
                } else {
                    totalScore = scores[1]
                }

            }
            scores.size == 1 -> {
                val score = scores.first()

                if (isBust(score)) {
                    TermUi.echo("Bust!")
                    gameInProgress = false
                }

                if (isBlackjack(score)) {
                    TermUi.echo("Blackjack!")
                    gameInProgress = false
                }

                if (house && houseWinningHand(score)) {
                    gameInProgress = false
                }

                totalScore = score

            }
        }

        if (gameInProgress) {

            if (house) {
                hand.add(blackJack.dealCard())
            } else {
                TermUi.echo("Stick[s] or twist[t]?")

                when (readLine()) {
                    "s" -> {
                        gameInProgress = false
                    }
                    else -> hand.add(blackJack.dealCard())
                }
            }
        }
    }

    return totalScore
}

fun determineWinner(playerScore: Int, houseScore: Int) {
    if ((!isBust(playerScore) || isBust(houseScore)) && playerScore > houseScore) {
        TermUi.echo("Player Wins!")
    } else if ((!isBust(houseScore) || isBust(playerScore)) && houseScore > playerScore) {
        TermUi.echo("House Wins!")
    } else if (houseScore == playerScore) {
        TermUi.echo("Tie! ")
    }
}

fun createScoreConsoleOutput(scores: List<Int>): String {
    val playerScoreAceLow = scores[0]

    val sb = StringBuilder()
    sb.append(playerScoreAceLow)

    if (scores.size > 1 && !isBust(scores[1])) {
        sb.append("/")
        sb.append(scores[1])
    }

    return sb.toString()
}

fun getAllScores(hand: List<Card>): List<Int> {
    return if (hand.containsAce()) {
        listOf(hand.getHandScore(false), hand.getHandScore(true))
    } else {
        listOf(hand.getHandScore(false))
    }
}

fun isBust(score: Int) = getGameStateFromPotentialScores(score) == GameState.BUST &&
        getGameStateFromPotentialScores(score) == GameState.BUST

fun isBlackjack(score: Int) = getGameStateFromPotentialScores(score) == GameState.BLACKJACK &&
        getGameStateFromPotentialScores(score) == GameState.BLACKJACK

fun houseWinningHand(score: Int) = score in 17..21

fun getGameStateFromPotentialScores(score: Int): GameState {
    return when {
        score == 21 -> GameState.BLACKJACK
        score > 21 -> GameState.BUST
        else -> GameState.CONTINUE
    }
}
