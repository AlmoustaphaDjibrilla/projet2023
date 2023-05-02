package com.adi.projet2023.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.adi.projet2023.activity.AjouterComposant;
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

    Dialog dialogSupprimerUser;
    ImageButton cancel;
    Button final_suppression;
    TextView alertMessage;

    AlertDialog.Builder dialogWarning;

    public AdapterUserModel(Context context, ArrayList<UserModel> lesUsers, Local localEnCours) {
        this.context = context;
        this.lesUsers = lesUsers;
        this.localEnCours= localEnCours;
    }

    @NonNull
    @Override
    public AffichageUserModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AffichageUserModel affichageUserModel= new AffichageUserModel(LayoutInflater.from(context).inflate(R.layout.modele_user, parent, false));

        dialogSupprimerUser= new Dialog(context);
        View alertDialogCustomiser = LayoutInflater.from(context).inflate(R.layout.dialog_alert_customiser,null);
        dialogWarning= new AlertDialog.Builder(context);
        dialogWarning.setView(alertDialogCustomiser);
        cancel = (ImageButton) alertDialogCustomiser.findViewById(R.id.cancel_button);
        alertMessage = (TextView) alertDialogCustomiser.findViewById(R.id.alert_message);
        final_suppression = (Button) alertDialogCustomiser.findViewById(R.id.final_delete_button);

        return affichageUserModel;
    }

    @Override
    public void onBindViewHolder(@NonNull AffichageUserModel holder, int position) {

        UserModel userCourant= lesUsers.get(position);
        holder.remplirChampsUserModel(userCourant);

        holder.cardUserModel.setOnClickListener(
                v->{
                    verifierAdminUser(userCourant, ACTION_AFFICHER_HISTORIQUE);
                }
        );

        holder.cardUserModel.setOnLongClickListener(
                view -> {
                    final AlertDialog dialog = dialogWarning.create();
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    alertMessage.setText("Voulez-vous retirer l'utilisateur "+userCourant.getNom()+" ?");

                    String userId = FirebaseAuth.getInstance()
                            .getCurrentUser()
                            .getUid();
                    DatabaseUtils.getUser(userId, UserModel.class, new DatabaseUtils.OnValueReceivedListener<UserModel>() {
                        @Override
                        public void onValueReceived(UserModel value) {
                            if(value.isAdmin()){
                                dialog.show();
                            }
                        }
                    });
                    final_suppression.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.cancel();
                            verifierAdminUser(userCourant, ACTION_RETIRER);
                        }
                    });

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.cancel();
                        }
                    });
                    return true;
                }
        );

    }

    @Override
    public int getItemCount() {
        return lesUsers.size();
    }

    private void verifierAdminUser(UserModel userModel, String actionAFaire){

        String userId = FirebaseAuth.getInstance()
            .getCurrentUser()
            .getUid();
        DatabaseUtils.getUser(userId, UserModel.class, new DatabaseUtils.OnValueReceivedListener<UserModel>() {
            @Override
            public void onValueReceived(UserModel value) {
                if(value.isAdmin()){
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
                else {
                    return;
                }
            }
        });

    }
}
