package com.casino.josh.casino_java.Helpers;

import android.support.v4.util.Pair;

import com.casino.josh.casino_java.Models.BuildModel;
import com.casino.josh.casino_java.Models.CardModel;
import com.casino.josh.casino_java.Models.ComputerPlayerModel;
import com.casino.josh.casino_java.Models.TableModel;
import com.casino.josh.casino_java.Models.TurnLogModel;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;

/**
 * GameTreeNode class
 */
public class GameTreeNode {
    private TableModel table;
    private String action;
    private Pair<CardModel, CardModel> handPair;
    private Vector<CardModel> hand;
    private Vector<GameTreeNode> nodes;
    private CardModel card;
    private int weight;


    Map<Pair<CardModel, CardModel>, Vector<CardModel>> buildMap;
    Map<Pair<CardModel, CardModel>, Pair<Integer, Vector<CardModel>>> multiBuildMap;
    Map<String, Vector<CardModel>> setMap;

    /**
     * Constructor, takes a deep copy of the table, a refernce to a card, the player's hand,
     * and a indicator of what action should be performed in this instance of the turn.
     * @param table
     * @param action
     */
    public GameTreeNode(TableModel table, CardModel card, Vector<CardModel> hand, final String action){
        // private data members.
        this.table = table;
        this.action = action;
        this.hand = hand;
        this.card = card;
        // containers for moves generated for this turn instance.
        setMap = new LinkedHashMap<>();
        buildMap = new LinkedHashMap<>();
        multiBuildMap = new LinkedHashMap<>();

        this.nodes = new Vector<>();
        this.weight = -1;

        assessOptions();
    }

    /**
     * check the action given to the node and perform the corresponding heuristic assessment.
     */
    private void assessOptions(){

        if(action.equals("capture")) {
           checkCapture();

        }else if(action.equals("single")) {
            checkSingleBuilds();

        }else if(action.equals("multi")) {
            checkMultiBuilds();

        }else if(action.equals( "trail")) {
            trailCard();
        }
    }


    /**
     * Check if there any sets that can be captured and asses the weight of it.
     */
    private void checkCapture(){
        if(!table.isCaptureCard(hand, card, "Computer")) {
            Vector<Vector<CardModel>> sets = table.setCapture(card.getValue());
            Vector<CardModel> looseCards = table.captureLooseCards(card.getValue());

            for(CardModel cards : looseCards){
                weight += 1;
            }

            weight += table.checkBuilds(card);

            if (sets.size() > 0) {
                int size = sets.get(0).size();
                if(size > 0) {
                    setMap.put(card.toStringSave(), sets.get(0));
                    weight += size;

                    Vector<CardModel> capturedCards = setMap.get(card.toStringSave());
                    Vector<CardModel> capturedBuildCards = table.captureBuilds(card);

                    // If capture cards are not null ( meaning more than a build was captured).
                    // remove it from the table.
                    if (capturedCards != null) {
                        table.getLooseCards().removeAll(capturedCards);
                    }

                    if(looseCards.size() > 0){
                        table.getLooseCards().remove(looseCards);
                    }

                    if (capturedBuildCards != null) {
                        table.removeBuilds(card);
                    }
                }
            }
        }else{
            weight += table.checkBuilds(card);
        }
        System.out.println("Capture: " + Integer.toString(weight));
        handPair = new Pair<>(card, null);
    }

    /**
     * Check if builds can be made under the correct conditions. and asses the weight of each valid
     * build.
     */
    private void checkMultiBuilds(){
        Vector<Pair<CardModel, CardModel>> cardCombos = new Vector<>();
        if(hand.size() >= 1) {
            for (CardModel selectedCard : hand) {
                if (selectedCard != card && selectedCard.getValue() <= card.getValue()) {
                    if (!table.checkLooseCards(selectedCard)) {
                        Vector<Pair<Integer, Vector<CardModel>>> multiBuildOptions;
                        multiBuildOptions = table.checkMultiBuildCreation(selectedCard.getValue(),
                                card.getValue());
                        if (multiBuildOptions.size() > 0) {
                            Pair<CardModel, CardModel> cardCombo;
                            cardCombo = new Pair<>(card, selectedCard);
                            multiBuildMap.put(cardCombo, multiBuildOptions.get(0));
                            cardCombos.add(cardCombo);
                        } else {
                            multiBuildMap.put(new Pair<>(card, selectedCard),
                                    new Pair<>(null, null));
                        }
                    }
                }
            }

            int bestBuildWeight = -1;
            Pair<Pair<CardModel, CardModel>, Integer> comboWeights = new Pair<>(null, -1);
            // Check what is the best multiBuild Option.
            for (Pair<CardModel, CardModel> combo : cardCombos) {
                int weight = multiBuildMap.get(combo).first;
                if (weight > bestBuildWeight) {
                    bestBuildWeight = multiBuildMap.get(combo).second.size();
                    comboWeights = new Pair<>(combo, bestBuildWeight);
                    handPair = combo;
                }
            }

            // Add the build that will be made a multibuild to the weight.
            // Since cards will theoretically be captured eventually. (if the AI has it's way).
            if (bestBuildWeight > -1) {
                BuildModel build = table.getBuilds().get(multiBuildMap.get(comboWeights.first).first);
                for (Vector<CardModel> buildSets : build.getBuild()) {
                    for (CardModel card : buildSets)
                        bestBuildWeight += card.getValue();
                    weight = bestBuildWeight;
                }

                weight += card.getValue();
            }

            if(weight > -1){
                BuildModel selectedBuild;
                selectedBuild = table.getBuilds().get(multiBuildMap.get(handPair).first);
                Vector<CardModel> buildCards = multiBuildMap.get(handPair).second;
                CardModel selectedCard = handPair.first;

                boolean doesContain = false;
                for(Vector<CardModel> buildSet : selectedBuild.getBuild()){
                    if(buildSet.contains(selectedCard))
                        doesContain = true;
                }

                if(!doesContain){
                    table.createMultiBuild(selectedBuild, buildCards, card, hand, "Computer");
                    //if(hand.size() > 0)
                        //hand.remove(card);
                }
            }
        }
    }

    /**
     * Check if builds can be made under the correct conditions. and asses the weight of each valid
     * build.
     */
    private void checkSingleBuilds() {
        Vector<Pair<CardModel, CardModel>> cardCombos = new Vector<>();
        if (hand.size() >= 1) {
            for (CardModel captureCard : hand) {
                if (card != captureCard && card.getValue() < captureCard.getValue()) {
                    if (!table.isCaptureCard(hand, card, "Computer")) {
                        Vector<Vector<CardModel>> builds = table.checkBuildCreation(captureCard.getValue(), card.getValue());

                        if (builds.size() > 0) {
                            Pair<CardModel, CardModel> pair = new Pair<>(captureCard, card);
                            buildMap.put(new Pair<>(captureCard, card), builds.get(0));
                            cardCombos.add(pair);
                        }
                    }
                }
            }


            int bestBuildWeight = -1;
            Pair<CardModel, CardModel> cardCombo = null; // no need to allocate on the heap, will perform a null check before unboxing data.
            for (Pair<CardModel, CardModel> combo : cardCombos) {
                int size = buildMap.get(combo).size();
                if (bestBuildWeight < size && size > 0) {
                    bestBuildWeight = size;
                    handPair = combo;
                }
            }

            if (bestBuildWeight > -1)
                bestBuildWeight++;

            weight = bestBuildWeight;

            if(weight > -1)
                table.createBuild(buildMap.get(handPair), handPair.second, hand, "Computer");


        }
    }

    /**
     * give the weight of trailing -1 since it is the default option when no moves can be made.
     */
    private void trailCard(){
        weight = -1;
        handPair = new Pair<>(card, null);
    }


    /**
     * Return the map of all sets that can be captured.
     * @return
     */
    public final Map<String, Vector<CardModel>> getSetMap(){return setMap; }

    /**
     * Return a map of all builds that can be made with the corresponding card combinations.
     * @return
     */
    public Map<Pair<CardModel, CardModel>, Vector<CardModel>> getBuildMap(){ return buildMap; }

    /**
     * Return a map of card pairs to cards to make a build of.
     * @return Map<Pair<CardModel, CardModel>, Pair<Integer, Vector<CardModel>>>
     */
    public Map<Pair<CardModel, CardModel>, Pair<Integer, Vector<CardModel>>> getMultiMap(){ return multiBuildMap; }


    /**
     * Rerturn the current card used for evaulation within this node.
     * @return CardModel
     */
    public final CardModel getCard(){ return card; }

    /**
     * Get the hand container given to this node for evaluation.
     * @return Vector<CardModel>
     */
    public final Vector<CardModel> getHand(){ return  hand; }

    /**
     * return the collection of nodes that are children of this node.
     * @return Vector<GameTreeNode>
     */
    public final Vector<GameTreeNode> getNodes(){ return nodes; }


    /**
     * Return the action this node performed.
     * @return String
     */
    public final String getAction(){ return action; }

    /**
     * Get the pair of cards used for assesment within the node.
     * @return Pair<CardModel, CardModel>
     */
    public final Pair<CardModel, CardModel> getHandPair(){ return handPair; }

    /**
     * Return the table copy from this node.
     * @return TableModel
     */
    public final TableModel getTable(){return  table; }


    /**
     * Return the weight determined for this node.
     * @return integer
     */
    public final int getWeight(){ return weight; }
}
