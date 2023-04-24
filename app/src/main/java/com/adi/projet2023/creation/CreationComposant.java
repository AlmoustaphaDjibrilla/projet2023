package com.adi.projet2023.creation;

import com.adi.projet2023.model.composant.Composant;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class CreationComposant {

    final static String PATH_COMPOSANT_DATABASES= "Composant";

    public static void creationComposant(Composant composant){
        DocumentReference docComposant=
                FirebaseFirestore.getInstance()
                        .collection(PATH_COMPOSANT_DATABASES)
                        .document(composant.getIdComposant());

        docComposant.set(composant);

        DatabaseReference dbRef=
                FirebaseDatabase.getInstance()
                .getReference("Composant")
                .child(composant.getAdresseLocalEnCours())
                .child(composant.getAdressePieceEnCours())
                .child(composant.getChemin());
//
        dbRef.push().setValue("ON");
    }

}
