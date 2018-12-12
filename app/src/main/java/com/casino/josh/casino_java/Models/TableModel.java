package com.casino.josh.casino_java.Models;

import android.os.Build;
import android.support.v4.util.Pair;

import com.casino.josh.casino_java.Models.CardModel;
import com.casino.josh.casino_java.Models.DeckModel;
import com.casino.josh.casino_java.activites.GameActivity;

import java.util.Comparator;
import java.util.Objects;
import java.util.Vector;
import java.lang.Math;
import java.util.regex.Pattern;

/**
 * Created by josh on 11/5/18.
 */

public class TableModel {

    // Private member variables.
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
     * Constructor to load in deserialize data.
     * @param deck
     * @param looseCards
     * @param builds
     */
    public TableModel(DeckModel deck, Vector<CardModel> looseCards, Vector<BuildModel> builds){
        _deck = deck;
        _looseCards = looseCards;
        mBuilds = builds;
    }

    /**
     * Copy Constructor for TableModel.
     * @param table
     */
    public TableModel(TableModel table){
        this._looseCards = (Vector<CardModel>) table.getLooseCards().clone();
        this._deck = table.getDeck();

        mBuilds = new Vector<>();
        for(BuildModel build : table.getBuilds())
            mBuilds.add(new BuildModel(build));
    }

    /**
     * Constructor to load create a new round once one is deemed to be over.
     * Passing previous vector objects to update the reference count to them so they are not cleaned
     * up by the JVM and thus can still be observed by live data models.
     * @param looseCards
     * @param builds
     */
    public TableModel(Vector<CardModel> looseCards, Vector<BuildModel> builds){
        _deck = new DeckModel();
        _deck.create(); // initialize the deck with cards.
        _looseCards = looseCards;
        mBuilds = builds;
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
    public boolean canTrailCard(CardModel playedCard, String name){

        // Check if any loose cards match the value of the chosen card.
        for(CardModel card: _looseCards){
            if(card.getValue() == playedCard.getValue())
                return false;
        }

        // Check if there are any build owners.
        for(BuildModel build : mBuilds){
            if(Objects.equals(build.getBuildOwner(), name))
                return false;
        }
        return true;
    }

    /**
     * Returns a boolean indicating if there are loose cards that can be captured.
     * @param selectedCard
     * @return
     */
    public boolean checkLooseCards(CardModel selectedCard){
        for(CardModel looseCard : _looseCards){
            if(looseCard.getValue() == selectedCard.getValue())
                return true;
        }

        return false;
    }

    /**
     * Returns references to cards that
     * have the same value as a selected card.
     * @param captureValue
     * @return Vector<CardModel>
     */
    public Vector<CardModel> captureLooseCards(final int captureValue){
        Vector<CardModel> capturedCards = new Vector<>();
        for(CardModel card : _looseCards){
            if(card.getValue() == captureValue)
                capturedCards.add(card);
        }

        return capturedCards;
    }

    /**
     * Checks if the player has more than one card with the same sum to capture with.
     * @param hand
     * @return boolean
     */
    public boolean isCaptureCard(Vector<CardModel> hand, CardModel selectedCard, final String playerName){
        if(mBuilds.size() <= 0)
            return false;

        boolean matchesBuild = false;

        for(BuildModel build : mBuilds){
            if(selectedCard.getValue() == build.getCaptureValue() && Objects.equals(playerName, build.getBuildOwner()))
                matchesBuild = true;
        }

        if(!matchesBuild)
            return false;

        for (BuildModel build : mBuilds) {
            if(selectedCard.getValue() == build.getCaptureValue()
                                       && Objects.equals(playerName, build.getBuildOwner())) {
                for(CardModel card : hand){
                        if (card.getValue() == build.getCaptureValue()
                                && Objects.equals(playerName, build.getBuildOwner())
                                && card != selectedCard) {
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
     * @param selectedLooseCards Vector<CardModel>
     * @return Vector<CardModel>
     */
    public Vector<CardModel> captureLooseCards(CardModel playedCard, Vector<CardModel> selectedLooseCards){
        int sum = 0;

        for(CardModel card : selectedLooseCards){
            sum += card.getValue();
        }

        if(sum % playedCard.getValue() == 0){
            for(CardModel card : _looseCards){
                if(card.getValue() == playedCard.getValue()){
                    if(!selectedLooseCards.contains(card)){
                        return new Vector<>();

                    }
                }
            }

            // remove all cards from loose cards.
            _looseCards.removeAll(selectedLooseCards);

            // return back the collection
            return selectedLooseCards;
        }

        return new Vector<>();
    }


    /**
     * Capture selected builds that are on the table.
     * @param builds
     * @param captureCard
     * @return
     */
    public Vector<CardModel> captureBuilds(Vector<BuildModel> builds, CardModel captureCard){
        if(builds != null) {
            for (BuildModel build : builds) {
                if (build.getCaptureValue() != captureCard.getValue())
                    return new Vector<>();
            }

            Vector<CardModel> capturedBuildCards = new Vector<>();

            for (BuildModel build : builds) {
                for (Vector<CardModel> set : build.getBuild()) {
                    for (CardModel card : set)
                        capturedBuildCards.add(card);
                }

                mBuilds.remove(build);
            }

            return capturedBuildCards;
        }

        return new Vector<>();
    }

    /**
     * Capture cards in builds that are eligble for capturing.
     * @param selectedCard
     * @return
     */
    public Vector<CardModel> captureBuilds(CardModel selectedCard){
        Vector<CardModel> capturedCards = new Vector<>();
        for(int i = mBuilds.size() -1; i >= 0; i--){
            // if the capture value is equal to the card value, capture
            // all cards in the build with that card.
            if(mBuilds.get(i).getCaptureValue() == selectedCard.getValue()){
                for(Vector<CardModel> cardSets : mBuilds.get(i).getBuild()){
                    for(CardModel card : cardSets){
                        capturedCards.add(card);
                    }
                }
            }
        }

        return capturedCards;
    }

    /**
     * Remove builds from collection with the same capture value as the selected card value.
     * @param selectedCard
     */
    public void removeBuilds(CardModel selectedCard){
        for(int i = mBuilds.size() - 1; i >= 0; i--){
            if(mBuilds.get(i).getCaptureValue() == selectedCard.getValue())
                mBuilds.remove(mBuilds.get(i));
        }
    }

    /**
     * Creates a new build object if the specified rules for build creation are met.
     * If not false is returned and the turn is not completed.
     * @param looseCards
     * @param chosenCard
     * @param hand
     * @return
     */
    public boolean createBuild(Vector<CardModel> looseCards, final CardModel chosenCard, final Vector<CardModel> hand, final String owner){
        int sum = 0;

        // If the loose card container is null, then we return false immediately.
        if(looseCards.size() <= 0)
            return false;

        for(CardModel card : looseCards){
            sum += card.getValue();
        }

        for(CardModel card : hand) {
            if (sum + chosenCard.getValue() == card.getValue() && chosenCard != card) {
                getLooseCards().removeAll(looseCards);
                BuildModel build = new BuildModel();
                looseCards.add(chosenCard);
                build.addBuildToBuild(looseCards);
                build.setBuildOwner(owner);
                build.setCaptureValue(card.getValue());
                mBuilds.add(build);

                return true;
            }
        }

        return false;
    }

    /**
     * Helper function to create multi build from previously created builds for both AI and human.
     * @param build
     * @param selectedLooseCards
     * @param chosenCard
     * @param hand
     * @return boolean
     */
    public boolean createMultiBuild(BuildModel build, final Vector<CardModel> selectedLooseCards,
                                    final CardModel chosenCard, final Vector<CardModel> hand,
                                    final String name){
        int sum = 0;
        for(CardModel card : selectedLooseCards){
            sum += card.getValue();
        }

        sum += chosenCard.getValue();

        if(sum == build.getCaptureValue()){
            for(CardModel card : hand){
                if(card.getValue() == build.getCaptureValue()){
                    selectedLooseCards.add(chosenCard);
                    build.addBuildToBuild(selectedLooseCards);
                    build.setBuildOwner(name);
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Functionality for increasing an existing build for both AI and computer players.
     * @param build
     * @param chosenCard
     * @param hand
     * @return
     */
    boolean increaseBuild(BuildModel build, final CardModel chosenCard, final Vector<CardModel> hand,
                          final String name){

        if(build.getBuildOwner().equals(name))
            return false;

        int sum = build.getCaptureValue();

        for(CardModel card : hand){
            if(card != chosenCard && card.getValue() == sum + chosenCard.getValue()){
                build.getBuild().get(0).add(chosenCard);
                build.setBuildOwner(name);
                build.setCaptureValue(sum + chosenCard.getValue());
                return true;
            }

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
    public DeckModel getDeck(){ return _deck; }

    /**
     * Override of the base object toString() function for serialization to file for game state
     * saving.
     * @return String
     */
    public final String toString(){
        StringBuilder tableString = new StringBuilder("Table: ");
        for(BuildModel build : mBuilds)
            tableString.append(build.toString() + " ");

        for(CardModel card : _looseCards)
           tableString.append(" " + card.toStringSave());

        tableString.append(System.getProperty("line.separator"));
        tableString.append(System.getProperty("line.separator"));

        tableString.append("Build Owners: ");

        if(mBuilds.size() <= 0) {
            tableString.append("none");
        }else {
            for (BuildModel build : mBuilds) {
                tableString.append(build.toString() + " ");
                tableString.append(build.getBuildOwner());
            }
        }

        tableString.append(System.getProperty("line.separator"));
        tableString.append(System.getProperty("line.separator"));

        tableString.append(_deck.toString());
        tableString.append(System.getProperty("line.separator"));


        return tableString.toString();
    }


    /**
     * Generate all possible combinations of loose cards. in relation to the value of the selected card.
     * Using n power set algorithm to generate all possible combinations.
     * After combination is created. Check to see if the sum of the cards is equal to the capture value.
     * @param captureValue
     * @return
     */
    public Vector<Vector<CardModel>> setCapture(final int captureValue){
       Vector<Vector<CardModel>> cardSets = new Vector<>();
       Vector<Vector<CardModel>> capturableSets = new Vector<>();

       for(int i = 0; i < (int) Math.pow(2, _looseCards.size()); i++){
           Vector<CardModel> cards = new Vector<>();

           for(int j = 0; j < _looseCards.size(); j++){
               // Check if jth bit in the i is set. If the bit
               // is set, we consider jth element from set
               if((i & (1 << j)) != 0){
                    cards.add(_looseCards.get(j));
               }

               // Check if the combination is already present.
               if(!cardSets.contains(cards)) {
                   cardSets.add(cards);
               }
           }
       }

       for(Vector<CardModel> cardSet : cardSets){
           int sum = 0;

           for(CardModel card : cardSet){
               int value = card.getValue();
               if(value == captureValue)
                   break;

               sum += value;


           }

           if(sum == captureValue)
               capturableSets.add(cardSet);
       }
        // Sort data based on length of the array.
       capturableSets.sort((o1, o2) -> {
           if(o1.size() > o2.size()){
               return 1;
           }else if(o1.size() < o2.size()){
               return -1;
           }

           return 0;
       });

        return capturableSets;
    }

    /**
     * Create all card combinations and see if a build can be made from any of them.
     * @param captureCardValue
     * @param selectedCardValue
     * @return  Vector
     */
    public Vector<Vector<CardModel>> checkBuildCreation(final int captureCardValue, final int selectedCardValue){
        Vector<Vector<CardModel>> cardSets = new Vector<>();
        Vector<Vector<CardModel>> buildSets = new Vector<>();
        Vector<Integer> buildSums = new Vector<>();


        for(BuildModel build : mBuilds)
            buildSums.add(build.getCaptureValue());

        for(int i = 0; i < (int) Math.pow(2, _looseCards.size()); i++){
            Vector<CardModel> cards = new Vector<>();

            for(int j = 0; j < _looseCards.size(); j++){
                // Check if jth bit in the i is set. If the bit
                // is set, we consider jth element from set
                if((i & (1 << j)) != 0){
                    cards.add(_looseCards.get(j));
                }

                // Check if the combination is already present.
                if(!cardSets.contains(cards)) {
                    cardSets.add(cards);
                }
            }
        }

        // check if cardSet is a valid build.
        for(Vector<CardModel> cardSet : cardSets) {
            int sum = 0;

            for (CardModel card : cardSet) {
                int value = card.getValue();

                if (captureCardValue == value)
                    break;
                else
                    sum += value;
            }
            sum += selectedCardValue;

            // If the cardSet is valid, then add it to the list of valid builds.
            if (sum == captureCardValue && !buildSums.contains(sum))
                buildSets.add(cardSet);
        }

        // Sort data based on length of the array.

        buildSets.sort((o1, o2) -> (Integer.valueOf(o1.size()).compareTo(o2.size())));

        return buildSets;
    }

    /**
     * Check al card combinations and see if a multibuild can be made from any of them
     * @param captureCardValue
     * @param selectedCardValue
     * @return
     */
    public Vector<Pair<Integer, Vector<CardModel>>> checkMultiBuildCreation(final int captureCardValue, final int selectedCardValue){
        Vector<Vector<CardModel>> cardSets = new Vector<>();
        Vector<Pair<Integer, Vector<CardModel>>> buildSets = new Vector<>();

        for(int i = 0; i < (int) Math.pow(2, _looseCards.size()); i++){
            Vector<CardModel> cards = new Vector<>();

            for(int j = 0; j < _looseCards.size(); j++){
                // Check if jth bit in the i is set. If the bit
                // is set, we consider jth element from set
                if((i & (1 << j)) != 0){
                    cards.add(_looseCards.get(j));
                }

                // Check if the combination is already present.
                if(!cardSets.contains(cards)) {
                    cardSets.add(cards);
                }
            }
        }

        // check if cardSet is a valid build.
        for(Vector<CardModel> cardSet : cardSets) {
            int sum = 0;

            for (CardModel card : cardSet) {
                int value = card.getValue();

                sum += value;
            }

            // Check if any build has the same capture value as one of the generated card sets
            // along with the selected card.
            for(int i = 0; i < mBuilds.size(); i++){
                if(sum + captureCardValue == selectedCardValue
                        && selectedCardValue == mBuilds.get(i).getCaptureValue()){
                    buildSets.add(new Pair<>(i, cardSet));
                }
            }
        }

        // Sort data based on length of the array.
        buildSets.sort((o1, o2) -> (Integer.valueOf(o1.second.size()).compareTo(o2.second.size())));

        return buildSets;
    }


    /**
     * Check if builds can be captured this turn.
     * @param selectedCard
     * @return Integer.
     */
    public int checkBuilds(CardModel selectedCard){
        int weight = 0;
        for(BuildModel build : mBuilds) {
            if (build.getCaptureValue() == selectedCard.getValue()) {
                for (Vector<CardModel> cardSets : build.getBuild()) {
                    for (CardModel card : cardSets) {
                        weight += 1;
                    }
                }
            }
        }

        return weight;
    }
}
