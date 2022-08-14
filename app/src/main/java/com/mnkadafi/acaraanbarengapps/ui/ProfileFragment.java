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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mnkadafi.acaraanbarengapps.DetailActivity;
import com.mnkadafi.acaraanbarengapps.model.EventModel;
import com.mnkadafi.acaraanbarengapps.LoginActivity;
import com.mnkadafi.acaraanbarengapps.adapter.ProfileAdapter;
import com.mnkadafi.acaraanbarengapps.R;
import com.mnkadafi.acaraanbarengapps.User;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;
    private FirebaseStorage mFirebaseStorage;

    private RecyclerView mRecyclerHistory;
    private ProfileAdapter mProfileAdapter;
    private ValueEventListener mDBListener;

    private ProgressBar mProgressBar;

    private List<EventModel> mItems = new ArrayList<>();
    private User userDetail;
    private TextView tvTotalEvent;
    private int totalEvent = 0;
    private int tvTotalSuccess = 0;


    private TextView tvFullname, tvLocation;
    private Button btnLogout, btnParticipant, btnEdit, btnDelete;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();

        tvFullname = root.findViewById(R.id.tvFullName);
        tvLocation = root.findViewById(R.id.tvLocation);
        tvTotalEvent = root.findViewById(R.id.tvTotalEvent);

        getProfile();

        btnParticipant = root.findViewById(R.id.btnParticipant);
        btnEdit = root.findViewById(R.id.btnEdit);
        btnDelete = root. findViewById(R.id.btnDelete);
        btnLogout = root.findViewById(R.id.btnLogout);

        mRecyclerHistory = root.findViewById(R.id.recyclerHistory);
        mRecyclerHistory.setHasFixedSize(true);
        mRecyclerHistory.setLayoutManager(new LinearLayoutManager(getParentFragment().getContext()));

        mProfileAdapter = new ProfileAdapter(getContext(), mItems);
        mProfileAdapter.setOnItemClickListener(new ProfileAdapter.OnItemClickListener() {

            @Override
            public void detailClick(int position) {
                detailEvent(mItems.get(position));
            }

            @Override
            public void showParticipantClick(int position) {
                showParticipant(position);
            }

            @Override
            public void editClick(int position) {
                editEvent(position);
            }

            @Override
            public void deleteClick(int position) {
                deleteEvent(position);
            }
        });

        mRecyclerHistory.setAdapter(mProfileAdapter);

        mFirebaseStorage = FirebaseStorage.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Events");
        mDBListener = mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mItems.clear();
                totalEvent = 0;

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    EventModel eventModel = postSnapshot.getValue(EventModel.class);
                    eventModel.setKey(postSnapshot.getKey());

                    if(eventModel.getIdUser().equals(mAuth.getCurrentUser().getUid())) {
                        totalEvent += 1;
                        mItems.add(eventModel);
                    }
                }

                tvTotalEvent.setText(String.valueOf(totalEvent));

                mProfileAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        return root;
    }

    private void detailEvent(EventModel eventModel) {
        Intent detailIntent = new Intent(getActivity(), DetailActivity.class);
        detailIntent.putExtra("eventDetail", eventModel);
        startActivity(detailIntent);
    }

    private void showParticipant(int position) {

    }

    private void editEvent(int position) {

    }

    private void deleteEvent(int position) {
        EventModel selectedItem = mItems.get(position);
        String selectedKey = selectedItem.getKey();

        StorageReference imageRef = mFirebaseStorage.getReferenceFromUrl(selectedItem.getImageUrl());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                mDatabaseReference.child(selectedKey).removeValue();
                Toast.makeText(getActivity(), "Event Deleted Successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getProfile() {
        String userID = mAuth.getCurrentUser().getUid();
        DatabaseReference userData = FirebaseDatabase.getInstance().getReference("Users");
        userData.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User dataUser = snapshot.getValue(User.class);

                tvFullname.setText(dataUser.getFullName());
                tvLocation.setText(dataUser.getLocation());

                userDetail = dataUser;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDatabaseReference.removeEventListener(mDBListener);
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }
}