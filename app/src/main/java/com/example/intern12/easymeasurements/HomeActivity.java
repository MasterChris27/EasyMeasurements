package com.example.intern12.easymeasurements;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.intern12.easymeasurements.data.AdapterMasurari;
import com.example.intern12.easymeasurements.data.Masurari;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class HomeActivity extends AppCompatActivity implements ListaMasurariFragment.OnFragmentInteractionListener,MapFragment.OnFragmentInteractionListener {

    Button menuButton;

    public ArrayList<Masurari> getMyMeasureList() {
        return myMeasureList;
    }

    ArrayList<Masurari> myMeasureList = new ArrayList<>();

    DatabaseReference mDatabase;
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
   public String key;
    Double[] startPoint = new Double[2];
    private AlertDialog alertDialogNetwork;
    private NavigationView nvDrawer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        alertDialogNetworkSetup();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        key = getIntent().getStringExtra("key");
        if(key!=null ) {
            startPoint[0]= getIntent().getDoubleExtra("startPointLat",0.0);
            startPoint[1] =getIntent().getDoubleExtra("startPointLng",0.0);
            String numeZona = getIntent().getStringExtra("numeZona");
            String numeCultura=getIntent().getStringExtra("numeCultura");
            String numeProprietar =getIntent().getStringExtra("numeProprietar");
            Fragment fragment = null;
            Class fragmentClass;
            fragmentClass = MapFragment.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Bundle bundle = new Bundle();
            bundle.putDouble("startPointLat",startPoint[0]);
            bundle.putDouble("startPointLng",startPoint[1]);
            bundle.putString("key",key);
            bundle.putString("numeZona",numeZona);
            bundle.putString("numeCultura",numeCultura);
            bundle.putString("numeProprietar",numeProprietar);
            fragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        }

        if(getIntent().getIntExtra("cod",0)==1){

            Fragment fragment = null;
            Class fragmentClass;
            fragmentClass = ListaMasurariFragment.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();


            } catch (Exception e) {
                e.printStackTrace();
            }
            FragmentManager fragmentManager = getSupportFragmentManager();
            ListaMasurariFragment listaMasurariFragment = new ListaMasurariFragment();
            listaMasurariFragment.setMyMeasureList(myMeasureList);
            fragmentManager.beginTransaction().replace(R.id.flContent, listaMasurariFragment).commit();
        }
        new AdapterMasurari(myMeasureList);

        menuButton = (Button) findViewById(R.id.btn_expand_menu);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDrawer.openDrawer(GravityCompat.START);

            }
        });

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        nvDrawer = (NavigationView) findViewById(R.id.nvView);

        // Setup drawer view
        setupDrawerContent(nvDrawer);

        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

    }


    private void setupDrawerContent(NavigationView navigationView) {

        navigationView.setNavigationItemSelectedListener(

                new NavigationView.OnNavigationItemSelectedListener() {

                    @Override

                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        selectDrawerItem(menuItem);

                        return true;

                    }

                });


    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!haveNetworkConnection()){
            alertDialogNetwork.show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                 myMeasureList.clear();

                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Masurari masurare = postSnapshot.getValue(Masurari.class);
                    myMeasureList.add(masurare);
                    Log.d("test","Update done ");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void selectDrawerItem(MenuItem menuItem) {

        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass;
        switch (menuItem.getItemId()) {
            case R.id.nav_first_fragment:
                fragmentClass = ListaMasurariFragment.class;
                break;
            case R.id.nav_second_fragment:
                fragmentClass = MapFragment.class;
                break;
            default:
                fragmentClass = ListaMasurariFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();


        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();

        if(fragmentClass == ListaMasurariFragment.class) {
            ListaMasurariFragment listaMasurariFragment = new ListaMasurariFragment();
            listaMasurariFragment.setMyMeasureList(myMeasureList);

            fragmentManager.beginTransaction().replace(R.id.flContent, listaMasurariFragment).commit();
        }
        else{

        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        }


        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private boolean haveNetworkConnection() {
        boolean status=false;
        try{
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.getState()==NetworkInfo.State.CONNECTED) {
                status= true;
            }else {
                netInfo = cm.getActiveNetworkInfo();
                if(netInfo!=null && netInfo.getState()==NetworkInfo.State.CONNECTED)
                    status= true;
            }
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return status;

    }


    public void alertDialogNetworkSetup(){
        alertDialogNetwork = new AlertDialog.Builder(HomeActivity.this).create();
        alertDialogNetwork.setTitle(getString(R.string.alert_net_tile));
        alertDialogNetwork.setMessage(getString(R.string.alert_net_message));
        alertDialogNetwork.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.alert_net_settings), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                startActivity(intent);
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(!haveNetworkConnection()){
            alertDialogNetwork.show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // The action bar home/up action should open or close the drawer.

        switch (item.getItemId()) {

            case android.R.id.home:

                mDrawer.openDrawer(GravityCompat.START);

                return true;

        }


        return super.onOptionsItemSelected(item);


    }
}
