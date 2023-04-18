package com.adi.projet2023.creation;

import com.adi.projet2023.model.local.AutreLocal;
import com.adi.projet2023.model.local.Entreprise;
import com.adi.projet2023.model.local.Maison;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class CreationLocal {
    final static String PATH_LOCAL_DATABASES= "Local";

    public static void creationMaison(String nomMaison, String quartierMaison, String villeMaison){
        Maison maison= new Maison(nomMaison, quartierMaison, villeMaison);

        DocumentReference docMaison=
                FirebaseFirestore.getInstance()
                        .collection(PATH_LOCAL_DATABASES)
                        .document(maison.getIdLocal());
        docMaison.set(maison);
    }

    public static void creationEntreprise(String nomEntreprise, String quartierEntreprise, String villeEntreprise){
        Entreprise entreprise= new Entreprise(nomEntreprise, quartierEntreprise, villeEntreprise);

        DocumentReference docEntreprise=
                FirebaseFirestore.getInstance()
                        .collection(PATH_LOCAL_DATABASES)
                        .document(entreprise.getIdLocal());
        docEntreprise.set(entreprise);
    }

    public static void creationAutreLocal(String nomAutreLocal, String quartierAutreLocal, String villeAutreLocal){
        AutreLocal autreLocal= new AutreLocal(nomAutreLocal, quartierAutreLocal, villeAutreLocal);

        DocumentReference docAutreLocal=
                FirebaseFirestore.getInstance()
                        .collection(PATH_LOCAL_DATABASES)
                        .document(autreLocal.getIdLocal());
        docAutreLocal.set(autreLocal);
    }
}
