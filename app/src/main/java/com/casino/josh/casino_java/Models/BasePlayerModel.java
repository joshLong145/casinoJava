package com.casino.josh.casino_java.Models;

import java.util.Vector;

/**
 * Created by josh on 11/7/18.
 */

public abstract class BasePlayerModel {
    private Vector<CardModel> _hand;
    private Vector<CardModel> _pile;
    private int _points;
    private boolean _isTurn;

    /**
     * Enum for verb actions that can be performed.
     */
    public enum TurnOptions{
        BUILD, CAPTURE, TRIAL, AI;
    }

    /**
     * Default constructor for Player.
     */
    public BasePlayerModel(){
        _hand = new Vector<>();
        _pile = new Vector<>();
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
     * Set a flag for the current player turn.
     * @param value
     */
    public void setTurn(final boolean value){ _isTurn = value; }

    /**
     * Serialize data within the class.
     * @return
     */
    public String toString(){ return ""; }

    /**
     * Trails a card for the player.
     * @param table
     * @return boolean
     */
    protected  boolean trailCardAction(final TableModel table){return false; }

    /**
     * Capture cards on the table for the player
     * @param table TableModel
     * @param cardValue integer
     * @return boolean
     */
    protected boolean captureCardOnTable(final TableModel table, final int cardValue){ return false; }

    //std::vector<std::vector<std::string>> setCapture(const std::shared_ptr<Table> &table, const int cardValue);

    //protected boolean createSingleBuild();

    //bool createMultiBuild(const std::shared_ptr<Table> &table, const unsigned int cardIndex, const int buildIndex, std::string captureCardIndex, std::vector<int> cardIndexes);

    //bool captureBuild(const int cardValue, const std::shared_ptr<Table> &table);

}