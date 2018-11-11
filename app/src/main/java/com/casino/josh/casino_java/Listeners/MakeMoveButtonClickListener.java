package com.casino.josh.casino_java.Listeners;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.casino.josh.casino_java.Adapters.ComputerHandAdapter;
import com.casino.josh.casino_java.Models.BasePlayerModel;
import com.casino.josh.casino_java.Models.CardModel;
import com.casino.josh.casino_java.Fragments.MakeMoveButtonFragment;
import com.casino.josh.casino_java.Adapters.HandAdapter;
import com.casino.josh.casino_java.Models.RoundModel;
import com.casino.josh.casino_java.R;
import com.casino.josh.casino_java.Adapters.TableAdapter;
import com.casino.josh.casino_java.activites.GameActivity;

import java.util.Vector;

/**
 * Created by josh on 11/4/18.
 */

public class MakeMoveButtonClickListener implements View.OnClickListener {
    private MakeMoveButtonFragment makeMoveFragment;
    public static boolean mIsSelected = false;
    public static int mTurnOption = -1;
    private HandAdapter mHand;
    private TableAdapter mTable;
    private ComputerHandAdapter mComputerHand;

    /**
     * Constructor for MakeMoveButtonClickListener
     * @param makeMoveFragment MakeMoveButtonFragment
     * @param a_humanAdapter HandAdapter
     * @param a_tableAdapter ComputerHandAdapter
     */
    public MakeMoveButtonClickListener(MakeMoveButtonFragment makeMoveFragment, HandAdapter a_humanAdapter, TableAdapter a_tableAdapter, ComputerHandAdapter a_computerAdapter){
        this.makeMoveFragment = makeMoveFragment;
        mHand = a_humanAdapter;
        mTable = a_tableAdapter;
        mComputerHand = a_computerAdapter;
    }

    /**
     * Executes on the event of button being pressed.
     * @param v View
     */
    @Override
    public void onClick(View v) {
        if(GameActivity.mTournament.getCurrentRound().getTurn() == RoundModel.CurrentTurn.Human) {
            LayoutInflater li = LayoutInflater.from(makeMoveFragment.getActivity());
            View promptsView = li.inflate(R.layout.prompt_move, null);
            RadioGroup turnOptions = promptsView.findViewById(R.id.move_options);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(makeMoveFragment.getActivity());
            alertDialogBuilder.setView(promptsView);

            alertDialogBuilder
                    .setCancelable(true)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        /** Executes when positive button on alert dialog is pressed.
                         * @param dialog DialogInterface
                         * @param which int
                         */
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (turnOptions.getCheckedRadioButtonId() == R.id.trail) {

                                if (GameActivity.mChosenCard != null) {

                                    if (GameActivity.mTournament.getCurrentRound().execTurn(BasePlayerModel.TurnOptions.TRIAL)) {
                                        mTable.getCards().clear();
                                        mTable.getCards().addAll(GameActivity.mTournament.getCurrentRound().getTable().getLooseCards());
                                        mHand.getCards().clear();
                                        mHand.getCards().addAll(GameActivity.mTournament.getHumanPlayer().getHand());
                                        mTable.notifyDataSetChanged();
                                        mHand.notifyDataSetChanged();
                                        GameActivity.mChosenCard = null;
                                        GameActivity.mTournament.getCurrentRound().setCurrentPlayerIndex(1);

                                    } else {
                                        Toast toast = Toast.makeText(makeMoveFragment.getContext(),
                                                                    "Need to capture with that card",
                                                                            Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                } else {
                                    Toast toast = Toast.makeText(makeMoveFragment.getContext(),
                                                                    "Please select a card",
                                                                            Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            } else if (turnOptions.getCheckedRadioButtonId() == R.id.capture) {
                                if (GameActivity.mChosenCard != null) {
                                    if(GameActivity.mTournament.getCurrentRound().execTurn(BasePlayerModel.TurnOptions.CAPTURE)){
                                        mHand.getCards().clear();
                                        mHand.getCards().addAll(GameActivity.mTournament.getCurrentRound().getCurrenntPlayer().getHand());
                                        mTable.getCards().clear();
                                        mTable.getCards().addAll(GameActivity.mTournament.getCurrentRound().getTable().getLooseCards());
                                        mTable.notifyDataSetChanged();
                                        mHand.notifyDataSetChanged();
                                        GameActivity.mChosenCard = null;
                                        GameActivity.mTournament.getCurrentRound().setCurrentPlayerIndex(1);

                                    }else{
                                        Toast toast = Toast.makeText(makeMoveFragment.getContext(),
                                                "Cannot capture with that card.",
                                                Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                } else {
                                    Toast toast = Toast.makeText(makeMoveFragment.getContext(),
                                                            "Please select a card.",
                                                                    Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            }
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();

            alertDialog.show();
        }else{
                GameActivity.mTournament.getCurrentRound().execTurn(BasePlayerModel.TurnOptions.AI);
                mComputerHand.getCards().remove(0);
                mComputerHand.notifyDataSetChanged();
                mTable.getCards().clear();
                mTable.getCards().addAll(GameActivity.mTournament.getCurrentRound().getTable().getLooseCards());
                GameActivity.mTournament.getCurrentRound().setCurrentPlayerIndex(0);
        }
    }
}