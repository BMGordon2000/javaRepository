import java.util.Scanner;

public class Game {


    public static void main(String[] args) {


    int playerBank = 1000;
    boolean game = true;

    Scanner scanner = new Scanner(System.in);

        // while loop that allows the player to continually play the game
        while (game) {

            // setting players intial bet to 0, user input to ask how much they would like to bet
        int bet = 0;
        System.out.println("Your current bank is: " + playerBank);

        // while loop that checks the validity of the user input, subtracts bet from players bank
        while (bet < 20 || bet > playerBank) {
            System.out.println("New game has started, how much would you like to bet? Minimum bet is 20 ");
            bet = scanner.nextInt();
            if (bet < 20){
                System.out.println("Please try again. Bet must be 20 or more and less than your bank. ");
            }
            else{
                playerBank -= bet;
            }
        }

        // creating new instance of Deck, shuffling deck of cards, calling dealGame method to run game
        Dealer dealer = new Dealer();
        Card[] shuffleDeck = dealer.shuffleCards();
        int betReturn = dealer.dealGame(shuffleDeck, bet);

        // adding the bet money returned from the game
        playerBank += betReturn;

        // asking the player if they would like to play again
        System.out.println("\nWould you like to play again? Press 1 to play again. ");
        int playGame = scanner.nextInt();

        // Ends game if player does not wish to continue
        if (playGame != 1){
            game = false;
        }

        // makes sure player has enough for the minimum bet requirement
        if (playerBank < 20){
            System.out.println("\nNot enough money in bank for minimum bet requirement. com.example.tipcalculator.Game ending");
            game = false;
        }

    }


    }

}