package com.casino.josh.casino_java.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.casino.josh.casino_java.Listeners.TurnLogOnClickListener;

/**
 * Created by josh on 11/10/18.
 */

public class LogButtonFragment extends Fragment {

    /**
     * Default constructor for class.
     */
    public LogButtonFragment() {}

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

        Button logButton = new Button(getActivity());

        logButton.setTextColor(0x000000);
        logButton.setBackgroundColor(0x8555b4);
        logButton.setOnClickListener(new TurnLogOnClickListener(this));


        return logButton;
    }

}
