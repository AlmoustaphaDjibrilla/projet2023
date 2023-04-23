package com.adi.projet2023.creation;

import com.adi.projet2023.model.Piece.Piece;
import com.adi.projet2023.model.local.Local;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class CreationPiece {
    final static String PATH_PIECE_DATABASES = "Piece";
    final static String PATH_LOCAL_DATABASES= "Local";


    public static void creationPiece(Piece nouvellePiece){
        DocumentReference docMaison=
                FirebaseFirestore.getInstance()
                        .collection(PATH_PIECE_DATABASES)
                        .document(nouvellePiece.getIdPiece());
        docMaison.set(nouvellePiece);
    }

}
