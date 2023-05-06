package com.adi.projet2023.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adi.projet2023.R;
import com.adi.projet2023.Utils.DatabaseUtils;
import com.adi.projet2023.Utils.LocalUtils;
import com.adi.projet2023.activity.AjouterComposant;
import com.adi.projet2023.activity.ChoixLocalActivity;
import com.adi.projet2023.activity.main_page.AffichageCommandeUser;
import com.adi.projet2023.activity.main_page.MainPage;
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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterUserModel extends RecyclerView.Adapter<AffichageUserModel>{
    Local localEnCours;

    Context context;
    ArrayList<UserModel> lesUsers;
    LayoutInflater inflater;

    Dialog dialogSupprimerUser;

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
                    String userId = FirebaseAuth.getInstance()
                            .getCurrentUser()
                            .getUid();
                    DatabaseUtils.getUser(userId, UserModel.class, new DatabaseUtils.OnValueReceivedListener<UserModel>() {
                        @Override
                        public void onValueReceived(UserModel value) {
                            if(value.isAdmin()){
                                Intent intent= new Intent(context.getApplicationContext(), AffichageCommandeUser.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("userEnCours",userCourant);
                                context.startActivity(intent);
                            }
                        }
                    });
                });

        holder.cardUserModel.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                String userId = FirebaseAuth.getInstance()
                        .getCurrentUser()
                        .getUid();
                DatabaseUtils.getUser(userId, UserModel.class, new DatabaseUtils.OnValueReceivedListener<UserModel>() {
                    @Override
                    public void onValueReceived(UserModel value) {
                        if(value.isAdmin()){
                            builder
                                    .setCancelable(true)
                                    .setTitle("Retrait d'un utilisateur")
                                    .setMessage("Voulez-vous retirer l'utilisateur "+userCourant.getNom()+" ?")
                                    .setPositiveButton("Retirer", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            retirerUserLocal(userCourant);
                                            retirerUserLocal(userCourant);
                                            lesUsers.remove(userCourant);
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
                        }
                    }
                });
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return lesUsers.size();
    }

    private void retirerUserLocal(UserModel userARetirer){

        Map<String, Object> user= new HashMap<>();
        user.put("uid", userARetirer.getUid());
        user.put("password", userARetirer.getPassword());
        user.put("email", userARetirer.getEmail());
        user.put("nom", userARetirer.getNom());
        user.put("dateEnregistrement", userARetirer.getDateEnregistrement());
        user.put("admin", userARetirer.isAdmin());
        user.put("user", userARetirer.isUser());

        CollectionReference localRef = FirebaseFirestore.getInstance().collection("Local");
        Query req = localRef.whereEqualTo("idLocal", localEnCours.getIdLocal());
        req.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                DocumentSnapshot localDoc = querySnapshot.getDocuments().get(0);
                List<Map<String, Object>> lesUsers = (List<Map<String, Object>>) localDoc.get("lesUsers");

                boolean trouve = false;
                for (Map<String, Object> user : lesUsers) {
                    if (user.get("uid").equals(userARetirer.getUid())) {
                        lesUsers.remove(user);
                        trouve = true;
                        break;
                    }
                }
                if(trouve){
                    localDoc.getReference().update("lesUsers",lesUsers)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(context, userARetirer.getNom()+" retiré du local "+localEnCours.getNomLocal(), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(context, ChoixLocalActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    context.startActivity(intent);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context," Echec de la suppression ", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG",e.getMessage());
            }
        });
    }
}
