package com.casino.josh.casino_java.Models;

import android.support.v7.widget.RecyclerView;

import com.casino.josh.casino_java.Adapters.ComputerHandAdapter;
import com.casino.josh.casino_java.Adapters.HandAdapter;
import com.casino.josh.casino_java.Models.BasePlayerModel;
import com.casino.josh.casino_java.Models.TableModel;
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
    public RoundModel(Vector<BasePlayerModel> players){
        mTable = new TableModel();
        mPlayers = players;
        mCurrentPlayerIndex = 0;
        mCurrentTurn = CurrentTurn.Human;
        dealCards();


        mTable.getLooseCards().addAll(mTable.getDeck().dealHand());
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
            }else{
                mCurrentTurn = CurrentTurn.Human;
            }

            // check if new hands need to be dealt to the player.
            dealCards();
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
     * Serialize current class.
     * @return String
     */
    public String toString(){ return ""; }

    /**
     * Deals cards to both player and computer if both hands are empty.
     */
    private void dealCards(){
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
                ComputerHandAdapter computerHand = (ComputerHandAdapter) GameActivity.mComputerModelView.getAdapter();

                computerHand.getCards().addAll(mPlayers.get(1).getHand());

                computerHand.notifyDataSetChanged();
            }

            if(GameActivity.mHumanHandModelView != null) {
                HandAdapter humanHand = (HandAdapter) GameActivity.mHumanHandModelView.getAdapter();

                humanHand.getCards().addAll(mPlayers.get(0).getHand());
                humanHand.notifyDataSetChanged();
            }
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
     * Returns Vector of players active within the current round.
     * @return
     */
    public final Vector<BasePlayerModel> getPlayers(){ return mPlayers; }


    /**
     * Returns the player who's turn it currently is.
     * @return BasePlayerModel &
     */
    public final BasePlayerModel getCurrenntPlayer(){ return mPlayers.get(mCurrentPlayerIndex); }

}
