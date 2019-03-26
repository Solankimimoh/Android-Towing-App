package com.example.towingapp;

import android.app.ProgressDialog;
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

public class AddPriceActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private Button addPrice;
    private EditText twoWheelerEd;
    private EditText fourWheelerEd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_price);

        initView();

    }

    private void initView() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        twoWheelerEd = findViewById(R.id.activity_add_price_two_wheeler_ed);
        fourWheelerEd = findViewById(R.id.activity_add_price_four_wheeler_ed);
        addPrice = findViewById(R.id.activity_add_price_btn);

        addPrice.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        final String twoWheeler = twoWheelerEd.getText().toString().trim();
        final String fourWheeler = fourWheelerEd.getText().toString().trim();

        databaseReference
                .child(AppConfig.FIREBASE_DB_PENLTY_PRICE)
                .child("prices")
                .setValue(new PenltyPriceModel(twoWheeler, fourWheeler),
                        new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                if (databaseError != null) {
                                    Toast.makeText(AddPriceActivity.this, "Error : " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(AddPriceActivity.this, "Inserted Successfully ", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                        });

    }
}
