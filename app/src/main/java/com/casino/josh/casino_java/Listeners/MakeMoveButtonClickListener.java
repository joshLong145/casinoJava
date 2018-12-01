package com.casino.josh.casino_java.Listeners;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.casino.josh.casino_java.Adapters.BuildAdapter;
import com.casino.josh.casino_java.Adapters.ComputerHandAdapter;
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
    private BuildAdapter mBuilds;
    /**
     * Constructor for MakeMoveButtonClickListener
     * @param makeMoveFragment MakeMoveButtonFragment
     * @param a_humanAdapter HandAdapter
     * @param a_tableAdapter ComputerHandAdapter
     */
    public MakeMoveButtonClickListener(MakeMoveButtonFragment makeMoveFragment, HandAdapter a_humanAdapter, TableAdapter a_tableAdapter, ComputerHandAdapter a_computerAdapter, BuildAdapter a_BuildAdapter){
        this.makeMoveFragment = makeMoveFragment;
        mHand = a_humanAdapter;
        mTable = a_tableAdapter;
        mComputerHand = a_computerAdapter;
        mBuilds = a_BuildAdapter;
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
                        // TODO: refactor into modular switch statement to avoid code redundancy.
                        /** Executes when positive button on alert dialog is pressed.
                         * @param dialog DialogInterface
                         * @param which int
                         */
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            boolean turnSuccess = false;
                            if (GameActivity.mChosenCard != null) {
                                switch (turnOptions.getCheckedRadioButtonId()) {
                                    case R.id.trail:
                                        if (GameActivity.mTournament.runRound(makeMoveFragment, 1)) {
                                            turnSuccess = true;
                                        } else {
                                            Toast toast = Toast.makeText(makeMoveFragment.getContext(),
                                                    "Unable to trail selected card card",
                                                    Toast.LENGTH_SHORT);
                                            toast.show();
                                        }
                                        break;
                                    case R.id.build:
                                        if (GameActivity.mLooseCards != null) {
                                            if (GameActivity.mTournament.runRound(makeMoveFragment, 2)) {
                                                turnSuccess = true;
                                            } else {
                                                Toast toast = Toast.makeText(makeMoveFragment.getContext(),
                                                        "Unable to build with selected cards",
                                                        Toast.LENGTH_SHORT);
                                                toast.show();
                                            }
                                        } else {
                                            Toast toast = Toast.makeText(makeMoveFragment.getContext(),
                                                    "Please select cards to build with.",
                                                    Toast.LENGTH_SHORT);
                                            toast.show();
                                        }
                                        break;

                                    case R.id.multi_build:
                                        if (GameActivity.mTournament.runRound(makeMoveFragment, 3)) {
                                            turnSuccess = true;
                                        } else {
                                            Toast toast = Toast.makeText(makeMoveFragment.getContext(),
                                                    "Cannot make multibuild with selection.",
                                                    Toast.LENGTH_SHORT);
                                            toast.show();
                                        }
                                        break;

                                    case R.id.increase_build:
                                        if (GameActivity.mTournament.runRound(makeMoveFragment, 4)) {
                                            turnSuccess = true;
                                        } else {
                                            Toast toast = Toast.makeText(makeMoveFragment.getContext(),
                                                    "Cannot make multibuild with selection.",
                                                    Toast.LENGTH_SHORT);
                                            toast.show();
                                        }
                                        break;

                                    case R.id.capture:
                                        if (GameActivity.mLooseCards != null) {
                                            if (GameActivity.mTournament.runRound(makeMoveFragment, 5)) {
                                                turnSuccess = true;
                                            } else {
                                                Toast toast = Toast.makeText(makeMoveFragment.getContext(),
                                                        "Cannot capture with selection.",
                                                        Toast.LENGTH_SHORT);
                                                toast.show();
                                            }
                                        } else {
                                            Toast toast = Toast.makeText(makeMoveFragment.getContext(),
                                                    "Please select loose cards to capture.",
                                                    Toast.LENGTH_SHORT);
                                            toast.show();
                                        }
                                        break;

                                }

                                // If the players turn was valid, change turns and notify adapters.
                                if(turnSuccess){
                                    GameActivity.updateView();
                                    GameActivity.mTournament.getCurrentRound().setCurrentPlayerIndex(1);
                                    GameActivity.mCurrentTurn.setText("Current turn: " +
                                            GameActivity.mTournament.getCurrentRound().getTurn());
                                }

                                // Clear all input containers after move is made
                                // regardless of move status ( success failure).
                                GameActivity.mChosenCard = null;
                                GameActivity.mLooseCards = new Vector<>();
                                GameActivity.mBuilds = new Vector<>();
                            } else {
                                Toast toast = Toast.makeText(makeMoveFragment.getContext(),
                                        "Please select a card",
                                        Toast.LENGTH_SHORT);
                                toast.show();

                                // Clear all input containers after move is made
                                // regardless of move status ( success failure).
                                GameActivity.mChosenCard = null;
                                GameActivity.mLooseCards = new Vector<>();
                                GameActivity.mBuilds = new Vector<>();
                            }

                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();

            alertDialog.show();


        // If it is the computers turn then no prompting is needed for input.
        }else{
                GameActivity.mTournament.runRound(makeMoveFragment, -1);
                GameActivity.updateComputerHandView();

                mTable.notifyDataSetChanged();
                mBuilds.notifyDataSetChanged();

                GameActivity.mTournament.getCurrentRound().setCurrentPlayerIndex(0);
                GameActivity.mCurrentTurn.setText("Current turn: " + GameActivity.mTournament.getCurrentRound().getTurn());
        }
    }
}
