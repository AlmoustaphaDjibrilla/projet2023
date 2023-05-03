package com.adi.projet2023.creation;

import android.util.Log;

import androidx.annotation.NonNull;

import com.adi.projet2023.model.local.AutreLocal;
import com.adi.projet2023.model.local.Entreprise;
import com.adi.projet2023.model.local.Local;
import com.adi.projet2023.model.local.Maison;
import com.adi.projet2023.model.user.UserModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class CreationLocal {
    final static String PATH_LOCAL_DATABASES= "Local";

    public static void creationMaison(Maison maison){
        DocumentReference docMaison=
                FirebaseFirestore.getInstance()
                        .collection(PATH_LOCAL_DATABASES)
                        .document(maison.getIdLocal());
        docMaison.set(maison);
    }

    public static void creationEntreprise(Entreprise entreprise){
        DocumentReference docEntreprise=
                FirebaseFirestore.getInstance()
                        .collection(PATH_LOCAL_DATABASES)
                        .document(entreprise.getIdLocal());
        docEntreprise.set(entreprise);
    }

    public static void creationAutreLocal(AutreLocal autreLocal){
        DocumentReference docAutreLocal=
                FirebaseFirestore.getInstance()
                        .collection(PATH_LOCAL_DATABASES)
                        .document(autreLocal.getIdLocal());
        docAutreLocal.set(autreLocal);
    }

    public static void supprimerLocal(Local local){
        DocumentReference docLocalSupp=
                FirebaseFirestore.getInstance()
                        .collection(PATH_LOCAL_DATABASES)
                        .document(local.getIdLocal());

        docLocalSupp.delete();
    }

    public static void ajouterUserALocal(UserModel userAAjouter, Local localEnCours){

        Map<String, Object> newUser= new HashMap<>();
        newUser.put("uid", userAAjouter.getUid());
        newUser.put("password", userAAjouter.getPassword());
        newUser.put("email", userAAjouter.getEmail());
        newUser.put("nom", userAAjouter.getNom());
        newUser.put("dateEnregistrement", userAAjouter.getDateEnregistrement());
        newUser.put("admin", userAAjouter.isAdmin());
        newUser.put("user", userAAjouter.isUser());

        CollectionReference localRef = FirebaseFirestore.getInstance().collection("Local");
        Query req = localRef.whereEqualTo("idLocal", localEnCours.getIdLocal());
        req.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                DocumentSnapshot localDoc = querySnapshot.getDocuments().get(0);
                List<Map<String, Object>> lesUsers = (List<Map<String, Object>>) localDoc.get("lesUsers");
                if (lesUsers == null) {
                    lesUsers = new ArrayList<>();
                }
                lesUsers.add(newUser);
                localDoc.getReference().update("lesUsers",lesUsers);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG",e.getMessage());
            }
        });


    }

    public static void retirerUserLocal(UserModel userARetirer, Local localEnCours){

        Map<String, Object> user= new HashMap<>();
        user.put("uid", userARetirer.getUid());
        user.put("password", userARetirer.getPassword());
        user.put("email", userARetirer.getEmail());
        user.put("nom", userARetirer.getNom());
        user.put("dateEnregistrement", userARetirer.getDateEnregistrement());
        user.put("admin", userARetirer.isAdmin());
        user.put("user", userARetirer.isUser());

        CollectionReference localRef = FirebaseFirestore.getInstance().collection("Local");
        Query req = localRef.whereEqualTo("idLocal", localEnCours.getIdLocal());
        req.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                DocumentSnapshot localDoc = querySnapshot.getDocuments().get(0);
                List<Map<String, Object>> lesUsers = (List<Map<String, Object>>) localDoc.get("lesUsers");

                boolean trouve = false;
                for (Map<String, Object> user : lesUsers) {
                    if (user.get("uid").equals(userARetirer.getUid())) {
                        lesUsers.remove(user);
                        trouve = true;
                        break;
                    }
                }
                if(trouve){
                    localDoc.getReference().update("lesUsers",lesUsers);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG",e.getMessage());
            }
        });

    }
}
