package com.casino.josh.casino_java.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.casino.josh.casino_java.Models.CardModel;

import java.util.Vector;

/**
 * Created by josh on 11/9/18.
 */

public class ComputerPileViewModel extends ViewModel {

    private MutableLiveData<Vector<CardModel>> mCards;

    /**
     * @summary Constructor for the pile View Model
     */
    public ComputerPileViewModel() {
        mCards = new MutableLiveData<>();
    }

    /**
     * Returns data within the collection
     * @return
     */
    public LiveData<Vector<CardModel>> getCards() {
        return mCards;
    }

    /**
     * Sets data that is encapsulated within the live data object.
     * @param a_Hand
     */
    public void setCards(Vector<CardModel> a_Hand) {
        mCards.setValue(a_Hand);
    }

}