package com.adi.projet2023.Utils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DatabaseUtils {

    public interface OnValueReceivedListener<T> {
        void onValueReceived(T value);
    }

    public static <T> void getValueFromFirebase(String path,String child,Class<T> valueType, final OnValueReceivedListener<T> listener) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(path);
        databaseReference.child(child).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                T value = dataSnapshot.getValue(valueType);
                listener.onValueReceived(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // La récupération de la valeur a été annulée
                Log.w("TAG", "Failed to read value.", databaseError.toException());
            }
        });
    }


    public static<T> void getUser(String userId,Class<T> valueType, final OnValueReceivedListener<T> listener){

        DocumentReference documentReference =
                FirebaseFirestore.getInstance()
                        .collection("Users")
                        .document(userId);

        documentReference
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        T value = documentSnapshot.toObject(valueType);
                        listener.onValueReceived(value);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "");
                    }
                });
    }
}
