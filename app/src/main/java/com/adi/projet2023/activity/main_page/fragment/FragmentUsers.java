package com.adi.projet2023.activity.main_page.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.adi.projet2023.R;
import com.adi.projet2023.activity.main_page.ChercherUser;
import com.adi.projet2023.adapter.AdapterUserModel;
import com.adi.projet2023.model.local.Local;
import com.adi.projet2023.model.user.UserModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

public class FragmentUsers extends Fragment {

    final String PATH_USERS_DATABASE= "Users";
    private CollectionReference collectionUsers;

    Local localEnCours;
    ArrayList<UserModel> lesUsers;
    RecyclerView listUsers;
    FloatingActionButton btnAddUser;
    ImageView imgQuitterFragmentUsers;

    public FragmentUsers(Local local) {
        // Required empty public constructor
        this.localEnCours= local;
        List<UserModel> users = localEnCours.getLesUsers();
        lesUsers= new ArrayList<>();
        if (users!=null){
            for (UserModel u: users){
                lesUsers.add(u);
            }
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_users, container, false);

        initListOfUsers(view);
        
        btnAddUser.setOnClickListener(
                v->{
                    verifierAdmin(FirebaseAuth.getInstance().getCurrentUser());
                }
        );

        imgQuitterFragmentUsers.setOnClickListener(
                view1 -> getActivity().finish()
        );
        return view;
    }

    private void initListOfUsers(View view){
        listUsers= view.findViewById(R.id.listUsers);
        btnAddUser= view.findViewById(R.id.addUser);
        imgQuitterFragmentUsers= view.findViewById(R.id.imgQuitterFragmentUsers);

        AdapterUserModel adapterUserModel= new AdapterUserModel(this.getContext(), lesUsers, localEnCours);
        listUsers.setLayoutManager(new LinearLayoutManager(this.getContext()));
        listUsers.setAdapter(adapterUserModel);
    }

    private void verifierAdmin(FirebaseUser firebaseUser){
        collectionUsers= FirebaseFirestore.getInstance().collection(PATH_USERS_DATABASE);

        DocumentReference docUsermodel=
                collectionUsers
                        .document(firebaseUser.getUid());

        docUsermodel.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.getBoolean("admin")){

                            //Conduire à l'interface d'ajout
                            Intent intent= new Intent(getContext(), ChercherUser.class);
                            intent.putExtra("localEnCours", localEnCours);
                            startActivity(intent);

                        }else{
                            Toast.makeText(getContext(), "Désolé vous n'êtes pas administrateur", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Aucune réponse", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}