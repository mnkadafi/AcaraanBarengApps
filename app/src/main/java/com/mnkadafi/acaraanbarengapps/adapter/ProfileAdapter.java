package com.mnkadafi.acaraanbarengapps.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.mnkadafi.acaraanbarengapps.R;
import com.mnkadafi.acaraanbarengapps.model.EventModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileHolder> {

    private List<EventModel> mList;
    private Context mContext;
    private OnItemClickListener mListener;

    public ProfileAdapter(Context mContext, List<EventModel> mList) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ProfileHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(mContext).inflate(R.layout.item_history,parent, false);
        ProfileAdapter.ProfileHolder holder = new ProfileAdapter.ProfileHolder(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileHolder holder, @SuppressLint("RecyclerView") int position) {
        String idUser = FirebaseAuth.getInstance().getUid();

        EventModel eventModel = mList.get(position);
        holder.textViewTitle.setText(eventModel.getEventName());
        holder.textViewCategory.setText(eventModel.getCategory());
        holder.textViewStatus.setText("Status: "+eventModel.getStatus());
        Picasso.with(mContext)
                .load(eventModel.getImageUrl())
                .placeholder(R.drawable.blank_img)
                .fit()
                .centerCrop()
                .into(holder.imageViewEvent);

        if(eventModel.getIdUser().equals(idUser)) {
            holder.btnEdit.setVisibility(View.VISIBLE);
        } else {
            holder.btnEdit.setVisibility(View.GONE);
        }

        if(mListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.detailClick(position);
                }
            });

            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.deleteClick(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ProfileHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textViewTitle;
        public TextView textViewCategory;
        public TextView textViewStatus;
        public ImageView imageViewEvent;
        public Button btnEdit;
        public Button btnDelete;

        public ProfileHolder(View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.tvTitle);
            textViewCategory = itemView.findViewById(R.id.tvCategory);
            textViewStatus = itemView.findViewById(R.id.tvStatus);
            imageViewEvent = itemView.findViewById(R.id.ivHistory);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);

            itemView.setOnClickListener(this);
            btnEdit.setOnClickListener(this);
            btnDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION) {
                    mListener.detailClick(position);
                    mListener.showParticipantClick(position);
                    mListener.editClick(position);
                    mListener.deleteClick(position);
                }
            }
        }
    }

    public interface OnItemClickListener {
        void detailClick(int position);
        void showParticipantClick(int position);
        void editClick(int position);
        void deleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

}
