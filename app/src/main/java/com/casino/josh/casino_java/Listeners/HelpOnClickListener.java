package com.casino.josh.casino_java.Listeners;

import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.casino.josh.casino_java.Fragments.HelpButtonFragment;
import com.casino.josh.casino_java.Models.ComputerPlayerModel;
import com.casino.josh.casino_java.R;
import com.casino.josh.casino_java.activites.GameActivity;

/**
 * Created by josh on 11/30/18.
 */

public class HelpOnClickListener implements View.OnClickListener {
    private HelpButtonFragment mHelpButtonFragment;
    public HelpOnClickListener(HelpButtonFragment helpbutton){
        mHelpButtonFragment = helpbutton;
    }

    /**
     * Implementation of function from interface.
     * @param v
     */
    @Override
    public void onClick(View v) {
        LayoutInflater li = LayoutInflater.from(mHelpButtonFragment.getActivity());
        View promptsView = li.inflate(R.layout.layout_help, null);

        TextView helpTextField = promptsView.findViewById(R.id.prompt);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mHelpButtonFragment.getActivity());
        alertDialogBuilder.setView(promptsView);

        ComputerPlayerModel computer = (ComputerPlayerModel)GameActivity.mTournament.getComputerPlayer();
        String helpString = computer.moveHelp(GameActivity.mTournament.getCurrentRound().getTable(),
                          GameActivity.mTournament.getHumanPlayer().getHand());

        helpTextField.setText(helpString);
        alertDialogBuilder.setCancelable(true)
                .setPositiveButton("OK", null);

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }
}
