package com.example.towingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ReceiveVehicleListAdminItemClickListener, SearchView.OnQueryTextListener {


    private ArrayList<ReceiveVehicleDetailsModel> receiveVehicleDetailsModelArrayList;
    private ReceiveVehicleListAdminAdapter receiveVehicleListAdminAdapter;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
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


        recyclerView = findViewById(R.id.activity_home_receive_vehicle_list_rv);

        receiveVehicleDetailsModelArrayList = new ArrayList<>();
        receiveVehicleListAdminAdapter = new ReceiveVehicleListAdminAdapter(HomeActivity.this, receiveVehicleDetailsModelArrayList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(receiveVehicleListAdminAdapter);
        recyclerView.setLayoutManager(layoutManager);


        databaseReference
                .child(AppConfig.FIREBASE_DB_RECEIVE_VEHICLE)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot receiveModels : dataSnapshot.getChildren()) {
                            final ReceiveVehicleDetailsModel receiveVehicleDetailsModel = receiveModels.getValue(ReceiveVehicleDetailsModel.class);

                            databaseReference
                                    .child(AppConfig.FIREBASE_DB_TOWING_VEHICLE)
                                    .child(receiveVehicleDetailsModel.getTowingVehiclePushKey())
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            AddVehicleModel addVehicleModel = dataSnapshot.getValue(AddVehicleModel.class);
                                            receiveVehicleDetailsModel.setAddVehicleModel(addVehicleModel);
                                            databaseReference
                                                    .child(AppConfig.FIREBASE_DB_ZONAL_OFFICER)
                                                    .child(receiveVehicleDetailsModel.getZonalOfficerUuid())
                                                    .addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            ZonalOfficerModel zonalOfficerModel = dataSnapshot.getValue(ZonalOfficerModel.class);
                                                            receiveVehicleDetailsModel.setZonalOfficerModel(zonalOfficerModel);

                                                            Toast.makeText(HomeActivity.this, "" + receiveVehicleDetailsModel.getZonalOfficerModel().getName(), Toast.LENGTH_SHORT).show();
                                                            receiveVehicleDetailsModelArrayList.add(receiveVehicleDetailsModel);
                                                            receiveVehicleListAdminAdapter.notifyDataSetChanged();
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });
                                            receiveVehicleListAdminAdapter.notifyDataSetChanged();


                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });


                        }

                        receiveVehicleListAdminAdapter.notifyDataSetChanged();

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
        getMenuInflater().inflate(R.menu.home, menu);


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
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            MenuItem.OnActionExpandListener onActionExpandListener = new MenuItem.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionExpand(MenuItem menuItem) {
                    Toast.makeText(HomeActivity.this, "Action View Expanded", Toast.LENGTH_SHORT).show();
                    return true;
                }

                @Override
                public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                    Toast.makeText(HomeActivity.this, "Action View Collapsed", Toast.LENGTH_SHORT).show();
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

        if (id == R.id.nav_penlty_price) {
            final Intent gotoPenlty = new Intent(HomeActivity.this, AddPriceActivity.class);
            startActivity(gotoPenlty);
        } else if (id == R.id.nav_add_zonal) {
            final Intent gotoZonal = new Intent(HomeActivity.this, AddZonalActivity.class);
            startActivity(gotoZonal);
        } else if (id == R.id.nav_add_police) {
            final Intent gotoPolice = new Intent(HomeActivity.this, AddPoliceActivity.class);
            startActivity(gotoPolice);
        } else if (id == R.id.nav_add_zonal_officer) {
            final Intent gotoZonalOfficer = new Intent(HomeActivity.this, AddZonalOfficerActivity.class);
            startActivity(gotoZonalOfficer);
        } else if (id == R.id.nav_logout) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onReceiveVehicleListAdminItemClick(ReceiveVehicleDetailsModel receiveVehicleDetailsModel, View v) {

    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        String usertext = s.toLowerCase();
        ArrayList<ReceiveVehicleDetailsModel> searchArrayList = new ArrayList<>();

        for (int i = 0; i < receiveVehicleDetailsModelArrayList.size(); i++) {
            if (receiveVehicleDetailsModelArrayList.get(i).getAddVehicleModel().getVehicleNumber().toLowerCase().contains(usertext)) {
                searchArrayList.add(receiveVehicleDetailsModelArrayList.get(i));
            }
        }
        receiveVehicleListAdminAdapter.updateList(searchArrayList);
        return true;
    }
}
