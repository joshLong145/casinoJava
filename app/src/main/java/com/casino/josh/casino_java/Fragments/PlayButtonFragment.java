package com.casino.josh.casino_java.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.casino.josh.casino_java.Listeners.PlayButtonClickListener;

/**
 * Created by josh on 10/30/18.
 */

public class PlayButtonFragment extends Fragment{

    /**
     * Constructor for Fragment, default constructor as Fragments cannot take arguments.
     */
    public PlayButtonFragment() {}

    /**
     * Executed on the creation of the view, creates component and returns the view.
     * @param inflater LayoutInflater
     * @param container ViewGroup
     * @param savedInstanceState Bundle
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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


        Button playGame = new Button(getActivity());
        playGame.setTextColor(0x000000);
        playGame.setBackgroundColor(0x8555b4);
        playGame.setOnClickListener(new PlayButtonClickListener(this));
        return playGame;
    }
}
