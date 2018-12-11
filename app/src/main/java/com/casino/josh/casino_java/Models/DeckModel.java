package com.casino.josh.casino_java.Models;

import com.casino.josh.casino_java.Models.CardModel;

import java.util.Vector;
import java.util.Collections;

/**
 * Created by josh on 11/1/18.
 */

public class DeckModel {
    private Vector<CardModel> mCards = new Vector<>();
    private int _currentCardIndex = 51;

    /**
     * Default constructor.
     */
    public DeckModel(){}

    /**
     * Constructor to take a vector of cards to construct a deck.
     */
    public DeckModel(Vector<CardModel> cards){
        mCards = cards;
        _currentCardIndex = mCards.size() - 1;
    }

    /**
     * Creates the cards within the deck.
     */
    void create(){
        final int ACE = 1;
        final int MAXSUIT = 4;
        final int MAXVALUE = 13;

        char[] suits = {'h', 's', 'c', 'd'};

        for(int suit = 0; suit < MAXSUIT; suit++){
            for(int value = 1; value <= MAXVALUE; value++ ){
                if(value == ACE){
                    CardModel ace = new CardModel(suits[suit], value, true);
                    mCards.add(ace);
                }else{
                    CardModel card = new CardModel(suits[suit], value, false);
                    mCards.add(card);
                }
            }
        }

        // once the deck has been created, shuffle it.

        shuffle();
    }

    /**
     * Ranomizes card collection to simulate shuffling of the cards.
     */
    public void shuffle(){
        Collections.shuffle(mCards);
    }

    /**
     * removes 4 cards from a collection and distributes to a collection which is returned.
     * @return
     */
    public Vector<CardModel> dealHand(){
        final int HANDSIZE = 4;

        Vector<CardModel> dealtCards = new Vector<>();
        if(_currentCardIndex > 0 && _currentCardIndex < mCards.size()){
            /*
            for(int i = 0; i < HANDSIZE; i ++){
                CardModel card = mCards.lastElement();
                dealtCards.add(card);
                mCards.removeElementAt(_currentCardIndex);
                _currentCardIndex--;
            }
            */

            for(int i = 0; i < 4; i++){
                dealtCards.add(mCards.get(i));
                _currentCardIndex--;
            }

            mCards.removeAll(dealtCards);
        }

        return dealtCards;
    }

    /**
     * Returns a vector of CardModel objects
     * @return Vector<CardModel>
     */
    public final Vector<CardModel> getCards(){ return mCards; }

    /**
     * Return the size of the CardModel collection.
     * @return
     */
    public final int getSize(){return mCards.size();}

    /**
     * Serialize data to string.
     * @return
     */
    public final String toString(){
        StringBuilder deckString = new StringBuilder("Deck:");

        if(mCards.size() <= 0){
            deckString.append(" none");
        }else {
            for (CardModel card : mCards) {
                deckString.append(" ").append(card.toStringSave());
            }
        }

        return deckString.toString();
    }
}
