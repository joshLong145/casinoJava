package com.casino.josh.casino_java.Fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.casino.josh.casino_java.Listeners.HelpOnClickListener;
import com.casino.josh.casino_java.R;
/**
 * Created by josh on 11/30/18.
 */

public class HelpButtonFragment extends Fragment {

    /**
     * Default constructor
     */
    public HelpButtonFragment(){}

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

        Button helpButton = new Button(getActivity());
        helpButton.setTextColor(0x005b9c);
        helpButton.setBackgroundColor(0x8555b4);
        helpButton.setOnClickListener(new HelpOnClickListener(this));
        return helpButton;
    }
}
