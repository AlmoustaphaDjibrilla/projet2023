package com.adi.projet2023.activity.main_page;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.adi.projet2023.R;
import com.adi.projet2023.activity.main_page.fragment.FragmentAdmin;
import com.adi.projet2023.activity.main_page.fragment.FragmentHome;
import com.adi.projet2023.activity.main_page.fragment.FragmentProfil;

public class MainPage extends AppCompatActivity {

    //Nombre pour la selection, valeur par defaut 1 pour home
    private int selection=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        final LinearLayout home = findViewById(R.id.home);
        final LinearLayout profil = findViewById(R.id.profil);
        final LinearLayout admin = findViewById(R.id.admin);

        final ImageView homeImage=findViewById(R.id.imageHome);
        final ImageView profilImage=findViewById(R.id.imageProfil);
        final ImageView adminImage=findViewById(R.id.imageAdmin);

        final TextView textHome=findViewById(R.id.textHome);
        final TextView textProfil=findViewById(R.id.textProfil);
        final TextView textAdmin=findViewById(R.id.textAdmin);

        //Fragment par defaut
        remplacementFragment(new FragmentHome());
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Verifier si layout selectionner ou pas
                if(selection!=1){
                    //afficher home
                    remplacementFragment(new FragmentHome());

                    //deselection des autres sauf home
                    textProfil.setVisibility(View.GONE);
                    textAdmin.setVisibility(View.GONE);

                    profilImage.setImageResource(R.drawable.ic_person);
                    adminImage.setImageResource(R.drawable.icon_admin);

                    profil.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    admin.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                    //activer home
                    textHome.setVisibility(View.VISIBLE);
                    homeImage.setImageResource(R.drawable.icon_home);
                    home.setBackgroundResource(R.drawable.round_navbar);

                    //creer animation
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f,1.0f,1f,1f, Animation.RELATIVE_TO_SELF,0.0f,Animation.RELATIVE_TO_SELF,0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    home.startAnimation(scaleAnimation);

                    //selection sur home
                    selection=1;
                }
            }
        });

        profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Verifier si layout selectionner ou pas
                if(selection!=2){
                    //Afficher profile
                    remplacementFragment(new FragmentProfil());

                    //deselection des autres sauf profil
                    textHome.setVisibility(View.GONE);
                    textAdmin.setVisibility(View.GONE);

                    homeImage.setImageResource(R.drawable.icon_home);
                    adminImage.setImageResource(R.drawable.icon_admin);

                    home.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    admin.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                    //activer profil
                    textProfil.setVisibility(View.VISIBLE);
                    profilImage.setImageResource(R.drawable.ic_person);
                    profil.setBackgroundResource(R.drawable.round_navbar);

                    //creer animation
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f,1.0f,1f,1f, Animation.RELATIVE_TO_SELF,0.0f,Animation.RELATIVE_TO_SELF,0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    profil.startAnimation(scaleAnimation);

                    //selection sur profil
                    selection=2;
                }
            }
        });

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Verifier si layout selectionner ou pas
                if(selection!=3){
                    //Afficher Admin
                    remplacementFragment(new FragmentAdmin());

                    //deselection des autres sauf admin
                    textHome.setVisibility(View.GONE);
                    textProfil.setVisibility(View.GONE);

                    homeImage.setImageResource(R.drawable.icon_home);
                    homeImage.setImageResource(R.drawable.ic_person);

                    home.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    profil.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                    //activer profil
                    textAdmin.setVisibility(View.VISIBLE);
                    adminImage.setImageResource(R.drawable.icon_admin);
                    admin.setBackgroundResource(R.drawable.round_navbar);

                    //creer animation
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f,1.0f,1f,1f, Animation.RELATIVE_TO_SELF,0.0f,Animation.RELATIVE_TO_SELF,0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    admin.startAnimation(scaleAnimation);

                    //selection sur profil
                    selection=3;
                }
            }
        });
    }


    public void remplacementFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.conteneurFragment, fragment);
        fragmentTransaction.commit();
    }
}