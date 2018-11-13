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
        if(GameActivity.mChosenCard == null) {
            GameActivity.mChosenCard = mCard;
            Toast cardSelectedPrompt =  Toast.makeText(mContext, "Selected card: " + mCard.toString(), Toast.LENGTH_SHORT);
            cardSelectedPrompt.show();
        }else{
            GameActivity.mCaptureCard = mCard;
            Toast cardSelectedPrompt =  Toast.makeText(mContext, "Selected capture card: " + mCard.toString(), Toast.LENGTH_SHORT);
            cardSelectedPrompt.show();
        }

        if(GameActivity.mCaptureCard != null) {
            if (GameActivity.mChosenCard.getValue() > GameActivity.mCaptureCard.getValue()) {
                CardModel tmp = GameActivity.mCaptureCard;
                GameActivity.mCaptureCard = GameActivity.mChosenCard;
                GameActivity.mChosenCard = tmp;
                Toast cardSelectedPrompt =  Toast.makeText(mContext, "Swapped capture card and selected card." + mCard.toString(), Toast.LENGTH_SHORT);
                cardSelectedPrompt.show();
            }
        }

    }
}
