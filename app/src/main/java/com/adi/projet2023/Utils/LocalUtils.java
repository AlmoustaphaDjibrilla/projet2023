package com.adi.projet2023.Utils;

import com.adi.projet2023.adapter.AdapterListCommandes;
import com.adi.projet2023.model.Commande.Commande;
import com.adi.projet2023.model.Piece.Piece;
import com.adi.projet2023.model.local.Local;
import com.adi.projet2023.model.local.TypeLocal;
import com.adi.projet2023.model.user.UserModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

                //Ne pas toucher!!
                List<HashMap<String, Object>> list= (List<HashMap<String, Object>>) documentSnapshot.get("lesPieces");
                List<Piece> lesPieces = new ArrayList<>();
                if(list != null){
                    for (HashMap<String, Object> hashMap : list) {
                        String idPiece = (String) hashMap.get("idPiece");
                        String nom = (String) hashMap.get("nom");
                        String type = (String) hashMap.get("typePiece");
                        String chemin = (String) hashMap.get("chemin");
                        List<HashMap<String, Object>> listHashMap = (List<HashMap<String, Object>>) hashMap.get("composants");
                        lesPieces.add(new Piece(idPiece,type,nom,chemin,listHashMap));
                    }
                }

                List<HashMap<String, Object>> listUsers= (List<HashMap<String, Object>>) documentSnapshot.get("lesUsers");
                List<UserModel> lesUsers= new ArrayList<>();
                if (listUsers!=null){
                    for (HashMap<String, Object> hashMapUser: listUsers){
                        String userId= (String) hashMapUser.get("Uid");
                        String mailUser= (String) hashMapUser.get("email");
                        String nomUser= (String) hashMapUser.get("nom");
                        String dateEnregistrement= (String) hashMapUser.get("dateEnregistrement");

                        UserModel userModel= new UserModel();
                        userModel.setUid(userId);
                        userModel.setEmail(mailUser);
                        userModel.setNom(nomUser);
                        userModel.setDateEnregistrement(dateEnregistrement);

                        lesUsers.add(userModel);
                    }
                }

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
                local.setLesPieces(lesPieces);
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

    public static void supprimer_composer_realTime(String chemin){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(chemin);
        ref.removeValue();
    }

    private static void supprimer_route_temp(Local local){
        String chemin= "/"+local.getNomLocal().toLowerCase();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(chemin);
        ref.child("Temperature").removeValue();
    }

    private static void supprimer_route_presense(Local local){
        String chemin= "/"+local.getNomLocal().toLowerCase();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(chemin);
        ref.child("Presence").removeValue();
    }

    private static void supprimer_route_hum(Local local){
        String chemin= "/"+local.getNomLocal().toLowerCase();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(chemin);
        ref.child("Humidite").removeValue();
    }
    public static void supprimer_composant_piece_realTime(Piece piece){
        for(int i =0; i<piece.getLesComposants().size();i++){
            LocalUtils.supprimer_composer_realTime(piece.getLesComposants().get(i).getChemin());
        }
    }

    public  static void supprimer_local_realTime(Local local){
        supprimer_route_temp(local);
        supprimer_route_hum(local);
        supprimer_route_presense(local);
        if(local.getLesPieces()!=null){
            for(int i =0;i<local.getLesPieces().size();i++){
                supprimer_composant_piece_realTime(local.getLesPieces().get(i));
            }
        }
    }

    public static void supprimer_historique_local(Local local){
        CollectionReference collectionReference=
                FirebaseFirestore.getInstance()
                        .collection("Commandes");

        collectionReference.whereEqualTo("local",local.getIdLocal())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            documentSnapshot.getReference().delete();
                        }

                    }
                });
    }

}