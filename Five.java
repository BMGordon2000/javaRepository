package com.example.groupproj_blackjack;

public class Five extends Card { // Five class is subclass of Card

    public Five(Suit suit, int value, String name) { // uses Card suit, value, and name
        super(suit, 5, "Five"); // calls from superclass Card and implements these values for Five class
    } // closes constructor Five
} // closes class Five
