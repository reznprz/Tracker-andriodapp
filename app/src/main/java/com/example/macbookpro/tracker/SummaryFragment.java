package com.example.macbookpro.tracker;


import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Created by macbookpro on 3/10/18.
 */

public class SummaryFragment extends Fragment {
    RecyclerView recyclerView;
    IntentFilter intentFilter;
    SummaryRecyclerView summaryRecyclerView;
    BroadcastReceiver broadcastReceiver;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.summaryfragment,container,false);
        recyclerView=view.findViewById(R.id.myrecyclerview);
        summaryRecyclerView= new SummaryRecyclerView();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(summaryRecyclerView);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intentFilter=new IntentFilter("com.example.broadcast.MY_NOTIFICATION");
    }

    @Override
    public void onResume() {
        super.onResume();
        broadcastReceiver =new BroadcastReceiver() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.e("//","Broadcast received");
                summaryRecyclerView.notifyDataSetChanged();

            }
        };
        getActivity().registerReceiver(broadcastReceiver,intentFilter);

    }


    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(broadcastReceiver);
    }

    public class SummaryRecyclerView extends RecyclerView.Adapter<SummaryRecyclerView.MyViewHolder>{
        Drawable img;
        int currentHour;
        int currentHourin12;
        int minute;
        Calendar rightNow;
        public SummaryRecyclerView(){


        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.summaryitem, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            currentHourin12=0;
            rightNow = Calendar.getInstance();
            currentHour= rightNow.get(Calendar.HOUR_OF_DAY);
            minute=rightNow.get(Calendar.MINUTE);
            holder.stop.setText(MyIntentService.stop.get(position).getName().trim());
            if(Integer.parseInt(MyIntentService.stop.get(position).getTime())<minute){
                Log.e("...",MyIntentService.stop.get(position).getName()+"....."+ minute);
                currentHourin12=currentHour+1;
            }
            else{
                currentHourin12=currentHour;
            }
            if(currentHourin12>12){
                currentHourin12=currentHourin12-12;
            }
            holder.time.setText(" "+currentHourin12+":"+MyIntentService.stop.get(position).getTime());
            if(MyIntentService.stop.get(position).isNotifyEnabled()){
                img  = getContext().getResources().getDrawable(R.mipmap.ic_notifications_black_24dp );
                img.setBounds( 0, 0, 100, 100 );
                holder.time.setCompoundDrawables(img,null,null,null);
            }else{
                img  = getContext().getResources().getDrawable(R.mipmap.ic_notifications_white_24dp );
                img.setBounds( 0, 0, 100, 100);
                holder.time.setCompoundDrawables(img,null,null,null);

            }


        }

        @Override
        public int getItemCount() {
            return MyIntentService.stop.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder{
            TextView time;
            TextView stop;
            CardView myCardView;

            public MyViewHolder(View itemView) {
                super(itemView);
                time=itemView.findViewById(R.id.textviewtime);
                stop=itemView.findViewById(R.id.textviewstop);
                myCardView=itemView.findViewById(R.id.mycardview);
                myCardView.setOnClickListener(new View.OnClickListener() {
                    public PendingIntent alarmIntent;
                    public AlarmManager alarmMgr;

                    @Override
                    public void onClick(View v) {
                        int x=SearchActivity.stops.indexOf(MyIntentService.stop.get(getAdapterPosition()));
                        if(!SearchActivity.stops.get(x).isNotifyEnabled()){
                           img  = getContext().getResources().getDrawable(R.mipmap.ic_notifications_black_24dp );
                            img.setBounds( 0, 0, time.getCompoundDrawables()[0].getIntrinsicWidth(), time.getCompoundDrawables()[0].getIntrinsicHeight() );
                            time.setCompoundDrawables(img,null,null,null);
                            SearchActivity.stops.get(x).setNotifyEnabled(true);
                        }else{
                            img= getContext().getResources().getDrawable(R.mipmap.ic_notifications_white_24dp );
                            img.setBounds( 0, 0, time.getCompoundDrawables()[0].getIntrinsicWidth(), time.getCompoundDrawables()[0].getIntrinsicHeight() );
                            time.setCompoundDrawables(img,null,null,null);
                            SearchActivity.stops.get(x).setNotifyEnabled(false);
                        }

                    }
                });


            }
        }
    }



}
