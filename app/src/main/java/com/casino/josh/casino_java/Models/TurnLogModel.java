package com.casino.josh.casino_java.Models;

import java.util.Vector;

/**
 * Created by josh on 11/10/18.
 */

public class TurnLogModel {

    // private member variable.
    private static Vector<String> mLog = new Vector<>();

    /**
     * returns a Vector of strings containing turn information.
     * @return
     */
    public static final Vector<String> getLog(){return mLog; }


    /**
     * Add data to the Log of moves made by both players.
     * @param turnData String
     */
    public static void AddToLog(final String turnData){
        mLog.add(turnData);
    }

    /**
     * Takes the card from the hand that will be trailed and serializes it's data for the turn log.
     * @param card
     * @param playerName
     */
    public static void addTrailMoveToLog(final CardModel card, final String playerName){
        StringBuilder turnData = new StringBuilder(playerName + "Trail the card: ");
        turnData.append(card.toStringSave());

        mLog.add(turnData.toString());
    }

    /**
     * Takes loose cards / sets captured by player, serializes data and adds it to the log.
     * @param cards
     * @param handCard
     */
    public static void addCaptureMoveToLog(final Vector<CardModel> cards, final CardModel handCard,
                                           final String playerName){
        StringBuilder turnData = new StringBuilder(playerName + "Captured cards:");
        for(CardModel card : cards){
            turnData.append(" ")
                    .append(card.toStringSave());
        }

        turnData.append(" With the card: ")
                .append(handCard.toStringSave());

        mLog.add(turnData.toString());
    }

    /**
     *
     * @param build
     * @param captureCard
     * @param selectedCard
     * @param playerName
     */
    public static void addBuildMoveToLog(final Vector<CardModel> build, final CardModel captureCard,
                                         final CardModel selectedCard, final String playerName){

        StringBuilder turnData = new StringBuilder(playerName + "create a build with the cards cards:");
        for(CardModel card : build){
            turnData.append(" ")
                    .append(card.toStringSave());
        }

        turnData.append(" ")
                .append(selectedCard.toStringSave())
                .append(" Capturing with the card: ")
                .append(captureCard.toStringSave());

        mLog.add(turnData.toString());
    }

}
