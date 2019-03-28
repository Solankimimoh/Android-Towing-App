package com.example.towinapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReceiveVehicleListZonalOficerActivity extends AppCompatActivity implements TowingVehiclePoliceItemClickListener, SearchView.OnQueryTextListener {


    private ArrayList<AddVehicleModel> addVehicleModelArrayList;
    private TowingVehicleListPoliceAdapter towingVehicleListPoliceAdapter;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private RecyclerView recyclerView;
    private ZonalOfficerModel zonalOfficerModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_vehicle_list_zonal_oficer);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        recyclerView = findViewById(R.id.activity_receive_vehicle_list_zonal_officer_rv);

        addVehicleModelArrayList = new ArrayList<>();
        towingVehicleListPoliceAdapter = new TowingVehicleListPoliceAdapter(ReceiveVehicleListZonalOficerActivity.this, addVehicleModelArrayList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(towingVehicleListPoliceAdapter);
        recyclerView.setLayoutManager(layoutManager);


        final String uuid = firebaseAuth.getCurrentUser().getUid();

        databaseReference.child(AppConfig.FIREBASE_DB_ZONAL_OFFICER)
                .child(uuid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        zonalOfficerModel = dataSnapshot.getValue(ZonalOfficerModel.class);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        databaseReference.child(AppConfig.FIREBASE_DB_TOWING_VEHICLE)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        addVehicleModelArrayList.clear();
                        for (DataSnapshot towingVehicleSnapshot : dataSnapshot.getChildren()) {
                            AddVehicleModel addVehicleModel = towingVehicleSnapshot.getValue(AddVehicleModel.class);
                            addVehicleModel.setPushKey(towingVehicleSnapshot.getKey());
                            if (addVehicleModel.getTowingZonePushKey().equals(zonalOfficerModel.getZonalPushKey())) {
                                if (addVehicleModel.isVerifyVehicle() && !addVehicleModel.isReceiveStatus()) {
                                    addVehicleModelArrayList.add(addVehicleModel);
                                }
                            }
                        }
                        towingVehicleListPoliceAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.police_home, menu);


        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);

        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            MenuItem.OnActionExpandListener onActionExpandListener = new MenuItem.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionExpand(MenuItem menuItem) {
                    Toast.makeText(ReceiveVehicleListZonalOficerActivity.this, "Action View Expanded", Toast.LENGTH_SHORT).show();
                    return true;
                }

                @Override
                public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                    Toast.makeText(ReceiveVehicleListZonalOficerActivity.this, "Action View Collapsed", Toast.LENGTH_SHORT).show();
                    return true;
                }
            };
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onTowingVehiclePoliceItemClick(AddVehicleModel addVehicleModel, View v) {


        final Intent gotoReceiveVehicleDetailsUser = new Intent(ReceiveVehicleListZonalOficerActivity.this, ReceiveUserDetailsForVehicleActivity.class);
        gotoReceiveVehicleDetailsUser.putExtra(AppConfig.FIREBASE_KEY_PENLTY_PRICE,
                addVehicleModel.getFineAmount());
        gotoReceiveVehicleDetailsUser.putExtra(AppConfig.FIREBASE_KEY_TOWING_VEHICLE_PUSH_KEY,
                addVehicleModel.getPushKey());
        startActivity(gotoReceiveVehicleDetailsUser);

    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        String usertext = s.toLowerCase();
        ArrayList<AddVehicleModel> searchArrayList = new ArrayList<>();

        for (int i = 0; i < addVehicleModelArrayList.size(); i++) {
            if (addVehicleModelArrayList.get(i).getVehicleNumber().toLowerCase().contains(usertext)) {
                searchArrayList.add(addVehicleModelArrayList.get(i));
            }
        }
        towingVehicleListPoliceAdapter.updateList(searchArrayList);
        return true;
    }
}
