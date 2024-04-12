package com.aplication.appgestionrepartos.cliente;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.aplication.appgestionrepartos.R;
import com.aplication.appgestionrepartos.adapter.PedidoComprasAdapter;
import com.aplication.appgestionrepartos.interfaz.InterfazClienteActivity;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.aplication.appgestionrepartos.model.Pedido;


public class ComprasPedidoActivity extends AppCompatActivity {

    Button btn_add, btn_add_fragment, btn_exit;
    PedidoComprasAdapter mAdapter;

    RecyclerView mRecycler;
    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;
    SearchView search_view;
    Query query;

    private SwipeRefreshLayout swipeRefreshLayout;


    @SuppressLint("NotifyDataSetChanged")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compras_pedido);

        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        search_view = findViewById(R.id.search);

        swipeRefreshLayout = findViewById(R.id.swipe1);

        btn_exit = findViewById(R.id.btn_close);

        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                startActivity(new Intent(ComprasPedidoActivity.this, InterfazClienteActivity.class));

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
        startActivity(new Intent(getApplicationContext(), ComprasPedidoActivity.class));
        overridePendingTransition(0,0);
        finish();
        overridePendingTransition(0,0);
    }





    @SuppressLint("NotifyDataSetChanged")
    private void setUpRecyclerView() {

        String id = mAuth.getUid();

        String idUser = mAuth.getCurrentUser().getUid();

        mRecycler = findViewById(R.id.recyclerViewSingle);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));


        query = mFirestore.collection("comprasfinal/"+idUser+"/cliente").whereEqualTo("id_user",idUser);


        FirestoreRecyclerOptions<Pedido> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Pedido>().setQuery(query, Pedido.class).build();

        mAdapter = new PedidoComprasAdapter(firestoreRecyclerOptions,this);
        mAdapter.notifyDataSetChanged();
        mRecycler.setAdapter(mAdapter);


        mAdapter.setOnItemClickListener(new PedidoComprasAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Pedido pedido = documentSnapshot.toObject(Pedido.class);
                String id = documentSnapshot.getId();
                String path = documentSnapshot.getReference().getPath();



                //Toast.makeText(ComprasPedidoActivity.this, "Position: " + position + " ID: " + id , Toast.LENGTH_SHORT).show();


                Intent i = new Intent(ComprasPedidoActivity.this, ListadoProductosActivity.class);

                i.putExtra("codigo",id);
                startActivity(i);

                overridePendingTransition(R.anim.left_in, R.anim.left_out);


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
        FirestoreRecyclerOptions<Pedido> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Pedido>()
                        .setQuery(query.orderBy("color")
                                .startAt(s).endAt(s+"~"), Pedido.class).build();
        mAdapter = new PedidoComprasAdapter(firestoreRecyclerOptions,this);
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