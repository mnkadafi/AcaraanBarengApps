package com.mnkadafi.acaraanbarengapps.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mnkadafi.acaraanbarengapps.R;
import com.mnkadafi.acaraanbarengapps.model.EventModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeHolder> {
    private List<EventModel> mList;
    private Context mContext;
    private OnItemClickListenerHome mListener;

    public HomeAdapter(Context mContext, List<EventModel> mList) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public HomeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(mContext).inflate(R.layout.item_event,parent, false);
        HomeHolder holder = new HomeHolder(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HomeHolder holder, @SuppressLint("RecyclerView") int position) {
        EventModel eventModel = mList.get(position);
        holder.textViewTitle.setText(eventModel.getEventName());
        holder.textViewLocation.setText(eventModel.getLocation());
        holder.textViewCategory.setText(eventModel.getCategory());
        Picasso.with(mContext)
                .load(eventModel.getImageUrl())
                .placeholder(R.drawable.blank_img)
                .fit()
                .centerCrop()
                .into(holder.imageViewEvent);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.detailClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class HomeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textViewTitle;
        public TextView textViewLocation;
        public TextView textViewCategory;
        public ImageView imageViewEvent;

        public HomeHolder(View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.tvTitle);
            textViewLocation = itemView.findViewById(R.id.tvLocation);
            textViewCategory = itemView.findViewById(R.id.tvCategory);
            imageViewEvent = itemView.findViewById(R.id.ivEvent);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION) {
                    mListener.detailClick(position);
                }
            }
        }
    }

    public interface OnItemClickListenerHome {
        void detailClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListenerHome listener) {
        mListener = listener;
    }
}
