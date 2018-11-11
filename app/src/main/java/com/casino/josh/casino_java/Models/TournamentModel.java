package com.casino.josh.casino_java.Models;

import android.support.v7.widget.RecyclerView;

import com.casino.josh.casino_java.Adapters.HandAdapter;

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
    private Vector<BasePlayerModel> mPlayers;
    private Vector<RoundModel> mRounds;

    /**
     * Constructor for tournament Model.
     */
    public TournamentModel(){
        mPlayers = new Vector<>();
        mRounds = new Vector<>();
        mPlayers.add(new HumanPlayerModel());
        mPlayers.add(new ComputerPlayerModel());
        mRounds.add(new RoundModel(mPlayers));
        mRoundNumber = 1;
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
     * Creates a new round object and adds it to the mRounds collection.
     */
    public void makeNewRound(){
        mRounds.add(new RoundModel(mPlayers));
        mCurrentRound++;
        mRoundNumber++;
    }
}
