package com.casino.josh.casino_java.Listeners;

import android.content.Intent;
import android.view.View;

import com.casino.josh.casino_java.Fragments.LoadGameButtonFragment;
import com.casino.josh.casino_java.activites.GameActivity;


/**
 * Created by josh on 11/17/18.
 */

public class LoadGameOnClickListener implements View.OnClickListener {
    private LoadGameButtonFragment loadGameFragment;

    /**
     *
     * @param fragment
     */
    public LoadGameOnClickListener(LoadGameButtonFragment fragment){
        loadGameFragment = fragment;
    }

    /**
     * Overrided onClick event listener.
     * @param v
     */
    @Override
    public void onClick(View v) {
        // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
        // browser.
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        // Filter to only show results that can be "opened", such as a
        // file (as opposed to a list of contacts or timezones)
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // Filter to show only images, using the image MIME data type.
        // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
        // To search for all documents available via installed storage providers,
        // it would be "*/*".
        intent.setType("text/plain");

        loadGameFragment.getActivity().startActivityForResult(intent, 42);
    }
}
