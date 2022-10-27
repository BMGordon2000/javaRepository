package com.example.groupproj_blackjack;

import com.example.groupproj_blackjack.Card;

import java.util.ArrayList;

public class Hand { // class Hand
    ArrayList<Card> dealerHand = new ArrayList<Card>(); // calling the array in class card for dealer
    ArrayList<Card> playerHand = new ArrayList<Card>(); // calling the array in class card for player

    public ArrayList<Card> createDealerHand(Card[] shuffleDeck){ // creating which cards dealer gets
        dealerHand.add(shuffleDeck[0]);
        dealerHand.add(shuffleDeck[2]);

        return dealerHand; // returns dealer with one card
    } // closes createDealerHand method

    public ArrayList<Card> createPlayerHand(Card[] shuffleDeck){ // creating which cards player gets
        playerHand.add(shuffleDeck[1]);
        playerHand.add(shuffleDeck[3]);

        return playerHand; // returns player with 2 cards
    } // closes createPlayerHand method

    public ArrayList<Card> addPlayerHand(int deckIndex, Card[] shuffleDeck, ArrayList<Card> playerHand){
        // adds a card to player
        playerHand.add(shuffleDeck[deckIndex]); // gets a card

        return playerHand; // returns the card
    } // closes addPlayerHand method

    public ArrayList<Card> addDealerHand(int deckIndex, Card[] shuffleDeck, ArrayList<Card> dealerHand){
        // adds a card to dealer
        dealerHand.add(shuffleDeck[deckIndex]); // gets a card

        return dealerHand; // returns card
    } // closes addDealerHand method
} // closes Hand class
