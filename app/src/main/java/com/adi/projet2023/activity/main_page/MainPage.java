package com.adi.projet2023.activity.main_page;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.adi.projet2023.R;
import com.adi.projet2023.activity.main_page.fragment.FragmentUsers;
import com.adi.projet2023.activity.main_page.fragment.FragmentHome;
import com.adi.projet2023.activity.main_page.fragment.FragmentProfil;
import com.adi.projet2023.model.local.Local;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainPage extends AppCompatActivity {

    //Nombre pour la selection, valeur par defaut 1 pour home
    private int selection=1;
    Local localEnCours;
    String PATH_PRESENCE_PERSONNE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        localEnCours = (Local) getIntent().getSerializableExtra("localId");
        PATH_PRESENCE_PERSONNE= localEnCours.getIdLocal()+"/Presence";


        //Lancer une notification en cas d'intrusion
        lancerNotification();


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
        remplacementFragment(new FragmentHome(localEnCours));
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Verifier si layout selectionner ou pas
                if(selection!=1){
                    //afficher home
                    remplacementFragment(new FragmentHome(localEnCours));

                    //deselection des autres sauf home
                    textProfil.setVisibility(View.GONE);
                    textAdmin.setVisibility(View.GONE);

                    profilImage.setImageResource(R.drawable.ic_person);
                    adminImage.setImageResource(R.drawable.icon_all_users);

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
                    remplacementFragment(new FragmentProfil(localEnCours));

                    //deselection des autres sauf profil
                    textHome.setVisibility(View.GONE);
                    textAdmin.setVisibility(View.GONE);

                    homeImage.setImageResource(R.drawable.icon_home);
                    adminImage.setImageResource(R.drawable.icon_all_users);

                    home.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    admin.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                    //activer profil
                    textProfil.setVisibility(View.VISIBLE);
                    profilImage.setImageResource(R.drawable.ic_person);
                    profil.setBackgroundResource(R.drawable.round_navbar);

                    //creer animation
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f,1.0f,1f,1f, Animation.RELATIVE_TO_SELF,0.0f,Animation.RELATIVE_TO_SELF,0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setDuration(300);
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
                    remplacementFragment(new FragmentUsers(localEnCours));

                    //deselection des autres sauf admin
                    textHome.setVisibility(View.GONE);
                    textProfil.setVisibility(View.GONE);

                    homeImage.setImageResource(R.drawable.icon_home);
                    profilImage.setImageResource(R.drawable.ic_person);

                    home.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    profil.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                    //activer admin
                    textAdmin.setVisibility(View.VISIBLE);
                    adminImage.setImageResource(R.drawable.icon_all_users);
                    admin.setBackgroundResource(R.drawable.round_navbar);

                    //creer animation
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f,1.0f,1f,1f, Animation.RELATIVE_TO_SELF,0.0f,Animation.RELATIVE_TO_SELF,0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    admin.startAnimation(scaleAnimation);

                    //selection sur admin
                    selection=3;
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        //Lancer une notification en cas d'intrusion
        lancerNotification();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Lancer une notification en cas d'intrusion
        lancerNotification();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Lancer une notification en cas d'intrusion
        lancerNotification();
    }

    public void remplacementFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.conteneurFragment, fragment);
        fragmentTransaction.commit();
    }

    private void lancerNotification(){
        DatabaseReference reference= FirebaseDatabase.getInstance()
                .getReference(PATH_PRESENCE_PERSONNE);

        reference.addValueEventListener(new ValueEventListener() {
            Long val= 0l;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Long presenceValue=(Long) snapshot.getValue();
                val= presenceValue;
                //Nouvelle intrusion
                if (presenceValue==1) {
                    notification();
                    vibrationTelephone();
                    sonnerieTelephone();
                    reference.setValue(0L);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    /**
     * Permet de faire vibrer le téléphone
     */
    private void vibrationTelephone(){
        Vibrator vibrator= (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(VibrationEffect.createOneShot(3500, VibrationEffect.DEFAULT_AMPLITUDE));
    }

    /**
     * Permet de faire sonner le telephone
     */
    private void sonnerieTelephone(){
        Ringtone ringtone= RingtoneManager
                .getRingtone(getApplicationContext(), RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
        new CountDownTimer(10000, 1000){

            @Override
            public void onTick(long l) {
                ringtone.play();
            }

            @Override
            public void onFinish() {
                ringtone.stop();
            }
        }.start();
    }

    private void notification(){
        NotificationChannel channel = new NotificationChannel(
                "Id Effraction",
                "Alerte effraction",
                NotificationManager.IMPORTANCE_HIGH
        );
        getSystemService(NotificationManager.class).createNotificationChannel(channel);
        Notification.Builder notification =
                new Notification.Builder(
                        getApplicationContext(), "Id Effraction"
                )
                        .setContentTitle("Alerte effraction")
                        .setContentText("Attention, présence inconnue detectée dans le local: "+localEnCours.getNomLocal())
                        .setSmallIcon(R.drawable.icon_alert)
                        .setAutoCancel(true);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        NotificationManagerCompat
                .from(this)
                .notify(1, notification.build());
    }
}