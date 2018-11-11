package com.casino.josh.casino_java.Listeners;

import android.view.View;

import com.casino.josh.casino_java.Models.CardModel;
import com.casino.josh.casino_java.activites.GameActivity;

/**
 * Created by josh on 11/11/18.
 */

public class LooseCardOnClickListener implements View.OnClickListener {
    private CardModel mCard;

    /**
     *
     * @param card
     */
    public LooseCardOnClickListener(CardModel card){
        mCard = card;
    }

    /**
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        if(!GameActivity.mLooseCards.contains(mCard))
            GameActivity.mLooseCards.add(mCard);
    }
}
