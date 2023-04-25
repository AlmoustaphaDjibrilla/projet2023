package com.adi.projet2023.activity.main_page.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.adi.projet2023.R;
import com.adi.projet2023.activity.main_page.ChercherUser;
import com.adi.projet2023.adapter.AdapterUserModel;
import com.adi.projet2023.model.local.Local;
import com.adi.projet2023.model.user.UserModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

public class FragmentUsers extends Fragment {

    Local localEnCours;
    ArrayList<UserModel> lesUsers;
    RecyclerView listUsers;
    FloatingActionButton btnAddUser;
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
                    Intent intent= new Intent(getContext(), ChercherUser.class);
                    intent.putExtra("localEnCours", localEnCours);
                    startActivity(intent);
                }
        );

        return view;
    }

    private void initListOfUsers(View view){
        listUsers= view.findViewById(R.id.listUsers);
        btnAddUser= view.findViewById(R.id.addUser);

        AdapterUserModel adapterUserModel= new AdapterUserModel(this.getContext(), lesUsers);
        listUsers.setLayoutManager(new LinearLayoutManager(this.getContext()));
        listUsers.setAdapter(adapterUserModel);
    }
}