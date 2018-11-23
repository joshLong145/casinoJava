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
    public HumanPlayerModel(){
        mName = "Human";
    }

    /**
     * Constructor to create object from deserialized data.
     * @param score
     * @param hand
     * @param pile
     */
    public HumanPlayerModel(int score, Vector<CardModel> hand, Vector<CardModel> pile){
        _points = score;
        _hand = hand;
        _pile = pile;
        mName = "Human";
    }

    /**
     * Implements abstract class within base class for move execution.
     * @param table TableModel
     * @param option TurnOption
     * @return boolean
     */
    @Override
    public boolean makeMove(TableModel table, TurnOptions option) {
        if(option == TurnOptions.TRIAL){
            if(!GameActivity.mTournament.getCurrentRound().getTable().isCaptureCard(getHand(), GameActivity.mChosenCard, mName)) {
                if (table.canTrailCard(GameActivity.mChosenCard, mName)) {
                    getHand().remove(GameActivity.mChosenCard);
                    table.getLooseCards().add(GameActivity.mChosenCard);
                    TurnLogModel.AddToLog("Human trailed the card: " + GameActivity.mChosenCard.toString());
                    return true;
                } else {
                    return false;
                }
            }
        }else if (option == TurnOptions.CAPTURE){
            Vector<CardModel> capturedLooseCards = table.captureLooseCards(GameActivity.mChosenCard, GameActivity.mLooseCards);
            Vector<CardModel> capturedBuildCards = table.captureBuilds(GameActivity.mBuilds, GameActivity.mChosenCard);

            if(capturedLooseCards.size() > 0){
                capturedLooseCards.add(GameActivity.mChosenCard);
                getPile().addAll(capturedLooseCards);
                getHand().remove(GameActivity.mChosenCard);

                String turnLog = "Human captured with the card: " + GameActivity.mChosenCard.toString()
                        + "Capturing cards: ";
                for(CardModel card : capturedLooseCards){
                    turnLog += " " + card.toString();
                }

                TurnLogModel.AddToLog(turnLog);

                if(capturedBuildCards.size() > 0){
                    getPile().addAll(capturedBuildCards);
                }

                return true;
            }
            if(table.canTrailCard(GameActivity.mChosenCard, mName)) {
                if (capturedBuildCards.size() > 0) {
                    getPile().addAll(capturedBuildCards);
                    getPile().add(GameActivity.mChosenCard);
                    getHand().remove(GameActivity.mChosenCard);
                    return true;
                }
            }
        } else if(option == TurnOptions.BUILD){
            if(table.createBuild(GameActivity.mLooseCards, GameActivity.mChosenCard, getHand(), mName)){
                getHand().remove(GameActivity.mChosenCard);

                String turnLog = "Human created a build with the card: " + GameActivity.mChosenCard.toString();
                TurnLogModel.AddToLog(turnLog);

                return true;
            }
        } else if(option == TurnOptions.MULTIBUILD){
            if(GameActivity.mBuilds.size() <= 0)
                return false;

            if(GameActivity.mBuilds.size() <= 0)
                return false;

            if(table.createMultiBuild(GameActivity.mBuilds.get(0),GameActivity.mLooseCards, GameActivity.mChosenCard, getHand())){
                getHand().remove(GameActivity.mChosenCard);
                return true;
            }
        }

        return false;
    }

    /**
     * Get the name of the player (Human).
     * @return String
     */
    public String getName(){return mName;}
}
