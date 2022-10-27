package com.example.groupproj_blackjack;

public class Ace extends Card { // Ace class is subclass of Card

    public Ace(Suit suit, int value, String name) { // uses Card suit, value, and name
        super(suit, 11, "Ace"); // calls from superclass Card and implements these values for Ace class
    } // closes constructor Ace
} // closes Ace class

