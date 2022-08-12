package com.mnkadafi.acaraanbarengapps.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mnkadafi.acaraanbarengapps.R;
import com.mnkadafi.acaraanbarengapps.User;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ParticipantAdapter extends RecyclerView.Adapter<ParticipantAdapter.ParticipantHolder> {

    private List<User> mList;
    private Context mContext;

    public ParticipantAdapter(Context mContext, List<User> mList) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ParticipantHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(mContext).inflate(R.layout.item_participant,parent, false);
        ParticipantHolder holder = new ParticipantHolder(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ParticipantHolder holder, @SuppressLint("RecyclerView") int position) {
        User userModel = mList.get(position);
        holder.textViewFullName.setText(userModel.getFullName());
        holder.textViewLocation.setText(userModel.getLocation());
        holder.textViewPhone.setText(userModel.getPhone());;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ParticipantHolder extends RecyclerView.ViewHolder {
        public TextView textViewFullName;
        public TextView textViewLocation;
        public TextView textViewPhone;
        public ImageView imageViewUser;

        public ParticipantHolder(View itemView) {
            super(itemView);

            textViewFullName = itemView.findViewById(R.id.tvFullName);
            textViewLocation = itemView.findViewById(R.id.tvLocation);
            textViewPhone = itemView.findViewById(R.id.tvPhone);
        }
    }

}
