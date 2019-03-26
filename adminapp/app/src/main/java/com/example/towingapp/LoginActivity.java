package com.example.towingapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText emailEd;
    private EditText passwordEd;
    private Button loginbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();


    }

    private void initView() {
        emailEd = findViewById(R.id.activity_login_email_ed);
        passwordEd = findViewById(R.id.activity_login_password_ed);
        loginbtn = findViewById(R.id.activity_login_btn);


        loginbtn.setOnClickListener(this);

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


        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please Enter details", Toast.LENGTH_SHORT).show();
        } else {
            if (email.equals("admin@gmail.com") && password.equals("admin")) {
                final Intent gotoHome = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(gotoHome);
            } else {
                Toast.makeText(this, "Credential Wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
