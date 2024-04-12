package com.aplication.appgestionrepartos.administrador;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.aplication.appgestionrepartos.R;
import com.aplication.appgestionrepartos.adapter.PedidoFinalAdapter;
import com.aplication.appgestionrepartos.interfaz.InterfazAdministradorActivity;
import com.aplication.appgestionrepartos.model.Pedido;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class PedidoFinalActivity extends AppCompatActivity {

    Button btn_exit;
    PedidoFinalAdapter mAdapter;


    RecyclerView mRecycler;
    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;
    SearchView search_view;
    Query query;

    private SwipeRefreshLayout swipeRefreshLayout;


    private String codigoCliente;



    @SuppressLint("NotifyDataSetChanged")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido_final);


        codigoCliente = getIntent().getStringExtra("codigoid");


        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        search_view = findViewById(R.id.search);

        swipeRefreshLayout = findViewById(R.id.swipe1);


        btn_exit = findViewById(R.id.btn_close);

        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(PedidoFinalActivity.this, InterfazAdministradorActivity.class));

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
        startActivity(new Intent(getApplicationContext(), PedidoFinalActivity.class));
        overridePendingTransition(0,0);
        finish();
        overridePendingTransition(0,0);
    }





    @SuppressLint("NotifyDataSetChanged")
    private void setUpRecyclerView() {

        mRecycler = findViewById(R.id.recyclerViewSingle);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        //Query query = mFirestore.collection("pet").whereEqualTo("id_user", mAuth.getCurrentUser().getUid());


        query = mFirestore.collection("comprasfinal/"+codigoCliente+"/cliente");

        FirestoreRecyclerOptions<Pedido> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Pedido>().setQuery(query, Pedido.class).build();

        mAdapter = new PedidoFinalAdapter(firestoreRecyclerOptions, this, getSupportFragmentManager());
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

        FirestoreRecyclerOptions<Pedido> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Pedido>()
                        .setQuery(query.orderBy("nombre")
                                .startAt(s).endAt(s+"~"), Pedido.class).build();


        mAdapter = new PedidoFinalAdapter(firestoreRecyclerOptions, this, getSupportFragmentManager());
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