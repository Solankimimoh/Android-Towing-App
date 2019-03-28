package com.example.towinapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class ReceiveUserDetailsForVehicleActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private Intent intent;
    private String penltyPrice;
    private String pushKey;

    private EditText nameEd;
    private EditText emailEd;
    private EditText mobileEd;
    private EditText addressEd;
    private EditText extraAmountEd;
    private EditText extraAmountReasonEd;
    private EditText totalAmountEd;
    private Button chooseDocumentBtn;
    private Button choosePersonBtn;
    private Button addBtn;
    private int enterAmount;
    private Uri clickDocumentUri;
    private int CAMERA_REQUEST_DOCUMENT = 100;
    private int CAMERA_REQUEST_PERSON = 200;
    private Uri clickPersonUri;
    private String documentPhotoUrl;
    private String personPhotoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_user_details_for_vehicle);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        intent = getIntent();

        initView();

        penltyPrice = intent.getStringExtra(AppConfig.FIREBASE_KEY_PENLTY_PRICE);
        pushKey = intent.getStringExtra(AppConfig.FIREBASE_KEY_TOWING_VEHICLE_PUSH_KEY);


        totalAmountEd.setText(penltyPrice);


    }

    private void initView() {
        nameEd = findViewById(R.id.activity_receive_user_details_for_vehicle_name);
        emailEd = findViewById(R.id.activity_receive_user_details_for_vehicle_email);
        mobileEd = findViewById(R.id.activity_receive_user_details_for_vehicle_mobile);
        addressEd = findViewById(R.id.activity_receive_user_details_for_vehicle_address);
        extraAmountEd = findViewById(R.id.activity_receive_user_details_for_vehicle_extra_amount);
        extraAmountReasonEd = findViewById(R.id.activity_receive_user_details_for_vehicle_extra_amount_reason);
        totalAmountEd = findViewById(R.id.activity_receive_user_details_for_vehicle_total_amount);
        chooseDocumentBtn = findViewById(R.id.activity_receive_user_details_for_vehicle_document_btn);
        choosePersonBtn = findViewById(R.id.activity_receive_user_details_for_vehicle_person_btn);
        addBtn = findViewById(R.id.activity_receive_user_details_for_vehicle_btn);

        extraAmountEd.addTextChangedListener(this);


        chooseDocumentBtn.setOnClickListener(this);
        choosePersonBtn.setOnClickListener(this);
        addBtn.setOnClickListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() > 0) {
            Toast.makeText(this, "GOOD" + s.toString(), Toast.LENGTH_SHORT).show();
            int totalAmount = Integer.parseInt(totalAmountEd.getText().toString());
            totalAmount = totalAmount + Integer.parseInt(s.toString());
            totalAmountEd.setText(totalAmount + "");
        } else {
            totalAmountEd.setText(penltyPrice);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_receive_user_details_for_vehicle_document_btn:
                clickDocumentPhoto();
                break;
            case R.id.activity_receive_user_details_for_vehicle_person_btn:
                clickPersonPhoto();
                break;
            case R.id.activity_receive_user_details_for_vehicle_btn:
                uploadData();
                break;
        }
    }


    private void clickPersonPhoto() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            return;
        }
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST_PERSON);
    }

    private void clickDocumentPhoto() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            return;
        }
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST_DOCUMENT);

    }

    private void uploadData() {

        final StorageReference sRefDocument = storageReference.child("images/" + UUID.randomUUID());
        final StorageReference sRefPerson = storageReference.child("images/" + UUID.randomUUID());


        if (clickDocumentUri == null || clickPersonUri == null) {
            Toast.makeText(this, "Please Click the photos", Toast.LENGTH_SHORT).show();
            return;
        }

        sRefDocument.putFile(clickDocumentUri)
                .addOnSuccessListener(
                        new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                sRefDocument.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        documentPhotoUrl = uri.toString();
                                        Log.e("MYURL", documentPhotoUrl);
                                        sRefPerson.putFile(clickPersonUri)
                                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                        sRefPerson.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(Uri personUri) {
                                                                personPhotoUrl = personUri.toString();
                                                                Log.e("MYURL", personPhotoUrl);
                                                                insertInToDatabase(documentPhotoUrl, personPhotoUrl);
                                                            }
                                                        });
                                                    }
                                                });
                                    }
                                });
                            }
                        });
    }

    private void insertInToDatabase(String documentPhotoUrl, String personPhotoUrl) {

        final String name = nameEd.getText().toString().trim();
        final String email = emailEd.getText().toString().trim();
        final String mobile = mobileEd.getText().toString().trim();
        final String address = addressEd.getText().toString().trim();
        final String extraamount = extraAmountEd.getText().toString().trim();
        final String extraamountReason = extraAmountReasonEd.getText().toString().trim();
        final String totalAmount = totalAmountEd.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty()
                || mobile.isEmpty() || address.isEmpty() || extraamount.isEmpty() ||
                extraamountReason.isEmpty() || totalAmount.isEmpty()) {
            Toast.makeText(this, "Please fill the details", Toast.LENGTH_SHORT).show();
        } else {
            if (documentPhotoUrl.isEmpty() || personPhotoUrl.isEmpty()) {
                Toast.makeText(this, "Please Click the photos", Toast.LENGTH_SHORT).show();
                return;
            }

            final String uuid = firebaseAuth.getCurrentUser().getUid();
            Date date = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            String formattedDate = df.format(date);
            databaseReference
                    .child(AppConfig.FIREBASE_DB_RECEIVE_VEHICLE)
                    .push()
                    .setValue(new
                            ReceiveVehicleDetailsModel(documentPhotoUrl, personPhotoUrl,
                            name, mobile, email, address,
                            extraamount, extraamountReason, totalAmount, uuid, pushKey, formattedDate), new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference1) {

                            if (databaseError != null) {
                                Toast.makeText(ReceiveUserDetailsForVehicleActivity.this, "Error" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ReceiveUserDetailsForVehicleActivity.this, "Success", Toast.LENGTH_SHORT).show();

                                databaseReference
                                        .child(AppConfig.FIREBASE_DB_TOWING_VEHICLE)
                                        .child(pushKey)
                                        .child("receiveStatus")
                                        .setValue(true, new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                                                if (databaseError != null) {
                                                    Toast.makeText(ReceiveUserDetailsForVehicleActivity.this, "Error" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(ReceiveUserDetailsForVehicleActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                }
                                            }
                                        });

                            }
                        }
                    });

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_DOCUMENT && resultCode == RESULT_OK) {
            Toast.makeText(this, "HELLO", Toast.LENGTH_SHORT).show();
            //uploading the file
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP

            if (photo != null) {
                clickDocumentUri = getImageUri(getApplicationContext(), photo);
                chooseDocumentBtn.setText("Image Clicked Done");
            }

        } else if (requestCode == CAMERA_REQUEST_PERSON && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP

            if (photo != null) {
                clickPersonUri = getImageUri(getApplicationContext(), photo);
                choosePersonBtn.setText("Image Clicked Done");
            }
        } else {
            Toast.makeText(this, "No file chosen", Toast.LENGTH_SHORT).show();
        }
    }


    private Uri getImageUri(Context applicationContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        Bitmap OutImage = Bitmap.createScaledBitmap(inImage, 1000, 1000, true);
        OutImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), OutImage, "Title", null);
        return Uri.parse(path);

    }


}
