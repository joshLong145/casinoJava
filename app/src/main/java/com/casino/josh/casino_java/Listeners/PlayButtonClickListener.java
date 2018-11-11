package com.casino.josh.casino_java.Listeners;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;

import com.casino.josh.casino_java.Fragments.PlayButtonFragment;
import com.casino.josh.casino_java.R;
import com.casino.josh.casino_java.activites.GameActivity;

/**
 * Created by josh on 10/30/18.
 */

public class PlayButtonClickListener implements View.OnClickListener {
    private PlayButtonFragment playFragment;

    /**
     * Constructor for class.
     * @param playFragment PlayButtonFragment
     */
    public PlayButtonClickListener(PlayButtonFragment playFragment) {
        this.playFragment = playFragment;
    }

    /**
     * Executes on event of button being pressed.
     * @param view View
     */
    @Override
    public void onClick(View view) {
        Intent eventInformationIntent = new Intent(playFragment.getActivity(), GameActivity.class);

        LayoutInflater li = LayoutInflater.from(playFragment.getActivity());
        View promptsView = li.inflate(R.layout.prompt_cointoss, null);
        RadioGroup turnOptions = promptsView.findViewById(R.id.toss_option);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(playFragment.getActivity());

        alertDialogBuilder.setView(promptsView);

        alertDialogBuilder
                .setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        playFragment.startActivity(eventInformationIntent);
                    }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }
}