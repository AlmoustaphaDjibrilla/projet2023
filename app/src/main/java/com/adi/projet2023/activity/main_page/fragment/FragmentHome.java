package com.adi.projet2023.activity.main_page.fragment;


import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.adi.projet2023.R;
import com.adi.projet2023.model.Piece.Autre;
import com.adi.projet2023.model.Piece.Chambre;
import com.adi.projet2023.model.Piece.Cuisine;
import com.adi.projet2023.model.Piece.Douche;
import com.adi.projet2023.model.Piece.Piece;
import com.adi.projet2023.model.Piece.Salon;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

public class FragmentHome extends Fragment {
    String LocalId;
    GridLayout gridPiece;
    public FragmentHome() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            LocalId = getArguments().getString("envoiLocal");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_home,null);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference locauxRef = db.collection("Local");
        Query requete = locauxRef.whereEqualTo("id",LocalId);
        requete.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<Piece> piecesList = new ArrayList<>();
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                    String nom = documentSnapshot.getString("nom");
                    String type = documentSnapshot.getString("TypePiece");
                    if(type.equals("Chambre")){
                        Piece p = new Chambre(nom);
                        piecesList.add(p);
                    }
                    else if(type.equals("Salon")){
                        Piece p = new Salon(nom);
                        piecesList.add(p);
                    }
                    else if(type.equals("Cuisie")){
                        Piece p = new Cuisine(nom);
                        piecesList.add(p);
                    }
                    else if(type.equals("Douche")){
                        Piece p = new Douche(nom);
                        piecesList.add(p);
                    }
                    else if(type.equals("Autre")){
                        Piece p = new Autre(nom);
                        piecesList.add(p);
                    }
                }
                gridPiece = (GridLayout) root.findViewById(R.id.GridPieces);
                int marginPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics());
                for(int i =0; i<piecesList.size(); i++) {
                    View cardView = inflater.inflate(R.layout.piece_card, gridPiece, false);
                    GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
                    layoutParams.columnSpec = GridLayout.spec(i % 2, 1f);
                    cardView.setLayoutParams(layoutParams);
                    layoutParams.setMargins(marginPx, marginPx, marginPx, marginPx);
                    TextView nom = cardView.findViewById(R.id.nomPiece);
                    ImageView img = cardView.findViewById(R.id.imagePiece);
                    nom.setText(piecesList.get(i).getNomPiece());
                    switch (piecesList.get(i).getTypePiece().toString()) {
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

                        }
                    });
                }
            }
        });
        // Inflate the layout for this fragment
        return root;
    }

//    public void ajouterPiece(){
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        CollectionReference locauxRef = db.collection("Local");
//        DocumentReference localRef = locauxRef.document(LocalId);
//        CollectionReference piecesRef = localRef.collection("Piece");
//
//        localRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @SuppressLint("RestrictedApi")
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//                        List<Piece> pieces = (List<Piece>) document.get("ListPiece");
//                        if (pieces == null) {
//                            pieces = new ArrayList<>();
//                        }
//                        // Ajouter la nouvelle pièce à la liste
//                        pieces.add(new Piece(nom));
//                        // Mettre à jour la liste de pièces du document
//                        localRef.update("ListPiece", pieces);
//                    } else {
//                        Log.d(TAG, "Aucun document trouvé avec l'identifiant : " + LocalId);
//                    }
//                } else {
//                    Log.d(TAG, "Erreur lors de la récupération du document : ", task.getException());
//                }
//            }
//        });
//
//    }

}