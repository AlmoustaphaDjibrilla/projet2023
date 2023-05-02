package com.adi.projet2023.activity.main_page.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.adi.projet2023.R;
import com.adi.projet2023.activity.CommandesUserActivity;
import com.adi.projet2023.model.user.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FragmentProfil extends Fragment {

    private final String PATH_USER_DATABASE= "Users";

    FirebaseUser firebaseUser;
    UserModel userModel;

    TextView txtNomUser1, txtNomUser2, txtMailUser1, txtMailUser2, txtDateEnregistrementUser;
    ImageView imgProfilUser;

    Button historiquelUser;



    public FragmentProfil() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_profil, container, false);

        init(view);

        getUserModel(firebaseUser);
        UserModel modelUser= getUserModel();

        if (modelUser!=null) {
            updateComponants(modelUser);
        }

        historiquelUser.setOnClickListener(
                v->{
                    Intent intentHistorique= new Intent(getContext(), CommandesUserActivity.class);
                    startActivity(intentHistorique);
                }
        );

        return view;
    }


    /**
     * Initialisation des composants
     * graphiques de ce fragment
     */
    private void init(View view){
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        txtNomUser1= view.findViewById(R.id.txtNomUser1);
        txtNomUser2= view.findViewById(R.id.txtNomUser2);

        txtMailUser1= view.findViewById(R.id.txtMailUser1);
        txtMailUser2= view.findViewById(R.id.txtMailUser2);

        txtDateEnregistrementUser= view.findViewById(R.id.txtDateEnregistrementUser);

        imgProfilUser= view.findViewById(R.id.imgProfilUser);

        historiquelUser= view.findViewById(R.id.btnHistoriqueUser);
    }


    private void updateComponants(UserModel userModel){
        if (userModel !=null){
            if (userModel.getEmail()!=null && !userModel.equals("")){
                txtMailUser1.setText(userModel.getEmail());
                txtMailUser2.setText(userModel.getEmail());
            }
            if (userModel.getNom() !=null && !userModel.equals("")){
                txtNomUser1.setText(userModel.getNom());
                txtNomUser2.setText(userModel.getNom());
            }
            if (userModel.getDateEnregistrement() != null  && !userModel.getDateEnregistrement().equals("")){
                txtDateEnregistrementUser.setText(userModel.getDateEnregistrement());
            }
        }
    }


    private void getUserModel(FirebaseUser firebaseUser){
        String userId= firebaseUser.getUid();
        if (userId!=null && !userId.equals("")){
            DocumentReference documentReference=
                    FirebaseFirestore.getInstance()
                            .collection(PATH_USER_DATABASE)
                            .document(FirebaseAuth.getInstance().getCurrentUser().getUid());
            documentReference.get()
                    .addOnSuccessListener(
                            documentSnapshot->{
                                UserModel modelUser= documentSnapshot.toObject(UserModel.class);
                                setUserModel(modelUser);
                                updateComponants(userModel);
                            }
                    );
        }
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }
}