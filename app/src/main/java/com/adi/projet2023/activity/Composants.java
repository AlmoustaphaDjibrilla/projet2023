package com.adi.projet2023.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.adi.projet2023.R;
import com.adi.projet2023.Utils.LocalUtils;
import com.adi.projet2023.Utils.DatabaseUtils;
import com.adi.projet2023.activity.main_page.MainPage;
import com.adi.projet2023.creation.CreationCommande;
import com.adi.projet2023.model.Commande.Commande;
import com.adi.projet2023.model.Piece.Piece;
import com.adi.projet2023.model.composant.Composant;
import com.adi.projet2023.model.local.Local;
import com.adi.projet2023.model.user.UserModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Composants extends AppCompatActivity {

    final String DETAIL_ALLUMAGE= "Allumage";
    final String DETAIL_EXTINCTION= "Extinction";

    LinearLayout layout;
    List<Composant> composantList;
    Local localEnCours;
    Piece pieceEnCours;
    FloatingActionButton btnAddComposant;
    Dialog dialogSupprimerComposant;
    TextView txtTypeComposant, txtNomComposant, txtNomLocal, txtNomPiece, titrePiece;
    Button btnSupprimerComposant;

    ImageView imgQuitterComposant;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affichage_composants);
        init();
        dialogSupprimerComposant = new Dialog(this);
        composantList = (List<Composant>) getIntent().getSerializableExtra("composants");
        localEnCours = (Local) getIntent().getSerializableExtra("localEnCours");
        pieceEnCours = (Piece) getIntent().getSerializableExtra("pieceEnCours");
        recuperer_temperature_humidite();
        titrePiece.setText(pieceEnCours.getNomPiece());
        afficher_composants(composantList);
        initSwitches(composantList);
        imgQuitterComposant.setOnClickListener(view -> finish());
    }
    private void init () {
        layout = findViewById(R.id.composant);
        titrePiece = findViewById(R.id.titrePiece);
        btnAddComposant = findViewById(R.id.addComposant);
        imgQuitterComposant = findViewById(R.id.imgQuitterComposant);
        titrePiece = findViewById(R.id.titrePiece);
        btnAddComposant = findViewById(R.id.addComposant);
    }

    private void afficher_composants (List < Composant > composantList) {
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        //Initialisation des composants
        if(composantList != null && composantList.size()!=0){
            for (int i = 0; i < composantList.size(); i++) {
                Composant composantEnCours = composantList.get(i);
                View cardView = inflater.inflate(R.layout.composant_card, layout, false);
                View cardView2 = inflater.inflate(R.layout.composant2_card, layout, false);
                if (composantList.get(i).getTypeComposant().equals("AMPOULE") ||
                        composantList.get(i).getTypeComposant().equals("REFRIGERATEUR") ||
                        composantList.get(i).getTypeComposant().equals("CLIMATISEUR") ||
                        composantList.get(i).getTypeComposant().equals("AUTRE")||
                        composantList.get(i).getTypeComposant().equals("TONDEUSE")) {
                    TextView nom = cardView.findViewById(R.id.nom_composant);
                    ImageView img = cardView.findViewById(R.id.img_composant);
                    nom.setText(composantList.get(i).getNomComposant());
                    switch (composantList.get(i).getTypeComposant()) {
                        case "AMPOULE":
                            img.setImageResource(R.drawable.ampoules);
                            break;
                        case "CLIMATISEUR":
                            img.setImageResource(R.drawable.clim);
                            break;
                        case "REFRIGERATEUR":
                            img.setImageResource(R.drawable.frigo);
                            break;
                        case "TONDEUSE":
                            img.setImageResource(R.drawable.tondeuse);
                            break;
                        case "AUTRE":
                            img.setImageResource(R.drawable.autre);
                            break;
                        default:
                            break;
                    }
                    cardView.setTag(composantEnCours.getIdComposant());
                    layout.addView(cardView);
                    cardView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            String userId = FirebaseAuth.getInstance()
                                    .getCurrentUser()
                                    .getUid();
                            DatabaseUtils.getUser(userId, UserModel.class, new DatabaseUtils.OnValueReceivedListener<UserModel>() {
                                @Override
                                public void onValueReceived(UserModel value) {
                                    if(value.isAdmin()){
                                        dialogSupprimerComposant.setContentView(R.layout.supprimer_composant);
                                        initComponentsOfDialog();
                                        dialogSupprimerComposant.show();
                                        remplirChampsDialog(composantEnCours);
                                        btnSupprimerComposant.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                supprimer_composant(composantEnCours.getIdComposant(), composantEnCours.getChemin());
                                                dialogSupprimerComposant.dismiss();
                                            }
                                        });
                                    }
                                }
                            });

                            return true;
                        }
                    });
                } else {
                    TextView nom1 = cardView2.findViewById(R.id.nom_composant2);
                    ImageView img1 = cardView2.findViewById(R.id.img_composant2);
                    nom1.setText(composantList.get(i).getNomComposant());
                    switch (composantList.get(i).getTypeComposant()) {
                        case "PORTE":
                            img1.setImageResource(R.drawable.porte);
                            break;
                        case "VENTILATEUR":
                            img1.setImageResource(R.drawable.ventilateur);
                            break;
                        case "ARROSAGE":
                            img1.setImageResource(R.drawable.arrosage);
                            break;
                        default:
                            break;
                    }
                    cardView2.setTag(composantEnCours.getIdComposant());
                    layout.addView(cardView2);
                    cardView2.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            String userId = FirebaseAuth.getInstance()
                                    .getCurrentUser()
                                    .getUid();
                            DatabaseUtils.getUser(userId, UserModel.class, new DatabaseUtils.OnValueReceivedListener<UserModel>() {
                                @Override
                                public void onValueReceived(UserModel value) {
                                    if(value.isAdmin()){
                                        dialogSupprimerComposant.setContentView(R.layout.supprimer_composant);
                                        initComponentsOfDialog();
                                        dialogSupprimerComposant.show();
                                        remplirChampsDialog(composantEnCours);
                                        btnSupprimerComposant.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                supprimer_composant(composantEnCours.getIdComposant(), composantEnCours.getChemin());
                                                dialogSupprimerComposant.dismiss();
                                            }
                                        });
                                    }
                                }
                            });
                            return true;
                        }
                    });
                }

            }
        }else {
            View vide = inflater.inflate(R.layout.liste_vide, layout, false);
            layout.addView(vide);
        }

        //Si admin afficher boutton ajout piece
        String userId = FirebaseAuth.getInstance()
                .getCurrentUser()
                .getUid();
        DatabaseUtils.getUser(userId, UserModel.class, new DatabaseUtils.OnValueReceivedListener<UserModel>() {
            @Override
            public void onValueReceived(UserModel value) {
                if(value.isAdmin()){
                    btnAddComposant.show();
                    btnAddComposant.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getApplicationContext(), AjouterComposant.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("localEnCours", localEnCours);
                            intent.putExtra("pieceEnCours", pieceEnCours);
                            startActivity(intent);
                        }
                    });
                }
            }
        });
    }

    private void initSwitches (List<Composant> composantList){
        for (int i = 0; i < composantList.size(); i++) {
            Composant composantEnCours = composantList.get(i);
            if (composantList.get(i).getTypeComposant().equals("AMPOULE") ||
                    composantList.get(i).getTypeComposant().equals("REFRIGERATEUR") ||
                    composantList.get(i).getTypeComposant().equals("CLIMATISEUR") ||
                    composantList.get(i).getTypeComposant().equals("AUTRE") ||
                    composantList.get(i).getTypeComposant().equals("TONDEUSE")) {
                View cardView = layout.findViewWithTag(composantEnCours.getIdComposant());
                Switch switchCompat = cardView.findViewById(R.id.Switch);

                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
                DatabaseReference valueRef = databaseRef.child(composantEnCours.getChemin());

                valueRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Object data = snapshot.getValue();
                        if (data instanceof String) {
                            // Traitement pour une chaîne de caractères
                            String valeur = (String) data;
                            if (valeur.equals("ON")) {
                                switchCompat.setChecked(true);
                            } else {
                                switchCompat.setChecked(false);
                            }
                        } else if (data instanceof HashMap) {
                            // Traitement pour une HashMap
                            HashMap<String, Object> hashMap = (HashMap<String, Object>) data;
                            String valeur = (String) hashMap.get(composantEnCours.getNomComposant());
                            if (valeur.equals("ON")) {
                                switchCompat.setChecked(true);
                            } else {
                                switchCompat.setChecked(false);
                            }
                        } else {
                            // Traitement pour les autres types de données
                            Log.d("TAG", "Type de données inconnu");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("TAG", "Erreur : " + error.getMessage());
                    }
                });

                switchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    composantEnCours.setEtat(isChecked);
                    String etat = isChecked ? "ON" : "OFF";
                    Commande commande= new Commande(composantEnCours, localEnCours.getIdLocal());
                    if (etat.equals("ON"))
                        commande.setDetail_commande(DETAIL_ALLUMAGE);
                    else
                        commande.setDetail_commande(DETAIL_EXTINCTION);
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                    ref
                            .child(composantEnCours.getChemin())
                            .setValue(etat);
                    CreationCommande.enregistrerNouvelleCommande(commande);
                });
            } else {
                View cardView2 = layout.findViewWithTag(composantEnCours.getIdComposant());
                SeekBar seekBar = cardView2.findViewById(R.id.Seekbar);

                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
                DatabaseReference valueRef = databaseRef.child(composantEnCours.getChemin());
                valueRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Object data = snapshot.getValue();
                        if (data instanceof Long) {
                            Long valeur = (Long) data;
                            if (valeur == 1) {
                                seekBar.setProgress(1);

                            } else if (valeur == 2) {
                                seekBar.setProgress(2);
                            } else {
                                seekBar.setProgress(0);
                            }
                        } else if (data instanceof HashMap) {
                            // Traitement pour une HashMap
                            HashMap<String, Object> hashMap = (HashMap<String, Object>) data;
                            Long valeur = (Long) hashMap.get(composantEnCours.getNomComposant());
                            Log.d("TAG", "valeur" + cardView2.getTag());

                            if (valeur == 1L) {
                                seekBar.setProgress(1);

                            } else if (valeur == 2L) {
                                seekBar.setProgress(2);
                            } else {
                                seekBar.setProgress(0);
                            }
                        } else {
                            // Traitement pour les autres types de données
                            seekBar.setProgress(0);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("TAG", "Erreur : " + error.getMessage());
                    }
                });

                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        composantEnCours.setValeur(progress);
                        Commande commande= new Commande(composantEnCours, localEnCours.getIdLocal());
                        if(composantEnCours.getTypeComposant().equals("PORTE")){
                            if (progress==0){
                                commande.setDetail_commande("FERMETURE");
                            }
                            else{
                                commande.setDetail_commande("OUVERTURE");
                            }
                        }else{
                            if (progress==0){
                                commande.setDetail_commande("ARRET");
                            }
                            else{
                                commande.setDetail_commande("MARCHE");
                            }
                        }
                        CreationCommande.enregistrerNouvelleCommande(commande);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });
            }
        }
    }

    private void initComponentsOfDialog () {
        txtTypeComposant = dialogSupprimerComposant.findViewById(R.id.txtTypeComposantSupprimer);
        txtNomPiece = dialogSupprimerComposant.findViewById(R.id.txtNomPieceComposantSupprimer);
        txtNomLocal = dialogSupprimerComposant.findViewById(R.id.txtNomLocalComposantSupprimer);
        txtNomComposant = dialogSupprimerComposant.findViewById(R.id.txtNomComposantSupprimer);

        btnSupprimerComposant = dialogSupprimerComposant.findViewById(R.id.btnSupprimerComposant);
    }

    private void remplirChampsDialog(Composant composant){
        if (composant != null) {
            txtTypeComposant.setText(composant.getTypeComposant());
            txtNomPiece.setText(pieceEnCours.getNomPiece());
            txtNomLocal.setText(localEnCours.getNomLocal());
            txtNomComposant.setText(composant.getNomComposant());
        }
    }

    private void supprimer_composant (String idComposant, String chemin){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = db.collection("Local");
        collectionRef.whereEqualTo("idLocal", localEnCours.getIdLocal())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        DocumentSnapshot localDoc = querySnapshot.getDocuments().get(0);
                        List<Map<String, Object>> lesPieces = (List<Map<String, Object>>) localDoc.get("lesPieces");

                        boolean composantTrouvee = false;
                        Map<String, Object> pieceActu = null;
                        for (Map<String, Object> piece : lesPieces) {
                            if (piece.get("idPiece").equals(pieceEnCours.getIdPiece())) {
                                pieceActu = piece;
                                break;
                            }
                        }
                        List<Map<String, Object>> composants = (List<Map<String, Object>>) pieceActu.get("composants");

                        for (Map<String, Object> composant : composants) {
                            if (composant.get("idComposant").equals(idComposant)) {
                                composantTrouvee = true;
                                composants.remove(composant);
                                break;
                            }
                        }

                        if (composantTrouvee) {
                            localDoc.getReference().update("lesPieces", lesPieces)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getApplicationContext(), "Composant supprimée avec succès", Toast.LENGTH_SHORT).show();
                                            LocalUtils.supprimer_composer_realTime(chemin);
                                            //Aller vers MainPage
                                            LocalUtils.getLocalById(localEnCours.getIdLocal(), new OnSuccessListener<Local>() {
                                                @Override
                                                public void onSuccess(Local local) {
                                                    // Aller vers Main Page avec local mis a jour
                                                    Intent intent = new Intent(getApplicationContext(), MainPage.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    intent.putExtra("localId", local);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }, new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Erreur lors de la recuperation du local mis a jour
                                                    Toast.makeText(getApplicationContext(), "Erreur ", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getApplicationContext(), "Erreur lors de la suppression de la pièce : ", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(getApplicationContext(), "Composant non trouvée", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Une erreur s'est produite", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void recuperer_temperature_humidite(){
        String chemin = "/"+localEnCours.getNomLocal().toLowerCase();
        TextView humidite = findViewById(R.id.humidite);
        TextView temperature = findViewById(R.id.temperature);

        DatabaseUtils.getValueFromFirebase(chemin,"Temperature", Long.class, new DatabaseUtils.OnValueReceivedListener<Long>() {
            @Override
            public void onValueReceived(Long value) {
                temperature.setText(value+" C");
            }
        });

        DatabaseUtils.getValueFromFirebase(chemin,"Humidite", Long.class, new DatabaseUtils.OnValueReceivedListener<Long>() {
            @Override
            public void onValueReceived(Long value) {
                humidite.setText(value+" %");
            }
        });

    }

}

