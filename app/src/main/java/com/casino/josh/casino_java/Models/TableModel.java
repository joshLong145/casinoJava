package com.casino.josh.casino_java.Models;

import android.os.Build;

import com.casino.josh.casino_java.Models.CardModel;
import com.casino.josh.casino_java.Models.DeckModel;
import com.casino.josh.casino_java.activites.GameActivity;

import java.util.Vector;

/**
 * Created by josh on 11/5/18.
 */

public class TableModel {
    private int _lastCaptured = 0;
    private DeckModel _deck;
    private Vector<CardModel> _looseCards;
    Vector<BuildModel> mBuilds;

    /**
     * Constructor for TableModel
     */
    public TableModel(){
        _deck = new DeckModel();
        _deck.create();
        _looseCards = new Vector<>();
        mBuilds = new Vector<>();
    }

    /**
     * Constructor to load in a seeded deck into the tournament.
     * @param deck
     */
    public TableModel(DeckModel deck){
        _deck = deck;
    }

    /**
     * Returns collection of loose cards
     * @return
     */
    public final Vector<CardModel> getLooseCards(){return _looseCards; }


    /**
     * Returns a collection of build model objects.
     * @return Vector<BuildModel>
     */
    public final Vector<BuildModel> getBuilds(){return mBuilds; }

    /**
     * Checks if a card can be trailed from a players hand.
     * @return boolean
     */
    public boolean canTrailCard(CardModel playedCard){
        for(CardModel card: _looseCards){
            if(card.getValue() == playedCard.getValue())
                return false;
        }
        return true;
    }

    /**
     * Checks if the player has more than one card with the same sum to capture with.
     * @param hand
     * @return boolean
     */
    public boolean isCaptureCard(Vector<CardModel> hand, CardModel selectedCard, final String playerName){
        for (BuildModel build : mBuilds) {
            if(selectedCard.getValue() == build.getCaptureValue() && playerName == build.getBuildOwner()) {
                for(CardModel card : hand){
                        if (card.getValue() == build.getCaptureValue() && playerName == build.getBuildOwner()) {
                            return false;
                        }
                    }

                }
        }



        return true;
    }

    /**
     * Returns a vector of CardModels that are the same value as the card being played.
     * @param playedCard CardModel
     * @return Vector<CardModel>
     */
    public Vector<CardModel> captureLooseCardsOnTable(CardModel playedCard){
        Vector<CardModel> capturedCards = new Vector<>();
        for(CardModel card : _looseCards){
            if(card.getValue() == playedCard.getValue()){
               capturedCards.add(card);
            }
        }

        return capturedCards;
    }


    public Vector<CardModel> captureBuilds(Vector<BuildModel> builds, CardModel captureCard){
        for(BuildModel build : builds){
            if(build.getCaptureValue() != captureCard.getValue())
                return new Vector<>();
        }

        Vector<CardModel> capturedBuildCards = new Vector<>();

        for(BuildModel build : builds){
            for(Vector<CardModel> set : build.getBuild()){
                for(CardModel card : set)
                    capturedBuildCards.add(card);
            }

            mBuilds.remove(build);
        }

        return capturedBuildCards;
    }

    /**
     * Creates a new build object if the specified rules for build creation are met. if not false is returned and the turn is not completed.
     * @param looseCards
     * @param chosenCard
     * @param captureCard
     * @return
     */
    public boolean createBuild(Vector<CardModel> looseCards, final CardModel chosenCard, final CardModel captureCard, final String owner){
        int sum = 0;

        for(CardModel card : looseCards){
            sum += card.getValue();
        }

        if(sum + chosenCard.getValue() == captureCard.getValue()){
            getLooseCards().removeAll(looseCards);
            BuildModel build = new BuildModel();
            looseCards.add(chosenCard);
            build.addBuildToBuild(looseCards);
            build.setBuildOwner(owner);
            build.setCaptureValue(captureCard.getValue());
            mBuilds.add(build);

            return true;
        }

        return false;
    }



    /**
     *  Add CardModel to loose card collection.
     * @param card
     */
    public void addLooseCard(CardModel card){ _looseCards.add(card); }

    /**
     * Returns a DeckModel object.
     * @return DeckModel.
     */
    public DeckModel getDeck(){return _deck; }

}
