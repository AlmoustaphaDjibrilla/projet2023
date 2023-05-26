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

}
