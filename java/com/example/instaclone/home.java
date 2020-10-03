package com.example.instaclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import fargements.NotificationFragment;
import fargements.ProfileFragment;
import fargements.SearchFragment;
import fargements.homeFragment;

public class home extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private Fragment selectorFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_home:
                        selectorFragment=new homeFragment();
                        break;
                    case R.id.nav_fav:
                        selectorFragment=new NotificationFragment();
                        break;
                    case R.id.nav_add:
                        selectorFragment=null;
                        startActivity(new Intent(home.this,Postactivity.class));
                        break;
                    case R.id.nav_search:
                        selectorFragment=new SearchFragment();
                        break;
                    case R.id.nav_person:
                        selectorFragment=new ProfileFragment();
                        break;
                }
                if (selectorFragment!=null){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectorFragment).commit();

                }
                return true;

            }
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new homeFragment()).commit();
    }
}