package com.casino.josh.casino_java.Helpers;

import android.os.Build;

import com.casino.josh.casino_java.Models.BasePlayerModel;
import com.casino.josh.casino_java.Models.BuildModel;
import com.casino.josh.casino_java.Models.CardModel;

import java.util.Objects;
import java.util.Vector;
import java.util.regex.Pattern;

/**
 * Created by josh on 11/18/18.
 */

public class Serialization {
    private String mSaveData = null;

    private int mRoundNumber;

    private int mHumanScore;
    private Vector<CardModel> mHumanHand;
    private Vector<CardModel> mHumanPile;

    private int mComputerScore;
    private Vector<CardModel> mComputerHand;
    private Vector<CardModel> mComputerPile;

    private Vector<BuildModel> mBuilds;
    private Vector<CardModel> mLooseCards;

    private Vector<CardModel> mDeck;

    private Vector<BasePlayerModel> mPlayers;


    /**
     * Constructor for class.
     * @param saveData
     */
    public Serialization(String saveData){
        mSaveData = saveData;
    }

    /**
     * Parses data from string read from a file and constructs java objects from it.
     * @return boolean
     */
    public boolean parseData(){
        if(mSaveData == null)
            return false;

        int roundIndex = mSaveData.indexOf("Round:");
        mRoundNumber = Integer.parseInt("" + mSaveData.charAt(roundIndex + 7));

        //
        int humanScoreIndex = mSaveData.indexOf("Score:");
        mHumanScore = Integer.parseInt("" + mSaveData.charAt(humanScoreIndex + 7));
        int humanHandIndex = mSaveData.indexOf("Hand:");
        String humanHandString = mSaveData.substring(humanHandIndex + 5, mSaveData.indexOf("Pile:"));
        mHumanHand = createCards(tokenizeInput(humanHandString));
        int humanPileIndex = mSaveData.indexOf("Pile:");
        String humanPileString = mSaveData.substring(humanPileIndex + 5, mSaveData.indexOf("Computer:"));
        mHumanPile = createCards(tokenizeInput(humanPileString));

        int computerScoreIndex = mSaveData.lastIndexOf("Score:");
        mComputerScore = Integer.parseInt("" + mSaveData.charAt(computerScoreIndex + 7));
        int computerHandIndex = mSaveData.lastIndexOf("Hand:");
        String computerHandString = mSaveData.substring(computerHandIndex + 5, mSaveData.lastIndexOf("Pile:"));
        mComputerHand = createCards(tokenizeInput(computerHandString));
        int computerPileIndex = mSaveData.lastIndexOf("Pile:");
        String computerPileString = mSaveData.substring(computerPileIndex + 5, mSaveData.indexOf("Table:"));

        int tableIndex = mSaveData.indexOf("Table:");
        String tableString = mSaveData.substring(tableIndex + 7, mSaveData.indexOf("Build Owners:"));

        if(tableString.indexOf('[') != -1) {
            int buildsIndex = tableString.indexOf('[');
            int endOfBuildsIndex = tableString.lastIndexOf(']') + 1;
            String buildString = tableString.substring(buildsIndex, endOfBuildsIndex);
            mBuilds = createBuilds(tokenizeBuilds(buildString));
            String looseCardString = tableString.substring(endOfBuildsIndex + 2);
            mLooseCards = createCards(tokenizeInput(looseCardString));
        }else{
            mLooseCards = createCards(tokenizeInput(tableString));
        }


        int deckIndex = mSaveData.indexOf("Deck:");
        String deckString = mSaveData.substring(deckIndex + 6, mSaveData.indexOf("Next Player:"));
        mDeck = createCards(tokenizeInput(deckString));

        return true;
    }


    /**
     * parses string at spaces to construct an array of string from which CardModel's can be created.
     * @param serialData
     * @return
     */
    private final String[] tokenizeInput(final String serialData){
        String[] data = serialData.split("\\s+");

        return data;
    }

    private final String[] tokenizeBuilds(final String builds){
        // split each build but keep brackets using regex statement: "((?<=%1$s)|(?=%1$s))"
        String[] buildTokens = builds.split(String.format("((?<=%1$s)|(?=%1$s))", Pattern.quote( " ] [ ")));

        return buildTokens;
    }

    /**
     * Takes tokenized data and creates CardModel objects from the data set.
     * @param tokenData
     * @return
     */
    private final Vector<CardModel> createCards(final String[] tokenData){
        Vector<CardModel> cards = new Vector<>();

        if(tokenData.length <= 0)
            return new Vector<>();

        for(int i = 0; i < tokenData.length; i++){
            if(!Objects.equals(tokenData[i], "")) {
                char suit = tokenData[i].charAt(0);
                int value = Integer.parseInt(tokenData[i].substring(1));
                if (value != 1)
                    cards.add(new CardModel(suit, value, false));
                else
                    cards.add(new CardModel(suit, value, true));
            }
        }

        return cards;
    }

    /**
     * Creates BuildModel objects from serial data parsed by helper functions.
     * @param buildString String
     * @return Vector<BuildModel>
     */
    private final Vector<BuildModel> createBuilds(final String[] buildString){
        Vector<String[]> buildTokens =  new Vector<>();
        for(String build : buildString){
            String[] cards = build.split("\\[([^]]+)\\]");
            buildTokens.add(cards);
        }

        Vector<BuildModel> builds = new Vector<>();

        for(String[] buildData : buildTokens){
            BuildModel build = new BuildModel();
            for(String cardString : buildData){
                build.addBuildToBuild(createCards(tokenizeInput(cardString)));
            }
            int buildSum = 0;
            for(CardModel card : build.getBuild().get(0)){
                buildSum+= card.getValue();
            }

            builds.add(build);
            build.setCaptureValue(buildSum);
        }

        return builds;
    }
}
