package com.example.groupproj_blackjack;

public class Three extends Card { // Three class is subclass of Card

    public Three(Suit suit, int value, String name) { // uses Card suit, value, and name
        super(suit, 3, "Three"); // calls from superclass Card and implements these values for Three class
    } // closes constructor Three
} // closes class Three
