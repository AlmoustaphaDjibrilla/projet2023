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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adi.projet2023.R;
import com.adi.projet2023.Utils.LocalUtils;
import com.adi.projet2023.activity.main_page.MainPage;
import com.adi.projet2023.creation.CreationLocal;
import com.adi.projet2023.model.Piece.Piece;
import com.adi.projet2023.model.local.Local;
import com.adi.projet2023.model.user.UserModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AdapterLocal extends RecyclerView.Adapter<AffichageLocal> {

    final String PATH_USERS_DATABASE= "Users";

    Context context;
    ArrayList<Local> lesLocaux;
    LayoutInflater inflater;

    Dialog dialogSupprimerLocal;

    TextView txtTypeLocal, txtNomLocal, txtQuartierLocal, txtVilleLocal, txtDateEnregistrementLocal;
    Button btnSupprimerLocal;

    AlertDialog.Builder dialogWarning;

    public AdapterLocal(Context context, ArrayList<Local> lesLocaux) {
        this.context = context;
        this.lesLocaux = lesLocaux;
    }

    @NonNull
    @Override
    public AffichageLocal onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AffichageLocal affichageLocal= new AffichageLocal(LayoutInflater.from(context).inflate(R.layout.modele_local, parent, false));

        dialogSupprimerLocal= new Dialog(context);
        dialogWarning= new AlertDialog.Builder(context);

        dialogSupprimerLocal.setContentView(R.layout.layout_supprimer_local);

        initComponentsOfDialog();


        return affichageLocal;
    }

    @Override
    public void onBindViewHolder(@NonNull AffichageLocal holder, int position) {
        Local local= lesLocaux.get(position);
        holder.remplirChamps(local);

        holder.layoutLocal.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context.getApplicationContext(), MainPage.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("localId",local);
                        context.startActivity(intent);
                    }
                });

        holder.layoutLocal.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                String userId= FirebaseAuth.getInstance()
                        .getCurrentUser()
                        .getUid();

                DocumentReference documentReference=
                        FirebaseFirestore.getInstance()
                                .collection(PATH_USERS_DATABASE)
                                .document(userId);

                documentReference
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                UserModel userModel= documentSnapshot.toObject(UserModel.class);

                                //Vérifier si le user courant est un admin
                                if (userModel.isAdmin()){
                                    remplirChamps(local);

                                    dialogSupprimerLocal.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    dialogSupprimerLocal.setCancelable(true);
                                    dialogSupprimerLocal.show();

                                    btnSupprimerLocal.setOnClickListener(
                                            v->{

                                                List<Piece> lesPieces = local.getLesPieces();
                                                int nbrPiece=0;
                                                if (lesPieces!=null)
                                                    nbrPiece= lesPieces.size();

                                                dialogSupprimerLocal.dismiss();
                                                dialogWarning.setTitle("Attention")
                                                        .setMessage("Ce local contient "+nbrPiece+" pièce(s)\nVoulez-vous vraiment le supprimer?")
                                                        .setCancelable(true)
                                                        .setPositiveButton("Supprimer", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                dialogInterface.dismiss();
                                                                supprimerLocal(local);
                                                            }
                                                        })
                                                        .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                dialogInterface.dismiss();
                                                                dialogSupprimerLocal.dismiss();
                                                            }
                                                        })
                                                        .setIcon(R.drawable.icon_warning)
                                                        .show();
                                            });
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "Problème rencontré!!!", Toast.LENGTH_SHORT).show();
                            }
                        });
                return true;
            }
        });
    }

    private void initComponentsOfDialog(){
        txtTypeLocal= dialogSupprimerLocal.findViewById(R.id.txtTypeLocalSupprimerLocal);
        txtNomLocal= dialogSupprimerLocal.findViewById(R.id.txtNomLocalSupprimerLocal);
        txtQuartierLocal= dialogSupprimerLocal.findViewById(R.id.txtQuartierLocalSupprimerLocal);
        txtVilleLocal= dialogSupprimerLocal.findViewById(R.id.txtVilleLocalSupprimerLocal);
        txtDateEnregistrementLocal= dialogSupprimerLocal.findViewById(R.id.txtDateEnregistrementSupprimerLocal);

        btnSupprimerLocal= dialogSupprimerLocal.findViewById(R.id.btnSupprimerLocal);
    }

    private void remplirChamps(Local local){
        if (local!=null){

            String typeLocal= local.getDesignationLocal();
            String nomLocal= local.getNomLocal();
            String quartierLocal= local.getQuartierLocal();
            String villeLocal= local.getVilleLocal();
            String dateEnregistrementLocal= local.getDateEnregistrement();

            txtTypeLocal.setText(typeLocal);
            txtNomLocal.setText(nomLocal);
            txtQuartierLocal.setText(quartierLocal);
            txtVilleLocal.setText(villeLocal);
            txtDateEnregistrementLocal.setText(dateEnregistrementLocal);
        }
    }

    @Override
    public int getItemCount() {
        return lesLocaux.size();
    }

    private void supprimerLocal(Local local){
        //Suppression du Local en cours
        CreationLocal.supprimerLocal(local);
        lesLocaux.remove(local);
        LocalUtils.supprimer_local_realTime(local);
        LocalUtils.supprimer_historique_local(local);
        notifyDataSetChanged();
        Toast.makeText(context, local.getNomLocal()+" supprimé..", Toast.LENGTH_SHORT).show();
    }
}