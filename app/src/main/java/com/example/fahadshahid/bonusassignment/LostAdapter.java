package com.example.fahadshahid.bonusassignment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fahadshahid.bonusassignment.Model.PhoneNumber;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by Fahad Shahid on 12/24/2017.
 */

public class LostAdapter extends RecyclerView.Adapter<LostAdapter.MyViewHolder> {

    private ArrayList<PhoneNumber> lostList;
    ArrayList phonelist = new ArrayList<PhoneNumber>();
    RecyclerView recyclerView;
    Activity context;
    Gson gson;
    int i=0;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvNumber;

        public MyViewHolder(View view) {
            super(view);
            tvNumber = (TextView) view.findViewById((R.id.tvNumber));

        }
    }

    public LostAdapter(Activity context, ArrayList<PhoneNumber> lostList) {
        this.context = context;
        this.lostList = lostList;
    }
    public void changeset(ArrayList<PhoneNumber> lostList){
        this.lostList = lostList;
        this.notifyDataSetChanged();

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.lost_item_list,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        PhoneNumber lost = lostList.get(position);
        holder.tvNumber.setText(lost.getNumber());
       /* ArrayList<PhoneNumber> phoneList = new ArrayList<PhoneNumber>();
        phoneList.add(lost);*/



        phonelist.add(lost.getNumber().toString());
        i++;

        if (i == lostList.size()) {
            Log.i("view", "onBindViewHolder: " + phonelist);


            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = sharedPref.edit();
            Gson gson = new Gson();
            String str = gson.toJson(phonelist);
            //put your value
            editor.putString("list", str);

            //commits your edits
            editor.apply();
            editor.commit();

        }

            }




    @Override
    public int getItemCount() {
        /*if (lostList==null){
            return 0;
        }
        else {
            return lostList.size();
        }*/
        return lostList.size();
    }


}
