package com.casino.josh.casino_java.Helpers;

import android.os.Build;
import android.util.Log;

import com.casino.josh.casino_java.Models.BasePlayerModel;
import com.casino.josh.casino_java.Models.BuildModel;
import com.casino.josh.casino_java.Models.CardModel;
import com.casino.josh.casino_java.Models.ComputerPlayerModel;
import com.casino.josh.casino_java.Models.DeckModel;
import com.casino.josh.casino_java.Models.HumanPlayerModel;
import com.casino.josh.casino_java.Models.RoundModel;
import com.casino.josh.casino_java.Models.TableModel;
import com.casino.josh.casino_java.Models.TournamentModel;

import java.util.Arrays;
import java.util.Objects;
import java.util.Stack;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by josh on 11/18/18.
 */

public class Serialization {
    private String mSaveData = null;

    private int mRoundNumber = 0;

    private int mHumanScore = 0;
    private Vector<CardModel> mHumanHand = new Vector<>();
    private Vector<CardModel> mHumanPile = new Vector<>();

    private int mComputerScore = 0;
    private Vector<CardModel> mComputerHand = new Vector<>();
    private Vector<CardModel> mComputerPile = new Vector<>();

    private Vector<BuildModel> mBuilds = new Vector<>();
    private Vector<CardModel> mLooseCards = new Vector<>();

    private DeckModel mDeck;

    private TableModel mTable;

    private Vector<BasePlayerModel> mPlayers = new Vector<>();

    private int mCurrentTurn = 0;


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
        try {
            int roundIndex = mSaveData.indexOf("Round:");
            mRoundNumber = Integer.parseInt("" + mSaveData.charAt(roundIndex + 7));

            //
            int humanScoreIndex = mSaveData.indexOf("Score:");
            String humanScoreString = mSaveData.substring(humanScoreIndex+ 7, mSaveData.indexOf("Hand:"));
            humanScoreString.replaceAll("\\s+", "");
            mHumanScore = Integer.parseInt(humanScoreString);
            int humanHandIndex = mSaveData.indexOf("Hand:");
            String humanHandString = mSaveData.substring(humanHandIndex + 5, mSaveData.indexOf("Pile:"));
            mHumanHand = createCards(tokenizeInput(humanHandString));
            int humanPileIndex = mSaveData.indexOf("Pile:");
            String humanPileString = mSaveData.substring(humanPileIndex + 5, mSaveData.indexOf("Computer:"));
            mHumanPile = createCards(tokenizeInput(humanPileString));

            mPlayers.add(createHumanPlayer(mHumanScore, mHumanHand, mHumanPile));

            int computerScoreIndex = mSaveData.lastIndexOf("Score:");
            String computerScoreString = mSaveData.substring(computerScoreIndex + 7, mSaveData.lastIndexOf("Hand:"));
            computerScoreString.replaceAll("\\s+", "");
            mComputerScore = Integer.parseInt(computerScoreString);
            int computerHandIndex = mSaveData.lastIndexOf("Hand:");
            String computerHandString = mSaveData.substring(computerHandIndex + 5, mSaveData.lastIndexOf("Pile:"));
            mComputerHand = createCards(tokenizeInput(computerHandString));
            int computerPileIndex = mSaveData.lastIndexOf("Pile:");
            String computerPileString = mSaveData.substring(computerPileIndex + 5, mSaveData.indexOf("Table:"));
            mComputerPile = createCards(tokenizeInput(computerPileString));

            mPlayers.add(createComputerPlayer(mComputerScore, mComputerHand, mComputerPile));

            int tableIndex = mSaveData.indexOf("Table:");
            String tableString = mSaveData.substring(tableIndex + 7, mSaveData.indexOf("Build Owners:"));

            if (tableString.indexOf('[') != -1) {
                int buildsIndex = tableString.indexOf('[');
                int endOfBuildsIndex = tableString.lastIndexOf(']') + 1;
                String buildString = tableString.substring(buildsIndex, endOfBuildsIndex);
                mBuilds = createBuilds(tokenizeBuilds(buildString));
                int buildOwnerIndex = mSaveData.indexOf("Build Owners:");
                addOwnersToBuild(parseBuildOwers(mSaveData.substring(buildOwnerIndex + 12, mSaveData.indexOf("Deck:"))));

                String looseCardString = tableString.substring(endOfBuildsIndex + 1);
                mLooseCards = createCards(tokenizeInput(looseCardString));
            } else {
                mLooseCards = createCards(tokenizeInput(tableString));
            }


            int deckIndex = mSaveData.indexOf("Deck:");
            String deckString = mSaveData.substring(deckIndex + 6, mSaveData.indexOf("Next Player:"));
            if(deckString.equals("none"))
                    mDeck = createDeck(new Vector<>());
            else
                mDeck = createDeck(createCards(tokenizeInput(deckString)));

            mTable = createTable(mDeck, mLooseCards, mBuilds);

            int currentTurnIndex = mSaveData.indexOf("Next Player:");
            String nextPlayerString = mSaveData.substring(currentTurnIndex + 13);

            if(Objects.equals(nextPlayerString, "Human"))
                mCurrentTurn = 0;
            else
                mCurrentTurn = 1;

        }catch (Exception e){
            Log.i("Error while parsing data: ", Arrays.toString(e.getStackTrace()));
            return false;
        }

        // if parsing is successful, return true.
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

    /**
     * Parse builds for deserialization.
     * @param builds
     * @return
     */
    private final Vector<String> tokenizeBuilds(String builds){
        Vector<String> buildTokens = new Vector<>();

        while (builds != ""){
            int endOfBuild = builds.indexOf(" ] [ ");
            if(endOfBuild != -1){
                String buildString = builds.substring(0, endOfBuild + 2);
                buildTokens.add(buildString.substring(buildString.indexOf('[') + 1, buildString.lastIndexOf(']') - 1));
                if(builds.contains(" ] [ ")){
                   builds = builds.substring(endOfBuild + 2);
                }else{
                    builds = "";
                }
            }else{
                endOfBuild = builds.lastIndexOf(']');
                String buildString = builds.substring(0, endOfBuild + 1);
                buildTokens.add(buildString.substring(buildString.indexOf('[') + 1, buildString.lastIndexOf(']') - 1));
                builds = "";
            }
        }

        for(String build : buildTokens){
            build = build.substring(1, build.length() - 2);
        }

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
                suit = Character.toLowerCase(suit);
                String rawValue = tokenData[i].substring(1);
                int value = 0;

                if(rawValue.equals("Q")){
                    value = 12;

                }else if(rawValue.equals("K")){
                    value = 13;

                } else if(rawValue.equals("J")){
                    value = 11;

                }else if(rawValue.equals("A")){
                    value = 1;

                } else if(rawValue.equals("X")){
                    value = 10;

                } else{
                    value = Integer.parseInt(tokenData[i].substring(1));
                }

                if (value != 1)
                    cards.add(new CardModel(suit, value, false));
                else
                    cards.add(new CardModel(suit, value, true));
            }
        }

        return cards;
    }

    /**
     * Create a DeckModel object from deserialize data
     * @param cards
     * @return
     */
    private final DeckModel createDeck(Vector<CardModel> cards){
        DeckModel deck = new DeckModel(cards);
        return deck;
    }

    /**
     * Creates BuildModel objects from serial data parsed by helper functions.
     * @param buildStrings String
     * @return Vector<BuildModel>
     */
    private final Vector<BuildModel> createBuilds(final Vector<String> buildStrings){
        Vector<BuildModel> builds = new Vector<>();
        for(String buildString : buildStrings) {
            BuildModel build = new BuildModel();
            Vector<Vector<CardModel>> cards = parseBuildCards(buildString);
            for(Vector<CardModel> cardContainer : cards){
                build.addBuildToBuild(cardContainer);
            }
            if(build.getBuild().size() > 0){
                int sum = 0;
                for(CardModel card : build.getBuild().get(0))
                    sum += card.getValue();
                build.setCaptureValue(sum);
            }
            builds.add(build);
        }
        return builds;
    }

    /**
     * Takes parsed build owner names and adds them to the correct build.
     * builds and owners are parsed in the same order.
     * @param owners
     */
    private void addOwnersToBuild(final Vector<String> owners){
        for(int i = 0; i < mBuilds.size(); i++){
            mBuilds.get(i).setBuildOwner(owners.get(i));
        }
    }

    public Vector<String> parseBuildOwers(String buildOwnerString){
        Vector<String> buildOwers = new Vector<>();

        while(buildOwnerString != ""){
            if(buildOwnerString.contains(" [ ") && buildOwnerString.contains(" ] ")){
                int buildEnd = buildOwnerString.indexOf(" ] ");
                buildOwnerString = buildOwnerString.substring(buildEnd + 1);
                if(buildOwnerString.contains(" [ ")) {
                    String name = buildOwnerString.substring(2, buildOwnerString.indexOf(" [ ") - 1);
                    name.replace(" ", "");
                    buildOwers.add(name);
                    buildOwnerString = buildOwnerString.substring(buildOwnerString.indexOf(" [ "));
                }else{
                    String owner = buildOwnerString;
                    owner.replace(" ", "");
                    buildOwers.add(owner.substring(2, owner.length()));
                    buildOwnerString = "";
                }
            }
        }

        return buildOwers;
    }

    /**
     * Parses cards for builds and creates card objects from serial data.
     * @param buildString
     * @return
     */
    private final Vector<Vector<CardModel>> parseBuildCards(final String buildString){
        Stack<Integer> stack = new Stack<>();
        Vector<Vector<CardModel>> cards = new Vector<>();
       for(int i = 0; i < buildString.length(); i++){
           if(buildString.charAt(i) == '['){
               stack.push(i);
           }else if(buildString.charAt(i) == ']'){
               int top_index = stack.peek();
               String cardString = buildString.substring(top_index + 1, i);
               cards.add(createCards(tokenizeInput(cardString)));
           }
       }

       return cards;
    }

    /**
     * Construct a table from deserialize data.
     * @param deck
     * @param looseCards
     * @param builds
     * @return TableModel
     */
    private TableModel createTable(DeckModel deck, Vector<CardModel> looseCards, Vector<BuildModel> builds){
        TableModel table = new TableModel(deck, looseCards, builds);

        return table;
    }

    /**
     * Creates a Human player object from deserialized data.
     * @param score
     * @param hand
     * @param pile
     * @return
     */
    private BasePlayerModel createHumanPlayer(int score, Vector<CardModel> hand, Vector<CardModel> pile){
        BasePlayerModel player = new HumanPlayerModel(score, hand, pile);

        return player;
    }

    /**
     * Creates a computer player object from deserialize data.
     * @param score
     * @param hand
     * @param pile
     * @return
     */
    private BasePlayerModel createComputerPlayer(int score, Vector<CardModel> hand, Vector<CardModel> pile){
        ComputerPlayerModel computer = new ComputerPlayerModel(score, hand, pile);

        return computer;
    }


    /**
     * Returns a tournament object with all data loaded from deserialization process.
     * @return
     */
    public final TournamentModel createTournament(){
        return new TournamentModel(mPlayers, new RoundModel(mPlayers, mTable, mCurrentTurn, true), mRoundNumber);
    }



}
