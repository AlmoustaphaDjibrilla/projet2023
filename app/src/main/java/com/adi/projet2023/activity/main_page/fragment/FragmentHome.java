package com.adi.projet2023.activity.main_page.fragment;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.adi.projet2023.R;
import com.adi.projet2023.activity.AjoutPieceActivity;
import com.adi.projet2023.activity.Composants;
import com.adi.projet2023.creation.CreationPiece;
import com.adi.projet2023.model.Piece.Piece;
import com.adi.projet2023.model.composant.Composant;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FragmentHome extends Fragment {
    final String PATH_PIECE_DATABASES = "Piece";
    final String PATH_COMPOSANT_DATABASE= "Composant";
    final String PATH_USERS_DATABASES = "Users";

    GridLayout gridPiece;
    Local localEnCours;
    ArrayList<Piece> piecesList;
//    HashMap<String, Piece> piecesList;
    List<Composant> composants;
//    HashMap<String, Composant> composants;

    FloatingActionButton btnAddPiece;


    Dialog dialogSupprimerPiece;
    TextView txtTypePiece, txtNomPiece, txtNomLocal, txtCheminPiece;
    Button btnSupprimerPiece;


    public FragmentHome(Local local) {
        // Required empty public constructor
        this.localEnCours= local;
        piecesList= new ArrayList<>();
        composants= new ArrayList<>();

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialogSupprimerPiece= new Dialog(this.getContext());

        CollectionReference collectionPiece=
                FirebaseFirestore.getInstance()
                        .collection(PATH_PIECE_DATABASES);

        collectionPiece
                .whereEqualTo("adresseLocalEnCours", localEnCours.getAdresseLocal())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            Piece piece= documentSnapshot.toObject(Piece.class);
                            piecesList.add(piece);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Erreur...", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_home,null);
        btnAddPiece = root.findViewById(R.id.addPiece);

        btnAddPiece.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AjoutPieceActivity.class);
                intent.putExtra("localEnCours", localEnCours);
                startActivity(intent);
            }
        });

        gridPiece = (GridLayout) root.findViewById(R.id.GridPieces);
        int marginPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());


        CollectionReference collectionPiece=
                FirebaseFirestore.getInstance()
                        .collection(PATH_PIECE_DATABASES);

        collectionPiece
                .whereEqualTo("adresseLocalEnCours", localEnCours.getAdresseLocal())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    ArrayList<Piece> piecesList= new ArrayList<>();

                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Piece piece = documentSnapshot.toObject(Piece.class);
                            piecesList.add(piece);
                        }

                        if (piecesList != null && piecesList.size() != 0) {
                            int numColumns = 2; // Nombre de colonnes de la grille
                            int numRows = (int) Math.ceil(piecesList.size() / (float) numColumns); // Nombre de lignes de la grille
                            gridPiece.setColumnCount(numColumns); // Définir le nombre de colonnes de la grille
                            gridPiece.setRowCount(numRows); // Définir le nombre de lignes de la grille

                            for (int i = 0; i < piecesList.size(); i++) {


                                Piece pieceActuelle = piecesList.get(i);

                                CollectionReference collectionComposants =
                                        FirebaseFirestore.getInstance()
                                                .collection(PATH_COMPOSANT_DATABASE);

                                collectionComposants
                                        .whereEqualTo("adressePieceEnCours", pieceActuelle.getIdPiece())
                                        .get()
                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                                    composants.add(documentSnapshot.toObject(Composant.class));
                                                }
                                            }
                                        });
                                //                List<Composant> composants = piecesList.get(i).getLesComposants();
                                View cardView = inflater.inflate(R.layout.piece_card, gridPiece, false);
                                GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
                                layoutParams.columnSpec = GridLayout.spec(i % numColumns, 1f);
                                layoutParams.rowSpec = GridLayout.spec(i / numColumns, 1f);
                                cardView.setLayoutParams(layoutParams);
                                layoutParams.setMargins(marginPx, marginPx, marginPx, marginPx);
                                TextView nomP = cardView.findViewById(R.id.nomPiece);
                                ImageView img = cardView.findViewById(R.id.imagePiece);
                                nomP.setText(piecesList.get(i).getNomPiece());
                                switch (piecesList.get(i).getLeTypePiece().toString()) {
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
                                        intent.putExtra("pieceActuelle", (Serializable) pieceActuelle);
                                        startActivity(intent);
                                    }
                                });

                                /**
                                 * Pour supprimer une piece
                                 * sur un long click
                                 */
                                cardView.setOnLongClickListener(
                                        l->{
                                            dialogSupprimerPiece.setContentView(R.layout.layout_supprimer_piece);
                                            initComponentsOfDialog();
                                            dialogSupprimerPiece.show();

                                            remplirChampsDialog(pieceActuelle);

                                            btnSupprimerPiece.setOnClickListener(
                                                    v->{
                                                        supprimerPiece(pieceActuelle);
                                                    }
                                            );

                                            return true;
                                        }
                                );
                            }
                        } else {
                            Log.d("TAG", "Aucune piece");
                        }
                    }
                });
        return root;
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
            txtTypePiece.setText(piece.getLeTypePiece().toString());
            txtNomPiece.setText(piece.getNomPiece());
            txtNomLocal.setText(piece.getAdresseLocalEnCours());
            txtCheminPiece.setText(piece.getAdressePiece());
        }
    }

    private void supprimerPiece(Piece pieceSuppimer){
        CollectionReference collectionUsers=
                FirebaseFirestore.getInstance().collection(PATH_USERS_DATABASES);

        DocumentReference docUser=
                collectionUsers
                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        docUser.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.getBoolean("admin")){
                            CreationPiece.supprimerPiece(pieceSuppimer);
                            Toast.makeText(getContext(), pieceSuppimer.getNomPiece()+" supprimé...", Toast.LENGTH_SHORT).show();
                            dialogSupprimerPiece.dismiss();
                        }else{
                            Toast.makeText(getContext(), "Désolé vous n'êtes pas administrateur", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}