package com.example.towinapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
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

public class PoliceHomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, TowingVehiclePoliceItemClickListener, SearchView.OnQueryTextListener {


    private ArrayList<AddVehicleModel> addVehicleModelArrayList;
    private TowingVehicleListPoliceAdapter towingVehicleListPoliceAdapter;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_police_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_vehicle);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Intent gotoAddNewVehicle = new Intent(PoliceHomeActivity.this, AddVehiclePoliceActivity.class);
                startActivity(gotoAddNewVehicle);

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        recyclerView = findViewById(R.id.activity_police_home_vehicle_list_rv);

        addVehicleModelArrayList = new ArrayList<>();
        towingVehicleListPoliceAdapter = new TowingVehicleListPoliceAdapter(PoliceHomeActivity.this, addVehicleModelArrayList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(towingVehicleListPoliceAdapter);
        recyclerView.setLayoutManager(layoutManager);


        final String uuid = firebaseAuth.getCurrentUser().getUid();

        databaseReference.child(AppConfig.FIREBASE_DB_TOWING_VEHICLE)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        addVehicleModelArrayList.clear();
                        for (DataSnapshot towingVehicleSnapshot : dataSnapshot.getChildren()) {
                            AddVehicleModel addVehicleModel = towingVehicleSnapshot.getValue(AddVehicleModel.class);
                            if (addVehicleModel.getPoliceUUID().equals(uuid)) {
                                Toast.makeText(PoliceHomeActivity.this, "" + addVehicleModel.getFineAmount(), Toast.LENGTH_SHORT).show();
                                addVehicleModelArrayList.add(addVehicleModel);
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
                    Toast.makeText(PoliceHomeActivity.this, "Action View Expanded", Toast.LENGTH_SHORT).show();
                    return true;
                }

                @Override
                public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                    Toast.makeText(PoliceHomeActivity.this, "Action View Collapsed", Toast.LENGTH_SHORT).show();
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

        if (id == R.id.nav_police_logout) {

            firebaseAuth.signOut();
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onTowingVehiclePoliceItemClick(AddVehicleModel addVehicleModel, View v) {

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
