package com.casino.josh.casino_java.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.casino.josh.casino_java.Models.CardModel;

import java.util.Vector;

/**
 * Created by josh on 11/1/18.
 */
public class HandViewModel extends ViewModel {

    private MutableLiveData<Vector<CardModel>> mHand;

    /** Constructor for Hand View Model.
     * @summary Constructor for the post View Model
     */
    public HandViewModel() {
        mHand = new MutableLiveData<>();
    }

    /**
     * Returns live data object.
     * @return
     */
    public LiveData<Vector<CardModel>> getHand() {
        return mHand;
    }

    /**
     * Assigns data encapsulated by live data object.
     * @param a_Hand
     */
    public void setHand(Vector<CardModel> a_Hand) {
        mHand.setValue(a_Hand);
    }
}
