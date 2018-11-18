package com.casino.josh.casino_java.Listeners;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.casino.josh.casino_java.Fragments.SaveGameButtonFragment;
import com.casino.josh.casino_java.R;
import com.casino.josh.casino_java.activites.GameActivity;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by josh on 11/16/18.
 */

public class SaveGameButtonOnClickListener implements View.OnClickListener {
    private SaveGameButtonFragment saveGameFragment;

    /**
     *
     * @param fragment
     */
    public SaveGameButtonOnClickListener(SaveGameButtonFragment fragment){
        saveGameFragment = fragment;
    }

    /**
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        LayoutInflater li = LayoutInflater.from(saveGameFragment.getActivity());
        View promptsView = li.inflate(R.layout.prompt_save_game, null);
        TextView fileName = promptsView.findViewById(R.id.file_name);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(saveGameFragment.getActivity());
        alertDialogBuilder.setView(promptsView);

        alertDialogBuilder
                .setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveFile("" + fileName.getText());
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }

    /**
     * Save file to downloads directory within local storage, cant you designated
     * storage directory for application due to spec doc requirements.
     * @return
     */
    private boolean saveFile(String name){
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(path, name);

        try {
            // if the file doesnt exist, create a new file, if it does, overwrite data existing.
            if(!file.exists())
                file.createNewFile();

            PrintWriter writer = new PrintWriter(file.getAbsoluteFile(), "UTF-8");
            String savedData = GameActivity.mTournament.toString();
            writer.print(savedData);
            writer.close();

            MediaScannerConnection.scanFile(saveGameFragment.getActivity().getApplicationContext(),
                    new String[]{file.toString()},
                    null,
                    null);

            Toast toast = Toast.makeText(saveGameFragment.getContext(),
                    "game has been saved.",
                    Toast.LENGTH_SHORT);
            toast.show();


            saveGameFragment.getActivity().finish();
        } catch (IOException e) {
            Toast toast = Toast.makeText(saveGameFragment.getContext(),
                    "Error while saving game state.",
                    Toast.LENGTH_SHORT);
            toast.show();

            e.printStackTrace();
            return false;
        }

        return true;
    }
}
