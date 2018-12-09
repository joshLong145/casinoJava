package com.casino.josh.casino_java.Listeners;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.casino.josh.casino_java.Models.CardModel;
import com.casino.josh.casino_java.activites.GameActivity;

/**
 * Created by josh on 11/5/18.
 */

public class HandOnClickListener implements View.OnClickListener {
    private CardModel mCard;
    private Context mContext;

    /**
     * Constructor for event listener.
     * @param card
     */
    public HandOnClickListener(CardModel card, Context a_context){
        mContext = a_context;
        mCard = card;
    }

    /**
     * Executes the event listner when the button is pressed.
     * @param v View
     */
    @Override
    public void onClick(View v) {
        GameActivity.mChosenCard = mCard;
        Toast cardSelectedPrompt =  Toast.makeText(mContext,
                                              "Selected card: " + mCard.toStringSave(),
                                                   Toast.LENGTH_SHORT);
        cardSelectedPrompt.show();

    }
}

