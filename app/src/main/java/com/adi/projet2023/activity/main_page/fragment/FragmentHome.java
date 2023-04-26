package com.adi.projet2023.activity.main_page.fragment;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.adi.projet2023.R;
import com.adi.projet2023.Utils.LocalUtils;
import com.adi.projet2023.activity.AjoutPieceActivity;
import com.adi.projet2023.activity.Composants;
import com.adi.projet2023.activity.main_page.MainPage;
import com.adi.projet2023.model.Piece.Piece;
import com.adi.projet2023.model.composant.Composant;
import com.adi.projet2023.model.local.Local;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FragmentHome extends Fragment {
    GridLayout gridPiece;
    Local localEnCours;
    List<Piece> piecesList;

    FloatingActionButton btnAddPiece;
    Dialog dialogSupprimerPiece;
    TextView txtTypePiece, txtNomPiece, txtNomLocal, txtCheminPiece, titreLocal;
    Button btnSupprimerPiece;

    ImageView imgQuitterMainPage;

    public FragmentHome(){

    }

    public FragmentHome(Local local) {
        // Required empty public constructor
        this.localEnCours= local;
        if( local!=null){
            piecesList= localEnCours.getLesPieces();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialogSupprimerPiece= new Dialog(this.getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_home,null);
        btnAddPiece = root.findViewById(R.id.addPiece);

        titreLocal= root.findViewById(R.id.titreLocal);
        titreLocal.setText(localEnCours.getNomLocal());

        imgQuitterMainPage=root.findViewById(R.id.imgQuitterMainPage);
        imgQuitterMainPage.setOnClickListener(
                view -> getActivity().finish()
        );

        btnAddPiece.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AjoutPieceActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("localEnCours",localEnCours);
                startActivity(intent);
            }
        });

        gridPiece = root.findViewById(R.id.GridPieces);
        afficher_pieces(piecesList);
        return root;
    }

    public void afficher_pieces(List<Piece> piecesList){
        LayoutInflater inflater = LayoutInflater.from(getContext());
        int marginPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
        if(piecesList != null && piecesList.size()!=0){
            // Nombre de colonnes de la grille
            int numColumns = 2;

            // Nombre de lignes de la grille
            int numRows = Math.max((int) Math.ceil(piecesList.size() / (float) numColumns), 1);

            // Aligner les cartes avec les limites de la grille
            gridPiece.setAlignmentMode(GridLayout.ALIGN_BOUNDS);

            // Activer les marges par défaut de la grille
            gridPiece.setUseDefaultMargins(true);
            gridPiece.setColumnCount(numColumns); // Définir le nombre de colonnes de la grille
            gridPiece.setRowCount(numRows); // Définir le nombre de lignes de la grille
            for (int i = 0; i < piecesList.size(); i++) {
                List<Composant> composants = piecesList.get(i).getLesComposants();
                Piece pieceEncours = piecesList.get(i);

                //Une vue pour chaque piece
                View cardView = inflater.inflate(R.layout.piece_card, gridPiece, false);

                //specification ligne et colonne pour la card
                GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
                layoutParams.width = getResources().getDisplayMetrics().widthPixels / numColumns - marginPx * 2;
                layoutParams.height = GridLayout.LayoutParams.WRAP_CONTENT;
                layoutParams.setMargins(marginPx/2, marginPx, marginPx, marginPx);
                layoutParams.columnSpec = GridLayout.spec(i % numColumns,1);
                layoutParams.rowSpec = GridLayout.spec(i / numColumns,1);

                //Si derniere piece
                if (i == piecesList.size() - 1) {
                    layoutParams.columnSpec = GridLayout.spec(i % numColumns, 1, 0f);
                    layoutParams.rowSpec = GridLayout.spec(i / numColumns, 1, 0f);
                }

                //Marge
                cardView.setLayoutParams(layoutParams);
                TextView nomP = cardView.findViewById(R.id.nomPiece);
                ImageView img = cardView.findViewById(R.id.imagePiece);
                nomP.setText(piecesList.get(i).getNomPiece());
                switch (piecesList.get(i).getTypePiece()) {
                    case "CHAMBRE":
                        img.setImageResource(R.drawable.chambre);
                        break;
                    case "SALON":
                        img.setImageResource(R.drawable.salon);
                        break;
                    case "CUISINE":
                        img.setImageResource(R.drawable.cuisine);
                        break;
                    case "DOUCHE":
                        img.setImageResource(R.drawable.douche);
                        break;
                    case "AUTRE":
                        img.setImageResource(R.drawable.autre_piece);
                        break;
                    default:
                        break;
                }
                gridPiece.addView(cardView);
                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), Composants.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("composants",(Serializable) composants) ;
                        intent.putExtra("localEnCours",(Serializable) localEnCours) ;
                        intent.putExtra("pieceEnCours",(Serializable) pieceEncours) ;
                        startActivity(intent);
                    }
                });
                cardView.setOnLongClickListener(
                        l->{
                            dialogSupprimerPiece.setContentView(R.layout.supprimer_piece);
                            initComponentsOfDialog();
                            dialogSupprimerPiece.show();

                            remplirChampsDialog(pieceEncours);

                            btnSupprimerPiece.setOnClickListener(
                                    v->{
                                        supprimer_piece(pieceEncours.getIdPiece());
                                        LocalUtils.supprimer_composant_piece_realTime(pieceEncours);
                                        dialogSupprimerPiece.dismiss();
                                    }
                            );

                            return true;
                        }
                );
            }
        }
        else {
            Log.d("TAG", "Aucune piece");
        }
    }

    private void initComponentsOfDialog(){
        txtTypePiece= dialogSupprimerPiece.findViewById(R.id.txtTypePieceSupprimer);
        txtNomPiece= dialogSupprimerPiece.findViewById(R.id.txtNomPieceSupprimerPiece);
        txtNomLocal= dialogSupprimerPiece.findViewById(R.id.txtNomLocalSupprimerPiece);
        txtCheminPiece= dialogSupprimerPiece.findViewById(R.id.txtCheminPieceSupprimerComposant);

        btnSupprimerPiece= dialogSupprimerPiece.findViewById(R.id.btnSupprimerPiece);
    }

    private void remplirChampsDialog(Piece piece){
        if (piece!=null){
            txtTypePiece.setText(piece.getTypePiece());
            txtNomPiece.setText(piece.getNomPiece());
            txtNomLocal.setText(localEnCours.getNomLocal());
            txtCheminPiece.setText(piece.getAdressePiece());
        }
    }
    public void supprimer_piece(String id) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = db.collection("Local");

        collectionRef.whereEqualTo("idLocal", localEnCours.getIdLocal())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        DocumentSnapshot localDoc = queryDocumentSnapshots.getDocuments().get(0);
                        List<Map<String, Object>> lesPieces = (List<Map<String, Object>>) localDoc.get("lesPieces");

                        boolean pieceTrouvee = false;
                        for (Map<String, Object> piece : lesPieces) {
                            if (piece.get("idPiece").equals(id)) {
                                lesPieces.remove(piece);
                                pieceTrouvee = true;
                                break;
                            }
                        }

                        if (pieceTrouvee) {
                            localDoc.getReference().update("lesPieces", lesPieces)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getActivity(), "Piece supprimée avec succès", Toast.LENGTH_SHORT).show();
                                            //Aller vers MainPage
                                            LocalUtils.getLocalById(localEnCours.getIdLocal(), new OnSuccessListener<Local>() {
                                                @Override
                                                public void onSuccess(Local local) {
                                                    // Aller vers Main Page avec local mis a jour
                                                    gridPiece.removeAllViews();
                                                    afficher_pieces(local.getLesPieces());
                                                }
                                            }, new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Erreur lors de la recuperation du local mis a jour
                                                    Toast.makeText(getActivity(), "Erreur : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getActivity(), "Erreur lors de la suppression de la pièce : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(getActivity(), "Pièce non trouvée", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Erreur lors de la récupération Local : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}