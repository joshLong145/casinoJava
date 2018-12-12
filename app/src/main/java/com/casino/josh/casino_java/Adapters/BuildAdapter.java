package com.casino.josh.casino_java.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.casino.josh.casino_java.Listeners.BuildOnClickListener;
import com.casino.josh.casino_java.Models.BuildModel;
import com.casino.josh.casino_java.R;

import java.util.Vector;

/**
 * Created by josh on 11/11/18.
 */

public class BuildAdapter extends RecyclerView.Adapter<BuildAdapter.ViewHolder> {

    // Private member variables.
    private Vector<BuildModel> mDataSet;
    private Context context;
    private FragmentActivity mActivity;
    /**
     * Constructor for the Adapter
     * @param a_dataset Vector<CardModel>
     * @param a_context Context
     */
    public BuildAdapter(Vector<BuildModel> a_dataset, Context a_context, FragmentActivity a_activity) {
        super();
        mDataSet = a_dataset;
        context = a_context;
        mActivity = a_activity;
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
                .inflate(R.layout.layout_builds, parent, false);

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
        final String data = mDataSet.get(position).toString() + "\n" + mDataSet.get(position).getBuildOwner();
        holder.mButton.setText(data);
        //holder.mButton.setBackgroundColor(Color.TRANSPARENT);
        holder.mButton.setOnClickListener(new BuildOnClickListener(mDataSet.get(position), holder.itemView.getContext()));
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
     * @return Vector<BuildModel>
     */
    public Vector<BuildModel> getBuilds(){return mDataSet; }

    /**
     * Wrapper class for binding to internal collection of data to be added to the view.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private Button mButton;
        public ViewHolder(View itemView) {
            super(itemView);
            mButton = itemView.findViewById(R.id.build_view);
        }
    }




}
