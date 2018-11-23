package com.casino.josh.casino_java.Models;

import com.casino.josh.casino_java.Models.BasePlayerModel;
import com.casino.josh.casino_java.Models.TableModel;

import java.util.Vector;

/**
 * Created by josh on 11/7/18.
 */

public class ComputerPlayerModel extends BasePlayerModel {

    /**
     * Default constructor for the Computer PLayer Class.
     */
    public ComputerPlayerModel(){
        mName = "Computer";
    }

    /**
     *
     * @param score
     * @param hand
     * @param pile
     */
    public ComputerPlayerModel(int score, Vector<CardModel> hand, Vector<CardModel> pile) {
        _points = score;
        _hand = hand;
        _pile = pile;
    }

    /**
     * Implementation of abstract class within BasePlayerModel.
     * @param table TableModel
     * @param option TurnOptions
     * @return boolean
     */
    @Override
    public boolean makeMove(TableModel table, TurnOptions option) {
        table.getLooseCards().add(getHand().get(0));

        TurnLogModel.AddToLog("Computer trailed the card: " + getHand().get(0).toString());

        getHand().remove(0);

        return true;
    }

    /**
     * Get the name of the Computer player (Computer).
     * @return String
     */
    public String getName(){return mName;}
}
