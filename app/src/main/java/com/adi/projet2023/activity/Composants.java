package com.adi.projet2023.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.adi.projet2023.R;
import com.adi.projet2023.creation.CreationComposant;
import com.adi.projet2023.model.Piece.Piece;
import com.adi.projet2023.model.Piece.TypePiece;
import com.adi.projet2023.model.composant.Composant;
import com.adi.projet2023.model.composant.TypeComposant;
import com.adi.projet2023.model.local.Local;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Composants extends AppCompatActivity {

    final String PATH_USERS_DATABASE= "Users";
    final String PATH_COMPOSANT_DATABASES= "Composant";



    LinearLayout layout;
    List<Composant> composantList;
    Local localEnCours;
    Piece pieceEnCours;
    FloatingActionButton btnAddComposant;

    TypeComposant[] lesTypesComposants;


    Dialog dialogAjoutComposant;
    AutoCompleteTextView txtTypeComposant;
    EditText txtNomComposant;
    Button btnAjoutComposant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affichage_composants);
        init();
//        composantList = (List<Composant>) getIntent().getSerializableExtra("composants");
        composantList= new ArrayList<>();
        pieceEnCours= (Piece) getIntent().getSerializableExtra("pieceActuelle");


//        localEnCours = (Local) getIntent().getSerializableExtra("localEnCours");
//        pieceEnCours = (Piece) getIntent().getSerializableExtra("pieceEnCours");

        dialogAjoutComposant= new Dialog(this);


        btnAddComposant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), AjouterComposant.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra("localEnCours",localEnCours);
//                intent.putExtra("pieceEnCours",pieceEnCours);
//                startActivity(intent);

                dialogAjoutComposant.setContentView(R.layout.activity_ajouter_composant);
                initComponentsOfDialog();
                dialogAjoutComposant.show();
                btnAjoutComposant.setOnClickListener(
                        v->{
                            String choixComposant= txtTypeComposant.getText().toString();
                            String nomComposant= txtNomComposant.getText().toString();

                            ajouterComposant(choixComposant, nomComposant);
                        }
                );
            }
        });


        CollectionReference collectionComposants=
                FirebaseFirestore.getInstance().collection(PATH_COMPOSANT_DATABASES);

        collectionComposants
                .whereEqualTo("adressePieceEnCours", pieceEnCours.getAdressePiece())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            Composant composantActuelle= documentSnapshot.toObject(Composant.class);
                            composantList.add(composantActuelle);
                        }

                        //Initialisation des composants
                        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                        for(int i =0; i<composantList.size(); i++){
                            View cardView = inflater.inflate(R.layout.composant_card, layout,false);
                            View cardView2 = inflater.inflate(R.layout.composant2_card, layout,false);
                            if(composantList.get(i).getTypeComposant().toString().equals("AMPOULE")||
                                    composantList.get(i).getTypeComposant().toString().equals("REFRIGERATEUR")||
                                    composantList.get(i).getTypeComposant().toString().equals("CLIMATISEUR")||
                                    composantList.get(i).getTypeComposant().toString().equals("AUTRE")){
                                TextView nom = cardView.findViewById(R.id.nom_composant);
                                ImageView img = cardView.findViewById(R.id.img_composant);
                                nom.setText(composantList.get(i).getNomComposant());
                                switch (composantList.get(i).getTypeComposant().toString()){
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
                            }else {
                                TextView nom = cardView2.findViewById(R.id.nom_composant);
                                ImageView img = cardView2.findViewById(R.id.img_composant);
                                nom.setText(composantList.get(i).getNomComposant());
                                switch (composantList.get(i).getTypeComposant().toString()){
                                    case "AMPOULE":
                                        img.setImageResource(R.drawable.ampoule_animee);
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
                                layout.addView(cardView2);
                            }
                        }

                    }
                });
    }

    private void init(){
        layout=findViewById(R.id.composant);
        btnAddComposant = findViewById(R.id.addComposant);

    }


    private void initComponentsOfDialog(){
        txtTypeComposant= dialogAjoutComposant.findViewById(R.id.txtTypeAjoutComposant);
        txtNomComposant= dialogAjoutComposant.findViewById(R.id.txtNomAjoutComposant);
        btnAjoutComposant= dialogAjoutComposant.findViewById(R.id.btnAjouterComposant);

        lesTypesComposants= TypeComposant.values();
        ArrayAdapter<TypeComposant> adapter= new ArrayAdapter<>(this, R.layout.type_composants, lesTypesComposants);

        txtTypeComposant.setAdapter(adapter);
    }

    private void ajouterComposant(String choixComposant, String nomComposant){
        if (choixComposant==null || choixComposant.equals("")){
            txtTypeComposant.setError("Veuillez choisir le type du composant");
            return;
        }
        if (nomComposant==null || nomComposant.equals("")){
            txtNomComposant.setError("Veuillez saisir le nom du composant");
            return;
        }

        CollectionReference collectionUsers=
                FirebaseFirestore.getInstance().collection(PATH_USERS_DATABASE);

        DocumentReference docUsermodel=
                collectionUsers
                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        docUsermodel.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.getBoolean("admin")){

                            TypeComposant typeComposant= TypeComposant.valueOf(choixComposant);

                            Composant composant= new Composant(typeComposant, nomComposant);
                            composant.setAdressePieceEnCours(pieceEnCours.getAdressePiece());
                            composant.setAdresseLocalEnCours(pieceEnCours.getAdresseLocalEnCours());

                            String nouvelIdComposant= pieceEnCours.getAdressePiece()+"_"+composant.getAdresseComposant();

                            composant.setIdComposant(nouvelIdComposant);
                            composant.setAdresseComposant(nouvelIdComposant);

                            enregistrementComposant(composant);
                            dialogAjoutComposant.dismiss();

                        }else{
                            Toast.makeText(getApplicationContext(), "Désolé vous n'êtes pas administrateur", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Aucune réponse", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void enregistrementComposant(Composant composant){
        CreationComposant.creationComposant(composant);
        Toast.makeText(this, composant.getNomComposant()+" ajouté avec succès", Toast.LENGTH_SHORT).show();
    }

}