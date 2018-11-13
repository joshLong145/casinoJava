package com.casino.josh.casino_java.Models;

import com.casino.josh.casino_java.Models.BasePlayerModel;
import com.casino.josh.casino_java.Models.TableModel;

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
