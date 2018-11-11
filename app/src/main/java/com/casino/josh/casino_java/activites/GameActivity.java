package com.casino.josh.casino_java.activites;

import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.casino.josh.casino_java.Adapters.ComputerHandAdapter;
import com.casino.josh.casino_java.Fragments.ComputerPileFragment;
import com.casino.josh.casino_java.Fragments.DeckButtonFragment;
import com.casino.josh.casino_java.Fragments.LogButtonFragment;
import com.casino.josh.casino_java.Models.CardModel;
import com.casino.josh.casino_java.ViewModels.ComputerHandViewModel;
import com.casino.josh.casino_java.Adapters.HandAdapter;
import com.casino.josh.casino_java.ViewModels.HandViewModel;
import com.casino.josh.casino_java.Fragments.MakeMoveButtonFragment;
import com.casino.josh.casino_java.Fragments.PlayerPileFragment;
import com.casino.josh.casino_java.R;
import com.casino.josh.casino_java.Adapters.TableAdapter;
import com.casino.josh.casino_java.ViewModels.TableViewModel;
import com.casino.josh.casino_java.Models.TournamentModel;

import java.util.Vector;

/** GameActivity
 * Created by josh on 10/30/18.
 */

public class GameActivity extends FragmentActivity  {
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private HandViewModel handVM;
    private ComputerHandViewModel mComputerHandVM;
    private TableViewModel tableVM;

    public static TournamentModel mTournament;
    public static RecyclerView mTableModelView;
    public static RecyclerView mHumanHandModelView;
    public static RecyclerView mComputerModelView;
    public static CardModel mChosenCard = null;

    /**
     * Executed on creation of the activity (When no instance of the activity is cached on disk of device).
     * Inflates layout and renders XML components defined within the specified layout.
     * @param savedInstanceState (instance of bundle that is brought in from disk.)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity);
        handVM = ViewModelProviders.of(this).get(HandViewModel.class);
        mComputerHandVM = ViewModelProviders.of(this).get(ComputerHandViewModel.class);
        tableVM = ViewModelProviders.of(this).get(TableViewModel.class);
        mTournament = new TournamentModel();

        // If the activity is in portrait. change to landscape.
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            getResources().getConfiguration().orientation = Configuration.ORIENTATION_LANDSCAPE;
        }

        handVM.setHand(mTournament.getCurrentRound().getPlayers().get(0).getHand());
        mComputerHandVM.setHand(mTournament.getCurrentRound().getPlayers().get(1).getHand());

        tableVM.setCards(mTournament.getCurrentRound().getTable().getLooseCards());
    }

    /**
     * Executed when the activity is activated and ready to run.
     * Creates components and attaches fragments to desired components defined within the layout.
     */
    @Override
    public void onStart(){
        super.onStart();

        // Find the recyclerview asssoicated with this object.
        mTableModelView = findViewById(R.id.table);
        mTableModelView.setHasFixedSize(true);

        // Create the layout manager for the recyclerview.
        RecyclerView.LayoutManager tableLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mTableModelView.setLayoutManager(tableLayoutManager);

        TableViewModel tableVM = ViewModelProviders.of(this).get(TableViewModel.class);

        Vector<CardModel> tableCards = new Vector<>();

        // Setup observer for the live data within the recyler view.
        tableVM.getCards().observe(this, (Vector<CardModel> tableData) ->{
            tableCards.addAll(tableData);
            TableAdapter mAdapter = new TableAdapter(tableCards, this);
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

        Vector<CardModel> cards = new Vector<>();

        // set up live data with initial data.
        handVM.getHand().observe(this, (Vector<CardModel> hand) ->{
            cards.addAll(hand);

            HandAdapter mAdapter = new HandAdapter(cards, this);
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

        Vector<CardModel> computerCards = new Vector<>();

        // Set up the live data for the view.
        computerHandVM.getHand().observe(this, (Vector<CardModel> hand) ->{
            computerCards.addAll(hand);

            ComputerHandAdapter mAdapter = new ComputerHandAdapter(computerCards, this);
            mComputerModelView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        });

        // Add fragment components framelayouts defined within the XML layout.
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.make_move, new MakeMoveButtonFragment()).commit();
        fragmentManager.beginTransaction().add(R.id.human_pile, new PlayerPileFragment()).commit();
        fragmentManager.beginTransaction().add(R.id.computer_pile, new ComputerPileFragment()).commit();
        fragmentManager.beginTransaction().add(R.id.see_deck, new DeckButtonFragment()).commit();
        fragmentManager.beginTransaction().add(R.id.turn_log, new LogButtonFragment()).commit();
    }
}
