package com.adi.projet2023.Utils;

import com.adi.projet2023.model.Piece.Piece;
import com.adi.projet2023.model.local.Local;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VerificationNom {

    public static void verifier_nom_local(String nom, OnSuccessListener<Boolean> successListener, OnFailureListener failureListener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection("Local");
        ref.whereEqualTo("nomLocal", nom)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        if(querySnapshot.isEmpty()){
                            successListener.onSuccess(true);
                        }
                        else{
                            successListener.onSuccess(false);
                        }
                    }
                }).addOnFailureListener(failureListener);
    }

    public static void verifier_nom_piece(String nom, Local local, OnSuccessListener<Boolean> successListener, OnFailureListener failureListener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection("Local");
        ref.whereEqualTo("idLocal", local.getIdLocal())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        DocumentSnapshot localDoc = querySnapshot.getDocuments().get(0);
                        List<HashMap<String, Object>> lesPieces = (List<HashMap<String, Object>>) localDoc.get("lesPieces");

                        boolean exist = false;
                        for (Map<String, Object> piece : lesPieces) {
                            if (piece.get("nom").equals(nom)) {
                                exist = true;
                                break;
                            }
                        }
                        successListener.onSuccess(exist);
                        }
                }).addOnFailureListener(failureListener);
    }

    public static void verifier_nom_composant(String nom, Local local, Piece piece, OnSuccessListener<Boolean> successListener, OnFailureListener failureListener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection("Local");
        ref.whereEqualTo("idLocal",local.getIdLocal())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        DocumentSnapshot localDoc = querySnapshot.getDocuments().get(0);
                        List<HashMap<String, Object>> lesPieces = (List<HashMap<String, Object>>) localDoc.get("lesPieces");

                        Map<String, Object> pieceActu = null;
                        for (Map<String, Object> p : lesPieces) {
                            if (p.get("idPiece").equals(piece.getIdPiece())) {
                                pieceActu = p;
                                break;
                            }
                        }

                        List<HashMap<String, Object>> lesComposants = (List<HashMap<String, Object>>) pieceActu.get("composants");
                        boolean exist = false;
                        for (Map<String, Object> composant : lesComposants) {
                            if (composant.get("nom").equals(nom)) {
                                exist = true;
                                break;
                            }
                        }
                        successListener.onSuccess(exist);
                    }
                }).addOnFailureListener(failureListener);
    }
}
