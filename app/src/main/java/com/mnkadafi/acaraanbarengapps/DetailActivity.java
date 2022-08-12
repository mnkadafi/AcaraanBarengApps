package com.mnkadafi.acaraanbarengapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.metrics.Event;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mnkadafi.acaraanbarengapps.model.BookmarkModel;
import com.mnkadafi.acaraanbarengapps.model.EventModel;
import com.mnkadafi.acaraanbarengapps.model.EventParticipantModel;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference, mBookmarkReference;
    private BookmarkModel bookmarkData;
    private EventParticipantModel eventParticipantData;

    private Button btnBackHome, btnBookmark, btnEdit, btnJoinEvent;
    private ImageView ivEvent;
    private TextView tvCreator, tvCategory, tvEvent, tvLocation, tvParticipant;
    private TextView tvDateStart, tvCost, tvRequirement, tvDescription;
    private Boolean isParticipant = false;
    private Boolean isMarked = false;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_detail);

        mAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("EventParticipant");
        mBookmarkReference = FirebaseDatabase.getInstance().getReference("BookmarkEvent");

        btnBackHome     = findViewById(R.id.btnBackHome);
        btnBookmark     = findViewById(R.id.btnBookmark);
        btnEdit         = findViewById(R.id.btnEdit);
        btnJoinEvent    = findViewById(R.id.btnJoinEvent);

        tvCreator       = findViewById(R.id.tvCreator);
        ivEvent         = findViewById(R.id.ivEvent);
        tvCategory      = findViewById(R.id.tvCategory);
        tvEvent         = findViewById(R.id.tvEvent);
        tvLocation      = findViewById(R.id.tvLocation);
        tvParticipant   = findViewById(R.id.tvParticipant);
        tvDateStart     = findViewById(R.id.tvDateStart);
        tvCost          = findViewById(R.id.tvCost);
        tvRequirement   = findViewById(R.id.tvRequirement);
        tvDescription   = findViewById(R.id.tvDescription);

        EventModel eventDetail = getIntent().getParcelableExtra("eventDetail");

        Picasso.with(mContext)
                .load(eventDetail.getImageUrl())
                .placeholder(R.drawable.blank_img)
                .fit()
                .centerCrop()
                .into(ivEvent);

        getProfile();
        tvCategory.setText(eventDetail.getCategory());
        tvEvent.setText(eventDetail.getEventName());
        tvLocation.setText(eventDetail.getLocation());
        tvParticipant.setText(eventDetail.getMinParticipant()+"-"+eventDetail.getMaxParticipant());
        tvDateStart.setText(eventDetail.getDateStart());
        tvCost.setText(eventDetail.getCost());
        tvRequirement.setText(eventDetail.getRequirement());
        tvDescription.setText(eventDetail.getDescription());

        getDetailBookmark(eventDetail.getIdEvent());

        if(eventDetail.getIdUser().equals(mAuth.getCurrentUser().getUid())) {
            btnJoinEvent.setText("SEE PARTICIPANT");
            isParticipant = true;
        } else {
            btnEdit.setVisibility(View.GONE);
            if(eventDetail.getStatus().equals("waiting")) {
                checkParticipant(eventDetail.getIdEvent());
            } else {
                btnJoinEvent.setText("NO LONGER PARTICIPANT");
            }
        }

         btnJoinEvent.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 if(isParticipant) {
                     Intent detailIntent = new Intent(DetailActivity.this, ParticipantActivity.class);
                     detailIntent.putExtra("eventDetail", eventDetail);
                     startActivity(detailIntent);
                 } else {
                     if(!eventDetail.getIdUser().equals(mAuth.getCurrentUser().getUid())) {
                         addJoinEvent(eventDetail.getIdEvent());
                     }
                 }
             }
         });

        btnBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBookmarkEvent(eventDetail);
            }
        });

        btnBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DetailActivity.this, MainActivity.class));
            }
        });
    }

    private void getDetailBookmark(String eventId) {
        String userId = mAuth.getCurrentUser().getUid();
        DatabaseReference dataBookmarkEvent = FirebaseDatabase.getInstance().getReference("BookmarkEvent");
        dataBookmarkEvent.orderByChild("idUser").equalTo(userId);
        dataBookmarkEvent.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        BookmarkModel detailBookmark = postSnapshot.getValue(BookmarkModel.class);
                        if(detailBookmark.getIdEvent().equals(eventId)) {
                            bookmarkData = detailBookmark;
                            btnBookmark.setVisibility(View.GONE);
                            isMarked = true;
                        } else {
                            btnBookmark.setVisibility(View.VISIBLE);
                            isMarked = false;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkParticipant(String eventId) {
        String userId = mAuth.getCurrentUser().getUid();
        DatabaseReference dataEventParticipant = FirebaseDatabase.getInstance().getReference("EventParticipant");
        dataEventParticipant.orderByChild("idUser").equalTo(userId);
        dataEventParticipant.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        EventParticipantModel detailEventParticipant = postSnapshot.getValue(EventParticipantModel.class);
                        if(detailEventParticipant.getIdEvent().equals(eventId) && detailEventParticipant.getIdUser().equals(userId)) {
                            eventParticipantData = detailEventParticipant;

                            btnJoinEvent.setText("SEE PARTICIPANT");
                            isParticipant = true;
                        } else {
                            btnJoinEvent.setText("JOIN EVENT");
                            isParticipant = false;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addBookmarkEvent(EventModel model) {
        String bookmarkEventId = mBookmarkReference.push().getKey();
        String idUser = mAuth.getCurrentUser().getUid();

        BookmarkModel bookmarkEvent = new BookmarkModel(bookmarkEventId, idUser, model.getIdEvent(),
                model.getEventName(), model.getCategory(), model.getImageUrl());
        mBookmarkReference
                .child(bookmarkEventId)
                .setValue(bookmarkEvent).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {;
                    Toast.makeText(DetailActivity.this, "Bookmark Event Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DetailActivity.this, "User Failed to Bookmark"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addJoinEvent(String idEvent) {
        String eventParticipantId = mDatabaseReference.push().getKey();
        EventParticipantModel eventParticipant = new EventParticipantModel(eventParticipantId, idEvent, mAuth.getCurrentUser().getUid());

        mDatabaseReference
                .child(eventParticipantId)
                .setValue(eventParticipant).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {;
                    Toast.makeText(DetailActivity.this, "Join Event Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(DetailActivity.this, MainActivity.class));
                } else {
                    Toast.makeText(DetailActivity.this, "User Failed to Join"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
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

                tvCreator.setText("Posted By: "+dataUser.getFullName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}