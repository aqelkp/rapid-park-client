package xyz.brozzz.rapidpark.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import in.aqel.quickparksdk.Objects.User;
import in.aqel.quickparksdk.Utils.AppConstants;
import in.aqel.quickparksdk.Utils.PrefUtils;
import xyz.brozzz.rapidpark.Fragments.BalanceFragment;
import xyz.brozzz.rapidpark.Fragments.MapFragment;
import xyz.brozzz.rapidpark.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Firebase ref;
    private static String LOG_TAG = "MainActivity";
    Context context = MainActivity.this;
    User user;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!PrefUtils.isLogedin(this)){
            Intent Nintent =new Intent(this,LoginActivity.class);
            startActivity(Nintent);
            finish();
        }
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Firebase.setAndroidContext(this);

        ref = new Firebase(AppConstants.SERVER);

/*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
       // LinearLayout headerView = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.nav_header_main, null);
        TextView Navemail=(TextView) headerView.findViewById(R.id.email);
        TextView Navname=(TextView) headerView.findViewById(R.id.name);
        ImageView Navprofile =(ImageView) headerView.findViewById(R.id.NavimageView);
        Navemail.setText(PrefUtils.getEmail(this));
        Log.d(LOG_TAG, "Email:" + PrefUtils.getEmail(this));
        Navname.setText(PrefUtils.getName(this));
        Glide.with(this).load(PrefUtils.getProfilePic(this)).into(Navprofile);

        fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, new MapFragment());
        fragmentTransaction.commit();

        ref.child("users").child(ref.getAuth().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0){
                    Log.d(LOG_TAG, "User " + dataSnapshot.toString());
                    user = dataSnapshot.getValue(User.class);
                    Log.d(LOG_TAG, "User working" + user.getName());

                    for (DataSnapshot parkingSnap: dataSnapshot.getChildren()) {
                        Log.d(LOG_TAG, parkingSnap.getValue().toString());
                        for (DataSnapshot child: parkingSnap.getChildren()) {
                            Log.d(LOG_TAG, "Key " + child.getKey());
                            Log.d(LOG_TAG, "Value " + child.getValue());
                            Log.d(LOG_TAG, "Par" + child.toString());


                        }
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public User getUser(){
        return user;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_map) {
            // Handle the camera action
        } else if (id == R.id.nav_balance) {
            fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, new BalanceFragment());
            fragmentTransaction.commit();

        } else if (id == R.id.nav_booking_history) {


        }
        else if (id == R.id.nav_logout) {
            PrefUtils.clearpref(getBaseContext());
            Intent intent =new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
