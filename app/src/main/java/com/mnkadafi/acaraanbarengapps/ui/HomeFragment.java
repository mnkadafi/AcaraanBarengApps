package com.mnkadafi.acaraanbarengapps.ui;

// Tanggal 4 Agustus 2022
// NIM  : 10119197
// Nama : Mochamad Nurkhayal Kadafi
// Kelas: IF-5

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mnkadafi.acaraanbarengapps.DetailActivity;
import com.mnkadafi.acaraanbarengapps.adapter.HomeAdapter;
import com.mnkadafi.acaraanbarengapps.model.EventModel;
import com.mnkadafi.acaraanbarengapps.PostActivity;
import com.mnkadafi.acaraanbarengapps.R;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;

    private RecyclerView mRecyclerEvent;
    private HomeAdapter mHomeAdapter;
    private ValueEventListener mDBListener;

    private ProgressBar mProgressBar;

    private List<EventModel> mItems = new ArrayList<>();
    Button btnAddPost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        mAuth = FirebaseAuth.getInstance();

        mProgressBar = root.findViewById(R.id.progressBar);

        mRecyclerEvent = root.findViewById(R.id.recyclerEvent);
        mRecyclerEvent.setHasFixedSize(true);
        mRecyclerEvent.setLayoutManager(new LinearLayoutManager(getParentFragment().getContext()));

        mHomeAdapter = new HomeAdapter(getContext(), mItems);
        mHomeAdapter.setOnItemClickListener(new HomeAdapter.OnItemClickListenerHome() {
            @Override
            public void detailClick(int position) {
                detailEvent(mItems.get(position));
            }
        });
        mRecyclerEvent.setAdapter(mHomeAdapter);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Events");
        mDBListener = mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mItems.clear();

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    EventModel eventModel = postSnapshot.getValue(EventModel.class);
                    mItems.add(eventModel);
                }

                mHomeAdapter.notifyDataSetChanged();
                mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });

        btnAddPost = root.findViewById(R.id.btnAddPost);

        btnAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), PostActivity.class));
            }
        });

        return root;
    }

    private void detailEvent(EventModel eventModel) {
        Intent detailIntent = new Intent(getActivity(), DetailActivity.class);
        detailIntent.putExtra("eventDetail", eventModel);
        startActivity(detailIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDatabaseReference.removeEventListener(mDBListener);
    }
}