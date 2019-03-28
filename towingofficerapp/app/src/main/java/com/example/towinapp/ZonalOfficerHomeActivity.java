package com.example.towinapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ZonalOfficerHomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, TowingVehiclePoliceItemClickListener, SearchView.OnQueryTextListener {


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
        setContentView(R.layout.activity_zonal_officer_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        recyclerView = findViewById(R.id.activity_zonal_officer_home_vehicle_list_rv);

        addVehicleModelArrayList = new ArrayList<>();
        towingVehicleListPoliceAdapter = new TowingVehicleListPoliceAdapter(ZonalOfficerHomeActivity.this, addVehicleModelArrayList, this);
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
                                if (!addVehicleModel.isVerifyVehicle()) {
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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
                    Toast.makeText(ZonalOfficerHomeActivity.this, "Action View Expanded", Toast.LENGTH_SHORT).show();
                    return true;
                }

                @Override
                public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                    Toast.makeText(ZonalOfficerHomeActivity.this, "Action View Collapsed", Toast.LENGTH_SHORT).show();
                    return true;
                }
            };
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_receive_vehicle) {

            final Intent gotoReceiveVehicle = new Intent(ZonalOfficerHomeActivity.this
                    , ReceiveVehicleListZonalOficerActivity.class);
            startActivity(gotoReceiveVehicle);

        } else if (id == R.id.nav_logout) {
            firebaseAuth.signOut();
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onTowingVehiclePoliceItemClick(final AddVehicleModel addVehicleModel, View v) {


        AlertDialog.Builder builder = new AlertDialog.Builder(ZonalOfficerHomeActivity.this);

        builder.setTitle("Verify Vehicle");
        builder.setMessage("Are you sure want to verify this vehicle");

        builder.setPositiveButton("Verify", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                databaseReference.child(AppConfig.FIREBASE_DB_TOWING_VEHICLE)
                        .child(addVehicleModel.getPushKey())
                        .child("verifyVehicle")
                        .setValue(true, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                if (databaseError != null) {
                                    Toast.makeText(ZonalOfficerHomeActivity.this, "Error " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                } else {
                                    towingVehicleListPoliceAdapter.notifyDataSetChanged();
                                    Toast.makeText(ZonalOfficerHomeActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });


        builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
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
