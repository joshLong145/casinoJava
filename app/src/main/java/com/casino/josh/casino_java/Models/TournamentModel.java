package com.casino.josh.casino_java.Models;

import android.support.v4.util.Pair;

import com.casino.josh.casino_java.Fragments.MakeMoveButtonFragment;
import com.casino.josh.casino_java.Helpers.BooleanVariable;
import com.casino.josh.casino_java.activites.GameActivity;

import java.util.Vector;

/**
 * Created by josh on 11/5/18.
 */

public class TournamentModel {
    private boolean _tie;
    private int _currentRound = 0;
    private int _winner = 0;
    private final int _playerCount = 2;
    private int mRoundNumber = 0;
    private int mCurrentRound = 0;
    private BooleanVariable mRoundOver;
    private Vector<BasePlayerModel> mPlayers;
    private Vector<RoundModel> mRounds;

    /**
     * Constructor for tournament Model.
     */
    public TournamentModel(int firstTurn){
        mPlayers = new Vector<>();
        mRounds = new Vector<>();
        mPlayers.add(new HumanPlayerModel());
        mPlayers.add(new ComputerPlayerModel());
        mRounds.add(new RoundModel(mPlayers, firstTurn));
        mRoundOver = new BooleanVariable();
        mRoundNumber = 1;
    }

    /**
     *
     * @param players
     * @param round
     */
    public TournamentModel(Vector<BasePlayerModel> players, RoundModel round, final int roundNumber){
        mPlayers = players;
        mRounds = new Vector<>();
        mRounds.add(round);
        mRoundNumber = roundNumber;
        mRoundOver = new BooleanVariable();
    }

    /**
     * Returns an RoundModel object based on what round number is active.
     * @return RoundModel
     */
    public final RoundModel getCurrentRound(){ return mRounds.get(mCurrentRound); }

    /**
     * Returns BasePlayerModel of the human player object.
     * @return BasePlayerModel
     */
    public final BasePlayerModel getHumanPlayer(){ return mPlayers.get(0); }

    /**
     * Returns object corresponding to the computer player.
     * @return BasePlayerModel
     */
    public final BasePlayerModel getComputerPlayer(){ return mPlayers.get(1); }

    /**
     * Get the current round number.
     * @return integer
     */
    public final int getRoundNumber(){ return mRoundNumber; }

    /**
     *
     * @param menuOption
     * @return
     */
    public boolean runRound(MakeMoveButtonFragment moveButtonFragment, final int menuOption){
        boolean turnStatus = false;

        // If the value given is negative then. it is the AI's turn
        if (getCurrentRound().getTurn() == RoundModel.CurrentTurn.Computer){
            turnStatus = mRounds.get(mCurrentRound).execTurn(BasePlayerModel.TurnOptions.AI);
        }else {
            // If the value is non negative then we know it is the computers turn.
            switch (menuOption) {
                case 1:
                    turnStatus = mRounds.get(mCurrentRound).execTurn(BasePlayerModel.TurnOptions.TRIAL);
                    break;

                case 2:
                    turnStatus = mRounds.get(mCurrentRound).execTurn(BasePlayerModel.TurnOptions.BUILD);
                    break;

                case 3:
                    turnStatus = mRounds.get(mCurrentRound).execTurn(BasePlayerModel.TurnOptions.MULTIBUILD);
                    break;

                case 4:
                    turnStatus = mRounds.get(mCurrentRound).execTurn(BasePlayerModel.TurnOptions.EXTEND);
                    break;

                case 5:
                    turnStatus = mRounds.get(mCurrentRound).execTurn(BasePlayerModel.TurnOptions.CAPTURE);
                    break;
            }
        }

        // If the round is over, show data regarding the round and make a new one.
        if(mRounds.get(mCurrentRound).roundOver()){
            mRoundOver.setBool(true); // set the roundOver flag to true which will trigger an event.
        }

        return turnStatus;
    }

    /**
     * Creates a new round object and adds it to the mRounds collection.
     */
    public void makeNewRound(){
        Vector<CardModel> looseCards = getCurrentRound().getTable().getLooseCards();
        Vector<BuildModel> builds = getCurrentRound().getTable().getBuilds();
        looseCards.clear();

        TableModel newTable = new TableModel(looseCards, builds);
        mRounds.add(new RoundModel(mPlayers, newTable, 0, false));
        mCurrentRound++;
        mRoundNumber++;

        GameActivity.updateTableAdapterData();
        GameActivity.updateBuildAdapterData();
    }


    public final BooleanVariable getRoundOver(){
        return mRoundOver;
    }

    /**
     *
     * @return
     */
    public final Pair<Integer, Integer> calculateScores(){
        int humanScore = 0;
        int computerScore = 0;

        if(getHumanPlayer().getPile().size() > getComputerPlayer().getPile().size())
            humanScore += 3;
        else if(getComputerPlayer().getPile().size() > getHumanPlayer().getPile().size())
            computerScore += 3;

        int computerSpades = 0;
        int humanSpades = 0;

        for(CardModel card : getHumanPlayer().getPile()){
            if(card.getValue() == 1)
                humanScore += 1;
            else if(card.getValue() == 10 && card.getSuit() == 'd')
                humanScore += 2;
            else if(card.getSuit() == 's' && card.getValue() == 2)
                humanScore += 1;

            if(card.getSuit() == 's')
                humanSpades += 1;
        }

        for(CardModel card : getComputerPlayer().getPile()){
            if(card.getValue() == 1)
                computerScore += 1;
            if(card.getValue() == 10 && card.getSuit() == 'd')
                computerScore += 2;
            if(card.getSuit() == 's' && card.getValue() == 2)
                computerScore += 1;

            if(card.getSuit() == 's')
                computerSpades += 1;
        }

        if(computerSpades > humanSpades)
            computerScore += 1;

        else if(humanSpades > computerSpades)
            computerScore += 1;

        return new Pair<>(humanScore, computerScore);
    }

    /**
     * Serialize encapsulated data within class.
     * @return String
     */
    public final String toString(){
        StringBuilder tournamentData = new StringBuilder();
        tournamentData.append("Round: " + Integer.toString(mRoundNumber));
        tournamentData.append(System.getProperty("line.separator"));
        tournamentData.append(System.getProperty("line.separator"));

        tournamentData.append(mRounds.get(mCurrentRound).toString());

        return tournamentData.toString();
    }
}
