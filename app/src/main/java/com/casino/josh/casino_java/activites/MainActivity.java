package com.casino.josh.casino_java.activites;

import android.content.res.Configuration;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;

import com.casino.josh.casino_java.Fragments.LoadGameButtonFragment;
import com.casino.josh.casino_java.Fragments.PlayButtonFragment;
import com.casino.josh.casino_java.R;

/**
 * MainActivity
 */
public class MainActivity extends FragmentActivity {
    private FragmentManager fragmentManager = getSupportFragmentManager();

    /**
     * Executes on the creation of the Activity, Attempts to pull cached instance from disk and load into memory.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Don't let the app load if it is not in portrait mode.
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
            return;
        }

        // If the instance state is not cached, load in data.
        if (savedInstanceState == null) {
            // During initial setup, plug in button fragments.
            PlayButtonFragment playGameButton = new PlayButtonFragment();
            LoadGameButtonFragment loadGameButton = new LoadGameButtonFragment();

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.play_game, playGameButton);
            transaction.add(R.id.load_game, loadGameButton).commit();
        }
    }
}
