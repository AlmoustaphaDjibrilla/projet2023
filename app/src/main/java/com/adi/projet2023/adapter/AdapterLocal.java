package com.adi.projet2023.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adi.projet2023.R;
import com.adi.projet2023.activity.main_page.MainPage;
import com.adi.projet2023.creation.CreationLocal;
import com.adi.projet2023.model.local.Local;
import com.adi.projet2023.model.user.UserModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AdapterLocal extends RecyclerView.Adapter<AffichageLocal> {

    final String PATH_USERS_DATABASE= "Users";

    Context context;
    ArrayList<Local> lesLocaux;
    LayoutInflater inflater;

    Dialog dialogSupprimerLocal;

    TextView txtTypeLocal, txtNomLocal, txtQuartierLocal, txtVilleLocal, txtDateEnregistrementLocal;
    Button btnSupprimerLocal;

    public AdapterLocal(Context context, ArrayList<Local> lesLocaux) {
        this.context = context;
        this.lesLocaux = lesLocaux;
    }

    @NonNull
    @Override
    public AffichageLocal onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AffichageLocal affichageLocal= new AffichageLocal(LayoutInflater.from(context).inflate(R.layout.modele_local, parent, false));

        dialogSupprimerLocal= new Dialog(context);
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


//                        Toast.makeText(context, local.getHashMapPieces().get(0).toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        holder.layoutLocal.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                remplirChamps(local);

                dialogSupprimerLocal.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogSupprimerLocal.setCancelable(true);
                dialogSupprimerLocal.show();

                btnSupprimerLocal.setOnClickListener(
                        v->{
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

                                                        //Suppression du Local en cours
                                                        CreationLocal.supprimerLocal(local);
                                                        lesLocaux.remove(local);
                                                        notifyDataSetChanged();
                                                        Toast.makeText(context, local.getNomLocal()+" supprimé..", Toast.LENGTH_SHORT).show();
                                                    }
                                                    else{
                                                        Toast.makeText(context, "Vous n'êtes pas admin", Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(context, "Problème rencontré!!!", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                            dialogSupprimerLocal.dismiss();
                        }
                );

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
}
