package com.adi.projet2023.creation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreationComposant {
    public static void creationComposant(String idLocal, String nomPiece, String nom, String type){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

// Créer un objet Map pour représenter les données du nouveau composant
        Map<String, Object> nouveauComposant = new HashMap<>();
        nouveauComposant.put("nom", nom);
        nouveauComposant.put("type", type);

        Query query = db.collection("Local").whereEqualTo("idlocal", idLocal);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // Récupérer la liste de pièces dans le champ "lesPieces"
                        List<Map<String, Object>> lesPieces = (List<Map<String, Object>>) document.get("lesPieces");

                        // Parcourir la liste de pièces pour trouver celle à laquelle ajouter le composant
                        for (Map<String, Object> piece : lesPieces) {
                            if (piece.get("nom").equals(nomPiece)) {
                                // Récupérer la liste de composants dans la pièce
                                List<Map<String, Object>> lesComposants = (List<Map<String, Object>>) piece.get("composants");

                                // Ajouter le nouveau composant à la liste de composants de la pièce
                                lesComposants.add(nouveauComposant);

                                // Mettre à jour la liste de composants dans le champ "composant" de la pièce
                                document.getReference().update("lesPieces", lesPieces)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    // Le nouveau composant a été ajouté avec succès à la pièce
                                                } else {
                                                    // Une erreur s'est produite lors de la mise à jour du document
                                                }
                                            }
                                        });
                                break;
                            }
                        }
                    }
                } else {
                    // Une erreur s'est produite lors de la récupération des documents
                }
            }
        });
    }
}
