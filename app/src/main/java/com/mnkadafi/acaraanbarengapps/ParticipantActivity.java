package com.mnkadafi.acaraanbarengapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mnkadafi.acaraanbarengapps.adapter.BookmarkAdapter;
import com.mnkadafi.acaraanbarengapps.adapter.ParticipantAdapter;
import com.mnkadafi.acaraanbarengapps.model.BookmarkModel;
import com.mnkadafi.acaraanbarengapps.model.EventModel;
import com.mnkadafi.acaraanbarengapps.model.EventParticipantModel;

import java.util.ArrayList;
import java.util.List;

public class ParticipantActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUser, mDatabaseEventParticipant;
    private RecyclerView mRecyclerParticipant;
    private ParticipantAdapter mParticipantAdapter;

    private List<User> mList = new ArrayList<>();

    private TextView eventName;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_participant);

        mAuth = FirebaseAuth.getInstance();
        mDatabaseUser = FirebaseDatabase.getInstance().getReference("Users");
        mDatabaseEventParticipant = FirebaseDatabase.getInstance().getReference("EventParticipant");

        eventName = findViewById(R.id.tvEventName);
        btnBack = findViewById(R.id.btnBackHome);

        mRecyclerParticipant = findViewById(R.id.recyclerParticipant);
        mRecyclerParticipant.setHasFixedSize(true);
        mRecyclerParticipant.setLayoutManager(new LinearLayoutManager(this));

        EventModel eventDetail = getIntent().getParcelableExtra("eventDetail");

        eventName.setText(eventDetail.getEventName());

        mParticipantAdapter = new ParticipantAdapter(this, mList);
        mRecyclerParticipant.setAdapter(mParticipantAdapter);

        getDetailEventParticipant(eventDetail.getIdEvent());

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailIntent = new Intent(ParticipantActivity.this, DetailActivity.class);
                detailIntent.putExtra("eventDetail", eventDetail);
                startActivity(detailIntent);
            }
        });
    }

    private void getDetailEventParticipant(String eventId) {
        String userId = mAuth.getCurrentUser().getUid();

        mDatabaseEventParticipant.child(eventId);
        mDatabaseEventParticipant.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mList.clear();

                if(snapshot.exists()) {
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        EventParticipantModel eventParticipantModel = postSnapshot.getValue(EventParticipantModel.class);

                        mDatabaseUser.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshots) {
                                if(snapshots.exists()) {
                                    for (DataSnapshot postSnapshots : snapshots.getChildren()) {
                                        User userList = postSnapshots.getValue(User.class);

                                        if(eventParticipantModel.getIdUser().equals(userList.getIdUser())) {
                                            if(!mList.contains(userList.getIdUser())) {
                                                mList.add(userList);
                                            }
                                        }
                                    }

                                    mParticipantAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}