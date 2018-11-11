package com.casino.josh.casino_java.Models;

import com.casino.josh.casino_java.Models.CardModel;
import com.casino.josh.casino_java.Models.DeckModel;

import java.util.Vector;

/**
 * Created by josh on 11/5/18.
 */

public class TableModel {
    private int _lastCaptured = 0;
    private DeckModel _deck;
    private Vector<CardModel> _looseCards;
    //Vector<std::sBuild>> _uncapturedBuilds;

    /**
     * Constructor for TableModel
     */
    public TableModel(){
        _deck = new DeckModel();
        _deck.create();
        _looseCards = new Vector<>();

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
