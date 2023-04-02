package com.startup.go4lunch;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.AuthUI.IdpConfig;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.startup.go4lunch.di.ViewModelFactory;
import com.startup.go4lunch.ui.MainActivityViewModel;
import com.startup.go4lunch.ui.ViewPagerAdapter;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MainActivityViewModel viewModel;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(MainActivityViewModel.class);
        firebaseAuth = FirebaseAuth.getInstance();
        verifyFirebaseUser();
        getCurrentLocation();
        configureViewPager();
    }

    private void verifyFirebaseUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            startSignInActivity();
        }
    }

    ActivityResultLauncher<Intent> signInLuncher = registerForActivityResult(new FirebaseAuthUIActivityResultContract(), this::onSignInResult);

    private void startSignInActivity() {
        List<IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build();
        signInLuncher.launch(signInIntent);
    }

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        if (result.getResultCode() == RESULT_OK) {
             // TODO : Create Workmate
        } else {
            startSignInActivity();
        }
    }

    private void getCurrentLocation() {
        if (!checkLocationPermission()) {
            return;
        }
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener(location -> viewModel.updateLocation(location));
    }

    private boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        getCurrentLocation();
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void configureViewPager() {
        ViewPager2 viewPager = findViewById(R.id.view_pager);
        TabLayout tabLayout = findViewById(R.id.menu);

        viewPager.setAdapter(new ViewPagerAdapter(this));

        TabLayoutMediator.TabConfigurationStrategy tabConfigurationStrategy = (tab, position) -> {
            switch (position) {
                case 0 : tab.setText(R.string.tab_map);
                         tab.setIcon(R.drawable.icon_menu_map_24);
                         break;
                case 1 : tab.setText(R.string.tab_restaurant);
                         tab.setIcon(R.drawable.icon_menu_restaurant_24);
                         break;
                case 2 : tab.setText(R.string.tab_workmate);
                         tab.setIcon(R.drawable.icon_menu_workmate_24);
                         break;
            }
        };

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager, tabConfigurationStrategy);
        tabLayoutMediator.attach();
    }
}