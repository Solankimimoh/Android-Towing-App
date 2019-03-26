package com.example.towingapp;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddZonalOfficerActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {


    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private EditText nameEd;
    private EditText emailEd;
    private EditText passwordEd;
    private EditText mobileEd;
    private EditText policeEd;
    private Button addBtn;
    private Spinner zonalSpinner;

    private String pushKey = "";
    private ProgressDialog progressDialog;

    private ArrayList<String> zonalNameArrayList;
    private ArrayAdapter<String> stringArrayAdapter;
    private ArrayList<ZonalModel> zonalModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_zonal_officer);

        initView();
        zonalNameArrayList = new ArrayList<>();
        zonalModelArrayList = new ArrayList<>();
        stringArrayAdapter = new ArrayAdapter<String>(AddZonalOfficerActivity.this, android.R.layout.simple_list_item_1, zonalNameArrayList);
        zonalSpinner.setAdapter(stringArrayAdapter);

        databaseReference.child(AppConfig.FIREBASE_DB_ZONAL_ADDRESS)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot zonalModelSnapshot : dataSnapshot.getChildren()) {
                            ZonalModel zonalModel = zonalModelSnapshot.getValue(ZonalModel.class);
                            zonalNameArrayList.add(zonalModel.getZonalName());
                            zonalModel.setPushKey(zonalModelSnapshot.getKey());
                            zonalModelArrayList.add(zonalModel);
                            Log.e("RAJ", zonalModelArrayList.get(0).getPushKey());
                        }
                        stringArrayAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    private void initView() {

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        nameEd = findViewById(R.id.activity_add_zonal_officer_name_ed);
        emailEd = findViewById(R.id.activity_add_zonal_officer_email_ed);
        passwordEd = findViewById(R.id.activity_add_zonal_officer_password_ed);
        mobileEd = findViewById(R.id.activity_add_zonal_officer_mobile_ed);
        policeEd = findViewById(R.id.activity_add_zonal_officer_id_ed);
        addBtn = findViewById(R.id.activity_add_zonal_officer_btn);
        zonalSpinner = findViewById(R.id.activity_add_zonal_officer_zonal_sp);

        addBtn.setOnClickListener(this);
        zonalSpinner.setOnItemSelectedListener(this);

    }


    @Override
    public void onClick(View v) {

        final String name = nameEd.getText().toString().trim();
        final String email = emailEd.getText().toString().trim();
        final String password = passwordEd.getText().toString().trim();
        final String mobile = mobileEd.getText().toString().trim();
        final String policeId = policeEd.getText().toString().trim();


        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || mobile.isEmpty() || policeId.isEmpty()) {
            Toast.makeText(this, "Please insert the details", Toast.LENGTH_SHORT).show();
        } else {
            firebaseAuth
                    .createUserWithEmailAndPassword(email, password).addOnCompleteListener(AddZonalOfficerActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(AddZonalOfficerActivity.this, "Error " + task.getException(), Toast.LENGTH_SHORT).show();
                    } else {
                        insertIntoDatabase(name, email, password, mobile, policeId);
                    }
                }
            });
        }

    }

    private void insertIntoDatabase(String name, String email, String password, String mobile, String policeId) {

        final String uuid = firebaseAuth.getCurrentUser().getUid();

        databaseReference
                .child(AppConfig.FIREBASE_DB_ZONAL_OFFICER)
                .child(uuid)
                .setValue(new ZonalOfficerModel(name, email, password, mobile, policeId, pushKey), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            Toast.makeText(AddZonalOfficerActivity.this, "Error : " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AddZonalOfficerActivity.this, "Inserted Successfully ", Toast.LENGTH_SHORT).show();
                            firebaseAuth.signOut();
                            finish();
                        }
                    }
                });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (zonalModelArrayList.size() > 0) {
            pushKey = zonalModelArrayList.get(position).getPushKey();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
