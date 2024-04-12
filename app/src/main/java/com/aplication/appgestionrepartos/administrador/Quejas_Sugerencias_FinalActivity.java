package com.aplication.appgestionrepartos.administrador;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.aplication.appgestionrepartos.R;
import com.aplication.appgestionrepartos.adapter.Quejas_SugerenciasAdapter;
import com.aplication.appgestionrepartos.interfaz.InterfazAdministradorActivity;
import com.aplication.appgestionrepartos.model.Quejas_Sugerencias;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Quejas_Sugerencias_FinalActivity extends AppCompatActivity {


    Button btn_exit;
    Quejas_SugerenciasAdapter mAdapter;


    RecyclerView mRecycler;
    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;
    SearchView search_view;
    Query query;

    private SwipeRefreshLayout swipeRefreshLayout;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quejas_sugerencias_final);


        String id = getIntent().getStringExtra("codigoid");


        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        search_view = findViewById(R.id.search);

        swipeRefreshLayout = findViewById(R.id.swipe1);


        btn_exit = findViewById(R.id.btn_close);

        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                startActivity(new Intent(Quejas_Sugerencias_FinalActivity.this, InterfazAdministradorActivity.class));

                overridePendingTransition(R.anim.left_in, R.anim.left_out);

                finish();

            }
        });








        setUpRecyclerView();


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                refresh();
                swipeRefreshLayout.setRefreshing(false);

            }
        });


        search_view();






    }


    private void refresh(){
        Toast.makeText(getApplicationContext(), "Refreshed", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), Quejas_Sugerencias_FinalActivity.class));
        overridePendingTransition(0,0);
        finish();
        overridePendingTransition(0,0);
    }


    @SuppressLint("NotifyDataSetChanged")
    private void setUpRecyclerView() {

        mRecycler = findViewById(R.id.recyclerViewSingle);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        //Query query = mFirestore.collection("pet").whereEqualTo("id_user", mAuth.getCurrentUser().getUid());


        query = mFirestore.collection("Incidencias");



        FirestoreRecyclerOptions<Quejas_Sugerencias> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Quejas_Sugerencias>().setQuery(query, Quejas_Sugerencias.class).build();

        mAdapter = new Quejas_SugerenciasAdapter(firestoreRecyclerOptions, this, getSupportFragmentManager());
        mAdapter.notifyDataSetChanged();


        mRecycler.setAdapter(mAdapter);

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

        FirestoreRecyclerOptions<Quejas_Sugerencias> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Quejas_Sugerencias>()
                        .setQuery(query.orderBy("incidencia")
                                .startAt(s).endAt(s+"~"), Quejas_Sugerencias.class).build();


        mAdapter = new Quejas_SugerenciasAdapter(firestoreRecyclerOptions, this, getSupportFragmentManager());
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