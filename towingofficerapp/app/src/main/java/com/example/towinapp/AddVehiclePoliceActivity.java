package com.example.towinapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class AddVehiclePoliceActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {


    private static final int CAMERA_REQUEST = 100;
    private EditText vehicleNumberEd;
    private EditText towingAreaEd;
    private Spinner zonalAreaSp;
    private Spinner vehicleTypeSp;
    private Button clickImage;
    private Button addBtn;
    private Uri selectedFileIntent;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private ArrayList<String> zonalNameArrayList;
    private ArrayList<ZonalModel> zonalModelArrayList;
    private ArrayList<PenltyPriceModel> penltyPriceModelArrayList;
    private ArrayAdapter stringArrayAdapter;

    private String zonalPushKey = "";
    private String penltyPrice = "";
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle_police);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(AddVehiclePoliceActivity.this);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        zonalNameArrayList = new ArrayList<>();
        zonalModelArrayList = new ArrayList<>();
        penltyPriceModelArrayList = new ArrayList<>();

        initView();


        stringArrayAdapter = new ArrayAdapter<String>(AddVehiclePoliceActivity.this, android.R.layout.simple_list_item_1, zonalNameArrayList);
        zonalAreaSp.setAdapter(stringArrayAdapter);


        databaseReference.child(AppConfig.FIREBASE_DB_ZONAL_ADDRESS)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot zonalModelSnapshot : dataSnapshot.getChildren()) {
                            ZonalModel zonalModel = zonalModelSnapshot.getValue(ZonalModel.class);
                            zonalNameArrayList.add(zonalModel.getZonalName());
                            zonalModel.setPushKey(zonalModelSnapshot.getKey());
                            zonalModelArrayList.add(zonalModel);
                        }
                        stringArrayAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


        databaseReference.child(AppConfig.FIREBASE_DB_PENLTY_PRICE)
                .child("prices")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        PenltyPriceModel penltyPriceModel = dataSnapshot.getValue(PenltyPriceModel.class);
                        penltyPriceModelArrayList.add(penltyPriceModel);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    private void initView() {

        vehicleNumberEd = findViewById(R.id.activity_add_vehicle_number_ed);
        towingAreaEd = findViewById(R.id.activity_add_vehicle_towing_area_ed);
        zonalAreaSp = findViewById(R.id.activity_add_vehicle_zonal_sp);
        vehicleTypeSp = findViewById(R.id.activity_add_vehicle_type_sp);

        clickImage = findViewById(R.id.activity_add_vehicle_choose_camera_btn);
        addBtn = findViewById(R.id.activity_add_vehicle_btn);


        clickImage.setOnClickListener(this);
        addBtn.setOnClickListener(this);

        zonalAreaSp.setOnItemSelectedListener(this);
        vehicleTypeSp.setOnItemSelectedListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.activity_add_vehicle_choose_camera_btn:
                clickImage();
                break;
            case R.id.activity_add_vehicle_btn:
                uploadData();
                break;
        }
    }

    private void uploadData() {
        progressDialog.setTitle("Add Vehicle");
        progressDialog.setMessage("Image Uploading..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        if (selectedFileIntent != null && !penltyPrice.equals("")) {
            final StorageReference sRef = storageReference.child("images/" + UUID.randomUUID());
            sRef.putFile(selectedFileIntent)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    insertInToDatabase(uri.toString());
                                }
                            });
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    progressDialog.setMessage("Uploading.. " + progress);
                }
            });
        } else {
            progressDialog.dismiss();
            Toast.makeText(this, "Please click the picture", Toast.LENGTH_SHORT).show();
        }


    }

    private void insertInToDatabase(String photoUrl) {

        progressDialog.setMessage("Data inserting..");
        final String vehicleNumber = vehicleNumberEd.getText().toString().trim();
        final String towingArea = towingAreaEd.getText().toString().trim();

        if (!vehicleNumber.isEmpty() || !towingArea.isEmpty()) {

            Date date = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            SimpleDateFormat df1 = new SimpleDateFormat("hh:mm:ss a");
            String formattedDate = df.format(date);
            String formattedTime = df1.format(date);

            final String uuid = firebaseAuth.getCurrentUser().getUid();

            databaseReference
                    .child(AppConfig.FIREBASE_DB_TOWING_VEHICLE)
                    .push()
                    .setValue(new AddVehicleModel(vehicleNumber, photoUrl,
                                    towingArea, zonalPushKey, formattedDate, formattedTime
                                    , random(), penltyPrice, uuid, false, false)
                            , new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                    if (databaseError != null) {
                                        progressDialog.dismiss();
                                        Toast.makeText(AddVehiclePoliceActivity.this, "Error " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(AddVehiclePoliceActivity.this, "Add Success", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }
                            });

        } else {
            progressDialog.dismiss();
            Toast.makeText(this, "Please fill the details", Toast.LENGTH_SHORT).show();
        }

    }

    public static String random() {
        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(7);

        for (int i = 0; i < 7; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int) (AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }

    private void clickImage() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            return;
        }
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            //uploading the file
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP

            if (photo != null) {
                selectedFileIntent = getImageUri(getApplicationContext(), photo);
                Log.e("TUMUM", selectedFileIntent + "");
                clickImage.setText("Image Clicked Done");
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


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {
            case R.id.activity_add_vehicle_zonal_sp:
                zonalPushKey = zonalModelArrayList.get(position).getPushKey();
                break;
            case R.id.activity_add_vehicle_type_sp:
                getPenltyPrice(position);
                break;
        }
    }

    private void getPenltyPrice(int position) {
        if (penltyPriceModelArrayList.size() > 0) {
            if (position == 0) {
                penltyPrice = penltyPriceModelArrayList.get(0).getFourWheelerPenelty();
            } else if (position == 1) {
                penltyPrice = penltyPriceModelArrayList.get(0).getTwoWheelerPenelty();
            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
