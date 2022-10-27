package com.example.groupproj_blackjack;

public class Deck { // class Deck, names each card

    private int i;
    public  Card[] noShuffle = new Card[52]; // calls Card class with 52 values and puts it in an array called noShuffle


    public Card[] createDeck(){ // method createDeck for the 52 cards used in the game
        for (Suit suit : Suit.values()){ // calling enum Suit (for symbols)
            noShuffle[i++] = new Two(suit, 2, "Two"); // calls class Two and sets these variables to it
            noShuffle[i++] = new Three(suit, 3, "Three"); // calls class Three and sets these variables to it
            noShuffle[i++] = new Four(suit, 4, "Four"); // calls class Four and sets these variables to it
            noShuffle[i++] = new Five(suit, 5, "Five"); // calls class Five and sets these variables to it
            noShuffle[i++] = new Six(suit, 6, "Six"); // calls class Six and sets these variables to it
            noShuffle[i++] = new Seven(suit, 7, "Seven"); // calls class Seven and sets these variables to it
            noShuffle[i++] = new Eight(suit, 8, "Eight"); // calls class Eight and sets these variables to it
            noShuffle[i++] = new Nine(suit, 9, "Nine"); // calls class Nine and sets these variables to it
            noShuffle[i++] = new Ten(suit, 10, "Ten"); // calls class Ten and sets these variables to it
            noShuffle[i++] = new Face(suit, 10, "Jack"); // calls class Jack and sets these variables to it
            noShuffle[i++] = new Face(suit, 10, "Queen"); // calls class Queen and sets these variables to it
            noShuffle[i++] = new Face(suit, 10, "King"); // calls class King and sets these variables to it
            noShuffle[i++] = new Ace(suit, 11, "Ace"); // calls class Ace and sets these variables to it
        } // closes createDeck
        return noShuffle; // returns noShuffle
    } // closes method Deck
}
