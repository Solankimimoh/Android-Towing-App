package com.example.towinguser;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {


    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private CardView cardView;

    private EditText vehicleEd;
    private ImageButton searchBtn;
    private ImageView vehicleImg;
    private TextView vehicleNumber;
    private TextView date;
    private TextView time;
    private TextView area;
    private TextView uniqueChallan;
    private TextView fineamount;
    private TextView status;
    private TextView zonalAddress;
    private boolean flag = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        initView();

    }

    private void initView() {
        vehicleEd = findViewById(R.id.activity_home_vehicle_number_ed);
        searchBtn = findViewById(R.id.activity_home_img_btn);
        searchBtn.setOnClickListener(this);
        cardView = findViewById(R.id.activity_home_towing_result);

        vehicleImg = findViewById(R.id.activity_home_vehicle_img);
        vehicleNumber = findViewById(R.id.activity_home_vehicle_number);
        date = findViewById(R.id.activity_home_vehicle_date);
        time = findViewById(R.id.activity_home_vehicle_time);
        area = findViewById(R.id.activity_home_vehicle_towing_area);
        uniqueChallan = findViewById(R.id.activity_home_vehicle__unique_challan);
        fineamount = findViewById(R.id.activity_home_vehicle__fine_amount);
        status = findViewById(R.id.activity_home_vehicle_verify_status);
        zonalAddress = findViewById(R.id.activity_home_vehicle_zonal_address);
    }

    @Override
    public void onClick(View v) {

        final String vehicleNumberstr = vehicleEd.getText().toString().trim();

        if (vehicleNumberstr.isEmpty()) {
            Toast.makeText(this, "Please Fill the details", Toast.LENGTH_SHORT).show();
        } else {
            databaseReference
                    .child(AppConfig.FIREBASE_DB_TOWING_VEHICLE)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot towingVehicle : dataSnapshot.getChildren()) {

                                AddVehicleModel addVehicleModel = towingVehicle.getValue(AddVehicleModel.class);
                                if (addVehicleModel.getVehicleNumber().equals(vehicleNumberstr) && !addVehicleModel.isReceiveStatus()) {
                                    cardView.setVisibility(View.VISIBLE);

                                    Glide.with(HomeActivity.this)
                                            .load(addVehicleModel.getPhotoUrl())
                                            .placeholder(R.mipmap.ic_launcher_round)
                                            .into(vehicleImg);

                                    vehicleNumber.setText(addVehicleModel.getVehicleNumber());
                                    date.setText(addVehicleModel.getDate());
                                    time.setText(addVehicleModel.getTime());
                                    area.setText(addVehicleModel.getTowinArea());
                                    uniqueChallan.setText(addVehicleModel.getUniqueChallan());
                                    fineamount.setText(addVehicleModel.getFineAmount());

                                    if (addVehicleModel.isVerifyVehicle()) {
                                        status.setText("Reached the zone");
                                        status.setTextColor(Color.GREEN);
                                    } else {
                                        status.setText("On the way to zone");
                                        status.setTextColor(Color.RED);
                                    }

                                    databaseReference
                                            .child(AppConfig.FIREBASE_DB_ZONAL_ADDRESS)
                                            .child(addVehicleModel.getTowingZonePushKey())
                                            .addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    ZonalModel zonalModel = dataSnapshot.getValue(ZonalModel.class);
                                                    zonalAddress.setText(zonalModel.getZonalName() + " | " + zonalModel.getZonalAddress());
                                                    flag = true;
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });
                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

            if(!flag)
            {
                cardView.setVisibility(View.INVISIBLE);
//                Toast.makeText(HomeActivity.this, "Not Found", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
