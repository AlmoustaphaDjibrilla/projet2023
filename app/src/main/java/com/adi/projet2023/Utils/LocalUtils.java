package com.adi.projet2023.Utils;

import com.adi.projet2023.model.Piece.Piece;
import com.adi.projet2023.model.local.Local;
import com.adi.projet2023.model.local.TypeLocal;
import com.adi.projet2023.model.user.UserModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocalUtils {

    public static void getLocalById(String id, OnSuccessListener<Local> successListener, OnFailureListener failureListener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference localRef = db.collection("Local");
        Query query = localRef.whereEqualTo("idLocal", id);
        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
            if (documents.size() > 0) {
                DocumentSnapshot documentSnapshot = documents.get(0);

                String designationLocal= documentSnapshot.getString("designationLocal");
                String adresseLocal= documentSnapshot.getString("adresseLocal");
                String idLocal= documentSnapshot.getString("idLocal");

                List<HashMap<String, Object>> list= (List<HashMap<String, Object>>) documentSnapshot.get("lesPieces");
                List<Piece> lesPieces = new ArrayList<>();
                if(list != null){
                    for (HashMap<String, Object> hashMap : list) {
                        String idPiece = (String) hashMap.get("idPiece");
                        String nom = (String) hashMap.get("nom");
                        String type = (String) hashMap.get("typePiece");
                        String chemin = (String) hashMap.get("chemin");
//                        List<HashMap<String, Object>> listHashMap = (List<HashMap<String, Object>>) hashMap.get("composants");
//                        lesPieces.add(new Piece(idPiece,type,nom,chemin,listHashMap));
                    }
                }

                List<UserModel> lesUsers= (List<UserModel>) documentSnapshot.get("lesUsers");

                String nomLocal= documentSnapshot.getString("nomLocal");
                String quartierLocal= documentSnapshot.getString("quartierLocal");
                TypeLocal typeLocal= TypeLocal.valueOf(documentSnapshot.getString("typeLocal"));
                String villeLocal= documentSnapshot.getString("villeLocal");
                String dateEnregistrementLocal = documentSnapshot.getString("dateEnregistrement");

                //creer un nouvel local et lui affecter les attributs recuperés ci-dessus
                Local local= new Local();
                local.setDesignationLocal(designationLocal);
                local.setAdresseLocal(adresseLocal);
                local.setIdLocal(idLocal);
             //   local.setLesPieces(lesPieces);
                local.setLesUsers(lesUsers);
                local.setNomLocal(nomLocal);
                local.setQuartierLocal(quartierLocal);
                local.setTypeLocal(typeLocal);
                local.setVilleLocal(villeLocal);
                local.setDateEnregistrement(dateEnregistrementLocal);

                successListener.onSuccess(local);
            } else {
                successListener.onSuccess(null);
            }
        }).addOnFailureListener(failureListener);
    }

}
