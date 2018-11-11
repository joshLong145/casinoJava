package com.casino.josh.casino_java.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.casino.josh.casino_java.Adapters.BuildAdapter;
import com.casino.josh.casino_java.Adapters.ComputerHandAdapter;
import com.casino.josh.casino_java.Adapters.HandAdapter;
import com.casino.josh.casino_java.Listeners.MakeMoveButtonClickListener;
import com.casino.josh.casino_java.Adapters.TableAdapter;
import com.casino.josh.casino_java.activites.GameActivity;

/**
 * Created by josh on 11/1/18.
 */

public class MakeMoveButtonFragment extends Fragment {

    /**
     * Constructor for Fragment, default constructor as Fragments cannot take arguments.
     */
    public MakeMoveButtonFragment(){ }

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


        Button makeMove = new Button(getActivity());
        makeMove.setTextColor(0x000000);
        makeMove.setBackgroundColor(0x8555b4);
        makeMove.setOnClickListener(new MakeMoveButtonClickListener(this,
                                                                        (HandAdapter) GameActivity.mHumanHandModelView.getAdapter(),
                                                                        (TableAdapter) GameActivity.mTableModelView.getAdapter(),
                                                                        (ComputerHandAdapter)GameActivity.mComputerModelView.getAdapter(),
                                                                        (BuildAdapter)GameActivity.mBuildModelView.getAdapter())
        );

        return makeMove;
    }
}
