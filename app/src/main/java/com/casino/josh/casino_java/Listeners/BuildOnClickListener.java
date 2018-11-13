package com.casino.josh.casino_java.Listeners;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.casino.josh.casino_java.Models.BuildModel;
import com.casino.josh.casino_java.activites.GameActivity;

import java.util.Vector;

/**
 * Created by josh on 11/12/18.
 */

public class BuildOnClickListener implements View.OnClickListener {
    private BuildModel mBuild;
    private Context mContext;
    
    /**
     * Default constructor.
     */
    public BuildOnClickListener(BuildModel build, Context context){
        mBuild = build;
        mContext = context;
    }

    /**
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        if(GameActivity.mBuilds == null)
            GameActivity.mBuilds = new Vector<>();

        if (!GameActivity.mBuilds.contains(mBuild)) {
            GameActivity.mBuilds.add(mBuild);

            Toast toast = Toast.makeText(mContext,
                    "Build Selected",
                    Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
