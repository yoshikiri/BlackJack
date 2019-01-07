import java.util.Random

fun main(args: Array<String>){
  val deck = Deck()

  var player = Player()
  player.addHand(deck)
  player.addHand(deck)

  for(i in player.hands){
    println(i)
  }

  player.calcScore()
  println("player score: " + player.score)

  var dealer = Player()
  dealer.addHand(deck)
  dealer.addHand(deck)
  println("Dealer's hand: " + dealer.hands[0] + ", ?")

  while(!player.isStand){
    println("Input D: Draw Next Card, S: Stand(Stop Drawing) -> ")
    var play = readLine()
    while(play != "D" && play != "S") {
      println("Input 'D' or 'S' -> ")
      play = readLine()
    }

    if(play == "D") player.addHand(deck)
    else if(play == "S") player.isStand = true

    println(player.hands)
    player.calcScore()
    println("Player's Score: " + player.score)

    player.checkBurst()
    player.checkBlackJack()
  }

  println("Dealer's turn...")

  println(dealer.hands)
  dealer.calcScore()

  while(dealer.score < 17 && !dealer.isStand){
    dealer.addHand(deck)
    dealer.calcScore()
    println(dealer.hands)

    dealer.checkBurst()
    dealer.checkBlackJack()
  }

  println("Player's Score: ${player.score}")
  println("Dealer's Score: ${dealer.score}")

  val resultMessage = if((player.isBurst && dealer.isBurst) || ((player.isBlackJack && dealer.isBlackJack))) "Draw"
                      else if(player.isBlackJack || dealer.isBurst) "Win!"
                      else if(player.isBurst || dealer.isBlackJack) "Lose..."
                      else if(player.score > dealer.score) "Win!"
                      else if(player.score == dealer.score) "Draw"
                      else "Lose..."

  println(resultMessage)
}

data class Card(val number: String, val score: Int, val suit: String){
}

class Deck() {
  private val suitList = listOf("Spade", "Club", "Diamond", "Heart")
  val cardList: MutableList<Card> = mutableListOf()

  init {
    makeDeck()
  }

  fun drawCard() : Card {
    val index: Int = Random().nextInt(this.cardList.size)
    val card: Card = this.cardList[index]
    this.cardList.removeAt(index)
    return card
  }

  private fun makeDeck(){
    for(suit in this.suitList){
      for(i in 1..13){
        val num = when(i){
          1 -> "A"
          11 -> "J"
          12 -> "Q"
          13 -> "K"
          else -> i.toString()
        }

        val score = if(i in 11..13) 10
                    else i

        this.cardList.add(Card(num, score, suit))
      }
    }
  }
}

class Player(val hands: MutableList<Card> = mutableListOf(), var isStand: Boolean = false, var isBurst: Boolean = false, var isBlackJack: Boolean = false, var score: Int = 0){
  fun addHand(deck: Deck){
    this.hands.add(deck.drawCard())
  }

  fun calcScore(){
    this.score = 0
    for(c in this.hands){
      this.score += c.score
    }
  }

  fun checkBurst(){
    if(this.score > 21){
      this.isBurst = true
      this.isStand = true
      println("Burst")
    }
  }

  fun checkBlackJack(){
    if(this.score == 21){
      this.isBlackJack = true
      this.isStand = true
      println("BlackJack!!")
    }

  }
}
