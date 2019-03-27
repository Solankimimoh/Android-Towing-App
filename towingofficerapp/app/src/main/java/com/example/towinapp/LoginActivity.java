package com.example.towinapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {


    private EditText emailEd;
    private EditText passwordEd;
    private Button loginbtn;
    private RadioGroup loginType;
    private String loginTypeString = "";

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        initView();
    }

    private void initView() {


        emailEd = findViewById(R.id.activity_login_email_ed);
        passwordEd = findViewById(R.id.activity_login_password_ed);
        loginbtn = findViewById(R.id.activity_login_btn);
        loginType = findViewById(R.id.activity_login_type_rg);

        loginbtn.setOnClickListener(this);
        loginType.setOnCheckedChangeListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_login_btn:
                userLogin();
                break;
        }
    }

    private void userLogin() {

        final String email = emailEd.getText().toString().trim();
        final String password = passwordEd.getText().toString().trim();

        if (loginTypeString.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please Enter the details", Toast.LENGTH_SHORT).show();
        } else {
            checkLogin(loginTypeString, email, password);
        }

    }

    private void checkLogin(final String loginTypeString, String email, String password) {

        firebaseAuth
                .signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (!task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Error " + task.getException(), Toast.LENGTH_SHORT).show();
                } else {

                    final String uuid = firebaseAuth.getCurrentUser().getUid();

                    if (loginTypeString.equals(getString(R.string.zonal_officer))) {

                        databaseReference
                                .child(AppConfig.FIREBASE_DB_ZONAL_OFFICER)
                                .child(uuid)
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.hasChildren()) {
                                            Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                                            final Intent gotoHomeZonalOfficer = new Intent(LoginActivity.this, PoliceHomeActivity.class);
                                            startActivity(gotoHomeZonalOfficer);
                                        } else {
                                            Toast.makeText(LoginActivity.this, "Wrong Type Selected", Toast.LENGTH_SHORT).show();
                                            firebaseAuth.signOut();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                    } else if (loginTypeString.equals(getString(R.string.police))) {

                        databaseReference
                                .child(AppConfig.FIREBASE_DB_POLICE)
                                .child(uuid)
                                .addValueEventListener(new ValueEventListener() {

                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.hasChildren()) {
                                            Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                                            final Intent gotoHomeZonalOfficer = new Intent(LoginActivity.this, PoliceHomeActivity.class);
                                            startActivity(gotoHomeZonalOfficer);
                                        } else {
                                            Toast.makeText(LoginActivity.this, "Wrong Type Selected", Toast.LENGTH_SHORT).show();
                                            firebaseAuth.signOut();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                    }
                }
            }
        });

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        final RadioButton radioButton = findViewById(checkedId);

        switch (radioButton.getId()) {
            case R.id.activity_login_type_police:
                loginTypeString = radioButton.getText().toString();
                break;
            case R.id.activity_login_type_zonal_officer:
                loginTypeString = radioButton.getText().toString();
                break;
        }

    }


}
