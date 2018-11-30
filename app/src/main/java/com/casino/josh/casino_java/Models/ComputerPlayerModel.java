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
        // containers for mapping card data from the hand to correpsonding card data from the table.
        Map<Pair<CardModel, CardModel>, Vector<CardModel>> buildMap = new LinkedHashMap<>();
        Map<Pair<CardModel, CardModel>, Pair<Integer, Vector<CardModel>>> multiBuildMap = new LinkedHashMap<>();
        Map<String, Vector<CardModel>> setMap = new LinkedHashMap<>();

        Vector<Integer> captureWeights = new Vector<>();
        int bestCaptureWeight = -1;
        int bestSetCapture = -1;
        int bestSetIndex= -1;

        Pair<Integer, Integer> captureWeight = assesCaptureWeights(table, _hand, captureWeights,
                0, 0, setMap);

        bestCaptureWeight = captureWeight.first;
        bestSetIndex = captureWeight.second;


        int bestSingleBuildWeight = -1;
        Pair<Pair<CardModel, CardModel>, Integer> singleBuildWeight = assessSingleBuildWeights(table, _hand, buildMap);
        bestSingleBuildWeight = singleBuildWeight.second;

        int bestMultiBuildWeight = -1;
        Pair<Pair<CardModel, CardModel>, Integer> multiBuildWeight = assesMultiBuildWeights(table, _hand, multiBuildMap);
        bestMultiBuildWeight = multiBuildWeight.second;


        // Check if the weight of making a multibuild is more than making a single build.
        // order of move priority ( multi -> single -> capture)
        if(bestMultiBuildWeight >= bestSingleBuildWeight && bestMultiBuildWeight != -1){
               BuildModel selectedBuild;
               selectedBuild = table.getBuilds().get(multiBuildMap.get(multiBuildWeight.first).first);
               Vector<CardModel> buildCards = multiBuildMap.get(multiBuildWeight.first).second;
               CardModel selectedCard = multiBuildWeight.first.first;
               table.createMultiBuild(selectedBuild, buildCards, selectedCard, _hand);
               _hand.remove(selectedCard);
        }
        // Check what weight is the highest, with priority on creating builds, and multibuilds.
        // Reasoning is that this will allow for the largest capturing of cards in the future.
        else if(bestSingleBuildWeight >= bestCaptureWeight && bestSingleBuildWeight != -1){
            table.createBuild(buildMap.get(singleBuildWeight.first), singleBuildWeight.first.second, _hand, mName);
            _hand.remove(singleBuildWeight.first.second);

            TurnLogModel.addBuildMoveToLog(buildMap.get(singleBuildWeight.first),
                    singleBuildWeight.first.first, singleBuildWeight.first.second, mName);

        // If building is not an option, check if capturing is possible.
        } else if(0 < bestCaptureWeight){
                CardModel card = _hand.get(captureWeight.second);
                Vector<CardModel> capturedCards = setMap.get(card.toStringSave());
                Vector<CardModel> capturedBuildCards = table.captureBuilds(card);

                // If capture cards are not null ( meaning more than a build was captured).
                // remove it from the table.
                if(capturedCards != null) {
                    table.getLooseCards().removeAll(capturedCards);
                    TurnLogModel.addCaptureMoveToLog(capturedCards, card, mName);
                }

                if(capturedBuildCards != null) {
                    table.removeBuilds(card);
                    _pile.addAll(capturedBuildCards);
                    TurnLogModel.addCaptureMoveToLog(capturedBuildCards, card, mName);
                }

                _pile.add(card);
                _hand.remove(card);

        // If other options are not possible ( weights are -1). then trail the card.
        }else {

            table.getLooseCards().add(getHand().get(0));
            TurnLogModel.addTrailMoveToLog(getHand().get(0), mName);
            getHand().remove(0);
        }

        // return true indicating turn executed successfully.
        return true;
    }

    /**
     * Checks if making a single build is possible, and stores all possible build mappings
     * in a map of capture, selected cards to loose cards.
     * @param table
     * @param hand
     * @param buildMap
     * @return Pair<Pair<CardModel, CardModel>, Integer>
     */
    private Pair<Pair<CardModel, CardModel>, Integer> assessSingleBuildWeights(TableModel table, Vector<CardModel> hand,
                                                                               Map<Pair<CardModel, CardModel>, Vector<CardModel>> buildMap){

        Vector<Pair<CardModel, CardModel>> cardCombos = new Vector<>();
        if(hand.size() > 1){
            for(CardModel buildCard : hand){
                for(CardModel captureCard : hand){
                    if(buildCard != captureCard && buildCard.getValue() < captureCard.getValue()){
                        if(!table.isCaptureCard(hand, buildCard, mName)){
                            Vector<Vector<CardModel>> builds = table.checkBuildCreation(captureCard.getValue(), buildCard.getValue());

                            if(builds.size() > 0){
                                Pair<CardModel, CardModel> pair = new Pair<>(captureCard, buildCard);
                                buildMap.put(new Pair<>(captureCard, buildCard), builds.get(0));
                                cardCombos.add(pair);
                            }
                        }
                    }
                }
            }

            int bestBuildWeight = -1;
            Pair<CardModel, CardModel> cardCombo = null; // no need to allocate on the heap, will perform a null check before unboxing data.
            for(Pair<CardModel, CardModel> combo : cardCombos){
                int size = buildMap.get(combo).size();
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
     * Asses if making a multi build is viable for the AI. creates pairs of cards in hand, with
     * a pair of the build index with the set of cards to add to said build. a weight is assigned to
     * each multibuild inorder to check if it is the best option this turn.
     * @param table
     * @param hand
     * @return Pair<CardModel, CardModel>
     */
    private Pair<Pair<CardModel, CardModel>, Integer> assesMultiBuildWeights(final TableModel table,
                                                              final Vector<CardModel> hand,
                                                              Map<Pair<CardModel, CardModel>, Pair<Integer, Vector<CardModel>>> multiBuildMap){
        Vector<Pair<CardModel, CardModel>> cardCombos = new Vector<>();
        for(CardModel captureCard : hand){
            for(CardModel selectedCard : hand){
                if(selectedCard != captureCard && selectedCard.getValue() <= captureCard.getValue()) {
                    if(!table.checkLooseCards(selectedCard)){
                        Vector<Pair<Integer, Vector<CardModel>>> multiBuildOptions;
                        multiBuildOptions = table.checkMultiBuildCreation(captureCard.getValue(),
                                                                    selectedCard.getValue());
                        if(multiBuildOptions.size() > 0){
                            Pair<CardModel, CardModel> cardCombo;
                            cardCombo = new Pair<>(selectedCard, captureCard);
                            multiBuildMap.put(cardCombo, multiBuildOptions.get(0));
                            cardCombos.add(cardCombo);
                        }else{
                            multiBuildMap.put(new Pair<>(selectedCard, captureCard),
                                              new Pair<>(null, null));
                        }
                    }
                }
            }
        }

        int bestBuildWeight = -1;
        Pair<Pair<CardModel, CardModel>, Integer> comboWeights = new Pair<>(null, -1);
        // Check what is the best multiBuild Option.
        for(Pair<CardModel, CardModel> combo : cardCombos){
            int weight = multiBuildMap.get(combo).first;
            if(weight > bestBuildWeight){
                bestBuildWeight = multiBuildMap.get(combo).second.size();
                comboWeights = new Pair<>(combo, bestBuildWeight);
            }
        }

        // Add the build that will be made a multibuild to the weight.
        // Since cards will theoretically be captured eventually. (if the AI has it's way).
        if(bestBuildWeight > -1){
            BuildModel build = table.getBuilds().get(multiBuildMap.get(comboWeights.first).first);
            for(Vector<CardModel> buildSets : build.getBuild()){
                for(CardModel card : buildSets)
                    bestBuildWeight += card.getValue();
            }
        }

        return comboWeights;
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
    private Pair<Integer, Integer> assesCaptureWeights(TableModel table, final Vector<CardModel> hand, Vector<Integer> captureWeights,
                                                       int bestCaptureWeight, int bestSetIndex, Map<String, Vector<CardModel>> setMap){

        for(CardModel card : hand){
            int weight = checkSets(table, card, setMap);
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
    public int checkSets(final TableModel table, CardModel selectedCard, Map<String, Vector<CardModel>> setMap){
        if(!table.isCaptureCard(getHand(), selectedCard, mName)) {
            Vector<Vector<CardModel>> sets = super.setCaptureCombinations(table, selectedCard.getValue());
            sets.add(super.buildCapture(table, selectedCard));

            if(sets.size() <= 0)
                return 0;

            int size = sets.get(0).size();

            setMap.put(selectedCard.toStringSave(), sets.get(0));

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
