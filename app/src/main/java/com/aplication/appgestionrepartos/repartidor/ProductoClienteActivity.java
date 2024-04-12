package com.aplication.appgestionrepartos.repartidor;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.aplication.appgestionrepartos.R;
import com.aplication.appgestionrepartos.adapter.PedidoComprasAdapter;
import com.aplication.appgestionrepartos.adapter.PedidoFinalRepartidorAdapter;
import com.aplication.appgestionrepartos.cliente.ComprasPedidoActivity;
import com.aplication.appgestionrepartos.cliente.ListadoProductosActivity;
import com.aplication.appgestionrepartos.interfaz.InterfazRepartidorActivity;
import com.aplication.appgestionrepartos.model.Pedido;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.HashMap;

public class ProductoClienteActivity extends AppCompatActivity{

    Button btn_next, btn_exit;
    PedidoFinalRepartidorAdapter mAdapter;

    RecyclerView mRecycler;
    TextView mTextviewPagoTotal,mTextViewtotal_producto;
    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;

    SearchView search_view;
    Query query;

    private String totalcliente;
    private double precio_totalpet,precio_productoyy;

    private SwipeRefreshLayout swipeRefreshLayout;

    String codigocliente, idcliente;


    Button btn_cobrar;

    TextView mensaje_cobro;





    @SuppressLint({"NotifyDataSetChanged", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto_clientes);

        mFirestore = FirebaseFirestore.getInstance();
        search_view = findViewById(R.id.search);
        mTextViewtotal_producto = findViewById(R.id.total_precio);


        codigocliente = getIntent().getStringExtra("user");


        idcliente = getIntent().getStringExtra("id_user");


        swipeRefreshLayout = findViewById(R.id.swipe);

        btn_exit = findViewById(R.id.btn_regresar);

        mensaje_cobro = findViewById(R.id.mensaje_pago);


        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProductoClienteActivity.this, InterfazRepartidorActivity.class));
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
                finish();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        //getProductoCliente();



        DecimalFormat format = new DecimalFormat("0.00");
        mTextViewtotal_producto.setText(format.format(0.0));
        setUpRecyclerView();

        search_view();







        btn_cobrar = findViewById(R.id.btn_cobrar);




        btn_cobrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                mFirestore.collection("comprasfinal/"+idcliente+"/cliente").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot snapshot : task.getResult()){


                            String estadoproducto = snapshot.getString("color");


                            if (estadoproducto.equals("Pendiente")){

                                Toast.makeText(ProductoClienteActivity.this, "Los productos aun no son entregados", Toast.LENGTH_SHORT).show();


                            }else if (estadoproducto.equals("Entregado")){

                                HashMap<String, Object> mapS = new HashMap<>();
                                mapS.put("mensaje_pago","Cobrado:");



                                mFirestore.collection("comprasfinal/"+idcliente+"/MensajePago").document("PagoPedido").update(mapS);


                                refresh();


                            }else if (estadoproducto.equals("Enviado")){

                                Toast.makeText(ProductoClienteActivity.this, "Los productos aun no llegan a su destino", Toast.LENGTH_SHORT).show();


                            }







                        }
                    }
                });










            }
        });








    }


    private void refresh(){
        //Toast.makeText(getApplicationContext(), "Refreshed", Toast.LENGTH_SHORT).show();

        Intent i = new Intent(getApplicationContext(),ProductoClienteActivity.class);
        i.putExtra("id_user",idcliente);
        i.putExtra("user",codigocliente);

        startActivity(i);

        overridePendingTransition(0,0);
        finish();
        overridePendingTransition(0,0);
    }




    @SuppressLint("NotifyDataSetChanged")
    private void setUpRecyclerView() {
        mAuth = FirebaseAuth.getInstance();
        String idUser = mAuth.getCurrentUser().getUid();
        mRecycler = findViewById(R.id.recyclerViewSingle);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));


        query = mFirestore.collection("comprasfinal/"+idcliente+"/cliente");


        FirestoreRecyclerOptions<Pedido> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Pedido>().setQuery(query, Pedido.class).build();


        mAdapter = new PedidoFinalRepartidorAdapter(firestoreRecyclerOptions, this, getSupportFragmentManager());
        mAdapter.notifyDataSetChanged();
        mRecycler.setAdapter(mAdapter);












    }





    public void getProductoCliente(){




        mFirestore.collection("comprasfinal/"+idcliente+"/PagoPedido").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot snapshot :task.getResult()){


                    String mensaje_producto = snapshot.getString("mensaje_pago");


                    mensaje_cobro.setText(mensaje_producto);


                }



            }
        });








        mFirestore.collection("comprasfinal/"+idcliente+"/MensajePago").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                for (QueryDocumentSnapshot snapshot : task.getResult()){

                    Double precio_producto = snapshot.getDouble("precio_producto");


                    precio_productoyy = precio_productoyy + precio_producto;



                    DecimalFormat format = new DecimalFormat("0.00");

                    mTextViewtotal_producto.setText(format.format(precio_productoyy));




                }


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
                        .setQuery(query.orderBy("name")
                                .startAt(s).endAt(s+"~"), Pedido.class).build();
        mAdapter = new PedidoFinalRepartidorAdapter(firestoreRecyclerOptions, this, getSupportFragmentManager());
        mAdapter.startListening();

        mRecycler.setAdapter(mAdapter);



    }



    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.startListening();
        getProductoCliente();


    }
    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }







}