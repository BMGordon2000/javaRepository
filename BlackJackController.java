package com.example.groupproj_blackjack;

// needed for scene builder
import java.math.BigDecimal; // performs precise monetary calculations
import java.math.RoundingMode; // specify how BigDecimal values are rounded during calculations
import java.text.Bidi;
import java.text.NumberFormat; // provides numeric formatting capabilities
import javafx.beans.value.ChangeListener; // responds when user moves sliders thumb? may not be needed
import javafx.beans.value.ObservableValue; // value that generates an event when it changes
import javafx.event.ActionEvent; // indicates which button the user clicked
import javafx.fxml.FXML; // mark instance variables that should refer to javaFX
import javafx.scene.control.Label; // contains javafx control classes
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField; // contains javafx control classes
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

import javafx.scene.Group;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class BlackJackController {
    /**
     * PROGRAM PURPOSE: Controller that handles needed changes in the BlackJack GUI
     * NAME: Nixon, Ben, Matt, Tristan, Rosa
     * DATE: November 8th, 2021 - November 15th, 2021
     * SECTION: CSC-331-002
     * RESOURCES: CSC Textbook and Professor Shauna White
     */

    Hand hand = new Hand();
    Deck deck = new Deck();
    BlackJackCheck blackJack = new BlackJackCheck();
    int playerVal = 0;
    int dealerVal = 0;
    int playerMove = 1;
    int deckIndex = 4;
    int playerIndex = 2;
    int dealerIndex = 2;
    int totAce = 0;
    int totAceDealer = 0;
    int hitCount = 0;
    int dealerHitCount = 0;
    int playCount = 1;

    Card card;
    Card cardOne;
    Card cardTwo;
    Card cardHidden;

    Card[] shuffleDeck;
    ArrayList<Card> playerHand;
    ArrayList<Card> dealerHand;

    boolean player = true;
    boolean playerBust = false;
    boolean dealerBust = false;
    boolean playerIsBlackJack = false;
    boolean dealerIsBlackJack = false;
    private static final NumberFormat currency = NumberFormat.getCurrencyInstance(); // formatters for currency
    private BigDecimal total = new BigDecimal(1000); // makes the total amount when game starts $1,000
    private BigDecimal initialBet = new BigDecimal(0);

    // GUI controls defined in FXML and used by the controllers code
    @FXML // indicates the variable name can be used in the FXML file that describes the apps GUI
    private TextField betTextField; // user putting in how much they will bet

    @FXML // indicates the variable name can be used in the FXML file that describes the apps GUI
    private ImageView firstDealerImage; // first card for dealer (will show something)

    @FXML // indicates the variable name can be used in the FXML file that describes the apps GUI
    private ImageView secondDealerImage; // second card for dealer, concealed and then revealed (will show something)

    @FXML // indicates the variable name can be used in the FXML file that describes the apps GUI
    private ImageView thirdDealerImage; // possible third card for dealer (will show something)

    @FXML // indicates the variable name can be used in the FXML file that describes the apps GUI
    private ImageView firstPlayerImage; // possible third card for player, if decide to hit (will show something)

    @FXML // indicates the variable name can be used in the FXML file that describes the apps GUI
    private ImageView secondPlayerImage; // second card for player (will show something)

    @FXML // indicates the variable name can be used in the FXML file that describes the apps GUI
    private ImageView thirdPlayerImage; // first card for player (will show something)

    @FXML // indicates the variable name can be used in the FXML file that describes the apps GUI
    private TextField totalTextField; // shows total amount player has to bet/keep (will show something)

    @FXML
    private ImageView fourthPlayerImage;

    @FXML
    private ImageView fourthDealerImage;

    @FXML
    private TextArea gameinfo; // displays a message when someone wins

    // does an action
    @FXML // indicates the variable name can be used in the FXML file that describes the apps GUI
    private void playButtonPressed(ActionEvent event) {
        // put in code that allows the player to play/confirm
        try{
            BigDecimal bet = new BigDecimal(betTextField.getText());
            BigDecimal total = new BigDecimal(totalTextField.getText());
            if (bet.intValue() > total.intValue()){ // stops the user from betting more than they have
                throw new NumberFormatException();
            }
            Dealer dealer = new Dealer();
            shuffleDeck = dealer.shuffleCards();
            if (playCount >= 2){ //Resets the board and shuffles the deck
                playerVal = 0;
                dealerVal = 0;
                Image blankSlot = new Image(getClass().getResourceAsStream("blank_slot.PNG"));
                firstDealerImage.setImage(blankSlot);
                secondDealerImage.setImage(blankSlot);
                thirdDealerImage.setImage(blankSlot);
                fourthDealerImage.setImage(blankSlot);
                firstPlayerImage.setImage(blankSlot);
                secondPlayerImage.setImage(blankSlot);
                thirdPlayerImage.setImage(blankSlot);
                fourthPlayerImage.setImage(blankSlot);
                Dealer dealer1 = new Dealer();
                shuffleDeck = dealer1.shuffleCards();
                dealerHand = hand.createDealerHand(shuffleDeck);
            }
            playCount++;
            dealerHand = hand.createDealerHand(shuffleDeck);
            cardOne = dealerHand.get(0);
            dealerVal += cardOne.value;

            if (Objects.equals(cardOne.name, "Ace")) {
                totAceDealer++;
            }

            cardHidden = dealerHand.get(1);
            dealerVal += cardHidden.value;

            if (Objects.equals(cardHidden.name, "Ace")) {
                totAceDealer++;
            }

            dealerIsBlackJack = blackJack.isBlackJack(cardOne, cardHidden);

            Image dealerImage1 = getImage(cardOne); // loads the initial dealer images
            firstDealerImage.setImage(dealerImage1);
            Image blankCard = new Image(getClass().getResourceAsStream("card_back_red.png"));
            secondDealerImage.setImage(blankCard);

            playerHand = hand.createPlayerHand(shuffleDeck);
            cardOne = playerHand.get(0);
            playerVal += cardOne.value;
            Image playerImage1 = getImage(cardOne); // loads the first player card image
            firstPlayerImage.setImage(playerImage1);

            if (Objects.equals(cardOne.name, "Ace")) {
                totAce++;
            }

            cardTwo = playerHand.get(1);
            playerVal += cardTwo.value;
            Image playerImage2 = getImage(cardTwo); // loads the second player card image
            secondPlayerImage.setImage(playerImage2);

            if (Objects.equals(cardTwo.name, "Ace")) {
                totAce++;
            }
            if (playerVal > 21 && totAce >= 1) { //Sets the value of the ace from 11 to 1 in case of a bust
                playerVal -= 10;
                totAce--;
            }
            if (dealerVal > 21 && totAceDealer >= 1) {
                dealerVal -= 10;
                totAceDealer--;
            }

            playerIsBlackJack = blackJack.isBlackJack(cardOne, cardTwo);
        }
        catch(NumberFormatException e){ // makes sure the user enters a number between 0 and total
            betTextField.setText("Invalid Number");
            betTextField.selectAll();
            betTextField.requestFocus();
        }
    }

    @FXML
    private void hitButtonPressed(ActionEvent event) {
        // put in code that when pressed, another number is allowed to show basically picking another card
        hitCount++;
        if (hitCount <= 2){
            playerHand = hand.addPlayerHand(deckIndex, shuffleDeck, playerHand);
            card = playerHand.get(playerIndex++);
            playerVal += card.value;
            Image playerImage3 = getImage(card);
            if(hitCount == 1){
                thirdPlayerImage.setImage(playerImage3);
            }
            else{
                fourthPlayerImage.setImage(playerImage3);
            }

            if (Objects.equals(card.name, "Ace")) {
                totAce++;
            }
            deckIndex++;
        }

        if (playerVal > 21 && totAce >= 1) {
            playerVal -= 10;
            totAce--;
        }
        if (playerVal > 21 && totAce >= 1) {
            playerVal -= 10;
            totAce--;
        }
        if (playerVal > 21) { // checks for player bust
            gameinfo.setText("You have busted, Dealer Wins");
            BigDecimal bet = new BigDecimal(betTextField.getText());
            BigDecimal total = new BigDecimal(totalTextField.getText());
            BigDecimal newTotal = total.subtract(bet);
            totalTextField.setText(String.valueOf(newTotal));
            player = false;
            playerBust = true;
        }
        if (playerVal == 21) { // checks for player blackjack
            playerIsBlackJack = true;
        }

    }

    @FXML
    void standingButtonPressed(ActionEvent event) {
        // put in code that when pressed the current two numbers given stay and the dealer is now allowed to go
        player = false;
        while (dealerVal < 17 && !playerBust && dealerHitCount <= 2) { // Allows the dealer to hit until it reaches 17 or higher
            dealerHitCount++;

            Image dealerCardHidden = getImage(cardHidden);
            secondDealerImage.setImage(dealerCardHidden);

            dealerHand = hand.addDealerHand(deckIndex, shuffleDeck, dealerHand);
            card = dealerHand.get(dealerIndex++);
            Image dealerImage3 = getImage(card);
            if(dealerHitCount == 1){
                thirdDealerImage.setImage(dealerImage3);
            }
            else{
                fourthDealerImage.setImage(dealerImage3);
            }

            if (Objects.equals(card.name, "Ace")) {
                totAceDealer++;
            }
            dealerVal += card.value;
            deckIndex++;


            if (dealerVal > 21 && totAceDealer >= 1) {
                dealerVal -= 10;
                totAceDealer--;
            }
            if (dealerVal > 21 && totAceDealer >= 1) {
                dealerVal -= 10;
                totAceDealer--;
            }

            if (dealerVal > 21) {
                gameinfo.setText("Dealer has busted, you win!");
                dealerBust = true;
                break;
            }
        }

        if (dealerVal == 21) {
            dealerIsBlackJack = true;
        }
        if (playerVal == 21) {
            playerIsBlackJack = true;
        }
        if (dealerVal > 21){
            dealerBust = true;
        }
        if (playerVal > 21){
            playerBust = true;
        }
        Image imageA = getImage(cardHidden); // reveals the dealer's hidden card
        secondDealerImage.setImage(imageA);

        if (playerIsBlackJack && dealerIsBlackJack || dealerBust && playerBust || dealerVal == playerVal) {
            gameinfo.setText("No winner"); // checks in case of a tie
        }
        else if (playerIsBlackJack) { // Win message when player gets 21
            gameinfo.setText("The player has blackjack!");
            BigDecimal bet = new BigDecimal(betTextField.getText());
            BigDecimal total = new BigDecimal(totalTextField.getText());
            BigDecimal newTotal = total.add(bet);
            totalTextField.setText(String.valueOf(newTotal));
        }
        else if (dealerIsBlackJack) { // Win message when dealer gets 21
            gameinfo.setText("The dealer has blackjack!");
            BigDecimal bet = new BigDecimal(betTextField.getText());
            BigDecimal total = new BigDecimal(totalTextField.getText());
            BigDecimal newTotal = total.subtract(bet);
            totalTextField.setText(String.valueOf(newTotal));
        }
        else if (!playerBust) {
            if (dealerBust || playerVal > dealerVal) { // Win message when the player has more points
                gameinfo.setText("The player has won!");
                if (dealerBust){
                    gameinfo.setText("Dealer busted, the player has won!");
                }
                BigDecimal bet = new BigDecimal(betTextField.getText());
                BigDecimal total = new BigDecimal(totalTextField.getText());
                BigDecimal newTotal = total.add(bet);
                totalTextField.setText(String.valueOf(newTotal));
            }
            else if (playerVal < dealerVal){ // Win message when the dealer has more points
                gameinfo.setText("The dealer has won");
                BigDecimal bet = new BigDecimal(betTextField.getText());
                BigDecimal total = new BigDecimal(totalTextField.getText());
                BigDecimal newTotal = total.subtract(bet);
                totalTextField.setText(String.valueOf(newTotal));
            }
        }
        else if (!dealerBust) {
            if (playerBust || dealerVal > playerVal) {
                gameinfo.setText("The dealer has won");
                BigDecimal bet = new BigDecimal(betTextField.getText());
                BigDecimal total = new BigDecimal(totalTextField.getText());
                BigDecimal newTotal = total.subtract(bet);
                totalTextField.setText(String.valueOf(newTotal));
            }
        }
    }

    public void initialize() { // binds the total and bet values to their respective text fields
        totalTextField.setText(String.valueOf(total)); // when open window, put value in box, will be used whenever
        // value changes
        betTextField.setText(String.valueOf(initialBet));
        // 0-4 rounds down, 5-9 rounds up
        currency.setRoundingMode(RoundingMode.HALF_UP);
        // start game
    }

    public Image getImage(Card currentCard){  // Takes a card as a parameter and returns its PNG image
        Suit suit = currentCard.getSuit();
        String name = currentCard.getName();
        if (suit == Suit.DIAMOND && name.equals("Ace")){
            return new Image(getClass().getResourceAsStream("ace_of_diamonds.png"));
        }
        if (suit == Suit.DIAMOND && name.equals("King")){
            return new Image(getClass().getResourceAsStream("king_of_diamonds2.png"));
        }
        if (suit == Suit.DIAMOND && name.equals("Queen")){
            return new Image(getClass().getResourceAsStream("queen_of_diamonds2.png"));
        }
        if (suit == Suit.DIAMOND && name.equals("Jack")){
            return new Image(getClass().getResourceAsStream("jack_of_diamonds2.png"));
        }
        if (suit == Suit.DIAMOND && name.equals("Ten")){
            return new Image(getClass().getResourceAsStream("10_of_diamonds.png"));
        }
        if (suit == Suit.DIAMOND && name.equals("Nine")){
            return new Image(getClass().getResourceAsStream("9_of_diamonds.png"));
        }
        if (suit == Suit.DIAMOND && name.equals("Eight")){
            return new Image(getClass().getResourceAsStream("8_of_diamonds.png"));
        }
        if (suit == Suit.DIAMOND && name.equals("Seven")){
            return new Image(getClass().getResourceAsStream("7_of_diamonds.png"));
        }
        if (suit == Suit.DIAMOND && name.equals("Six")){
            return new Image(getClass().getResourceAsStream("6_of_diamonds.png"));
        }
        if (suit == Suit.DIAMOND && name.equals("Five")){
            return new Image(getClass().getResourceAsStream("5_of_diamonds.png"));
        }
        if (suit == Suit.DIAMOND && name.equals("Four")){
            return new Image(getClass().getResourceAsStream("4_of_diamonds.png"));
        }
        if (suit == Suit.DIAMOND && name.equals("Three")){
            return new Image(getClass().getResourceAsStream("3_of_diamonds.png"));
        }
        if (suit == Suit.DIAMOND && name.equals("Two")){
            return new Image(getClass().getResourceAsStream("2_of_diamonds.png"));
        }
        if (suit == Suit.CLUB && name.equals("Ace")){
            return new Image(getClass().getResourceAsStream("ace_of_clubs.png"));
        }
        if (suit == Suit.CLUB && name.equals("King")){
            return new Image(getClass().getResourceAsStream("king_of_clubs2.png"));
        }
        if (suit == Suit.CLUB && name.equals("Queen")){
            return new Image(getClass().getResourceAsStream("queen_of_clubs2.png"));
        }
        if (suit == Suit.CLUB && name.equals("Jack")){
            return new Image(getClass().getResourceAsStream("jack_of_clubs2.png"));
        }
        if (suit == Suit.CLUB && name.equals("Ten")){
            return new Image(getClass().getResourceAsStream("10_of_clubs.png"));
        }
        if (suit == Suit.CLUB && name.equals("Nine")){
            return new Image(getClass().getResourceAsStream("9_of_clubs.png"));
        }
        if (suit == Suit.CLUB && name.equals("Eight")){
            return new Image(getClass().getResourceAsStream("8_of_clubs.png"));
        }
        if (suit == Suit.CLUB && name.equals("Seven")){
            return new Image(getClass().getResourceAsStream("7_of_clubs.png"));
        }
        if (suit == Suit.CLUB && name.equals("Six")){
            return new Image(getClass().getResourceAsStream("6_of_clubs.png"));
        }
        if (suit == Suit.CLUB && name.equals("Five")){
            return new Image(getClass().getResourceAsStream("5_of_clubs.png"));
        }
        if (suit == Suit.CLUB && name.equals("Four")){
            return new Image(getClass().getResourceAsStream("4_of_clubs.png"));
        }
        if (suit == Suit.CLUB && name.equals("Three")){
            return new Image(getClass().getResourceAsStream("3_of_clubs.png"));
        }
        if (suit == Suit.CLUB && name.equals("Two")){
            return new Image(getClass().getResourceAsStream("2_of_clubs.png"));
        }
        if (suit == Suit.HEART && name.equals("Ace")){
            return new Image(getClass().getResourceAsStream("ace_of_hearts.png"));
        }
        if (suit == Suit.HEART && name.equals("King")){
            return new Image(getClass().getResourceAsStream("king_of_hearts2.png"));
        }
        if (suit == Suit.HEART && name.equals("Queen")){
            return new Image(getClass().getResourceAsStream("queen_of_hearts2.png"));
        }
        if (suit == Suit.HEART && name.equals("Jack")){
            return new Image(getClass().getResourceAsStream("jack_of_hearts2.png"));
        }
        if (suit == Suit.HEART && name.equals("Ten")){
            return new Image(getClass().getResourceAsStream("10_of_hearts.png"));
        }
        if (suit == Suit.HEART && name.equals("Nine")){
            return new Image(getClass().getResourceAsStream("9_of_hearts.png"));
        }
        if (suit == Suit.HEART && name.equals("Eight")){
            return new Image(getClass().getResourceAsStream("8_of_hearts.png"));
        }
        if (suit == Suit.HEART && name.equals("Seven")){
            return new Image(getClass().getResourceAsStream("7_of_hearts.png"));
        }
        if (suit == Suit.HEART && name.equals("Six")){
            return new Image(getClass().getResourceAsStream("6_of_hearts.png"));
        }
        if (suit == Suit.HEART && name.equals("Five")){
            return new Image(getClass().getResourceAsStream("5_of_hearts.png"));
        }
        if (suit == Suit.HEART && name.equals("Four")){
            return new Image(getClass().getResourceAsStream("4_of_hearts.png"));
        }
        if (suit == Suit.HEART && name.equals("Three")){
            return new Image(getClass().getResourceAsStream("3_of_hearts.png"));
        }
        if (suit == Suit.HEART && name.equals("Two")){
            return new Image(getClass().getResourceAsStream("2_of_hearts.png"));
        }
        if (suit == Suit.SPADE && name.equals("Ace")){
            return new Image(getClass().getResourceAsStream("ace_of_spades.png"));
        }
        if (suit == Suit.SPADE && name.equals("King")){
            return new Image(getClass().getResourceAsStream("king_of_spades2.png"));
        }
        if (suit == Suit.SPADE && name.equals("Queen")){
            return new Image(getClass().getResourceAsStream("queen_of_spades2.png"));
        }
        if (suit == Suit.SPADE && name.equals("Jack")){
            return new Image(getClass().getResourceAsStream("jack_of_spades2.png"));
        }
        if (suit == Suit.SPADE && name.equals("Ten")){
            return new Image(getClass().getResourceAsStream("10_of_spades.png"));
        }
        if (suit == Suit.SPADE && name.equals("Nine")){
            return new Image(getClass().getResourceAsStream("9_of_spades.png"));
        }
        if (suit == Suit.SPADE && name.equals("Eight")){
            return new Image(getClass().getResourceAsStream("8_of_spades.png"));
        }
        if (suit == Suit.SPADE && name.equals("Seven")){
            return new Image(getClass().getResourceAsStream("7_of_spades.png"));
        }
        if (suit == Suit.SPADE && name.equals("Six")){
            return new Image(getClass().getResourceAsStream("6_of_spades.png"));
        }
        if (suit == Suit.SPADE && name.equals("Five")){
            return new Image(getClass().getResourceAsStream("5_of_spades.png"));
        }
        if (suit == Suit.SPADE && name.equals("Four")){
            return new Image(getClass().getResourceAsStream("4_of_spades.png"));
        }
        if (suit == Suit.SPADE && name.equals("Three")){
            return new Image(getClass().getResourceAsStream("3_of_spades.png"));
        }
        if (suit == Suit.SPADE && name.equals("Two")){
            return new Image(getClass().getResourceAsStream("2_of_spades.png"));
        }
        return new Image(getClass().getResourceAsStream("black_joker.png"));
    }
}
