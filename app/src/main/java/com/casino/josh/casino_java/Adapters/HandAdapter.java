package com.casino.josh.casino_java.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;


import com.casino.josh.casino_java.Listeners.HandOnClickListener;
import com.casino.josh.casino_java.Models.CardModel;
import com.casino.josh.casino_java.R;

import java.util.Vector;

/** HandAdapter
 * Created by josh on 11/1/18.
 */

public class HandAdapter extends RecyclerView.Adapter<HandAdapter.ViewHolder> {

    private Vector<CardModel> mDataSet;
    private Context context;

    /**
     * Wrapper class for binding to internal collection of data to be added to the view.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageButton mimageBttn;
        public ViewHolder(View itemView) {
            super(itemView);
            mimageBttn = itemView.findViewById(R.id.card_image);
        }
    }

    /**
     * Constructor for the Adapter
     * @param a_dataset Vector<CardModel>
     * @param a_context Context
     */
    public HandAdapter(Vector<CardModel> a_dataset, Context a_context) {
        super();
        mDataSet = a_dataset;
        context = a_context;
    }

    /**
     * Executes when attaching to the View. Calls parent method.
     * @param recyclerView
     */
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    /**
     * Executes on creation of view, after attachment.
     * @param parent ViewGroup
     * @param viewType int
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hand_layout, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    /**
     * Executes when Binding to the view
     * @param holder ViewHolder
     * @param position int
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int res = context.getResources().getIdentifier(
                            mDataSet.get(position).toString(),
                    "drawable",
                            context.getPackageName()
        );

        holder.mimageBttn.setImageResource(res);
        holder.mimageBttn.setOnClickListener(new HandOnClickListener(mDataSet.get(position), holder.itemView.getContext()));
    }

    /**
     * Returns number of elements within data container
     * @return integer
     */
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    /**
     * Returns data within collection.
     * @return Vector<CardModel>
     */
    public Vector<CardModel> getCards(){return mDataSet; }
}
