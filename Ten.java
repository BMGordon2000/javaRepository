package com.example.groupproj_blackjack;

public class Ten extends Card { // Ten class is subclass of Card

    public Ten(Suit suit, int value, String name) { // uses Card suit, value, and name
        super(suit, 10, "Ten"); // calls from superclass Card and implements these values for Ten class
    } // closes constructor Ten
} // closes class Ten
