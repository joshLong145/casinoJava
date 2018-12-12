package com.casino.josh.casino_java.Models;

import android.widget.PopupWindow;

import java.util.Vector;

/**
 * Created by josh on 11/7/18.
 */

public abstract class BasePlayerModel {
    protected Vector<CardModel> _hand;
    protected Vector<CardModel> _pile;
    protected int _points;
    protected boolean _isTurn;
    protected String mName;

    /**
     * Enum for verb actions that can be performed.
     */
    public enum TurnOptions{
        BUILD, CAPTURE, TRIAL, AI, MULTIBUILD, EXTEND;
    }

    /**
     * Default constructor for Player.
     */
    public BasePlayerModel(){
        _hand = new Vector<>();
        _pile = new Vector<>();
    }

    /**
     * Constructor for deserializing data into BasePlayer object.
     * @param score
     * @param hand
     * @param pile
     */
    public BasePlayerModel(int score, Vector<CardModel> hand, Vector<CardModel> pile){
        _points = score;
        _hand = hand;
        _pile = pile;
    }

    /**
     * Returns collection that contains data related to players hand.
     * @return
     */
    public final Vector<CardModel> getHand(){ return _hand; }

    /**
     * Sets data that will correspond the the player's hand.
     * @param hand
     */
    public void setHand(final Vector<CardModel> hand){ _hand = hand; }

    /**
     * Returns collection that corresponds to the player's pile.
     * @return
     */
    public final Vector<CardModel> getPile(){return _pile; }

    /**
     * Add a card object to the player's pile.
     * @param card
     */
    public void addToPile(final CardModel card){ _pile.add(card); }

    /**
     * Returns the players current point quantity.
     * @return
     */
    public final int getPoints(){return _points; }

    /**
     * Sets the point quantity for the player.
     * @param points
     */
    public void setPoints(final int points){ _points = points; }

    /**
     * Abstract function to be defined within children classes for move execution.
     * @param table
     * @return
     */
    public abstract boolean makeMove(TableModel table, TurnOptions option);

    /**
     * Return a container containing combinations of cards from the table.
     * @param table
     * @param cardValue
     * @return Vector
     */
    protected Vector<Vector<CardModel>> setCaptureCombinations(final TableModel table, final int cardValue){
        return table.setCapture(cardValue);
    }

    /**
     * Capture builds if the capture value of the selected card value matches.
     * @param table
     * @param selectedCard
     * @return Vector<CardModel>
     */
    protected Vector<CardModel> buildCapture(final TableModel table, final CardModel selectedCard){
        return table.captureBuilds(selectedCard);
    }

    /**
     * Get the name of the current player.
     * @return String
     */
    public abstract String getName();

    /**
     * Serialize data within the class.
     * @return
     */
    public String toString(){
        StringBuilder playerData = new StringBuilder();
        playerData.append(mName + ":");
        playerData.append(System.getProperty("line.separator"));
        playerData.append("Score: " +Integer.toString(_points));
        playerData.append(System.getProperty("line.separator"));
        playerData.append("Hand:");
        for(CardModel card : _hand){
            playerData.append(" " + card.toStringSave());
        }
        playerData.append(System.getProperty("line.separator"));
        playerData.append("Pile:");
        for(CardModel card : _pile){
            playerData.append(" " + card.toStringSave());
        }
        playerData.append(System.getProperty("line.separator"));

        return playerData.toString();
    }
}