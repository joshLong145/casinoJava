package com.casino.josh.casino_java.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.casino.josh.casino_java.Models.BuildModel;

import java.util.Vector;

/**
 * Created by josh on 11/11/18.
 */

public class BuildViewModel extends ViewModel {
    private MutableLiveData<Vector<BuildModel>> mHand;

    /**
     * @summary Constructor for the post View Model
     */
    public BuildViewModel() {
        mHand = new MutableLiveData<>();
    }

    /**
     * Returns a live data object containing our data collection.
     * @return LiveData
     */
    public LiveData<Vector<BuildModel>> getBuilds() {
        return mHand;
    }

    /**
     * Assigns data that is encapsulated within the live data object.
     * @param a_builds
     */
    public void setBuilds(Vector<BuildModel> a_builds) {
        mHand.setValue(a_builds);
    }

}
