package com.adi.projet2023.creation;

import com.adi.projet2023.model.local.AutreLocal;
import com.adi.projet2023.model.local.Entreprise;
import com.adi.projet2023.model.local.Local;
import com.adi.projet2023.model.local.Maison;
import com.adi.projet2023.model.user.UserModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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
        DocumentReference docLocal=
                FirebaseFirestore.getInstance()
                        .collection(PATH_LOCAL_DATABASES)
                        .document(localEnCours.getIdLocal());


        Map<String, Object> mapUser= new HashMap<>();
        mapUser.put("uid", userAAjouter.getUid());
        mapUser.put("password", userAAjouter.getPassword());
        mapUser.put("email", userAAjouter.getEmail());
        mapUser.put("nom", userAAjouter.getNom());
        mapUser.put("dateEnregistrement", userAAjouter.getDateEnregistrement());
        mapUser.put("admin", userAAjouter.isAdmin());
        mapUser.put("user", userAAjouter.isUser());

        docLocal
                .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                Local local= (Local) documentSnapshot.toObject(Local.class);
                                List<Map<String, Object>> lesUsers= (List<Map<String, Object>>) documentSnapshot.get("lesUsers");
                                if (lesUsers==null)
                                    lesUsers= new ArrayList<>();

                                lesUsers.add(mapUser);
                            }
                        });

        localEnCours.ajouterUser(userAAjouter);
        docLocal.set(localEnCours);
    }
}
