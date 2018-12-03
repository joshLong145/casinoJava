package com.casino.josh.casino_java.Helpers;

import android.support.v4.util.Pair;

import com.casino.josh.casino_java.Models.BuildModel;
import com.casino.josh.casino_java.Models.CardModel;
import com.casino.josh.casino_java.Models.ComputerPlayerModel;
import com.casino.josh.casino_java.Models.TableModel;

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
    private int weight = -1;


    Map<Pair<CardModel, CardModel>, Vector<CardModel>> buildMap;
    Map<Pair<CardModel, CardModel>, Pair<Integer, Vector<CardModel>>> multiBuildMap;
    Map<String, Vector<CardModel>> setMap;

    /**
     *
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

        assessOptions();
    }

    private void assessOptions(){

        if(Objects.equals(action, "capture")) {
           checkCapture();

        }else if(Objects.equals(action, "single")) {
            checkSingleBuilds();

        }else if(Objects.equals(action, "multi")) {
            checkMultiBuilds();

        }else if(Objects.equals(action, "trail")) {
            trailCard();
        }
    }


    /**
     *
     */
    private void checkCapture(){
        if(!table.isCaptureCard(hand, card, "Computer")) {
            Vector<Vector<CardModel>> sets = table.setCapture(card.getValue());
            sets.add(table.captureBuilds(card));

            if (sets.size() <= 0) {
                int size = sets.get(0).size();
                setMap.put(card.toStringSave(), sets.get(0));
                weight = size;
            }else{
                weight = -1;
            }
        }

        handPair = new Pair<>(card, null);
    }

    /**
     *
     */
    private void checkMultiBuilds(){
        Vector<Pair<CardModel, CardModel>> cardCombos = new Vector<>();
        if(hand.size() > 1) {
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
            }
        }
    }

    /**
     *
     */
    private void checkSingleBuilds() {
        Vector<Pair<CardModel, CardModel>> cardCombos = new Vector<>();
        if (hand.size() > 1) {
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
        }
    }


    private void trailCard(){
        weight = -1;
        handPair = new Pair<>(card, null);
    }

    /**
     *
     * @return
     */
    public final CardModel getCard(){ return card; }


    public final Vector<CardModel> getHand(){ return  hand; }

    /**
     *
     * @return
     */
    public final Vector<GameTreeNode> getNodes(){ return nodes; }


    /**
     *
     * @return
     */
    public final String getAction(){ return action; }

    /**
     *
     * @return
     */
    public final Pair<CardModel, CardModel> getHandPair(){ return handPair; }

    /**
     *
     * @return
     */
    public final TableModel getTable(){return  table; }


    /**
     *
     * @return
     */
    public final int getWeight(){ return weight; }
}
