package blackjack

import com.github.ajalt.clikt.output.TermUi.echo

enum class GameState {
    BUST,
    BLACKJACK,
    CONTINUE
}

enum class GameResult {
    PLAYER_WINS,
    HOUSE_WINS,
    TIE
}

data class Blackjack(var deckOfCards: List<Card>) {
    fun dealCard(): Card {
        val card = deckOfCards.first()
        deckOfCards = deckOfCards.drop(1)
        return card
    }
}

fun playHand(hand: MutableList<Card>, blackJack: Blackjack, house: Boolean): Int {
    var gameInProgress = true
    var totalScore = 0

    while (gameInProgress) {
        val scores = getAllScores(hand).toList()
        val output = createScoreConsoleOutput(scores)

        echo(output)
        echo("$hand")

        totalScore = getHandScore(scores)
        gameInProgress = shouldTurnContinue(totalScore, house)

        if(!house && gameInProgress) {

            echo("Stick[s] or twist[t]?")

            when (readLine()) {
                "t" -> hand.add(blackJack.dealCard())
                else -> gameInProgress = false
            }
        } else {
            hand.add(blackJack.dealCard())
        }
    }

    return totalScore
}

fun getHandScore(scores: List<Int>): Int =
    when {
        scores.size == 2 -> {
            val (highScore, lowScore) = scores.sortedDescending()
            determineCorrectScoreToTake(highScore, lowScore)
        }
        else -> scores.first()
    }


fun determineCorrectScoreToTake(highScore: Int, lowScore: Int) = if(!isBust(highScore) && highScore > lowScore ) {
    highScore
} else {
    lowScore
}

fun determineWinner(playerScore: Int, houseScore: Int) =
    when {
        playerScore > houseScore || (isBust(houseScore) && !isBust(playerScore)) -> GameResult.PLAYER_WINS.name
        houseScore > playerScore || (!isBust(houseScore) && isBust(playerScore)) -> GameResult.HOUSE_WINS.name
        else -> GameResult.TIE.name
    }


fun createScoreConsoleOutput(scores: List<Int>): String {
    val playerScoreAceLow = scores.first()

    val sb = StringBuilder()
    sb.append(playerScoreAceLow)

    if (scores.size > 1 && !isBust(scores[1])) {
        sb.append("/")
        sb.append(scores[1])
    }

    return sb.toString()
}

fun getAllScores(hand: List<Card>): Set<Int> =
    setOf(hand.getHandScore(false), hand.getHandScore(true))

fun shouldTurnContinue(score: Int, housePlaying: Boolean): Boolean = when {
        isBust(score) -> false
        isBlackjack(score) -> false
        housePlaying && houseWinningHand(score) -> false
        else -> true
    }

fun isBust(score: Int) = getGameStateFromPotentialScores(score) == GameState.BUST

fun isBlackjack(score: Int) = getGameStateFromPotentialScores(score) == GameState.BLACKJACK

fun houseWinningHand(score: Int) = score in 17..21

fun getGameStateFromPotentialScores(score: Int): GameState {
    return when {
        score == 21 -> GameState.BLACKJACK
        score > 21 -> GameState.BUST
        else -> GameState.CONTINUE
    }
}
