package com.casino.josh.casino_java.activites;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;

import com.casino.josh.casino_java.Fragments.LoadGameButtonFragment;
import com.casino.josh.casino_java.Fragments.PlayButtonFragment;
import com.casino.josh.casino_java.Models.TournamentModel;
import com.casino.josh.casino_java.R;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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

        int readPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int writePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if(writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, permissions, 1);
        }

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

    /**
     *
     * @param requestCode
     * @param resultCode
     * @param resultData
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        Intent eventInformationIntent = new Intent(this, GameActivity.class);
        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.

        if (requestCode == 42 && resultCode == RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                InputStream inputStream = null;
                try {
                    inputStream = getContentResolver().openInputStream(uri);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(
                            inputStream));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }

                    inputStream.close();
                    eventInformationIntent.putExtra("saveData", stringBuilder.toString());
                    this.startActivity(eventInformationIntent);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
