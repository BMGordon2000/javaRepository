package com.example.groupproj_blackjack;

// needed for scene builder
import javafx.application.Application; // opens up scene builder
import javafx.fxml.FXMLLoader; // gets file
import javafx.scene.Parent; // makes the parent
import javafx.scene.Scene; // sets the stage
import javafx.stage.Stage; // creates the scene
// import java.io.IOException;

public class BlackJack extends Application {
    /**
     * PROGRAM PURPOSE: Main app class that loads and displays the Black Jack GUI
     * NAME: Nixon, Ben, Matt, Tristan, Rosa
     * DATE: November 8th, 2021 - November 15th, 2021
     * SECTION: CSC-331-002
     * RESOURCES: CSC Textbook and Professor Shauna White
     */

    // creates the GUI
    @Override
    public void start(Stage stage) throws Exception { // attaches the GUI to Scene and places it on stage, receives the
        // argument
        Parent root = FXMLLoader.load(getClass().getResource("BlackJack.fxml")); // uses class FXMLLoaders
        // static methods load to create the GUI scene graph
        Scene scene = new Scene(root); // attach scene graph to scene
        stage.setTitle("Black Jack"); // displayed in windows title bar
        stage.setScene(scene); // attach scene to stage
        stage.show(); // display the stage
    }

    // create a BlackJack object and call its start method
    public static void main(String[] args) { // main method that starts the program
        launch(args); // launches scene builder
    } // closes main method
} // closes class BlackJack
