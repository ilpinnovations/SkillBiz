package com.tcs.skillbiz;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by 966893 on 1/23/2016.
 */
public class RecycleAdapter extends RecyclerView.Adapter <RecycleAdapter.RecycleViewHolder>
{
    LayoutInflater layoutInflater;
    ArrayList<TopicBean> topicBeanList =new ArrayList<TopicBean>();
    Communicator communicator;
    private Context context;
    private int lastPosition = -1;


    public RecycleAdapter(Context context, ArrayList<TopicBean> topicBeanList)
    {
        layoutInflater=LayoutInflater.from(context);
        this.topicBeanList = topicBeanList;
        communicator = (Communicator)context;
        this.context=context;

    }


    @Override
    public RecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        RecycleViewHolder recycleViewHolder = null;
        switch (viewType)
        {
            case 0:
            {
                View view = layoutInflater.inflate(R.layout.odd_topic, parent, false);
                recycleViewHolder = new RecycleViewHolder(view);
            }
            break;
            case 2:
            {
                View view = layoutInflater.inflate(R.layout.even_topic, parent, false);
                recycleViewHolder = new RecycleViewHolder(view);
            }
            break;
        }
        Log.d("!!!","In Cons");
        return recycleViewHolder;
    }

    @Override
    public void onBindViewHolder(RecycleViewHolder holder, int position)
    {
        final TopicBean currentData = topicBeanList.get(position);
        holder.title.setText(currentData.getId()+"");
        holder.subTitle.setText(currentData.getName());
        setAnimation(holder.cardView, position);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
                    public void onClick(View v) {
                        communicator.callBack(currentData);
                    }
        });
        Log.d("!!!", "In BindView");
    }

    @Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        Log.d("!!!", "In ViewType");
        return position % 2 * 2;

    }

    @Override
    public int getItemCount()
    {
        Log.d("!!!", "In ItemCount");
        return topicBeanList.size();

    }

    public class RecycleViewHolder extends RecyclerView.ViewHolder{

        TextView title, subTitle;
        CardView cardView;

        public RecycleViewHolder(View itemView)
        {
            super(itemView);
            title = (TextView)itemView.findViewById(R.id.Title);
            subTitle = (TextView)itemView.findViewById(R.id.subTitle);
            cardView=(CardView)itemView.findViewById(R.id.CV);

            Log.d("!!!t", title.getText().toString());
            Log.d("!!!sT", subTitle.getText().toString());
            Log.d("!!!", "In VH Cons");
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView)
    {
        super.onAttachedToRecyclerView(recyclerView);
    }

    /**
     * Here is the key method to apply the animation
     */
    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Log.d("tag", "condition");
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.enter_vertical);
            animation.setStartOffset(position*animation.getDuration());
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}
