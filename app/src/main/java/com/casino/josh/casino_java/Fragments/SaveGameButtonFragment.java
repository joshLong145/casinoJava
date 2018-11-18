package com.casino.josh.casino_java.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.casino.josh.casino_java.Listeners.SaveGameButtonOnClickListener;
import com.casino.josh.casino_java.Listeners.TurnLogOnClickListener;

/**
 * Created by josh on 11/16/18.
 */

public class SaveGameButtonFragment extends Fragment {
    /**
     * Default constructor for class.
     */
    public SaveGameButtonFragment() {}

    /**
     * Executed on the creation of the view, creates component and returns the view.
     *
     * @param inflater           LayoutInflater
     * @param container          ViewGroup
     * @param savedInstanceState Bundle
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button saveGame = new Button(getActivity());

        saveGame.setTextColor(0x000000);
        saveGame.setBackgroundColor(0x8555b4);
        saveGame.setOnClickListener(new SaveGameButtonOnClickListener(this));

        return saveGame;
    }


}
