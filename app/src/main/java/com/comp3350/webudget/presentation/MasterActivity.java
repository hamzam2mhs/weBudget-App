package com.comp3350.webudget.presentation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.comp3350.webudget.R;
import com.comp3350.webudget.application.Services;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MasterActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);

        // appbar
        androidx.appcompat.widget.Toolbar toolbar =  findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        // navigation
        BottomNavigationView nav = findViewById(R.id.navigation);
        nav.setOnNavigationItemSelectedListener(this);

        // fragments
        load_fragment(new CalendarFragment());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_appbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.appbar_logout:
                try {
                    //logout and clear backstack
                    Services.userLogic().logout();
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    MasterActivity.this.finish();


                } catch (Exception e) {
                    Toast toast = Toast.makeText(this.getApplicationContext(),
                            e.getMessage(), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                }
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment frag = null;

        switch( menuItem.getItemId() ){

            case R.id.navigation_account:
                frag = new AccountFragment();
                break;

            case R.id.navigation_calendar:
                frag = new CalendarFragment();
                break;

            case R.id.navigation_groups:
                frag = new GroupFragment();
                break;
        }
        return load_fragment(frag);
    }

    private boolean load_fragment(Fragment frag){
        if ( frag != null ) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer , frag ).addToBackStack(null).commit();
            return true;
        }
        return false;
    }

}