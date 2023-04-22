package com.adi.projet2023.creation;

import com.adi.projet2023.model.Piece.Piece;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;

public class CreationPiece {
    final static String PATH_LOCAL_DATABASES= "Piece";

    public static void creationPiece(String nom,String type, List<HashMap<String, Object>> listHashMap){
        Piece piece= new Piece(type, nom,listHashMap);

        DocumentReference docMaison=
                FirebaseFirestore.getInstance()
                        .collection(PATH_LOCAL_DATABASES)
                        .document(piece.getIdPiece());
        docMaison.set(piece);
    }

}
