package com.casino.josh.casino_java.Listeners;

import android.view.View;
import android.widget.Toast;

import com.casino.josh.casino_java.Fragments.HelpButtonFragment;
import com.casino.josh.casino_java.Models.ComputerPlayerModel;
import com.casino.josh.casino_java.activites.GameActivity;

/**
 * Created by josh on 11/30/18.
 */

public class HelpOnClickListener implements View.OnClickListener {
    private HelpButtonFragment mHelpButtonFragment;
    public HelpOnClickListener(HelpButtonFragment helpbutton){
        mHelpButtonFragment = helpbutton;
    }

    @Override
    public void onClick(View v) {
        ComputerPlayerModel computer = (ComputerPlayerModel)GameActivity.mTournament.getComputerPlayer();
        String helpString = computer.moveHelp(GameActivity.mTournament.getCurrentRound().getTable(),
                          GameActivity.mTournament.getHumanPlayer().getHand());


        Toast toast = Toast.makeText(mHelpButtonFragment.getContext(),
                helpString,
                Toast.LENGTH_SHORT);
        toast.show();
    }
}
