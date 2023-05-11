package com.adi.projet2023.activity.main_page.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adi.projet2023.R;
import com.adi.projet2023.Utils.DatabaseUtils;
import com.adi.projet2023.activity.ActivityRegisterUser;
import com.adi.projet2023.activity.CommandesParLocalActivity;
import com.adi.projet2023.activity.main_page.AllUsersActivity;
import com.adi.projet2023.activity.main_page.ChercherUser;
import com.adi.projet2023.adapter.AdapterUserModel;
import com.adi.projet2023.model.local.Local;
import com.adi.projet2023.model.user.UserModel;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class FragmentUsers extends Fragment {

    Local localEnCours;
    ArrayList<UserModel> lesUsers;
    RecyclerView listUsers;
    FloatingActionButton btnAddUser, btnSearchUser, btnHistoriquesUsers, btnAllUsers;
    ExtendedFloatingActionButton actions;
    Boolean visibles;
    ImageView imgQuitterFragmentUsers;
    View view;

    public FragmentUsers(){

    }
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
    public void onResume() {
        super.onResume();
        initActionsButton(view);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_users, container, false);

        initListOfUsers(view);
        initActionsButton(view);

        imgQuitterFragmentUsers.setOnClickListener(
                v -> this.getActivity().finish()
        );
        return view;
    }

    private void initListOfUsers(View view){
        listUsers= view.findViewById(R.id.listUsers);
        AdapterUserModel adapterUserModel= new AdapterUserModel(this.getContext(), lesUsers, localEnCours);
        listUsers.setLayoutManager(new LinearLayoutManager(this.getContext()));
        listUsers.setAdapter(adapterUserModel);

        imgQuitterFragmentUsers= view.findViewById(R.id.imgQuitterFragmentUsers);
    }

    private void initActionsButton(View view){

        actions = view.findViewById(R.id.actions_supp);
        btnAddUser = view.findViewById(R.id.add_user_fab);
        btnSearchUser= view.findViewById(R.id.search_user_fab);
        btnHistoriquesUsers = view.findViewById(R.id.historique_fab);
        btnAllUsers= view.findViewById(R.id.allUsers_fab);

        btnAddUser.setVisibility(View.GONE);
        btnSearchUser.setVisibility(View.GONE);
        btnHistoriquesUsers.setVisibility(View.GONE);
        btnAllUsers.setVisibility(View.GONE);

        visibles=false;

        String userId = FirebaseAuth.getInstance()
                .getCurrentUser()
                .getUid();
        DatabaseUtils.getUser(userId, UserModel.class, new DatabaseUtils.OnValueReceivedListener<UserModel>() {
            @Override
            public void onValueReceived(UserModel value) {
                if(value.isAdmin()){
                    actions.show();
                    actions.shrink();
                }
            }
        });

        actions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!visibles){
                    view.setClickable(false);
                    btnAddUser.show();
                    btnSearchUser.show();
                    btnHistoriquesUsers.show();
                    btnAllUsers.show();
                    actions.extend();

                    visibles=true;
                }else{
                    view.setClickable(true);
                    btnAddUser.hide();
                    btnSearchUser.hide();
                    btnHistoriquesUsers.hide();
                    btnAllUsers.hide();
                    actions.shrink();

                    visibles=false;
                }
            }
        });

        btnSearchUser.setOnClickListener(
                v->{
                    Intent intent= new Intent(getContext(), ChercherUser.class);
                    intent.putExtra("localEnCours", localEnCours);
                    startActivity(intent);
                    btnAddUser.setVisibility(View.GONE);
                    btnSearchUser.setVisibility(View.GONE);
                    btnHistoriquesUsers.setVisibility(View.GONE);
                    btnAddUser.setVisibility(View.GONE);
                    actions.shrink();
                });
        btnAddUser.setOnClickListener(
                v->{
                    Intent intent= new Intent(getContext(), ActivityRegisterUser.class);
                    startActivity(intent);
                    btnAddUser.setVisibility(View.GONE);
                    btnSearchUser.setVisibility(View.GONE);
                    btnHistoriquesUsers.setVisibility(View.GONE);
                    btnAddUser.setVisibility(View.GONE);
                    actions.shrink();
                });
        btnHistoriquesUsers.setOnClickListener(
                v->{
                    Intent intent= new Intent(getContext(), CommandesParLocalActivity.class);
                    intent.putExtra("IdLocal", localEnCours.getIdLocal());
                    startActivity(intent);
                    btnAddUser.setVisibility(View.GONE);
                    btnSearchUser.setVisibility(View.GONE);
                    btnHistoriquesUsers.setVisibility(View.GONE);
                    btnAddUser.setVisibility(View.GONE);
                    actions.shrink();
                });
        btnAllUsers.setOnClickListener(
                view1 -> {
                    Intent intentAllUsers= new Intent(getContext(), AllUsersActivity.class);
                    startActivity(intentAllUsers);
                    btnAddUser.setVisibility(View.GONE);
                    btnSearchUser.setVisibility(View.GONE);
                    btnHistoriquesUsers.setVisibility(View.GONE);
                    btnAddUser.setVisibility(View.GONE);
                    actions.shrink();
                }
        );

    }
}