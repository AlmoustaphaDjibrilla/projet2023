package com.adi.projet2023.activity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adi.projet2023.R;
import com.adi.projet2023.Utils.DatabaseUtils;
import com.adi.projet2023.Utils.VerificationNom;
import com.adi.projet2023.adapter.AdapterLocal;
import com.adi.projet2023.creation.CreationLocal;
import com.adi.projet2023.databinding.ActivityChoixLocalBinding;
import com.adi.projet2023.model.Piece.Piece;
import com.adi.projet2023.model.local.AutreLocal;
import com.adi.projet2023.model.local.Entreprise;
import com.adi.projet2023.model.local.Local;
import com.adi.projet2023.model.local.Maison;
import com.adi.projet2023.model.local.TypeLocal;
import com.adi.projet2023.model.user.UserModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChoixLocalActivity extends AppCompatActivity {

    final String PATH_LOCAL_DATABASES= "Local";
    final String PATH_USERS_DATABASE= "Users";

    private CollectionReference collectionLocal;
    private CollectionReference collectionUsers;

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;

    private ActivityChoixLocalBinding binding;
    RecyclerView listLocaux;
    ArrayList<Local> lesLocaux;
    Dialog dialog;
    TextView titreChoixLocal;

    /**
     * Les composants du dialog qui va s'afficher
     * pour ajouter un nouveau local
     */
    RadioButton rdMaison, rdEntreprise, rdAutre;
    EditText txtNomLocal, txtQuartierLocal, txtVilleLocal;
    Button btnAjouterLocal;

    @Override
    protected void onResume() {
        super.onResume();
        lesLocaux.clear();

        //Actualiser en cas de retour pour que les modifications s'affichent
        afficherLocalEnFonctionUser(FirebaseAuth.getInstance().getCurrentUser());
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_local);
        dialog= new Dialog(this);


        binding = ActivityChoixLocalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initFirebaseComponents();
        init();

        if (lesLocaux.size()==0)
            titreChoixLocal.setText("Aucun local...");
        else{
            titreChoixLocal.setText("Choisissez un local");
        }

        String userId = FirebaseAuth.getInstance()
                .getCurrentUser()
                .getUid();
        DatabaseUtils.getUser(userId, UserModel.class, new DatabaseUtils.OnValueReceivedListener<UserModel>() {
            @Override
            public void onValueReceived(UserModel value) {
                if(value.isAdmin()){
                    binding.addlocal.show();
                }
            }
        });
        binding.addlocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ajoutLocal();
                verifierAdmin();
                btnAjouterLocal.setOnClickListener(
                        v->{
                            TypeLocal typeLocal;
                            String nomLocal= txtNomLocal.getText().toString();
                            String quartierLocal= txtQuartierLocal.getText().toString();
                            String villeLocal= txtVilleLocal.getText().toString();

                            if (rdMaison.isChecked())
                                typeLocal= TypeLocal.MAISON;
                            else if (rdEntreprise.isChecked())
                                typeLocal= TypeLocal.ENTREPRISE;
                            else
                                typeLocal= TypeLocal.AUTRE;

                            nouveauLocal(typeLocal, nomLocal, quartierLocal, villeLocal);
                        }
                );
            }
        });

    }

    /**
     * Initialisation des composants
     */
    private void init(){
        listLocaux= findViewById(R.id.listChoixLocal);
        lesLocaux= new ArrayList<>();
        titreChoixLocal=findViewById(R.id.titreChoixLocal);
    }

    private void verifierAdmin(){
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

    }

    private void recuperer_locaux(){
        collectionLocal.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){

                            //Récuperer manuellement tous les attributs du local
                            String designationLocal= documentSnapshot.getString("designationLocal");
                            String adresseLocal= documentSnapshot.getString("adresseLocal");
                            String idLocal= documentSnapshot.getString("idLocal");

                            //Ne pas toucher!!
                            List<HashMap<String, Object>> list= (List<HashMap<String, Object>>) documentSnapshot.get("lesPieces");
                            List<Piece> lesPieces = new ArrayList<>();
                            if(list != null){
                                for (HashMap<String, Object> hashMap : list) {
                                    String id=(String) hashMap.get("idPiece");
                                    String nom = (String) hashMap.get("nom");
                                    String type = (String) hashMap.get("typePiece");
                                    String adresse = (String) hashMap.get("chemin");
                                    List<HashMap<String, Object>> listHashMap = (List<HashMap<String, Object>>) hashMap.get("composants");
                                    lesPieces.add(new Piece(id,type,nom,adresse,listHashMap));
                                }
                            }

                            List<HashMap<String, Object>> users= (List<HashMap<String, Object>>) documentSnapshot.get("lesUsers");
                            List<UserModel> lesUsers= new ArrayList<>();
                            if (users != null){
                                for (HashMap<String, Object> hashMap: users){
                                    boolean admin= (boolean) hashMap.get("admin");
                                    String dateEnregistrement= (String) hashMap.get("dateEnregistrement");
                                    String email= (String) hashMap.get("email");
                                    String nom= (String) hashMap.get("nom");
                                    String password= (String) hashMap.get("password");
                                    String uid= (String) hashMap.get("uid");
                                    boolean user= (boolean) hashMap.get("user");

                                    UserModel userTrouve= new UserModel();
                                    userTrouve.setAdmin(admin);
                                    userTrouve.setDateEnregistrement(dateEnregistrement);
                                    userTrouve.setEmail(email);
                                    userTrouve.setNom(nom);
                                    userTrouve.setPassword(password);
                                    userTrouve.setUid(uid);
                                    userTrouve.setUser(user);

                                    lesUsers.add(userTrouve);
                                }
                            }

                            String nomLocal= documentSnapshot.getString("nomLocal");
                            String quartierLocal= documentSnapshot.getString("quartierLocal");
                            TypeLocal typeLocal= TypeLocal.valueOf(documentSnapshot.getString("typeLocal"));
                            String villeLocal= documentSnapshot.getString("villeLocal");
                            String dateEnregistrementLocal = documentSnapshot.getString("dateEnregistrement");

                            //creer un nouvel local et lui affecter les attributs recuperés ci-dessus
                            Local local= new Local();
                            local.setDesignationLocal(designationLocal);
                            local.setAdresseLocal(adresseLocal);
                            local.setIdLocal(idLocal);
                            local.setLesPieces(lesPieces);
                            local.setLesUsers(lesUsers);
                            local.setNomLocal(nomLocal);
                            local.setQuartierLocal(quartierLocal);
                            local.setTypeLocal(typeLocal);
                            local.setVilleLocal(villeLocal);
                            local.setDateEnregistrement(dateEnregistrementLocal);

                            //Ajouter le local créé à la liste des locaux
                            lesLocaux.add(local);
                        }
                        //afficher la liste des locaux dans la liste
                        updateListViewOfLocals();
                    }
                });

    }

    private void initFirebaseComponents(){
        mAuth= FirebaseAuth.getInstance();
        firebaseFirestore= FirebaseFirestore.getInstance();
        collectionLocal= firebaseFirestore.collection(PATH_LOCAL_DATABASES);
        collectionUsers= firebaseFirestore.collection(PATH_USERS_DATABASE);
    }

    public void updateListViewOfLocals(){
        AdapterLocal adapterLocal= new AdapterLocal(this, lesLocaux);
        listLocaux.setLayoutManager(new LinearLayoutManager(this));
        listLocaux.setAdapter(adapterLocal);

        if (lesLocaux.size()==0)
            titreChoixLocal.setText("Aucun local...");
        else{
            titreChoixLocal.setText("Choisissez un local");
        }
    }
    private void initComponentsOfDialog(){
        rdMaison= dialog.findViewById(R.id.rdBtnMaison);
        rdMaison.setChecked(true);
        rdEntreprise= dialog.findViewById(R.id.rdBtnEntreprise);
        rdAutre= dialog.findViewById(R.id.rdBtnAutre);

        txtNomLocal= dialog.findViewById(R.id.txtNomAjoutLocal);
        txtQuartierLocal= dialog.findViewById(R.id.txtQuartierAjoutLocal);
        txtVilleLocal= dialog.findViewById(R.id.txtVilleAjoutLocal);

        btnAjouterLocal=dialog.findViewById(R.id.btnAjoutLocal);
    }

    /**
     * Affichage du dialog
     * pour ajouter un nouveau local
     */
    void ajoutLocal(){
        dialog.setContentView(R.layout.layout_ajout_local);

        initComponentsOfDialog();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private void nouveauLocal(TypeLocal typeLocal, String nomLocal, String quartierLocal, String villeLocal){
        if(nomLocal==null || nomLocal.equals("")){
            txtNomLocal.setError("Saisissez le nom du local");
            return;
        }
        if(quartierLocal==null || quartierLocal.equals("")){
            txtQuartierLocal.setError("Saisissez le quartier du local");
            return;
        }
        if(villeLocal==null || villeLocal.equals("")){
            txtVilleLocal.setError("Saisissez la ville du local");
            return;
        }

        switch (typeLocal){
            case MAISON:
                VerificationNom.verifier_nom_local(nomLocal, new OnSuccessListener<Boolean>() {
                    @Override
                    public void onSuccess(Boolean exist) {
                        if(exist){
                            Maison maison= new Maison(nomLocal, quartierLocal, villeLocal);
                            CreationLocal.creationMaison(maison);
                            ajouter_local_a_realTime("/"+maison.getIdLocal());
                            lesLocaux.add(maison);
                            updateListViewOfLocals();
                            Toast.makeText(getApplicationContext(), nomLocal+" added successfully", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Un Local existe deja avec le nom saisi !", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                }, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Erreur survenue: Merci de reessayer " , Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                break;

            case ENTREPRISE:
                VerificationNom.verifier_nom_local(nomLocal, new OnSuccessListener<Boolean>() {
                    @Override
                    public void onSuccess(Boolean exist) {
                        if(exist){
                            Entreprise entreprise= new Entreprise(nomLocal, quartierLocal, villeLocal);
                            CreationLocal.creationEntreprise(entreprise);
                            ajouter_local_a_realTime("/"+entreprise.getIdLocal());
                            lesLocaux.add(entreprise);
                            updateListViewOfLocals();
                            Toast.makeText(getApplicationContext(), nomLocal+" added successfully", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Un Local existe deja avec le nom saisi !", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                }, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Erreur survenue: Merci de reessayer " , Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                break;

            case AUTRE:
                VerificationNom.verifier_nom_local(nomLocal, new OnSuccessListener<Boolean>() {
                    @Override
                    public void onSuccess(Boolean exist) {
                        if(exist){
                            AutreLocal autreLocal= new AutreLocal(nomLocal, quartierLocal, villeLocal);
                            CreationLocal.creationAutreLocal(autreLocal);
                            ajouter_local_a_realTime("/"+autreLocal.getIdLocal());
                            lesLocaux.add(autreLocal);
                            updateListViewOfLocals();
                            Toast.makeText(getApplicationContext(), nomLocal+" added successfully", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Un Local existe deja avec le nom saisi !", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                }, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Erreur survenue: Merci de reessayer " , Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                break;
            default:
                Toast.makeText(getApplicationContext(), "Error...", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                break;
        }
    }

    private void ajouter_local_a_realTime(String chemin){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(chemin);
        Map<String, Object> composant_valeur = new HashMap<>();
        composant_valeur.put("Temperature", 0);
        composant_valeur.put("Humidite", 0);
        composant_valeur.put("Presence", 0);
        ref.updateChildren(composant_valeur);
    }

    private void afficherLocalEnFonctionUser(FirebaseUser firebaseUser){
        DocumentReference docUsermodel=
                collectionUsers
                        .document(firebaseUser.getUid());

        docUsermodel.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.getBoolean("admin")){
                            //Pour un admin, afficher tous les locaux enregistrés

                            recuperer_locaux();

                        }else{
                            //Pour un user normal (non Admin), afficher les locaux auxquels il est affecté


                            collectionLocal.get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){

                                                //Récuperer manuellement tous les attributs du local
                                                String designationLocal= documentSnapshot.getString("designationLocal");
                                                String adresseLocal= documentSnapshot.getString("adresseLocal");
                                                String idLocal= documentSnapshot.getString("idLocal");

                                                //Ne pas toucher!!
                                                List<HashMap<String, Object>> list= (List<HashMap<String, Object>>) documentSnapshot.get("lesPieces");
                                                List<Piece> lesPieces = new ArrayList<>();
                                                if(list != null){
                                                    for (HashMap<String, Object> hashMap : list) {
                                                        String id=(String) hashMap.get("idPiece");
                                                        String nom = (String) hashMap.get("nom");
                                                        String type = (String) hashMap.get("typePiece");
                                                        String adresse = (String) hashMap.get("chemin");
                                                        List<HashMap<String, Object>> listHashMap = (List<HashMap<String, Object>>) hashMap.get("composants");
                                                        lesPieces.add(new Piece(id,type,nom,adresse,listHashMap));
                                                    }
                                                }

                                                List<HashMap<String, Object>> users= (List<HashMap<String, Object>>) documentSnapshot.get("lesUsers");
                                                List<UserModel> lesUsers= new ArrayList<>();
                                                if (users != null){
                                                    for (HashMap<String, Object> hashMap: users){
                                                        boolean admin= (boolean) hashMap.get("admin");
                                                        String dateEnregistrement= (String) hashMap.get("dateEnregistrement");
                                                        String email= (String) hashMap.get("email");
                                                        String nom= (String) hashMap.get("nom");
                                                        String password= (String) hashMap.get("password");
                                                        String uid= (String) hashMap.get("uid");
                                                        boolean user= (boolean) hashMap.get("user");

                                                        UserModel userTrouve= new UserModel();
                                                        userTrouve.setAdmin(admin);
                                                        userTrouve.setDateEnregistrement(dateEnregistrement);
                                                        userTrouve.setEmail(email);
                                                        userTrouve.setNom(nom);
                                                        userTrouve.setPassword(password);
                                                        userTrouve.setUid(uid);
                                                        userTrouve.setUser(user);

                                                        lesUsers.add(userTrouve);
                                                    }
                                                }
                                                String nomLocal= documentSnapshot.getString("nomLocal");
                                                String quartierLocal= documentSnapshot.getString("quartierLocal");
                                                TypeLocal typeLocal= TypeLocal.valueOf(documentSnapshot.getString("typeLocal"));
                                                String villeLocal= documentSnapshot.getString("villeLocal");
                                                String dateEnregistrementLocal = documentSnapshot.getString("dateEnregistrement");

                                                //creer un nouvel local et lui affecter les attributs recuperés ci-dessus
                                                Local local= new Local();
                                                local.setDesignationLocal(designationLocal);
                                                local.setAdresseLocal(adresseLocal);
                                                local.setIdLocal(idLocal);
                                                local.setLesPieces(lesPieces);
                                                local.setLesUsers(lesUsers);
                                                local.setNomLocal(nomLocal);
                                                local.setQuartierLocal(quartierLocal);
                                                local.setTypeLocal(typeLocal);
                                                local.setVilleLocal(villeLocal);
                                                local.setDateEnregistrement(dateEnregistrementLocal);


                                                collectionUsers
                                                        .document(firebaseUser.getUid())
                                                                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                UserModel userModel1= documentSnapshot.toObject(UserModel.class);
                                                                if (local.containsUser(userModel1)) {
                                                                    lesLocaux.add(local);
                                                                    updateListViewOfLocals();
                                                                }
                                                            }
                                                        });
                                            }
                                        }
                                    });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Erreur...", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}