package com.aplication.appgestionrepartos.pago;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import com.aplication.appgestionrepartos.R;
import com.aplication.appgestionrepartos.adapter.ClientesRegistroAdapter;
import com.aplication.appgestionrepartos.interfaz.InterfazAdministradorActivity;
import com.aplication.appgestionrepartos.model.UsuarioPago;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ClientesRegistradosActivity extends AppCompatActivity {


    Button btn_exit;
    ClientesRegistroAdapter mAdapter;

    RecyclerView mRecycler;
    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;
    SearchView search_view;
    Query query;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes_registrados);

        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        search_view = findViewById(R.id.search);

        btn_exit = findViewById(R.id.btn_close);

        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                startActivity(new Intent(ClientesRegistradosActivity.this, InterfazAdministradorActivity.class));
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
                finish();

            }
        });


        setUpRecyclerView();



        search_view();


    }



    private void setUpRecyclerView() {

        String id = mAuth.getUid();

        String idUser = mAuth.getCurrentUser().getUid();

        mRecycler = findViewById(R.id.recyclerViewSingle);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));


        query = mFirestore.collection("Cliente");


        FirestoreRecyclerOptions<UsuarioPago> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<UsuarioPago>().setQuery(query, UsuarioPago.class).build();

        mAdapter = new ClientesRegistroAdapter(firestoreRecyclerOptions, this, getSupportFragmentManager());
        mAdapter.notifyDataSetChanged();
        mRecycler.setAdapter(mAdapter);


        mAdapter.setOnItemClickListener(new ClientesRegistroAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                UsuarioPago usuarios = documentSnapshot.toObject(UsuarioPago.class);
                String id = documentSnapshot.getId();
                //String path = documentSnapshot.getReference().getPath();



                //Toast.makeText(ClientesRegistradosActivity.this, "Position: " + position + " ID: " + id, Toast.LENGTH_SHORT).show();



                Intent i = new Intent(ClientesRegistradosActivity.this, PagosPerfilClientesActivity.class);

                i.putExtra("id_ps",id);

                overridePendingTransition(R.anim.left_in, R.anim.left_out);

                startActivity(i);






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
        FirestoreRecyclerOptions<UsuarioPago> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<UsuarioPago>()
                        .setQuery(query.orderBy("estado_pago")
                                .startAt(s).endAt(s+"~"), UsuarioPago.class).build();
        mAdapter = new ClientesRegistroAdapter(firestoreRecyclerOptions, this, getSupportFragmentManager());
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