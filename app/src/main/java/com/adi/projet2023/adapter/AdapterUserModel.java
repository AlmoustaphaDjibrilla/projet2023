package com.adi.projet2023.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adi.projet2023.R;
import com.adi.projet2023.activity.main_page.AffichageCommandeUser;
import com.adi.projet2023.creation.CreationLocal;
import com.adi.projet2023.model.local.Local;
import com.adi.projet2023.model.user.UserModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AdapterUserModel extends RecyclerView.Adapter<AffichageUserModel>{
    final String PATH_USERS_DATABASE= "Users";

    final String ACTION_RETIRER= "retirer";
    final String ACTION_AFFICHER_HISTORIQUE= "historique";
    Local localEnCours;

    Context context;
    ArrayList<UserModel> lesUsers;
    LayoutInflater inflater;

    AlertDialog.Builder builder;

    public AdapterUserModel(Context context, ArrayList<UserModel> lesUsers, Local localEnCours) {
        this.context = context;
        this.lesUsers = lesUsers;
        this.localEnCours= localEnCours;
        builder= new AlertDialog.Builder(context);
    }

    @NonNull
    @Override
    public AffichageUserModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AffichageUserModel affichageUserModel= new AffichageUserModel(LayoutInflater.from(context).inflate(R.layout.modele_user, parent, false));

        return affichageUserModel;
    }

    @Override
    public void onBindViewHolder(@NonNull AffichageUserModel holder, int position) {

        UserModel userCourant= lesUsers.get(position);
        holder.remplirChampsUserModel(userCourant);

        holder.cardUserModel.setOnClickListener(
                v->{
                    verifierAdminUser(FirebaseAuth.getInstance().getCurrentUser(), userCourant, ACTION_AFFICHER_HISTORIQUE);
                }
        );

        holder.cardUserModel.setOnLongClickListener(
                view -> {

                    builder
                            .setCancelable(true)
                            .setTitle("Retrait d'un utilisateur")
                            .setMessage("Voulez-vous retirer l'utilisateur "+userCourant.getNom()+" ?")
                            .setPositiveButton("Retirer", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    verifierAdminUser(FirebaseAuth.getInstance().getCurrentUser(), userCourant, ACTION_RETIRER);
                                }
                            })
                            .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            })
                            .setIcon(R.drawable.icon_warning)
                            .show();

                    return true;
                }
        );

    }

    @Override
    public int getItemCount() {
        return lesUsers.size();
    }

    private void verifierAdminUser(FirebaseUser firebaseUser, UserModel userModel, String actionAFaire){
        CollectionReference collectionUsers=
                FirebaseFirestore.getInstance()
                        .collection(PATH_USERS_DATABASE);

        DocumentReference docUsermodel=
                collectionUsers
                        .document(firebaseUser.getUid());

        docUsermodel.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.getBoolean("admin")){

                            switch (actionAFaire){
                                case "historique":
                                    Intent intent= new Intent(context.getApplicationContext(), AffichageCommandeUser.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtra("userEnCours",userModel);
                                    context.startActivity(intent);
                                    break;

                                case "retirer":
                                    CreationLocal.retirerUserLocal(userModel, localEnCours);
                                    Toast.makeText(context, userModel.getNom()+" retiré du local "+localEnCours.getNomLocal(), Toast.LENGTH_SHORT).show();
                                    lesUsers.remove(userModel);
                                    notifyDataSetChanged();
                                    break;

                                default:
                                    break;
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context.getApplicationContext(), "Aucune réponse", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
