package com.casino.josh.casino_java.Listeners;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.casino.josh.casino_java.Adapters.PileAdapter;
import com.casino.josh.casino_java.Fragments.ComputerPileFragment;
import com.casino.josh.casino_java.Models.CardModel;
import com.casino.josh.casino_java.R;
import com.casino.josh.casino_java.ViewModels.ComputerPileViewModel;
import com.casino.josh.casino_java.ViewModels.PileViewModel;
import com.casino.josh.casino_java.activites.GameActivity;

import java.util.Vector;

/**
 * Created by josh on 11/9/18.
 */

public class ComputerpileOnClickListener implements View.OnClickListener {
    private ComputerPileFragment mComputerPileFragment;

    /**
     * Constructor for EventListener
     * @param a_ComputerPileFragment ComputerPileFragment
     */
    public ComputerpileOnClickListener(ComputerPileFragment a_ComputerPileFragment){
        mComputerPileFragment = a_ComputerPileFragment;
    }

    /**
     * Executes on the event of the corresponding button being pressed.
     * @param v View
     */
    @Override
    public void onClick(View v) {
        // Inflate the layout and constructor a AlerDialog.
        LayoutInflater li = LayoutInflater.from(mComputerPileFragment.getActivity());
        View promptsView = li.inflate(R.layout.prompt_pile, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mComputerPileFragment.getActivity());

        alertDialogBuilder.setView(promptsView);

        RecyclerView pile = promptsView.findViewById(R.id.pile);
        pile.setHasFixedSize(true);

        RecyclerView.LayoutManager pileLayoutManager = new LinearLayoutManager(mComputerPileFragment.getActivity(),
                                                                                LinearLayoutManager.HORIZONTAL,false);
        pile.setLayoutManager(pileLayoutManager);

        ComputerPileViewModel pileVM = ViewModelProviders.of(mComputerPileFragment.getActivity()).get(ComputerPileViewModel.class);

        pileVM.setCards(GameActivity.mTournament.getComputerPlayer().getPile());

        pileVM.getCards().observe(mComputerPileFragment.getActivity(), (Vector<CardModel> cards) -> {
            PileAdapter pileAdapter = new PileAdapter(cards, mComputerPileFragment.getActivity());
            pile.setAdapter(pileAdapter);
            pileAdapter.notifyDataSetChanged();
        });

        alertDialogBuilder.setCancelable(true)
                .setPositiveButton("OK", null);

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }
}
