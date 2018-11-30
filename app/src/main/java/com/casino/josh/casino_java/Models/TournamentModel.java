package com.casino.josh.casino_java.Models;

import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.casino.josh.casino_java.Adapters.HandAdapter;
import com.casino.josh.casino_java.Adapters.TableAdapter;
import com.casino.josh.casino_java.Fragments.MakeMoveButtonFragment;
import com.casino.josh.casino_java.Helpers.viewUpdater;
import com.casino.josh.casino_java.R;
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

        // If the value given is negative then. it is the AI's turn
        if (getCurrentRound().getTurn() == RoundModel.CurrentTurn.Computer){
            return mRounds.get(mCurrentRound).execTurn(BasePlayerModel.TurnOptions.AI);
        }
        boolean turnStatus = false;
        // If the value is non negative then we know it is the computers turn.
        switch(menuOption){
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

        // If the round is over, show data regarding the round and make a new one.
        if(mRounds.get(mCurrentRound).roundOver()){
            roundOverPrompt(moveButtonFragment);
            makeNewRound();
        }

        return turnStatus;
    }

    /**
     * Creates a new round object and adds it to the mRounds collection.
     */
    public void makeNewRound(){
        mRounds.add(new RoundModel(mPlayers, 0));
        mCurrentRound++;
        mRoundNumber++;

        GameActivity.mRoundNumber.setText("Round number: " + Integer.toString(getRoundNumber()));
        viewUpdater.updateTableAdapterData(mRounds.get(mCurrentRound).getTable().getLooseCards());
    }

    /**
     *
     */
    public void roundOverPrompt(MakeMoveButtonFragment moveButtonFragment){
        LayoutInflater li = LayoutInflater.from(moveButtonFragment.getActivity());
        View promptsView = li.inflate(R.layout.prompt_end_round, null);
        TextView computerDataContainer = promptsView.findViewById(R.id.computer_round_info);
        TextView humanDataContainer = promptsView.findViewById(R.id.human_round_info);


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(moveButtonFragment.getActivity());
        alertDialogBuilder.setView(promptsView);

        StringBuilder playerInfo = new StringBuilder("Pile:");
        for(CardModel card : mPlayers.get(1).getPile()){
                playerInfo.append(" ").append(card.toStringSave());
        }


        playerInfo.append("\n Score: ");

        StringBuilder computerInfo = new StringBuilder("Pile: ");

        for(CardModel card : mPlayers.get(0)._pile){
            computerInfo.append(" ").append(card.toStringSave());
        }

        computerInfo.append("\n");
        computerInfo.append("Score: ");

        computerDataContainer.setText(computerInfo.toString());
        humanDataContainer.setText(computerInfo.toString());

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
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
