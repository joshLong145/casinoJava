package com.casino.josh.casino_java.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.casino.josh.casino_java.Listeners.HandOnClickListener;
import com.casino.josh.casino_java.Models.CardModel;
import com.casino.josh.casino_java.R;

import java.util.Vector;

/** ComputerHandAdapter
 * Created by josh on 11/10/18.
 */

public class ComputerHandAdapter extends RecyclerView.Adapter<ComputerHandAdapter.ViewHolder> {
    private Vector<CardModel> mDataSet;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;

        /**
         * Constructor for viewholder, inflates component for recycler view.
         * @param itemView
         */
        public ViewHolder(View itemView){
            super(itemView);
            mImageView = itemView.findViewById(R.id.card_image);
        }
    }

    /**
     * Constructor for adapter, takes in dataset to show and current app context.
     * @param a_dataset Vector<CardModel>
     * @param a_context Context
     */
    public ComputerHandAdapter(Vector<CardModel> a_dataset, Context a_context) {
        super();
        mDataSet = a_dataset;
        context = a_context;
    }

    /**
     * Exectued when attaching model to recycler view.
     * @param recyclerView RecyclerView
     */
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    /**
     * Exectured on creation for view holder. which inflates internal components for view.
     * @param parent ViewGroup
     * @param viewType int
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.computer_hand_layout, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    /**
     * Executes when binding view to the recycler view.
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

        holder.mImageView.setImageResource(res);
    }

    /**
     * Returns the current amount of data within the internal collection.
     * @return
     */
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    /**
     * Get the collection stored within the adapter.
     * @return
     */
    public Vector<CardModel> getCards(){return mDataSet; }

}
