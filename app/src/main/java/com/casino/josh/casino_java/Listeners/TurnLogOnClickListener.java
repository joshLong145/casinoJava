package com.casino.josh.casino_java.Listeners;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.casino.josh.casino_java.Adapters.PileAdapter;
import com.casino.josh.casino_java.Adapters.TurnLogAdapter;
import com.casino.josh.casino_java.Fragments.LogButtonFragment;
import com.casino.josh.casino_java.Models.CardModel;
import com.casino.josh.casino_java.Models.TurnLogModel;
import com.casino.josh.casino_java.R;
import com.casino.josh.casino_java.ViewModels.PileViewModel;
import com.casino.josh.casino_java.ViewModels.TurnLogViewModel;
import com.casino.josh.casino_java.activites.GameActivity;

import java.util.Vector;

/**
 * Created by josh on 11/10/18.
 */

public class TurnLogOnClickListener implements View.OnClickListener {
    private LogButtonFragment mLogButtonFragment;

    /**
     *
     * @param a_turnLog
     */
    public TurnLogOnClickListener(LogButtonFragment a_turnLog){
        mLogButtonFragment = a_turnLog;
    }

    /**
     * Overrides on click listener for the TurnLog button.
     * Creates a alert dialog and displays all data related to previous turns.
     * @param v
     */
    @Override
    public void onClick(View v) {
        LayoutInflater li = LayoutInflater.from(mLogButtonFragment.getActivity());
        View promptsView = li.inflate(R.layout.prompt_turn_log, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mLogButtonFragment.getActivity());

        alertDialogBuilder.setView(promptsView);

        RecyclerView log = promptsView.findViewById(R.id.pile);
        log.setHasFixedSize(true);

        RecyclerView.LayoutManager pileLayoutManager = new LinearLayoutManager(mLogButtonFragment.getActivity(), LinearLayoutManager.VERTICAL, false);

        log.setLayoutManager(pileLayoutManager);

        TurnLogViewModel logVM = ViewModelProviders.of(mLogButtonFragment.getActivity()).get(TurnLogViewModel.class);

        logVM.setTurnLog(TurnLogModel.getLog());

        logVM.getTurnLog().observe(mLogButtonFragment.getActivity(), (Vector<String> logs) -> {
            TurnLogAdapter logAdapter = new TurnLogAdapter(logs, mLogButtonFragment.getActivity());
            log.setAdapter(logAdapter);
            logAdapter.notifyDataSetChanged();
        });

        alertDialogBuilder.setCancelable(true);

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }
}
