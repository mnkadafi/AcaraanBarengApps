package com.mnkadafi.acaraanbarengapps.ui;

// Tanggal 4 Agustus 2022
// NIM  : 10119197
// Nama : Mochamad Nurkhayal Kadafi
// Kelas: IF-5

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mnkadafi.acaraanbarengapps.adapter.BookmarkAdapter;
import com.mnkadafi.acaraanbarengapps.model.BookmarkModel;
import com.mnkadafi.acaraanbarengapps.R;

import java.util.ArrayList;
import java.util.List;

public class BookmarkFragment extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;

    private RecyclerView mRecyclerEvent;
    private BookmarkAdapter mBookmarkAdapter;
    private ValueEventListener mDBListener;
    private ProgressBar mProgressBar;

    private List<BookmarkModel> mItems = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_bookmark, container, false);

        mAuth = FirebaseAuth.getInstance();
        mProgressBar = root.findViewById(R.id.progressBar);

        mRecyclerEvent = root.findViewById(R.id.recyclerBookmark);
        mRecyclerEvent.setHasFixedSize(true);
        mRecyclerEvent.setLayoutManager(new LinearLayoutManager(getParentFragment().getContext()));

        mBookmarkAdapter = new BookmarkAdapter(getContext(), mItems);
        mBookmarkAdapter.setOnItemClickListener(new BookmarkAdapter.OnItemClickListenerBookmark() {
            @Override
            public void detailClick(int position) {
                detailEvent(mItems.get(position));
            }
        });
        mRecyclerEvent.setAdapter(mBookmarkAdapter);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("BookmarkEvent");
        mDBListener = mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mItems.clear();

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    BookmarkModel bookmarkModel = postSnapshot.getValue(BookmarkModel.class);
                    mItems.add(bookmarkModel);
                }

                mBookmarkAdapter.notifyDataSetChanged();
                mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });

        return root;
    }

    private void detailEvent(BookmarkModel bookmarkModel) {
//        Intent detailIntent = new Intent(getActivity(), DetailActivity.class);
//        detailIntent.putExtra("eventDetail", bookmarkModel);
//        startActivity(detailIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDatabaseReference.removeEventListener(mDBListener);
    }
}