package com.example.towingapp;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ZonalOfficerTransferActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {


    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private Spinner zonalUserSpinner;
    private Spinner zonalAddressSpinner;
    private Button updateBtn;
    private ArrayList<String> zonalOfficerStringArrayList;
    private ArrayList<ZonalOfficerModel> zonalOfficerModelArrayList;
    private ArrayList<String> zonalStringArrayList;
    private ArrayList<ZonalModel> zonalModelArrayList;

    private String zonalPushKey;
    private String zonalOfficerPushkey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zonal_officer_transfer);


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        zonalOfficerStringArrayList = new ArrayList<>();
        zonalOfficerModelArrayList = new ArrayList<>();
        zonalStringArrayList = new ArrayList<>();
        zonalModelArrayList = new ArrayList<>();

        zonalUserSpinner = findViewById(R.id.activity_zonal_officer_transfer_officer_sp);
        zonalAddressSpinner = findViewById(R.id.activity_zonal_officer_transfer_location_sp);
        updateBtn = findViewById(R.id.activity_zonal_officer_transfer_btn);

        zonalUserSpinner.setOnItemSelectedListener(this);
        zonalAddressSpinner.setOnItemSelectedListener(this);
        updateBtn.setOnClickListener(this);

        final ArrayAdapter<String> zonalofficerArrayAdapter = new ArrayAdapter<>(ZonalOfficerTransferActivity.this,
                android.R.layout.simple_list_item_1, zonalOfficerStringArrayList);
        zonalUserSpinner.setAdapter(zonalofficerArrayAdapter);

        final ArrayAdapter<String> zonalArrayAdapter = new ArrayAdapter<>(ZonalOfficerTransferActivity.this,
                android.R.layout.simple_list_item_1, zonalStringArrayList);
        zonalAddressSpinner.setAdapter(zonalArrayAdapter);

        databaseReference
                .child(AppConfig.FIREBASE_DB_ZONAL_OFFICER)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        zonalOfficerModelArrayList.clear();
                        zonalOfficerStringArrayList.clear();
                        for (DataSnapshot zonalOfficerSnapshot : dataSnapshot.getChildren()) {
                            ZonalOfficerModel zonalOfficerModel = zonalOfficerSnapshot.getValue(ZonalOfficerModel.class);
                            zonalOfficerModel.setUuid(zonalOfficerSnapshot.getKey());
                            zonalOfficerModelArrayList.add(zonalOfficerModel);
                            zonalOfficerStringArrayList.add(zonalOfficerModel.getName());
                        }
                        zonalofficerArrayAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        databaseReference
                .child(AppConfig.FIREBASE_DB_ZONAL_ADDRESS)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot zonalModelSnapshot : dataSnapshot.getChildren()) {
                            ZonalModel zonalModel = zonalModelSnapshot.getValue(ZonalModel.class);
                            zonalModel.setPushKey(zonalModelSnapshot.getKey());
                            zonalModelArrayList.add(zonalModel);
                            zonalStringArrayList.add(zonalModel.getZonalName());
                        }
                        zonalArrayAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    @Override
    public void onClick(View v) {

        databaseReference
                .child(AppConfig.FIREBASE_DB_ZONAL_OFFICER)
                .child(zonalOfficerPushkey)
                .child("zonalPushKey")
                .setValue(zonalPushKey, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            Toast.makeText(ZonalOfficerTransferActivity.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ZonalOfficerTransferActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.activity_zonal_officer_transfer_officer_sp:
                zonalOfficerPushkey = zonalOfficerModelArrayList.get(position).getUuid();
                Toast.makeText(this, "" + zonalOfficerPushkey, Toast.LENGTH_SHORT).show();
                break;
            case R.id.activity_zonal_officer_transfer_location_sp:
                zonalPushKey = zonalModelArrayList.get(position).getPushKey();
                Toast.makeText(this, "" + zonalPushKey, Toast.LENGTH_SHORT).show();
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
