package com.casino.josh.casino_java.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.casino.josh.casino_java.Models.CardModel;

import java.util.Vector;

/**
 * Created by josh on 11/1/18.
 */
public class TableViewModel extends ViewModel {

    private MutableLiveData<Vector<CardModel>> mCards;

    /**
     * @summary Constructor for the post View Model
     */
    public TableViewModel() {
        mCards = new MutableLiveData<>();
    }

    /**
     * Returns live data object.
     * @return
     */
    public LiveData<Vector<CardModel>> getCards() {
        return mCards;
    }

    /**
     * Assigns data encapsulated by live data object.
     * @param a_Hand
     */
    public void setCards(Vector<CardModel> a_Hand) {
        mCards.setValue(a_Hand);
    }
}
