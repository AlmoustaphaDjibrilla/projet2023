package com.adi.projet2023.activity.main_page.fragment;


import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.adi.projet2023.R;
import com.adi.projet2023.model.Piece.Autre;
import com.adi.projet2023.model.Piece.Chambre;
import com.adi.projet2023.model.Piece.Cuisine;
import com.adi.projet2023.model.Piece.Douche;
import com.adi.projet2023.model.Piece.Piece;
import com.adi.projet2023.model.Piece.Salon;
import com.adi.projet2023.model.local.Local;
import com.adi.projet2023.model.local.TypeLocal;
import com.adi.projet2023.model.user.UserModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.grpc.internal.Stream;

public class FragmentHome extends Fragment {
    GridLayout gridPiece;
    private static final String ARG_PARAM1 = "param1";
    private String mParam1;
    public FragmentHome() {
        // Required empty public constructor
    }
    public static FragmentHome newInstance(String param1) {
        FragmentHome fragment = new FragmentHome();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_home,null);
        List<Piece> piecesList = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference localref = db.collection("Local");
        Task<QuerySnapshot> requete = localref.whereEqualTo("idLocal",mParam1).get();
        requete.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshotSnapshot) {
                for (DocumentSnapshot documentSnapshot : querySnapshotSnapshot.getDocuments()) {

                    Map<String, Object> localData = documentSnapshot.getData();
                    List<Map<String, String>> piecesData = (List<Map<String, String>>) localData.get("lesPieces");

                    for(Map<String, String> pieceData: piecesData){
                        String nomPiece = pieceData.get("nom");
                        String typePiece = pieceData.get("typePiece");
                        Piece p = null;
                        if(typePiece.equals("Chambre")){
                            p = new Chambre(nomPiece);
                        } else if (typePiece.equals("Cuisine")) {
                            p = new Cuisine(nomPiece);
                        }else if (typePiece.equals("Salon")) {
                            p = new Salon(nomPiece);
                        }else if (typePiece.equals("Douche")) {
                            p = new Douche(nomPiece);
                        }else if (typePiece.equals("Autre")) {
                            p = new Autre(nomPiece);
                        }

                        if(p!=null){
                            piecesList.add(p);
                        }
                    }

                    String designationLocal= documentSnapshot.getString("designationLocal");
                    String adresseLocal= documentSnapshot.getString("adresseLocal");
                    String idLocal= documentSnapshot.getString("idLocal");
                    String nomLocal= documentSnapshot.getString("nomLocal");
                    String quartierLocal= documentSnapshot.getString("quartierLocal");
                    TypeLocal typeLocal= TypeLocal.valueOf(documentSnapshot.getString("typeLocal"));
                    String villeLocal= documentSnapshot.getString("villeLocal");

                    Local local = new Local();
                    local.setDesignationLocal(designationLocal);
                    local.setAdresseLocal(adresseLocal);
                    local.setIdLocal(idLocal);
                    local.setLesPieces(piecesList);
                    //local.setLesUsers(lesUsers);
                    local.setNomLocal(nomLocal);
                    local.setQuartierLocal(quartierLocal);
                    local.setTypeLocal(typeLocal);
                    local.setVilleLocal(villeLocal);

                    gridPiece = (GridLayout) root.findViewById(R.id.GridPieces);
                    int marginPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
                    if(piecesList != null){
                        int numColumns = 2; // Nombre de colonnes de la grille
                        int numRows = (int) Math.ceil(piecesList.size() / (float) numColumns); // Nombre de lignes de la grille
                        gridPiece.setColumnCount(numColumns); // Définir le nombre de colonnes de la grille
                        gridPiece.setRowCount(numRows); // Définir le nombre de lignes de la grille
                        for (int i = 0; i < piecesList.size(); i++) {
                            View cardView = inflater.inflate(R.layout.piece_card, gridPiece, false);
                            GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
                            layoutParams.columnSpec = GridLayout.spec(i % numColumns, 1f);
                            layoutParams.rowSpec = GridLayout.spec(i / numColumns, 1f);
                            cardView.setLayoutParams(layoutParams);
                            layoutParams.setMargins(marginPx, marginPx, marginPx, marginPx);
                            TextView nomP = cardView.findViewById(R.id.nomPiece);
                            ImageView img = cardView.findViewById(R.id.imagePiece);
                            nomP.setText(piecesList.get(i).getNomPiece());
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
                    else {
                        Log.d("TAG", "Aucune piece");
                    }
                }
            }
        });
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