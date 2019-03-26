package com.example.towingapp;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddPoliceActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private EditText nameEd;
    private EditText emailEd;
    private EditText passwordEd;
    private EditText mobileEd;
    private EditText policeEd;
    private Button addBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_police);

        initView();

    }

    private void initView() {

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        nameEd = findViewById(R.id.activity_add_police_name_ed);
        emailEd = findViewById(R.id.activity_add_police_email_ed);
        passwordEd = findViewById(R.id.activity_add_police_password_ed);
        mobileEd = findViewById(R.id.activity_add_police_mobile_ed);
        policeEd = findViewById(R.id.activity_add_police_id_ed);

        addBtn = findViewById(R.id.activity_add_police_btn);

        addBtn.setOnClickListener(this);


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
                    .createUserWithEmailAndPassword(email, password).addOnCompleteListener(AddPoliceActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(AddPoliceActivity.this, "Error " + task.getException(), Toast.LENGTH_SHORT).show();
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
                .child(AppConfig.FIREBASE_DB_POLICE)
                .child(uuid)
                .setValue(new PoliceModel(name, email, password, mobile, policeId), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            Toast.makeText(AddPoliceActivity.this, "Error : " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AddPoliceActivity.this, "Inserted Successfully ", Toast.LENGTH_SHORT).show();
                            firebaseAuth.signOut();
                            finish();
                        }
                    }
                });
    }
}
