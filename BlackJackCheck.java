package com.example.groupproj_blackjack;

public class BlackJackCheck { // checks if BlackJack has happened
    boolean blackJack = false; // sets blackJack as false

    public boolean isBlackJack(Card card1, Card card2) { // takes card1 and card2
        if (card1.getClass().getSimpleName().equals("Face") &&
        card2.getClass().getSimpleName().equals("Ace")) {
            blackJack = true; // black jack has been reached, changes false to true
        } // closes if statement
        else if (card1.getClass().getSimpleName().equals("Ace") &&
        card2.getClass().getSimpleName().equals("Face")) {
            blackJack = true; // black jack has been reached, changes false to true
        } // closes else if statement
        else { // if everything doesn't work
            blackJack = false; // black jack has not been reached, stays false
        } // closes else statement
        return blackJack; // returns whatever blackjack has been changes to
    } // closes isBlackJack method
} // closes class BlackJackCheck
