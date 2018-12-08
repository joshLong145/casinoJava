package com.casino.josh.casino_java.Models;

import android.os.Build;

import java.util.Vector;

/**
 * Created by josh on 11/11/18.
 */

public class BuildModel {
    private boolean mIsCaptured;
    private int mCaptureValue;
    private Vector<Vector<CardModel>> mCards;
    private String mOwner;

    /**
     * Default constructor
     */
    public BuildModel(){
        mCards = new Vector<>();
    }

    /**
     * Copy constructor.
     * @param build
     */
    public BuildModel(BuildModel build){
        mCards = new Vector<>();
        for(Vector<CardModel> sets : build.getBuild()) {
            mCards.add((Vector<CardModel>) sets.clone());
        }

        mOwner = build.getBuildOwner();

        mCaptureValue = build.getCaptureValue();
    }

    /**
     * Sets the owner of the build.
     * @param owner
     */
    public void setBuildOwner(final String owner){mOwner = owner; }


    /**
     * Get the owner of the build.
     * @return String
     */
    public String getBuildOwner(){ return mOwner; }

    /**
     *  Returns the current capture value for the build.
     * @return
     */
    public int getCaptureValue(){return mCaptureValue; }

    /**
     * Set the capture value for the build.
     * @param value integer
     */
    public void setCaptureValue(final int value){mCaptureValue = value; }

    /**
     * Add a collection of cards to the build.
     * @param build
     */
    public void addBuildToBuild(final Vector<CardModel> build){mCards.add(build); }


    /**
     * Return the container of builds
     * @return
     */
    public final Vector<Vector<CardModel>> getBuild(){ return mCards; }

    /**
     * Serialize data.
     * @return String
     */
    public final String toString(){
        String data = "[ ";
        for(Vector<CardModel> build : mCards){
            data += "[";
            for(CardModel card : build){
                data += " " + card.toStringSave();
            }
            data += "]";
        }
        data += " ]";
        return data;
    }
}
