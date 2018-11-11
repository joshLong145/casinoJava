package com.casino.josh.casino_java.Listeners;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.casino.josh.casino_java.Adapters.PileAdapter;
import com.casino.josh.casino_java.Fragments.DeckButtonFragment;
import com.casino.josh.casino_java.Models.CardModel;
import com.casino.josh.casino_java.R;
import com.casino.josh.casino_java.ViewModels.PileViewModel;
import com.casino.josh.casino_java.activites.GameActivity;

import java.util.Vector;

/**
 * Created by josh on 11/10/18.
 */

public class DeckOnClickListener implements View.OnClickListener {
    private DeckButtonFragment mDeckButtonFragment;

    public DeckOnClickListener(DeckButtonFragment a_DeckButtonFragment){
        mDeckButtonFragment = a_DeckButtonFragment;
    }

    @Override
    public void onClick(View v) {
        LayoutInflater li = LayoutInflater.from(mDeckButtonFragment.getActivity());
        View promptsView = li.inflate(R.layout.prompt_pile, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mDeckButtonFragment.getActivity());

        alertDialogBuilder.setView(promptsView);

        RecyclerView pile = promptsView.findViewById(R.id.pile);
        pile.setHasFixedSize(true);

        RecyclerView.LayoutManager pileLayoutManager = new LinearLayoutManager(mDeckButtonFragment.getActivity(), LinearLayoutManager.HORIZONTAL, false);

        pile.setLayoutManager(pileLayoutManager);

        PileViewModel pileVM = ViewModelProviders.of(mDeckButtonFragment.getActivity()).get(PileViewModel.class);

        pileVM.setCards(GameActivity.mTournament.getCurrentRound().getTable().getDeck().getCards());

        pileVM.getCards().observe(mDeckButtonFragment.getActivity(), (Vector<CardModel> cards) -> {
            PileAdapter pileAdapter = new PileAdapter(cards, mDeckButtonFragment.getActivity());
            pile.setAdapter(pileAdapter);
            pileAdapter.notifyDataSetChanged();
        });

        alertDialogBuilder.setCancelable(true)
                .setPositiveButton("OK", null);

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }
}
