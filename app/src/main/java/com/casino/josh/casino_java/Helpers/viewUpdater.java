package com.casino.josh.casino_java.Helpers;

import com.casino.josh.casino_java.Adapters.TableAdapter;
import com.casino.josh.casino_java.Models.CardModel;
import com.casino.josh.casino_java.activites.GameActivity;

import java.util.Vector;

/**
 * Created by josh on 11/25/18.
 */

public class viewUpdater {

    /**
     * Updates human views with new data.
     * recyclerviews used so notification to the adapter of data changed will update the view correctly.
     */
    public static void updateView(){
        GameActivity.mTableModelView.getAdapter().notifyDataSetChanged();
        GameActivity.mHumanHandModelView.getAdapter().notifyDataSetChanged();
        GameActivity.mBuildModelView.getAdapter().notifyDataSetChanged();
        GameActivity.mChosenCard = null;
        GameActivity.mLooseCards = new Vector<>();
        GameActivity.mBuilds = new Vector<>();
        GameActivity.mTournament.getCurrentRound().setCurrentPlayerIndex(1);
        GameActivity.mCurrentTurn.setText("Current turn: " + GameActivity.mTournament.getCurrentRound().getTurn());
    }

    /**
     * Updates humanHand view with new data.
     * recyclerviews used so notification to the adapter of data changed will update the view correctly.
     */
    public static void updateHuamnHandView(){
        GameActivity.mHumanHandModelView.getAdapter().notifyDataSetChanged();
    }

    /**
     * Updates computerHand view with new data.
     * recyclerviews used so notification to the adapter of data changed will update the view correctly.
     */
    public static void updateComputerHandView(){
        GameActivity.mComputerModelView.getAdapter().notifyDataSetChanged();
    }

    public static void updateTableAdapterData(Vector<CardModel> looseCards){
        TableAdapter adapter  = (TableAdapter)GameActivity.mTableModelView.getAdapter();
        adapter.getCards().clear();
        adapter.getCards().addAll(looseCards);
        adapter.notifyDataSetChanged();
    }

}
