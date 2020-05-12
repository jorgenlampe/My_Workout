package com.example.myworkout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.myworkout.data.DataViewModel;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private DataViewModel dataViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavHostFragment host = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        NavController navController = host.getNavController();


        //setup bottomNav
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        if (bottomNav != null) {
            NavigationUI.setupWithNavController(bottomNav, navController);
        }

        //dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);

        //  FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //        String firebaseId = firebaseUser.getUid();

        //          dataViewModel.getUser(this, firebaseId);

        //  Log.d("yyy", dataViewModel.getGetUserResponse().toString());

//            Log.d("yyy", firebaseId);
    }
}
