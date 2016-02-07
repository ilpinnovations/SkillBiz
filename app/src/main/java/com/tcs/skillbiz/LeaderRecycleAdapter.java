package com.tcs.skillbiz;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by 966893 on 1/23/2016.
 */
public class LeaderRecycleAdapter extends RecyclerView.Adapter <LeaderRecycleAdapter.RecycleViewHolder>
{
    LayoutInflater layoutInflater;
    ArrayList<LeaderBean> leaderBeanList  =new ArrayList<LeaderBean>();
    Communicator communicator;
    private Context context;
    private int lastPosition = -1;


    public LeaderRecycleAdapter(Context context, ArrayList<LeaderBean> leaderBeanList)
    {
        layoutInflater=LayoutInflater.from(context);
        this.leaderBeanList = leaderBeanList;
        communicator = (Communicator)context;
        this.context=context;

    }


    @Override
    public RecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        RecycleViewHolder recycleViewHolder = null;
                View view = layoutInflater.inflate(R.layout.listview_row_layout, parent, false);
                recycleViewHolder = new RecycleViewHolder(view);
        return recycleViewHolder;
    }

    @Override
    public void onBindViewHolder(RecycleViewHolder holder, int position)
    {
        final LeaderBean currentData = leaderBeanList.get(position);
        holder.empID.setText(currentData.getEmpId() + "");
        holder.name.setText(currentData.getName());
        holder.score.setText(currentData.getScore());
        holder.position.setText(position+1+"");
        Log.d("!!!", "In BindView");
    }

    @Override
    public int getItemCount()
    {
        Log.d("!!!", "In ItemCount");
        return leaderBeanList.size();

    }

    public class RecycleViewHolder extends RecyclerView.ViewHolder{

        TextView empID, name, score, position;

        public RecycleViewHolder(View itemView)
        {
            super(itemView);
            empID = (TextView)itemView.findViewById(R.id.leaaderEmpID);
            name  = (TextView)itemView.findViewById(R.id.leaaderName);
            score = (TextView)itemView.findViewById(R.id.leaaderScore);
            position = (TextView)itemView.findViewById(R.id.leaaderPosition);


        }
    }
}
