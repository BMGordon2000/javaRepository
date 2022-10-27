package com.example.groupproj_blackjack;

import java.util.*;

public class Dealer {

    // creating new instance of Hand, Deck and BlackJackCheck

    Hand hand = new Hand();
    Deck deck = new Deck();
    BlackJackCheck blackJack = new BlackJackCheck();
    Scanner scanner = new Scanner(System.in);

    // initializing variables to be used in dealGame method

    int playerVal = 0;
    int dealerVal = 0;
    int playerMove = 1;
    int deckIndex = 4;
    int playerIndex = 2;
    int dealerIndex = 2;
    int totAce = 0;
    int totAceDealer = 0;

    Card card;
    Card cardOne;
    Card cardTwo;

    boolean player = true;
    boolean playerBust = false;
    boolean dealerBust = false;
    boolean playerIsBlackJack = false;
    boolean dealerIsBlackJack = false;


    // shuffle method, creates a 52 card deck of all 4 suits and "shuffles" it

    public Card[] shuffleCards() {

        // creating a linked hash set to index the unshuffled deck
        Set<Integer> shuffleSet = new LinkedHashSet<Integer>();
        Random randInt = new Random();

        // method call to create the unshuffled deck
        Card[] noShuffle = deck.createDeck();


        // looping until the set is randomly filled with numbers from 0 - 51
        while (shuffleSet.size() < 52) {
            int rand = randInt.nextInt((noShuffle.length));
            shuffleSet.add(rand);

        }

        // creating spce for array of type int and type Card[]
        int[] shufflePointer = new int[shuffleSet.size()];
        Card[] shuffleDeck = new Card[52];

        int i = 0;

        // looping through the set and adding the random numbers to the pointer array
        for (int point : shuffleSet) {
            shufflePointer[i++] = point;
        }

        // looping the length of the deck and adding the random cards to the shuffled deck
        for (i = 0; i < noShuffle.length; i++) {
            shuffleDeck[i] = noShuffle[shufflePointer[i]];
        }

        // returning the shuffled deck
        return shuffleDeck;
    }

    // method for dealing the game
    public int dealGame(Card[] shuffleDeck, int bet) {


        // method calls to create the dealers hand and adding it to his total value
        ArrayList<Card> dealerHand = hand.createDealerHand(shuffleDeck);
        cardOne = dealerHand.get(0);
        dealerVal += cardOne.value;

        if (Objects.equals(cardOne.name, "Ace")) {
            totAceDealer++;
        }

        cardTwo = dealerHand.get(1);
        dealerVal += cardTwo.value;

        if (Objects.equals(cardTwo.name, "Ace")) {
            totAceDealer++;
        }

        // checking to see if the dealer has blackjack (a face card and an ace)
        dealerIsBlackJack = blackJack.isBlackJack(cardOne, cardTwo);

        System.out.println("The dealer is showing ");
        System.out.println(cardOne);


        // method calls to create the players hand and adding it to his total value
        ArrayList<Card> playerHand = hand.createPlayerHand(shuffleDeck);
        cardOne = playerHand.get(0);
        playerVal += cardOne.value;

        if (Objects.equals(cardOne.name, "Ace")) {
            totAce++;
        }

        cardTwo = playerHand.get(1);
        playerVal += cardTwo.value;

        if (Objects.equals(cardTwo.name, "Ace")) {
            totAce++;
        }

        // checking to see if the player has blackjack (a face card and an ace)
        playerIsBlackJack = blackJack.isBlackJack(cardOne, cardTwo);

        // while loops that only runs if neither the dealer or player have blackjack
        if (!playerIsBlackJack && !dealerIsBlackJack) {
            while (player && playerVal < 21) {

                System.out.println("Your cards are: ");
                System.out.println(playerHand);
                System.out.println(playerVal);

                System.out.println("Press 1 to hit, 2 to stay");
                playerMove = scanner.nextInt();


                // if player chooses to hit --
                // method call to add a card to players hand, add the value of the card to their total value
                if (playerMove == 1) {
                    playerHand = hand.addPlayerHand(deckIndex, shuffleDeck, playerHand);
                    card = playerHand.get(playerIndex++);
                    playerVal += card.value;

                    // checks to see if the card was an ace
                    if (Objects.equals(card.name, "Ace")) {
                        totAce++;
                    }
                    // incrementing the index of the deck
                    deckIndex++;
                }

                // if player chooses to stay, exits loop
                else {
                    player = false;
                }

                // checks to see if the player is over 21 and has an ace, if they do subtract ten from their total
                // ace goes from 11 --> 1 in value
                if (playerVal > 21 && totAce >= 1) {
                    playerVal -= 10;
                    totAce--;
                }

                // lets the player know if they busted, exits loop
                if (playerVal > 21) {
                    System.out.println("\nYour cards are: ");
                    System.out.println(playerHand);
                    System.out.println("You busted");
                    player = false;
                    playerBust = true;
                }

            }

            // dealers turn, always hits if under 17, loop does not execute if player busts (goes over 21)
            while (dealerVal < 17 && !playerBust) {

                System.out.println("\nThe dealer's hand");
                System.out.println(dealerHand);

                // adding to the dealers hand
                dealerHand = hand.addDealerHand(deckIndex, shuffleDeck, dealerHand);
                card = dealerHand.get(dealerIndex++);

                // checks to see if the card was an ace
                if (Objects.equals(card.name, "Ace")) {
                    totAceDealer++;
                }
                // adds the value of the card to the total value for the dealer
                dealerVal += card.value;

                // incrementing the index of the deck
                deckIndex++;

                // checks to see if the dealer is over 21 and has an ace, if they do subtract ten from their total
                // ace goes from 11 --> 1 in value
                if (dealerVal > 21 && totAceDealer >= 1) {
                    dealerVal -= 10;
                    totAceDealer--;
                }

                // checks to see if the dealer busted
                if (dealerVal > 21) {
                    System.out.println("\nThe dealer busted");
                    System.out.println(dealerHand);
                    dealerBust = true;
                    break;
                }
            }
        }
        System.out.println("\n\nThe player's hand");
        System.out.println(playerHand);
        System.out.println(playerVal);

        System.out.println("\nThe dealer's hand");
        System.out.println(dealerHand);
        System.out.println(dealerVal);


        // checks to see if both either got blackjack, busted or are equal in value -- results in no winner
        if (playerIsBlackJack && dealerIsBlackJack || dealerBust && playerBust || dealerVal == playerVal) {
            System.out.println("No winner");
        }

        // checks to see if the player has blackjack
        // player gains bet + bet * 1.5
        else if (playerIsBlackJack) {
            System.out.println("The player has blackjack");
            bet += bet * (1.5);
        }
        // checks to see if the dealer has blackjack
        // player loses intial bet
        else if (dealerIsBlackJack) {
            System.out.println("The dealer has blackjack");
            bet -= bet;
        }

        // checks to see if the player busted, if false checks for dealer bust or if player value is more
        // player gains double intial bet
        if (!playerBust) {
            if (dealerBust || playerVal > dealerVal) {
                System.out.println("The player won");
                bet += bet;
            }
        }

        // checks to see if the dealer busted, if false checks for player bust or if dealer value is more
        // player loses inital bet
        if (!dealerBust) {
            if (playerBust || dealerVal > playerVal) {
                System.out.println("The dealer won");
                bet -= bet;
            }
        }
        //returns bet
        return bet;
    }
}