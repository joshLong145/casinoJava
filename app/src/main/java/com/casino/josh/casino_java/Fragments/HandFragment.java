package com.casino.josh.casino_java.Fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.casino.josh.casino_java.Models.CardModel;
import com.casino.josh.casino_java.Adapters.HandAdapter;
import com.casino.josh.casino_java.ViewModels.HandViewModel;
import com.casino.josh.casino_java.R;

import java.util.Vector;

/**
 * Created by josh on 11/1/18.
 */

public class HandFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private HandAdapter mAdapter;

    /**
     * Constructor for Fragment, default constructor as Fragments cannot take arguments.
     */
    public HandFragment(){}

    /**
     * Executed on the creation of the view, creates component and returns the view.
     * @param inflater LayoutInflater
     * @param container ViewGroup
     * @param savedInstanceState Bundle
     * @return
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.game_activity, container, false);

        if (container == null) {
            // We have different layouts, and in one of them this
            // fragment's containing frame doesn't exist. The fragment
            // may still be created from its saved state, but there is
            // no reason to try to create its view hierarchy because it
            // isn't displayed. Note this isn't needed -- we could just
            // run the code below, where we would create and return the
            // view hierarchy; it would just never be used.
            return null;
        }

        mRecyclerView = view.findViewById(R.id.human_hand);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        mRecyclerView.setLayoutManager(mLayoutManager);

        HandViewModel handVM = ViewModelProviders.of(getActivity()).get(HandViewModel.class);

        Vector<CardModel> cards = new Vector<>();

        handVM.getHand().observe(getActivity(), hand ->{
                for(int i = 0; i < hand.size(); i++){
                    cards.add(hand.get(i));
                }

               // mAdapter = new HandAdapter(cards, getActivity());
               // mRecyclerView.setAdapter(mAdapter);
               // mAdapter.notifyDataSetChanged();
        });

        return view;
    }
}
