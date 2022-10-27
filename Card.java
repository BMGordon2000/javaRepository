package com.example.groupproj_blackjack;

public class Card { // class Card used for naming the cards of the deck

    final int value; // variable value
    final String name; // variable name
    final Suit suit; // variable suit, diamond, heart, spade or club (the symbol associated w the card)

    public Card(Suit suit, int value, String name){ // constructor is declaring our variables
        this.suit = suit; // declares suit
        this.value = value; // declares value
        this.name = name; // declares name
    } // closes constructor Card

    public int getValue() { // getter for Value
        return value; // returns value
    } // closes getter for Value

    public String getName() { // getter for Name
        return name; // returns name
    } // closes getter for Name

    public Suit getSuit() { // getter for Suit
        return suit; // returns suit
    } // closes getter for Suit

    public String toString() { // method string format
        return String.format("%s %s %s", suit, value, name); // will show the symbol, its value or number and the name
    } // closes method toString
} // closes class Card
