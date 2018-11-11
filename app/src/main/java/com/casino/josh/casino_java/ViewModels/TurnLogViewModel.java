package com.casino.josh.casino_java.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.casino.josh.casino_java.Models.CardModel;

import java.util.Vector;

/**
 * Created by josh on 11/10/18.
 */

public class TurnLogViewModel extends ViewModel {
    private MutableLiveData<Vector<String>> mTurns;

    /**
     * @summary Constructor for the pile View Model
     */
    public TurnLogViewModel() {
        mTurns = new MutableLiveData<>();
    }

    /**
     * returns live data object.
     * @return
     */
    public LiveData<Vector<String>> getTurnLog() {
        return mTurns;
    }

    /**
     * Sets data encapsulated within live data object.
     * @param a_TurnLog
     */
    public void setTurnLog(Vector<String> a_TurnLog) {
        mTurns.setValue(a_TurnLog);
    }



}
