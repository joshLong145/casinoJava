package com.casino.josh.casino_java.Listeners;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.casino.josh.casino_java.Models.CardModel;
import com.casino.josh.casino_java.activites.GameActivity;

import java.util.Vector;

/**
 * Created by josh on 11/11/18.
 */

public class LooseCardOnClickListener implements View.OnClickListener {
    private CardModel mCard;
    private ImageButton mButton;
    private Context mContext;
    /**
     *
     * @param card
     */
    public LooseCardOnClickListener(CardModel card, ImageButton cardButton, Context context){
        mButton = cardButton;
        mCard = card;
        mContext = context;
    }

    /**
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        if(GameActivity.mLooseCards == null)
            GameActivity.mLooseCards = new Vector<>();

        if(!GameActivity.mLooseCards.contains(mCard))
            GameActivity.mLooseCards.add(mCard);

        StringBuilder selectedCardPrompt = new StringBuilder(mCard.toStringSave() + " Selected, Current selected cards: ");
        for(CardModel card : GameActivity.mLooseCards)
            selectedCardPrompt.append(" ").append(card.toStringSave());


        Toast cardSelectedPrompt =  Toast.makeText(mContext,
                                                   selectedCardPrompt.toString(),
                                                   Toast.LENGTH_SHORT);

        cardSelectedPrompt.show();

    }
}
