package com.mnkadafi.acaraanbarengapps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.mnkadafi.acaraanbarengapps.model.EventModel;
import com.mnkadafi.acaraanbarengapps.model.EventParticipantModel;
import com.squareup.picasso.Picasso;

public class PostActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    StorageReference storageRef;
    DatabaseReference databaseReference, databaseReferenceParticipan;
    StorageTask uploadTask;

    EditText edtNameEvent, edtLocation, edtDateStart, edtCost, edtQuotaParticipant;
    EditText edtMinimalParticipant, edtRequirement, edtDescription;
    Spinner spCategory;
    ImageView ivEvent;
    Button btnBackHome, btnUploadImage, btnAddPost;
    ProgressBar progressBar;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_post);

        mAuth = FirebaseAuth.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference("uploads");
        databaseReference = FirebaseDatabase.getInstance().getReference("Events");
        databaseReferenceParticipan = FirebaseDatabase.getInstance().getReference("EventParticipant");

        ivEvent = findViewById(R.id.ivEvent);
        edtNameEvent = findViewById(R.id.edtNameEvent);
        spCategory = findViewById(R.id.spCategory);
        edtLocation = findViewById(R.id.edtLocation);
        edtDateStart = findViewById(R.id.edtDateStart);
        edtQuotaParticipant = findViewById(R.id.edtQuotaParticipant);
        edtMinimalParticipant = findViewById(R.id.edtMinimalParticipant);
        edtCost = findViewById(R.id.edtCost);
        edtRequirement = findViewById(R.id.edtRequirement);
        edtDescription = findViewById(R.id.edtDescription);

        progressBar = findViewById(R.id.progressBar);

        btnUploadImage = findViewById(R.id.btnUploadImage);
        btnBackHome = findViewById(R.id.btnBackHome);
        btnAddPost = findViewById(R.id.btnAddPost);

        btnBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PostActivity.this, MainActivity.class));
            }
        });

        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectUploadImage();
            }
        });

        btnAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(uploadTask != null && uploadTask.isInProgress()) {
                    Toast.makeText(PostActivity.this, "Posting event in progress ", Toast.LENGTH_SHORT).show();
                } else {
                    addNewEvent();
                }
            }
        });
    }

    private String getExtensionFile(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void addNewEvent() {
        if(imageUri != null) {
            StorageReference fileReference = storageRef.child(System.currentTimeMillis()+"."+getExtensionFile(imageUri));

            String name = edtNameEvent.getText().toString().trim();
            String category = spCategory.getSelectedItem().toString().trim();
            String location = edtLocation.getText().toString().trim();
            String dateStart = edtDateStart.getText().toString().trim();
            String cost = edtCost.getText().toString().trim();
            String maxParticipant = edtQuotaParticipant.getText().toString().trim();
            String minParticipant = edtMinimalParticipant.getText().toString().trim();
            String requirement = edtRequirement.getText().toString().trim();
            String description = edtDescription.getText().toString().trim();

            uploadTask = fileReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setProgress(0);
                                }
                            }, 500);

                            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Uri downloadUrl = uri;

                                    String eventId = databaseReference.push().getKey();
                                    String eventParticipant = databaseReferenceParticipan.push().getKey();

                                    EventModel event = new EventModel(
                                            eventId, name, downloadUrl.toString(),
                                            category, location, dateStart, cost, minParticipant,
                                            maxParticipant, requirement, description,
                                            "waiting", mAuth.getCurrentUser().getUid());

                                    EventParticipantModel eventParticipantModel = new EventParticipantModel(
                                            eventParticipant, eventId, mAuth.getCurrentUser().getUid()
                                    );

                                    databaseReference.child(eventId).setValue(event);
                                    databaseReferenceParticipan.child(eventParticipant).setValue(eventParticipantModel);

                                    Toast.makeText(PostActivity.this, "Event Posted Successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(PostActivity.this, MainActivity.class));
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(PostActivity.this, "Event Failed to Post", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(PostActivity.this, MainActivity.class));
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(PostActivity.this, "Event Failed to Post", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(PostActivity.this, MainActivity.class));
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            progressBar.setVisibility(View.VISIBLE);

                            double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                            progressBar.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(this, "No File Selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void selectUploadImage() {
        final CharSequence[] items = {"Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(PostActivity.this);
        builder.setTitle(getString(R.string.app_name));
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setItems(items, (dialog, item) -> {
           if(items[item].equals("Choose from Library")) {
               Intent intent = new Intent(Intent.ACTION_PICK);
               intent.setType("image/*");
               startActivityForResult(Intent.createChooser(intent, "Select Image"), 20);
           } else if(items[item].equals("Cancel")) {
               dialog.dismiss();
           }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 20 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();

            Picasso.with(this).load(imageUri).into(ivEvent);

//            final Uri path = data.getData();
//            Thread thread = new Thread(() -> {
//               try {
//                   InputStream inputStream = getContentResolver().openInputStream(path);
//                   Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                   imageUri = data.getData();
//
//                   ivEvent.post(() -> {
//                      ivEvent.setImageBitmap(bitmap);
//                   });
//               } catch (IOException e) {
//                   e.printStackTrace();
//               }
//            });
//            thread.start();
        }
    }
}