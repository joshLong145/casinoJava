package com.casino.josh.casino_java.Models;

import com.casino.josh.casino_java.activites.GameActivity;

import java.util.Vector;

/**
 * Created by josh on 11/5/18.
 */

public class RoundModel {
    private TableModel mTable;
    private Vector<BasePlayerModel> mPlayers;
    private CurrentTurn mCurrentTurn;
    private int mCurrentPlayerIndex = 0;
    private boolean mRoundEnd = false;
    private CurrentTurn mLastCapture;


    /**
     * Enum for Current Turn indication.
     */
    public enum CurrentTurn{
        Human, Computer
    }

    /**
     * Constructor for RoundModel.
     * @param players Vector<BasePlayerModel>
     */
    public RoundModel(Vector<BasePlayerModel> players, int firstTurn){
        mTable = new TableModel();
        mPlayers = players;
        mCurrentPlayerIndex = firstTurn;

        if(mCurrentPlayerIndex == 0)
            mCurrentTurn = CurrentTurn.Human;
        else
            mCurrentTurn = CurrentTurn.Computer;

        dealPlayerHands();
        mTable.getLooseCards().addAll(mTable.getDeck().dealHand());

        mLastCapture = null;
    }

    /**
     * Constructor to load in deserialized data from save file.
     * @param players
     * @param table
     * @param firstTurn
     */
    public RoundModel(Vector<BasePlayerModel> players, TableModel table, final int firstTurn, final boolean serial){
        mTable = table;
        mPlayers = players;
        mCurrentPlayerIndex = firstTurn;

        if(mCurrentPlayerIndex == 0)
            mCurrentTurn = CurrentTurn.Human;
        else
            mCurrentTurn = CurrentTurn.Computer;

        if(!serial){
            dealPlayerHands();
            mTable.getLooseCards().addAll(mTable.getDeck().dealHand());
        }
    }

    /**
     * Executes makeMove() for the current active player.
     * @param option
     * @return
     */
    public boolean execTurn(BasePlayerModel.TurnOptions option){

        if(mPlayers.get(mCurrentPlayerIndex).makeMove(mTable, option)) {
            if(mCurrentTurn == CurrentTurn.Human){
                mCurrentTurn = CurrentTurn.Computer;
                mCurrentPlayerIndex = 1;
            }else{
                mCurrentTurn = CurrentTurn.Human;
                mCurrentPlayerIndex = 0;
            }

            // check if new hands need to be dealt to the player.
            dealPlayerHands();
            return true;
        }

        return false;
    }

    /**
     * Returns a TableModel Object.
     * @return TableModel
     */
    public TableModel getTable(){ return mTable; }

    /**
     * Deals cards to both player and computer if both hands are empty.
     */
    private void dealPlayerHands(){
        int emptyHands = 0;
        for(BasePlayerModel player : mPlayers) {
            if (player.getHand().size() <= 0)
                emptyHands++;
        }

        if(emptyHands == mPlayers.size() && mTable.getDeck().getSize() > 0){
            for(BasePlayerModel player: mPlayers){
                player.getHand().clear();
                player.getHand().addAll(getTable().getDeck().dealHand());
            }

            if(GameActivity.mComputerModelView != null) {
                GameActivity.updateComputerHandView();
            }

            if(GameActivity.mHumanHandModelView != null) {
                GameActivity.updateHuamnHandView();
            }

        }else if(emptyHands == mPlayers.size() && mTable.getDeck().getSize() <= 0){
                mRoundEnd = true;
        }
    }

    /**
     * returns who current turn it is.
     * @return CurrentTurn enum
     */
    public CurrentTurn getTurn(){
        return mCurrentTurn;
    }

    /**
     * setter for CurrentPlayerIndex;
     * @param index Integer.
     */
    public void setCurrentPlayerIndex(final int index){ mCurrentPlayerIndex = index; }

    /**
     * Return an enum indicating who last made a capture action.
     * @return CurrentTurn
     */
    public CurrentTurn getLastCapture(){ return mLastCapture; }

    /**
     * Set the enum of who last made a capture action.
     * @param captured
     */
    public void setLastCapture(CurrentTurn captured){ mLastCapture = captured; }

    /**
     * Returns Vector of players active within the current round.
     * @return
     */
    public final Vector<BasePlayerModel> getPlayers(){ return mPlayers; }


    /**
     * Returns the status of the round. If true, the round is over and scores must be shown and
     * a new round can potentially be made.
     * @return
     */
    public final boolean roundOver(){ return mRoundEnd;}

    /**
     * Serialize data encapsulated within class.
     * @return
     */
    public final String toString(){
        StringBuilder roundData = new StringBuilder();

        for(BasePlayerModel player : mPlayers) {
            roundData.append(player.toString());
            roundData.append(System.getProperty("line.separator"));
        }

        roundData.append(mTable.toString());

        roundData.append("Next Player: " + mCurrentTurn.toString());
        
       return roundData.toString();
    }

}
