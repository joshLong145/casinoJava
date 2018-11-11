package com.casino.josh.casino_java.Listeners;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.casino.josh.casino_java.Models.CardModel;
import com.casino.josh.casino_java.Fragments.PlayerPileFragment;
import com.casino.josh.casino_java.Adapters.PileAdapter;
import com.casino.josh.casino_java.ViewModels.PileViewModel;
import com.casino.josh.casino_java.R;
import com.casino.josh.casino_java.activites.GameActivity;

import java.util.Vector;

/**
 * Created by josh on 11/7/18.
 */

public class HumanPileOnClickListener implements View.OnClickListener {
    private PlayerPileFragment mPlayerPileFragment;

    /**
     * Constructor for onClickListener
     * @param playerPileFragment PlayerPileFragment
     */
    public HumanPileOnClickListener(PlayerPileFragment playerPileFragment){
        mPlayerPileFragment = playerPileFragment;
    }


    /**
     * Executes on event of button being pressed.
     * @param v View
     */
    @Override
    public void onClick(View v) {
        LayoutInflater li = LayoutInflater.from(mPlayerPileFragment.getActivity());
        View promptsView = li.inflate(R.layout.prompt_pile, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mPlayerPileFragment.getActivity());

        alertDialogBuilder.setView(promptsView);

        RecyclerView pile = promptsView.findViewById(R.id.pile);
        pile.setHasFixedSize(true);

        RecyclerView.LayoutManager pileLayoutManager = new LinearLayoutManager(mPlayerPileFragment.getActivity(), LinearLayoutManager.HORIZONTAL, false);

        pile.setLayoutManager(pileLayoutManager);

        PileViewModel pileVM = ViewModelProviders.of(mPlayerPileFragment.getActivity()).get(PileViewModel.class);

        pileVM.setCards(GameActivity.mTournament.getHumanPlayer().getPile());

        pileVM.getCards().observe(mPlayerPileFragment.getActivity(), (Vector<CardModel> cards) -> {
                PileAdapter pileAdapter = new PileAdapter(cards, mPlayerPileFragment.getActivity());
                pile.setAdapter(pileAdapter);
                pileAdapter.notifyDataSetChanged();
        });

        alertDialogBuilder.setCancelable(true)
                          .setPositiveButton("OK", null);

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }
}
