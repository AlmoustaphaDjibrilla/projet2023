package com.adi.projet2023.activity.main_page;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.adi.projet2023.R;
import com.adi.projet2023.activity.main_page.fragment.FragmentHome;
import com.adi.projet2023.activity.main_page.fragment.FragmentProfile;
import com.adi.projet2023.databinding.ActivityMainPageBinding;
import com.adi.projet2023.model.local.Local;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class MainPage extends AppCompatActivity {

    ActivityMainPageBinding binding;
    Local localEnCours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent receiveLocal= getIntent();
        localEnCours= (Local) receiveLocal.getSerializableExtra("envoiLocal");


        remplacementFragment(new FragmentHome());

        binding.bottomNavigationView.setOnItemSelectedListener(
                new NavigationBarView.OnItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.mnHouse:
                                remplacementFragment(new FragmentHome());

                                break;

                            case R.id.mnProfil:
                                remplacementFragment(new FragmentProfile());
                                Toast.makeText(getApplicationContext(), "Local en cours: "+localEnCours, Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return true;
                    }
                }
        );
    }

    private void remplacementFragment(Fragment fragment){
        FragmentManager fragmentManager= getSupportFragmentManager();
        FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}