package com.casino.josh.casino_java.Models;

import com.casino.josh.casino_java.Adapters.TurnLogAdapter;
import com.casino.josh.casino_java.Models.BasePlayerModel;
import com.casino.josh.casino_java.Models.TableModel;
import com.casino.josh.casino_java.activites.GameActivity;

import java.util.Vector;

/**
 * Created by josh on 11/7/18.
 */

public class HumanPlayerModel extends BasePlayerModel {

    /**
     * Constructor for HumanPlayer
     */
    public HumanPlayerModel(){}

    /**
     * Implements abstract class within base class for move execution.
     * @param table TableModel
     * @param option TurnOption
     * @return boolean
     */
    @Override
    public boolean makeMove(TableModel table, TurnOptions option) {
        if(option == TurnOptions.TRIAL){
            if(table.canTrailCard(GameActivity.mChosenCard)){
                getHand().remove(GameActivity.mChosenCard);
                table.getLooseCards().add(GameActivity.mChosenCard);
                TurnLogModel.AddToLog("Human trailed the card: " + GameActivity.mChosenCard.toString());
                return true;
            }else{
                return false;
            }
        }else if (option == TurnOptions.CAPTURE){
            Vector<CardModel> capturedLooseCards = table.captureLooseCardsOnTable(GameActivity.mChosenCard);
            if(capturedLooseCards.size() > 0){
                table.getLooseCards().removeAll(capturedLooseCards);
                capturedLooseCards.add(GameActivity.mChosenCard);
                getPile().addAll(capturedLooseCards);
                getHand().remove(GameActivity.mChosenCard);

                String turnLog = "Human captured with the card: " + GameActivity.mChosenCard.toString()
                        + "Capturing cards: ";
                for(CardModel card : capturedLooseCards){
                    turnLog += " " + card.toString();
                }

                TurnLogModel.AddToLog(turnLog);

                return true;
            }
        } else if(option == TurnOptions.BUILD){
            if(table.createBuild(GameActivity.mLooseCards, GameActivity.mChosenCard, GameActivity.mCaptureCard)){
                getHand().remove(GameActivity.mChosenCard);

                String turnLog = "Human created a build with the card: " + GameActivity.mChosenCard.toString();
                TurnLogModel.AddToLog(turnLog);

                return true;
            }
        }

        return false;
    }
}
