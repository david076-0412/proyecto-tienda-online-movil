package com.aplication.appgestionrepartos.cliente;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.aplication.appgestionrepartos.cliente_ui.compras.DetallesComprasActivity;
import com.aplication.appgestionrepartos.R;
import com.aplication.appgestionrepartos.adapter.ProductosAdapter;
import com.aplication.appgestionrepartos.interfaz.InterfazClienteActivity;
import com.aplication.appgestionrepartos.model.Productos;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ListadoProductosActivity extends AppCompatActivity {


    Button btn_exit;
    ProductosAdapter mAdapter;
    RecyclerView mRecycler;
    TextView mTextviewPagoTotal,mTextViewtotal_producto;
    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;
    SearchView search_view;
    Query query;


    private String totalcliente;

    private double precio_totalpet;

    private double precio_vacunapet;

    private String nombreCarrito;

    private String idPedido;
    private String pedidosCliente;

    private double precio_finalCliente;

    private SwipeRefreshLayout swipeRefreshLayout;


    //-------------------------
    private String codigoP;


    //private String data = "";

    //int pageHeight = 1120;
    //int pagewidth = 1200;

    // creating a bitmap variable
    // for storing our images
    //Bitmap bmp, scaledbmp;

    //float[] prices = new float[]{0,200,450,325,500};


    public final static String EXTRA_MESSAGE = "com.aplication.appgestionrepartos.cliente";

    private String codigoCliente;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_productos);

        String id = getIntent().getStringExtra("id_pet");



        codigoCliente = getIntent().getStringExtra("codigo");

        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        search_view = findViewById(R.id.search);
        mTextViewtotal_producto = findViewById(R.id.total_precio);




        //bmp = BitmapFactory.decodeResource(getResources(), R.drawable.pizzahead);
        //scaledbmp = Bitmap.createScaledBitmap(bmp,1200,510,false);





        swipeRefreshLayout = findViewById(R.id.swipe);


        btn_exit = findViewById(R.id.btn_regresarLista);


        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                /*

                ComprasFragment comprasFragment = new ComprasFragment();

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

                fragmentTransaction.replace(R.id.container, comprasFragment).commit();
*/



                startActivity(new Intent(ListadoProductosActivity.this, InterfazClienteActivity.class));

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




        setUpRecyclerView();

        search_view();

    }



    private void refresh(){
        Toast.makeText(getApplicationContext(), "Refreshed", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), ListadoProductosActivity.class));
        overridePendingTransition(0,0);
        finish();
        overridePendingTransition(0,0);
    }



    @SuppressLint("NotifyDataSetChanged")
    private void setUpRecyclerView() {

        mRecycler = findViewById(R.id.recyclerViewSingle);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        String idUser = mAuth.getCurrentUser().getUid();

        //Toast.makeText(ListadoProductosActivity.this, codigoCliente , Toast.LENGTH_SHORT).show();


        query = mFirestore.collection("compras/"+idUser+"/"+codigoCliente).whereEqualTo("id_user", mAuth.getCurrentUser().getUid());
        //query = mFirestore.collection("carrito");

        FirestoreRecyclerOptions<Productos> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Productos>().setQuery(query, Productos.class).build();

        mAdapter = new ProductosAdapter(firestoreRecyclerOptions, this, getSupportFragmentManager());


        mAdapter.setOnItemClickListener(new ProductosAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Productos pedido = documentSnapshot.toObject(Productos.class);
                String id = documentSnapshot.getId();
                String path = documentSnapshot.getReference().getPath();



                //Toast.makeText(ComprasPedidoActivity.this, "Position: " + position + " ID: " + id , Toast.LENGTH_SHORT).show();







                Intent i = new Intent(ListadoProductosActivity.this, DetallesComprasActivity.class);




                i.putExtra("codigo",codigoCliente);

                i.putExtra("id_pro",id);

                overridePendingTransition(R.anim.left_in, R.anim.left_out);

                startActivity(i);



            }
        });




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
        FirestoreRecyclerOptions<Productos> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Productos>()
                        .setQuery(query.orderBy("name")
                                .startAt(s).endAt(s+"~"), Productos.class).build();
        mAdapter = new ProductosAdapter(firestoreRecyclerOptions, this, getSupportFragmentManager());
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