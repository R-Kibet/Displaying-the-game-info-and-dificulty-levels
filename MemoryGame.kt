package com.example.anew.models

import com.example.anew.utils.Default_Icon

//objective is to maintain memory of the game at a particular state in time
//delegate responsibility of creating all the card

class MemoryGame (private val boardSize: BoardSize){


    val cards : List<MemoryCard>
    var pairsFound = 0

    private var indexOfSelectedCard: Int? = null
    private var flips = 0

     init {
         //Adding the icons to the game
         val  chosenImage = Default_Icon.shuffled().take(boardSize.getPairs())
         val randomizedImage = (chosenImage + chosenImage ).shuffled()
         cards =  randomizedImage.map { MemoryCard(it) }// we cant use all the 3 parameters isfaceup and is mached as we have set default values


     }

    //game logic
    fun flipCard(position: Int): Boolean {
        flips++
        val card = cards[position]
        // 3 cases:
        // 0 cards previously flipped over -> flip over the selected card
        // 1 card previously flipped over -> flip over the nxt selected card + check for match
        // 2 cards previously flipped over -> The 2 cards are restored back + flip over the selected card

        var foundMatch = false
        if (indexOfSelectedCard == null){
            //this means there is either 0 cards flipped over or 2 cards flipped with no match

            restoreCards()
            indexOfSelectedCard = position
        }
        else{
            // card is previousle flipped over check for match
           foundMatch = checkForMatch(indexOfSelectedCard!! , position) //checks 2 card and returns a boolean result
            indexOfSelectedCard = null
        }


        card.isFaceUp = !card.isFaceUp //if it was face up it will be face down and vice versa
        return foundMatch
    }

    private fun checkForMatch(position1: Int, position2: Int): Boolean {
        return if (cards[position1].identifier != cards[position2].identifier)
            false
        else {
            cards[position1].isMatched = true
            cards[position2].isMatched = true
            pairsFound++

            true
        }
    }

    private fun restoreCards() {
        //iterate through list of all the cards we have
        for (card in cards) {
            if (!card.isMatched) {
                card.isFaceUp = false //turn everything back to default
            }
        }
    }

    fun haveWon(): Boolean {

        //game is over when the number of pairs is same to the number of board pairs
        return pairsFound == boardSize.getPairs()


    }

    fun isCardFaceUp(position: Int): Boolean {
        return cards[position].isFaceUp

    }

    fun getMoves(): Int {
        return flips / 2

    }
}