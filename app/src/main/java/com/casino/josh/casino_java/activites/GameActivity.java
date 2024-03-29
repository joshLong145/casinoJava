package com.casino.josh.casino_java.activites;


import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.casino.josh.casino_java.Adapters.BuildAdapter;
import com.casino.josh.casino_java.Adapters.ComputerHandAdapter;
import com.casino.josh.casino_java.Fragments.ComputerPileFragment;
import com.casino.josh.casino_java.Fragments.DeckButtonFragment;
import com.casino.josh.casino_java.Fragments.HelpButtonFragment;
import com.casino.josh.casino_java.Fragments.LogButtonFragment;
import com.casino.josh.casino_java.Fragments.SaveGameButtonFragment;
import com.casino.josh.casino_java.Helpers.BooleanVariable;
import com.casino.josh.casino_java.Helpers.Serialization;
import com.casino.josh.casino_java.Models.BuildModel;
import com.casino.josh.casino_java.Models.CardModel;
import com.casino.josh.casino_java.ViewModels.BuildViewModel;
import com.casino.josh.casino_java.ViewModels.ComputerHandViewModel;
import com.casino.josh.casino_java.Adapters.HandAdapter;
import com.casino.josh.casino_java.ViewModels.HandViewModel;
import com.casino.josh.casino_java.Fragments.MakeMoveButtonFragment;
import com.casino.josh.casino_java.Fragments.PlayerPileFragment;
import com.casino.josh.casino_java.R;
import com.casino.josh.casino_java.Adapters.TableAdapter;
import com.casino.josh.casino_java.ViewModels.TableViewModel;
import com.casino.josh.casino_java.Models.TournamentModel;

import java.util.Objects;
import java.util.Vector;

/** GameActivity
 * Created by josh on 10/30/18.
 */

public class GameActivity extends FragmentActivity  {

    // Private member variables.
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private HandViewModel handVM;
    private ComputerHandViewModel mComputerHandVM;

    private static TableViewModel tableVM;
    private static BuildViewModel buildVM;

    public static TournamentModel mTournament;

    public static RecyclerView mTableModelView;
    public static RecyclerView mHumanHandModelView;
    public static RecyclerView mComputerModelView;
    public static RecyclerView mBuildModelView;

    public static TextView mHumanScore;
    public static TextView mComputerScore;
    public static TextView mRoundNumber;
    public static TextView mCurrentTurn;

    public static CardModel mChosenCard = null;
    public static Vector<CardModel> mLooseCards = new Vector<>();
    public static Vector<BuildModel> mBuilds = new Vector<>();

    /**
     * Executed on creation of the activity (When no instance of the activity is cached on disk of device).
     * Inflates layout and renders XML components defined within the specified layout.
     * @param savedInstanceState (instance of bundle that is brought in from disk.)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity);

        // If the activity is in portrait. change to landscape.
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            getResources().getConfiguration().orientation = Configuration.ORIENTATION_LANDSCAPE;
        }

        int firstTurn = getIntent().getIntExtra("firstTurn", -1);

        String saveData = getIntent().getStringExtra("saveData");

        if(!Objects.equals(saveData, "")) {
            Serialization serial = new Serialization(saveData);
            serial.parseData();
            mTournament = serial.createTournament();
        }else{
            mTournament = new TournamentModel(firstTurn);
        }

        handVM = ViewModelProviders.of(this).get(HandViewModel.class);
        mComputerHandVM = ViewModelProviders.of(this).get(ComputerHandViewModel.class);
        tableVM = ViewModelProviders.of(this).get(TableViewModel.class);
        buildVM = ViewModelProviders.of(this).get(BuildViewModel.class);

        handVM.setHand(mTournament.getHumanPlayer().getHand());
        mComputerHandVM.setHand(mTournament.getComputerPlayer().getHand());

        tableVM.setCards(mTournament.getCurrentRound().getTable().getLooseCards());

        buildVM.setBuilds(mTournament.getCurrentRound().getTable().getBuilds());

        // figure out
        if(firstTurn == 0) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Human goes first.",
                    Toast.LENGTH_SHORT);
            toast.show();
        }else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Computer goes first.",
                    Toast.LENGTH_SHORT);
            toast.show();
        }

        // Find the recyclerview asssoicated with this object.
        mTableModelView = findViewById(R.id.table);
        mTableModelView.setHasFixedSize(true);

        // Create the layout manager for the recyclerview.
        RecyclerView.LayoutManager tableLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mTableModelView.setLayoutManager(tableLayoutManager);

        TableViewModel tableVM = ViewModelProviders.of(this).get(TableViewModel.class);

        // Setup observer for the live data within the recyler view.
        tableVM.getCards().observe(this, (Vector<CardModel> tableData) ->{
            TableAdapter mAdapter = new TableAdapter(tableData, this);
            mTableModelView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        });

        // Find the recycler view to inflate.
        mHumanHandModelView = findViewById(R.id.human_hand);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mHumanHandModelView.setHasFixedSize(true);

        // Create layout manager for recyclerview
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        // Assign the layout manager to the view.
        mHumanHandModelView.setLayoutManager(mLayoutManager);

        HandViewModel handVM = ViewModelProviders.of(this).get(HandViewModel.class);


        // set up live data with initial data.
        handVM.getHand().observe(this, (Vector<CardModel> hand) ->{
            HandAdapter mAdapter = new HandAdapter(hand, this);
            mHumanHandModelView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        });

        // inflate the recyclerview.
        mComputerModelView= findViewById(R.id.computer_hand);
        mComputerModelView.setHasFixedSize(true);

        // Create the layout manager for the view.
        RecyclerView.LayoutManager mComputerLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        // Set the layout manager for the view.
        mComputerModelView.setLayoutManager(mComputerLayoutManager);

        ComputerHandViewModel computerHandVM = ViewModelProviders.of(this).get(ComputerHandViewModel.class);

        // Set up the live data for the view.
        computerHandVM.getHand().observe(this, (Vector<CardModel> hand) ->{
            ComputerHandAdapter mAdapter = new ComputerHandAdapter(hand, this);
            mComputerModelView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        });

        mBuildModelView = findViewById(R.id.table_builds);
        mBuildModelView.setHasFixedSize(true);

        RecyclerView.LayoutManager mBuildLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mBuildModelView.setLayoutManager(mBuildLayoutManager);

        BuildViewModel buildVM = ViewModelProviders.of(this).get(BuildViewModel.class);

        buildVM.getBuilds().observe(this, (Vector<BuildModel> builds) ->{
            BuildAdapter adapter = new BuildAdapter(builds, this, this);
            mBuildModelView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        });

        // if the round is over, show round over menu.
        // displays score and pile information for each player.
        mTournament.getRoundOver().setListener(this::roundOverPrompt);

        mComputerScore = findViewById(R.id.computer_score);
        mComputerScore.setText("Computer Score: " + Integer.toString(mTournament.getComputerPlayer().getPoints()));
        mHumanScore = findViewById(R.id.human_score);
        mHumanScore.setText("Human Score: " + Integer.toString(mTournament.getHumanPlayer().getPoints()));
        mRoundNumber = findViewById(R.id.round_number);
        mRoundNumber.setText("Round number: " + Integer.toString(mTournament.getRoundNumber()));
        mCurrentTurn = findViewById(R.id.current_turn);
        mCurrentTurn.setText("Current turn: " + mTournament.getCurrentRound().getTurn());
    }

    /**
     * Executed when the activity is activated and ready to run.
     * Creates components and attaches fragments to desired components defined within the layout.
     */
    @Override
    public void onStart(){
        super.onStart();

        // Add fragment components framelayouts defined within the XML layout.
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.make_move, new MakeMoveButtonFragment()).commit();
        fragmentManager.beginTransaction().add(R.id.human_pile, new PlayerPileFragment()).commit();
        fragmentManager.beginTransaction().add(R.id.computer_pile, new ComputerPileFragment()).commit();
        fragmentManager.beginTransaction().add(R.id.see_deck, new DeckButtonFragment()).commit();
        fragmentManager.beginTransaction().add(R.id.turn_log, new LogButtonFragment()).commit();
        fragmentManager.beginTransaction().add(R.id.save_game, new SaveGameButtonFragment()).commit();
        fragmentManager.beginTransaction().add(R.id.ask_help, new HelpButtonFragment()).commit();
    }



    /**
     * Called when the round is determined to be over.
     * asesses scores at the end of the round and determines if a new round should start,
     * or if the tournament should end.
     */
    public void roundOverPrompt(){
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.prompt_end_round, null);
        TextView computerDataContainer = promptsView.findViewById(R.id.computer_round_info);
        TextView humanDataContainer = promptsView.findViewById(R.id.human_round_info);
        TextView prompting = promptsView.findViewById(R.id.prompt);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptsView);

        Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> scores = mTournament.calculateScores();
        int finalPlayerScore = mTournament.getHumanPlayer().getPoints() + scores.first.first;
        int finalComputerScore = mTournament.getComputerPlayer().getPoints() + scores.first.second;

        StringBuilder playerInfo = new StringBuilder("Human \n Pile:");
        for (CardModel card : mTournament.getHumanPlayer().getPile()) {
            playerInfo.append(" ").append(card.toStringSave());
        }

        playerInfo.append("\n Pile size: " + Integer.toString(mTournament.getHumanPlayer().getPile().size()));

        playerInfo.append("\n Score: ");

        StringBuilder computerInfo = new StringBuilder("Computer \n Pile: ");

        for (CardModel card : mTournament.getComputerPlayer().getPile()) {
            computerInfo.append(" ").append(card.toStringSave());
        }

        computerInfo.append("\n Pile size: " + Integer.toString(mTournament.getComputerPlayer().getPile().size()));

        computerInfo.append("\n Score: ");

        // if the tournament is over, then we need to modify the prompt.
        if(finalPlayerScore >= 21 || finalComputerScore >= 21) {

            if(finalPlayerScore > finalComputerScore)
                prompting.setText("Tournament Results. Player wins!");
            else
                prompting.setText("Tournament Results. Computer wins");

            playerInfo.append(finalPlayerScore);
            playerInfo.append("\nRound score: ");
            playerInfo.append(Integer.toString(scores.first.second));
            computerInfo.append(finalComputerScore);
            computerInfo.append("\nRound score: ");
            computerInfo.append(Integer.toString(scores.first.first));
        }else{
            if(scores.first.second > scores.first.first)
                prompting.setText("Round over, player wins!");
            else
                prompting.setText("Round over, computer wins!");

            computerInfo.append(scores.first.first);
            playerInfo.append(scores.first.second);
        }

        playerInfo.append("\n Spades captured: " + Integer.toString(scores.second.first));
        computerInfo.append("\n Spades captured: " + Integer.toString(scores.second.second));

        computerDataContainer.setText(computerInfo.toString());
        humanDataContainer.setText(playerInfo.toString());

        mTournament.getHumanPlayer().setPoints(finalPlayerScore);
        mTournament.getComputerPlayer().setPoints(finalComputerScore);


        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Continue", (dialog, which) -> {
            if(mTournament.getHumanPlayer().getPoints() >= 21 || mTournament.getComputerPlayer().getPoints() >= 21){
                finish(); // close the activity and revert back to ladning page if the scores are matching.
            }else{
                mTournament.makeNewRound();

                // update text views relating to the round, and scores.
                // Only needs to update if the tournament is not over.
                mRoundNumber.setText("Round number: " + Integer.toString(mTournament.getRoundNumber()));
                mComputerScore.setText("Computer score: " + mTournament.getComputerPlayer().getPoints());
                mHumanScore.setText("Human score: " + mTournament.getHumanPlayer().getPoints());
                mCurrentTurn.setText("Current turn: " + mTournament.getCurrentRound().getTurn());
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);

        alertDialog.show();
    }


    /**
     * Updates human views with new data.
     * recyclerviews used so notification to the adapter of data changed will update the view correctly.
     */
    public static void updateView(){
        mTableModelView.getAdapter().notifyDataSetChanged();
        mHumanHandModelView.getAdapter().notifyDataSetChanged();
        mBuildModelView.getAdapter().notifyDataSetChanged();
        mChosenCard = null;
        mLooseCards = new Vector<>();
        mBuilds = new Vector<>();
        mTournament.getCurrentRound().setCurrentPlayerIndex(1);
        mCurrentTurn.setText("Current turn: " + GameActivity.mTournament.getCurrentRound().getTurn());
    }

    /**
     * Updates humanHand view with new data.
     * recyclerviews used so notification to the adapter of data changed will update the view correctly.
     */
    public static void updateHuamnHandView(){
        mHumanHandModelView.getAdapter().notifyDataSetChanged();
    }

    /**
     * Updates computerHand view with new data.
     * recyclerviews used so notification to the adapter of data changed will update the view correctly.
     */
    public static void updateComputerHandView(){
        mComputerModelView.getAdapter().notifyDataSetChanged();
    }

    /**
     * Calls the notify function for the Table adapter.
     */
    public static void updateTableAdapterData(){
        mTableModelView.getAdapter().notifyDataSetChanged();
    }

    /**
     * Calls the notify function for the Build adapter.
     */
    public static void updateBuildAdapterData(){
        mBuildModelView.getAdapter().notifyDataSetChanged();
    }
}
