package com.casino.josh.casino_java.Models;

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
     * Serialize data.
     * @return String
     */
    public final String toString(){
        String data = "";
        for(Vector<CardModel> build : mCards){
            data += "[";
            for(CardModel card : build){
                data += " " + card.toString();
            }
            data += "]";
        }
        return data;
    }
}
