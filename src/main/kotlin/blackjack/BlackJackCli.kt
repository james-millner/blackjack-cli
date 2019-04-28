package blackjack

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.output.TermUi.echo
import java.util.concurrent.TimeUnit

class InitializeBlackjack : CliktCommand() {

    private var deckOfCards = createDeckOfCards().shuffled()

    override fun run() {
        echo("----------------------------------------------------------------------------------------------------")
        echo(
            "  .------..------..------..------..------..------..------..------..------.\n" +
                    "   |B.--. ||L.--. ||A.--. ||C.--. ||K.--. ||J.--. ||A.--. ||C.--. ||K.--. |\n" +
                    "   | :(): || :/\\: || (\\/) || :/\\: || :/\\: || :(): || (\\/) || :/\\: || :/\\: |\n" +
                    "   | ()() || (__) || :\\/: || :\\/: || :\\/: || ()() || :\\/: || :\\/: || :\\/: |\n" +
                    "   | '--'B|| '--'L|| '--'A|| '--'C|| '--'K|| '--'J|| '--'A|| '--'C|| '--'K|\n" +
                    "   `------'`------'`------'`------'`------'`------'`------'`------'`------'"
        )
        echo("----------------------------------------------------------------------------------------------------")

        val playerHand = emptyList<Card>().toMutableList()
        val houseHand = emptyList<Card>().toMutableList()

        val blackJack = Blackjack(deckOfCards)

        playerHand.add(blackJack.dealCard())
        houseHand.add(blackJack.dealCard())
        playerHand.add(blackJack.dealCard())
        houseHand.add(blackJack.dealCard())

        echo("Your hand:")

        val playerScore = playHand(playerHand, blackJack, false)

        if (isBust(playerScore)) {
            return
        }

        TimeUnit.SECONDS.sleep(1)

        echo("----------------------------------------------------------------------------------------------------")
        echo("House Turn")
        echo("----------------------------------------------------------------------------------------------------")
        TimeUnit.SECONDS.sleep(1)

        val houseScore = playHand(houseHand, blackJack, true)

        echo("----------------------------------------------------------------------------------------------------")
        echo("Player Score: $playerScore")
        echo("House: $houseScore")
        echo("----------------------------------------------------------------------------------------------------")

        determineWinner(playerScore, houseScore)

        echo("----------------------------------------------------------------------------------------------------")
    }
}

fun main(args: Array<String>) {
    InitializeBlackjack()
        .main(args)
}
