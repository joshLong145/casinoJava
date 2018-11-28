package com.casino.josh.casino_java.Models;

import android.app.ActionBar;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import com.casino.josh.casino_java.Models.BasePlayerModel;
import com.casino.josh.casino_java.Models.TableModel;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

/**
 * Created by josh on 11/7/18.
 */

@SuppressWarnings("ALL")
public class ComputerPlayerModel extends BasePlayerModel {
    // private member variables for easy AI.
    private Map<Pair<CardModel, CardModel>, Vector<CardModel>> mBuildMap = new LinkedHashMap<>();
    private Map<String, Pair<Integer, Vector<String>>> mMultiBuildMap = new LinkedHashMap<>();
    private Map<String, Vector<CardModel>> mSetMap = new LinkedHashMap<>();

    /**
     * Default constructor for the Computer PLayer Class.
     */
    public ComputerPlayerModel(){
        mName = "Computer";
    }

    /**
     *
     * @param score
     * @param hand
     * @param pile
     */
    public ComputerPlayerModel(int score, Vector<CardModel> hand, Vector<CardModel> pile) {
        _points = score;
        _hand = hand;
        _pile = pile;
        mName = "Computer";
    }

    /**
     * Implementation of abstract class within BasePlayerModel.
     * @param table TableModel
     * @param option TurnOptions
     * @return boolean
     */
    @Override
    public boolean makeMove(TableModel table, TurnOptions option) {


        Vector<Integer> captureWeights = new Vector<>();
        int bestCaptureWeight = -1;
        int bestSetCapture = -1;
        int bestSetIndex= -1;

        Pair<Integer, Integer> captureWeight = assesCaptureWeights(table, _hand, captureWeights, 0, 0 );
        bestCaptureWeight = captureWeight.first;
        bestSetIndex = captureWeight.second;


        int bestSingleBuildWeight = -1;
        Pair<Pair<CardModel, CardModel>, Integer> singleBuildWeight = assessSingleBuildWeights(table, _hand);
        bestSingleBuildWeight = singleBuildWeight.second;

        if(bestSingleBuildWeight >= bestCaptureWeight && bestSingleBuildWeight != -1){
            table.createBuild(mBuildMap.get(singleBuildWeight.first), singleBuildWeight.first.second, _hand, mName);
            _hand.remove(singleBuildWeight.first.second);

        } else if(0 < bestCaptureWeight){
                CardModel card = _hand.get(captureWeight.second);
                Vector<CardModel> capturedCards = mSetMap.get(card.toStringSave());
                Vector<CardModel> capturedBuildCards = table.captureBuilds(card);
                table.getLooseCards().removeAll(capturedCards);
                _hand.remove(card);
                if(capturedBuildCards != null) {
                    table.removeBuilds(card);
                    _pile.addAll(capturedBuildCards);
                }
                _pile.addAll(capturedCards);
                _pile.add(card);
        }else {

            table.getLooseCards().add(getHand().get(0));

            TurnLogModel.AddToLog("Computer trailed the card: " + getHand().get(0).toString());

            getHand().remove(0);
        }


        return true;
    }


    private Pair<Pair<CardModel, CardModel>, Integer> assessSingleBuildWeights(TableModel table, Vector<CardModel> hand){
        Vector<Pair<CardModel, CardModel>> cardCombos = new Vector<>();
        if(hand.size() > 1){
            for(CardModel buildCard : hand){
                for(CardModel captureCard : hand){
                    if(buildCard != captureCard && buildCard.getValue() < captureCard.getValue()) {
                        if(!table.isCaptureCard(hand, buildCard, mName)){
                            Vector<Vector<CardModel>> builds = table.checkBuildCreation(captureCard.getValue(), buildCard.getValue());

                            if(builds.size() > 0){
                                Pair<CardModel, CardModel> pair = new Pair<>(captureCard, buildCard);
                                mBuildMap.put(new Pair<>(captureCard, buildCard), builds.get(0));
                                cardCombos.add(pair);
                            }
                        }
                    }
                }
            }

            int bestBuildWeight = -1;
            Pair<CardModel, CardModel> cardCombo = null; // no need to allocate on the heap, will perform a null check before unboxing data.
            for(Pair<CardModel, CardModel> combo : cardCombos){
                int size = mBuildMap.get(combo).size();
                if(bestBuildWeight < size && size > 0){
                    bestBuildWeight = size;
                    cardCombo = combo;
                }
            }

            if(bestBuildWeight > -1)
                bestBuildWeight ++;

            return new Pair<>(cardCombo, bestBuildWeight);
        }

        // if the hand size is 1 then we canot back a build. we return a null card combo with a weight of -1 signifying it is invalid.
        return new Pair<>(null, -1);
    }

    /**
     * Finds all combinations of valid sets and assesses their capture value based on predefined heuristic values.
     * @param table
     * @param hand
     * @param captureWeights
     * @param bestCaptureWeight
     * @param bestSetIndex
     * @return Pair<Integer, Integer>
     */
    private Pair<Integer, Integer> assesCaptureWeights(TableModel table, final Vector<CardModel> hand, Vector<Integer> captureWeights, int bestCaptureWeight, int bestSetIndex){

        for(CardModel card : hand){
            int weight = checkSets(table, card);
            weight += checkBuilds(table, card);
            captureWeights.add(weight);
        }

        for(int i = 0; i < captureWeights.size(); i++){
            if(captureWeights.get(i) > bestCaptureWeight) {
                bestCaptureWeight = captureWeights.get(i);
                bestSetIndex = i;
            }
        }

        if(bestCaptureWeight == 0)
            bestCaptureWeight = -1;

        return new Pair<>(bestCaptureWeight, bestSetIndex);
    }

    /**
     * Check sets generated from each card and asses their heuristic weights.
     * @param table
     * @param selectedCard
     * @return Integer.
     */
    private int checkSets(final TableModel table, CardModel selectedCard){
        if(!table.isCaptureCard(getHand(), selectedCard, mName)) {
            Vector<Vector<CardModel>> sets = super.setCaptureCombinations(table, selectedCard.getValue());
            sets.add(super.buildCapture(table, selectedCard));

            if(sets.size() <= 0)
                return 0;

            int size = sets.get(0).size();

            mSetMap.put(selectedCard.toStringSave(), sets.get(0));

            return size;
        }

        return 0;
    }

    /**
     * Check the heuristics from capturing cards within a build.
     * @param table
     * @param card
     * @return Integer.
     */
    public int checkBuilds(TableModel table, CardModel card) {
        return table.checkBuilds(card);
    }

    /**
     * Get the name of the Computer player (Computer).
     * @return String
     */
    public String getName(){return mName;}
}
