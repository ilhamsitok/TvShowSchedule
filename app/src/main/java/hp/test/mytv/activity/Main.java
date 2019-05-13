package hp.test.mytv.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;


import java.util.List;

import hp.test.mytv.adapter.OnAirAdapter;
import hp.test.mytv.R;
import hp.test.mytv.model.OnAirItem;
import hp.test.mytv.model.OnAirResult;
import hp.test.mytv.utils.APIClient;
import hp.test.mytv.utils.TMDBInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Main extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar tb;
    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    TMDBInterface tmdbInterface;

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);

        //Initialize Toolbar

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initialize Recycle view


        recyclerView = findViewById(R.id.rv);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);


        //Initialize drawer

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //Api Client

        tmdbInterface = APIClient.getClient().create(TMDBInterface.class);


        refreshRv();

    }

    private void refreshRv(){
        Call<OnAirResult> onAirResultCall = tmdbInterface.getOnAir(1);
        Log.d("Main", "refreshRv: ");
        onAirResultCall.enqueue(new Callback<OnAirResult>() {
            @Override
            public void onResponse(@NonNull Call<OnAirResult> call, @NonNull Response<OnAirResult> response) {
                assert response.body() != null;
                List<OnAirItem>  onAirItems= response.body().getResults();
                mAdapter = new OnAirAdapter(onAirItems);
                recyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<OnAirResult> call, Throwable t) {

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

    //@Override
   // public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
   //     getMenuInflater().inflate(R.menu.main, menu);
   //     return true;
   // }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){

            case R.id.nav_setting:
                Intent setting = new Intent(Main.this,Setting.class);
                startActivity(setting);
                break;
            case R.id.nav_favorite:
                Intent favorite = new Intent(Main.this,Favorite.class);
                startActivity(favorite);
                break;
            case R.id.nav_info:
                Intent info = new Intent(Main.this,Info.class);
                startActivity(info);
                break;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
