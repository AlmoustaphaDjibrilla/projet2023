package com.adi.projet2023.activity.main_page;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.adi.projet2023.R;
import com.adi.projet2023.adapter.AdapterDisplayAllUsers;
import com.adi.projet2023.adapter.AdapterUserModel;
import com.adi.projet2023.model.user.UserModel;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class AllUsersActivity extends AppCompatActivity {

    final String PATH_USER_DATABASE= "Users";
    CollectionReference collectionUsers;


    RecyclerView listAllUsers;
    ImageView imgQuitter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);

        init();

        imgQuitter.setOnClickListener(
                view -> finish()
        );

        collectionUsers
                .get()
                .addOnSuccessListener(
                        v->{
                            var lesUsers= new ArrayList<UserModel>();
                            for (QueryDocumentSnapshot documentSnapshot: v){
                                var user= documentSnapshot.toObject(UserModel.class);
                                lesUsers.add(user);
                            }

                            var adapterUserModel= new AdapterDisplayAllUsers(this, lesUsers);
                            listAllUsers.setAdapter(adapterUserModel);
                        });
    }

    private void init(){
        listAllUsers= findViewById(R.id.listAllUsers);
        listAllUsers.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
        listAllUsers.setClickable(false);
        imgQuitter= findViewById(R.id.imgQuitterAllUsers);

        collectionUsers= FirebaseFirestore.getInstance().collection(PATH_USER_DATABASE);
    }
}