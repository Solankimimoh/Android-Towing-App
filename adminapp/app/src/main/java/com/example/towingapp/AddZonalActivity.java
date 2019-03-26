package com.example.towingapp;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddZonalActivity extends AppCompatActivity implements View.OnClickListener {


    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private EditText zonalEd;
    private EditText zonalAddressEd;
    private Button addBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_zonal);

        initView();

    }

    private void initView() {

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        zonalEd = findViewById(R.id.activity_add_zonal_name_ed);
        zonalAddressEd = findViewById(R.id.activity_add_zonal_address_ed);
        addBtn = findViewById(R.id.activity_add_zonal_btn);

        addBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        final String name = zonalEd.getText().toString().trim();
        final String address = zonalAddressEd.getText().toString().trim();


        if(name.isEmpty() || address.isEmpty())
        {
            Toast.makeText(this, "Please fill the details", Toast.LENGTH_SHORT).show();
        }else
        {
            databaseReference
                    .child(AppConfig.FIREBASE_DB_ZONAL_ADDRESS)
                    .push()
                    .setValue(new ZonalModel(name, address), new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            if (databaseError != null) {
                                Toast.makeText(AddZonalActivity.this, "Error : " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(AddZonalActivity.this, "Inserted Successfully ", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    });
        }

    }
}
