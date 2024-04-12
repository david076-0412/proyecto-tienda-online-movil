package com.aplication.appgestionrepartos.cliente;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import com.aplication.appgestionrepartos.R;
import com.aplication.appgestionrepartos.adapter.ContactosAdapter;
import com.aplication.appgestionrepartos.interfaz.InterfazClienteActivity;
import com.aplication.appgestionrepartos.model.Usuarios;
import com.aplication.appgestionrepartos.no_internet_connection.NetworkChangeListener;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

public class Lista_RepartidoresActivity extends AppCompatActivity {


    Button btn_exit;
    ContactosAdapter mAdapter;

    RecyclerView mRecycler;
    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;
    SearchView search_view;
    Query query;

    NetworkChangeListener networkChangeListener;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_repartidores);


        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        search_view = findViewById(R.id.search);

        btn_exit = findViewById(R.id.btn_close);

        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                startActivity(new Intent(Lista_RepartidoresActivity.this, InterfazClienteActivity.class));

                overridePendingTransition(R.anim.left_in, R.anim.left_out);

                finish();

            }
        });


        setUpRecyclerView();



        search_view();





    }



    @SuppressLint("NotifyDataSetChanged")
    private void setUpRecyclerView() {

        String id = mAuth.getUid();

        String idUser = mAuth.getCurrentUser().getUid();

        mRecycler = findViewById(R.id.recyclerViewSingle);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));


        query = mFirestore.collection("Repartidor");


        FirestoreRecyclerOptions<Usuarios> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Usuarios>().setQuery(query, Usuarios.class).build();

        mAdapter = new ContactosAdapter(firestoreRecyclerOptions, this, getSupportFragmentManager());
        mAdapter.notifyDataSetChanged();
        mRecycler.setAdapter(mAdapter);


        mAdapter.setOnItemClickListener(new ContactosAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Usuarios usuarios = documentSnapshot.toObject(Usuarios.class);

                String idRepartidor = documentSnapshot.getId();



                Intent i = new Intent(Lista_RepartidoresActivity.this, CarritoActivity.class);


                mFirestore.collection("Repartidor").document(idRepartidor).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot1) {


                        String nombreRepartidor = documentSnapshot1.getString("name");

                        String apellidoRepartidor = documentSnapshot1.getString("apellido");

                        String celularRepartidor = documentSnapshot1.getString("celular");


                        String direccionRepartidor = documentSnapshot1.getString("direccion");


                        String photo_Repartidor = documentSnapshot1.getString("photo_repartidor");



                        //-------------------------------------------------------


                        Map<String, Object> map = new HashMap<>();
                        map.put("id_repartidor", idRepartidor);
                        map.put("repartidor_nombre", nombreRepartidor);
                        map.put("repartidor_apellido",apellidoRepartidor);
                        map.put("celularrepartidor",celularRepartidor);
                        map.put("direccion_repartidor",direccionRepartidor);
                        map.put("photo_repartidor",photo_Repartidor);


                        mFirestore.collection("Cliente").document(idUser).update(map);
                        mFirestore.collection("Usuario/Cliente/"+idUser).document("registro").update(map);


                        //-------------------------------------------------------


                        overridePendingTransition(R.anim.left_in, R.anim.left_out);

                        startActivity(i);




                    }
                });












            }
        });







    }





    private void search_view() {
        search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                textSearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                textSearch(s);
                return false;
            }
        });
    }



    public void textSearch(String s){
        FirestoreRecyclerOptions<Usuarios> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Usuarios>()
                        .setQuery(query.orderBy("name")
                                .startAt(s).endAt(s+"~"), Usuarios.class).build();
        mAdapter = new ContactosAdapter(firestoreRecyclerOptions, this, getSupportFragmentManager());
        mAdapter.startListening();
        mRecycler.setAdapter(mAdapter);
    }




    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.startListening();


    }
    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();



    }









}