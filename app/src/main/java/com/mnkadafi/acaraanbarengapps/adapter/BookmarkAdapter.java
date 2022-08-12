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
import com.mnkadafi.acaraanbarengapps.model.BookmarkModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.BookmarkHolder> {
    private List<BookmarkModel> mList;
    private Context mContext;
    private OnItemClickListenerBookmark mListener;

    public BookmarkAdapter(Context mContext, List<BookmarkModel> mList) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public BookmarkHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(mContext).inflate(R.layout.item_bookmark,parent, false);
        BookmarkHolder holder = new BookmarkHolder(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BookmarkHolder holder, @SuppressLint("RecyclerView") int position) {
        BookmarkModel bookmarkModel = mList.get(position);
        holder.textViewTitle.setText(bookmarkModel.getEventName());
        holder.textViewCategory.setText(bookmarkModel.getCategory());
        Picasso.with(mContext)
                .load(bookmarkModel.getImageUrl())
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

    public class BookmarkHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textViewTitle;
        public TextView textViewCategory;
        public ImageView imageViewEvent;

        public BookmarkHolder(View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.tvTitle);
            textViewCategory = itemView.findViewById(R.id.tvCategory);
            imageViewEvent = itemView.findViewById(R.id.ivBookmark);

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

    public interface OnItemClickListenerBookmark {
        void detailClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListenerBookmark listener) {
        mListener = listener;
    }
}
