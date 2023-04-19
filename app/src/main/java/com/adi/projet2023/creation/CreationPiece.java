package com.adi.projet2023.creation;

import com.adi.projet2023.model.Piece.Autre;
import com.adi.projet2023.model.Piece.Chambre;
import com.adi.projet2023.model.Piece.Cuisine;
import com.adi.projet2023.model.Piece.Douche;
import com.adi.projet2023.model.Piece.Salon;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class CreationPiece {
    final static String PATH_LOCAL_DATABASES= "Piece";

    public static void creationChambre(String nom){
        Chambre chambre= new Chambre(nom);

        DocumentReference docMaison=
                FirebaseFirestore.getInstance()
                        .collection(PATH_LOCAL_DATABASES)
                        .document(chambre.getIdPiece());
        docMaison.set(chambre);
    }

    public static void creationSalon(String nom){
        Salon salon= new Salon(nom);

        DocumentReference docMaison=
                FirebaseFirestore.getInstance()
                        .collection(PATH_LOCAL_DATABASES)
                        .document(salon.getIdPiece());
        docMaison.set(salon);
    }

    public static void creationCuisine(String nom){
        Cuisine cuisine= new Cuisine(nom);

        DocumentReference docMaison=
                FirebaseFirestore.getInstance()
                        .collection(PATH_LOCAL_DATABASES)
                        .document(cuisine.getIdPiece());
        docMaison.set(cuisine);
    }
    public static void creationDouche(String nom){
        Douche douche= new Douche(nom);

        DocumentReference docMaison=
                FirebaseFirestore.getInstance()
                        .collection(PATH_LOCAL_DATABASES)
                        .document(douche.getIdPiece());
        docMaison.set(douche);
    }
    public static void creationAutrePiece(String nom){
        Autre autrePiece= new Autre(nom);

        DocumentReference docMaison=
                FirebaseFirestore.getInstance()
                        .collection(PATH_LOCAL_DATABASES)
                        .document(autrePiece.getIdPiece());
        docMaison.set(autrePiece);
    }
}
