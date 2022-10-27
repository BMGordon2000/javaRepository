package com.example.groupproj_blackjack;

public class Face extends Card { // Face class is subclass of Card

    public Face(Suit suit, int value, String name) { // uses Card suit, value, and name
        super(suit, 10, name); // calls from superclass Card and implements these values for Face class
    } // closes constructor Face
} // closes Face class
