package com.casino.josh.casino_java.Models;

/**
 * Created by josh on 11/1/18.
 */

public class CardModel {

    private char _cardSuit;
    public int _cardValue;
    private boolean _isAce = false;

    /**
     * Constructor for card model.
     * @param suit character
     * @param value integer
     * @param isAce boolean
     */
    public CardModel(final char suit, final int value, boolean isAce){
        _cardSuit = suit;
        _cardValue = value;
        _isAce = isAce;
    }

    /**
     * Returns the value for the card.
     * @return integer
     */
    public int getValue(){return _cardValue; }

    /**
     * Returns the suit of the card.
     * @return character
     */
    public char getSuit(){return _cardSuit; }

    /**
     * Serializes card object to pass data to other modules.
     * @return String
     */
    @Override
    public String toString(){
        String card = "";
        card += _cardSuit;

        switch(_cardValue){
            case 1:
                card += "_a";
                break;
            case 2:
                card += "_2";
                break;
            case 3:
                card += "_3";
                break;
            case 4:
                card += "_4";
                break;
            case 5:
                card += "_5";
                break;
            case 6:
                card += "_6";
                break;
            case 7:
                card += "_7";
                break;
            case 8:
                card += "_8";
                break;
            case 9:
                card += "_9";
                break;
            case 10:
                card += "_x";
                break;
            case 11:
                card += "_j";
                break;
            case 12:
                card += "_q";
                break;
            case 13:
                card += "_k";
                break;
        }

        return card;
    }

    /**
     *
     * @return
     */
    public final String toStringSave(){
       String cardData = "" + _cardSuit + Integer.toString(_cardValue);

        return cardData;
    }
}
