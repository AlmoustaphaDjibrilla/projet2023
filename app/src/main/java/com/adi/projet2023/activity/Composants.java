package com.adi.projet2023.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.adi.projet2023.R;
import com.adi.projet2023.Utils.LocalUtils;
import com.adi.projet2023.activity.main_page.MainPage;
import com.adi.projet2023.model.Piece.Piece;
import com.adi.projet2023.model.composant.Composant;
import com.adi.projet2023.model.local.Local;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Map;

public class Composants extends AppCompatActivity {
    LinearLayout layout;
    List<Composant> composantList;
    Local localEnCours;
    Piece pieceEnCours;
    FloatingActionButton btnAddComposant;
    Dialog dialogSupprimerComposant;
    TextView txtTypeComposant, txtNomComposant, txtNomLocal, txtNomPiece;
    Button btnSupprimerComposant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affichage_composants);
        init();
        dialogSupprimerComposant= new Dialog(this);
        composantList = (List<Composant>) getIntent().getSerializableExtra("composants");
        localEnCours = (Local) getIntent().getSerializableExtra("localEnCours");
        pieceEnCours = (Piece) getIntent().getSerializableExtra("pieceEnCours");
        afficher_composants(composantList);

        btnAddComposant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AjouterComposant.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("localEnCours",localEnCours);
                intent.putExtra("pieceEnCours",pieceEnCours);
                startActivity(intent);
            }
        });

    }

    private void init(){
        layout=findViewById(R.id.composant);
        btnAddComposant = findViewById(R.id.addComposant);
    }

    private void afficher_composants(List<Composant> composantList){
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());

        //Initialisation des composants
        for(int i =0; i<composantList.size(); i++){
            Composant composantEnCours = composantList.get(i);
            View cardView = inflater.inflate(R.layout.composant_card, layout,false);
            View cardView2 = inflater.inflate(R.layout.composant2_card, layout,false);
            if(composantList.get(i).getTypeComposant().equals("AMPOULE")||
                    composantList.get(i).getTypeComposant().equals("REFRIGERATEUR")||
                    composantList.get(i).getTypeComposant().equals("CLIMATISEUR")||
                    composantList.get(i).getTypeComposant().equals("AUTRE")){
                TextView nom = cardView.findViewById(R.id.nom_composant);
                ImageView img = cardView.findViewById(R.id.img_composant);
                nom.setText(composantList.get(i).getNomComposant());
                switch (composantList.get(i).getTypeComposant()){
                    case "AMPOULE":
                        img.setImageResource(R.drawable.ampoules);
                        break;
                    case "CLIMATISEUR":
                        img.setImageResource(R.drawable.clim);
                        break;
                    case "PORTE":
                        img.setImageResource(R.drawable.porte);
                        break;
                    case "REFRIGERATEUR":
                        img.setImageResource(R.drawable.frigo);
                        break;
                    case "VENTILATEUR":
                        img.setImageResource(R.drawable.ventilateur);
                        break;
                    case "AUTRE":
                        img.setImageResource(R.drawable.autre);
                        break;
                    default:
                        break;
                }
                layout.addView(cardView);
                cardView.setOnLongClickListener(
                        l->{
                            dialogSupprimerComposant.setContentView(R.layout.supprimer_composant);
                            initComponentsOfDialog();
                            dialogSupprimerComposant.show();

                            remplirChampsDialog(composantEnCours);

                            btnSupprimerComposant.setOnClickListener(
                                    v->{
                                        supprimer_composant(composantEnCours.getIdComposant());
                                        dialogSupprimerComposant.dismiss();
                                    }
                            );

                            return true;
                        }
                );
            }else {
                TextView nom1 = cardView2.findViewById(R.id.nom_composant2);
                ImageView img1 = cardView2.findViewById(R.id.img_composant2);
                nom1.setText(composantList.get(i).getNomComposant());
                switch (composantList.get(i).getTypeComposant()){
                    case "AMPOULE":
                        img1.setImageResource(R.drawable.ampoule_animee);
                        break;
                    case "CLIMATISEUR":
                        img1.setImageResource(R.drawable.clim);
                        break;
                    case "PORTE":
                        img1.setImageResource(R.drawable.porte);
                        break;
                    case "REFRIGERATEUR":
                        img1.setImageResource(R.drawable.frigo);
                        break;
                    case "VENTILATEUR":
                        img1.setImageResource(R.drawable.ventilateur);
                        break;
                    case "AUTRE":
                        img1.setImageResource(R.drawable.autre);
                        break;
                    default:
                        break;
                }
                layout.addView(cardView2);
                cardView2.setOnLongClickListener(
                        l->{
                            dialogSupprimerComposant.setContentView(R.layout.supprimer_composant);
                            initComponentsOfDialog();
                            dialogSupprimerComposant.show();

                            remplirChampsDialog(composantEnCours);

                            btnSupprimerComposant.setOnClickListener(
                                    v->{
                                        supprimer_composant(composantEnCours.getIdComposant());
                                        dialogSupprimerComposant.dismiss();
                                    }
                            );

                            return true;
                        }
                );
            }

        }
    }
    private void initComponentsOfDialog(){
        txtTypeComposant= dialogSupprimerComposant.findViewById(R.id.txtTypeComposantSupprimer);
        txtNomPiece= dialogSupprimerComposant.findViewById(R.id.txtNomPieceComposantSupprimer);
        txtNomLocal= dialogSupprimerComposant.findViewById(R.id.txtNomLocalComposantSupprimer);
        txtNomComposant= dialogSupprimerComposant.findViewById(R.id.txtNomComposantSupprimer);

        btnSupprimerComposant= dialogSupprimerComposant.findViewById(R.id.btnSupprimerComposant);
    }

    private void remplirChampsDialog(Composant composant){
        if (composant!=null){
            txtTypeComposant.setText(composant.getTypeComposant());
            txtNomPiece.setText(pieceEnCours.getNomPiece());
            txtNomLocal.setText(localEnCours.getNomLocal());
            txtNomComposant.setText(composant.getNomComposant());
        }
    }

    private void supprimer_composant(String idComposant){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = db.collection("Local");
        collectionRef.whereEqualTo("idLocal",localEnCours.getIdLocal())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        DocumentSnapshot localDoc = querySnapshot.getDocuments().get(0);
                        List<Map<String, Object>> lesPieces = (List<Map<String, Object>>) localDoc.get("lesPieces");

                        boolean composantTrouvee = false ;
                        Map<String,Object> pieceActu = null;
                        for (Map<String, Object> piece : lesPieces) {
                            if (piece.get("idPiece").equals(pieceEnCours.getIdPiece())) {
                                pieceActu = piece;
                                break;
                            }
                        }
                        List<Map<String, Object>> composants = (List<Map<String, Object>>) pieceActu.get("composants");

                        for (Map<String, Object> composant : composants){
                            if (composant.get("idComposant").equals(idComposant)){
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
                                            //Aller vers MainPage
                                            LocalUtils.getLocalById(localEnCours.getIdLocal(), new OnSuccessListener<Local>() {
                                                @Override
                                                public void onSuccess(Local local) {
                                                    // Aller vers Main Page avec local mis a jour
                                                    Intent intent = new Intent(getApplicationContext(), MainPage.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    intent.putExtra("localId",local);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }, new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Erreur lors de la recuperation du local mis a jour
                                                    Toast.makeText(getApplicationContext(), "Erreur " , Toast.LENGTH_SHORT).show();
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
}