package com.casino.josh.casino_java.Models;

import java.util.Vector;

/**
 * Created by josh on 11/10/18.
 */

public class TurnLogModel {

    // private member variable.
    private static Vector<String> mLog = new Vector<>();

    /**
     * returns a Vector of strings containing turn information.
     * @return
     */
    public static final Vector<String> getLog(){return mLog; }


    /**
     * Add data to the Log.
     * @param turnData String
     */
    public static void AddToLog(final String turnData){
        mLog.add(turnData);
    }

}
