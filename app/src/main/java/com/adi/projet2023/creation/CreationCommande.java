package com.adi.projet2023.creation;

import com.adi.projet2023.model.Commande.Commande;
import com.adi.projet2023.model.user.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class CreationCommande {
    final static String PATH_COMMANDES_DATABASE= "Commandes";
    final static String PATH_USER_DATABASE= "Users";


    public static void creationCommande(Commande commande){

    }

    public static void enregistrerNouvelleCommande( Commande commande){

        DocumentReference docRef= FirebaseFirestore.getInstance()
                .collection(PATH_USER_DATABASE)
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        docRef.get()
                .addOnSuccessListener(
                        documentSnapshot -> {
                            UserModel userModel= documentSnapshot.toObject(UserModel.class);
                            commande.setUserModel(userModel);
                            commande.setEmailUser(userModel.getEmail());

                            DocumentReference dfReferenceSaveCommande=
                                    FirebaseFirestore.getInstance()
                                            .collection(PATH_COMMANDES_DATABASE)
                                            .document();
                            dfReferenceSaveCommande.set(commande);
                        }
                );
    }
}
