package com.aplication.appgestionrepartos.repartidor;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.aplication.appgestionrepartos.R;
import com.aplication.appgestionrepartos.adapter.ArticulosDetallesAdapter;
import com.aplication.appgestionrepartos.model.Productos;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.Map;


public class ArticulosDetallesFragment extends Fragment {



    TextView mTextViewtotal_producto;



    ArticulosDetallesAdapter mAdapter;

    RecyclerView mRecycler;
    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;
    Query query;


    public String codigoCliente, id;


    Button btn_actualizar;


    private double precio_finalpt, precio_totalw,precioproducto=0,precio_totalproductos;





    public ArticulosDetallesFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();






    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_articulos_detalles, container, false);

        id = getActivity().getIntent().getStringExtra("user");



        codigoCliente = getActivity().getIntent().getStringExtra("id_user");



        mTextViewtotal_producto = view.findViewById(R.id.total_precio);

        btn_actualizar = view.findViewById(R.id.btn_add);


        btn_actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Double precio_finaltt = Double.parseDouble(mTextViewtotal_producto.getText().toString().trim());




                Map<String, Object> mapT = new HashMap<>();



                mapT.put("vaccine_price", precio_finaltt);




                mFirestore.collection("comprasfinal/"+codigoCliente+"/cliente").document(id).update(mapT);


                Toast.makeText(view.getContext(), "Finalizar Actualizacion", Toast.LENGTH_SHORT).show();



                Map<String, Object> mapy = new HashMap<>();



                mapy.put("precio_producto", precio_finaltt);



                mFirestore.collection("comprasfinal/"+codigoCliente+"/MensajePago").document(id).update(mapy);



                //Intent i = new Intent(view.getContext(), ProductoClienteActivity.class);
                //i.putExtra("id_user", codigoCliente);


                //startActivity(i);












            }
        });


        setUpRecyclerView(view);













        return view;


    }






    private void totalprecio(){



        mFirestore.collection("compras/"+codigoCliente+"/"+id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot snapshot : task.getResult()){

                    precioproducto = snapshot.getDouble("vaccine_price");


                    precio_totalproductos = precio_totalproductos + precioproducto;




                    BigDecimal cc = new BigDecimal(precio_totalproductos);

                    MathContext mc = new MathContext(3);




                    mTextViewtotal_producto.setText(""+cc.round(mc));






                }
            }
        });



    }




    @SuppressLint("NotifyDataSetChanged")
    private void setUpRecyclerView(View v) {

        mRecycler = v.findViewById(R.id.recyclerViewSingle);
        mRecycler.setLayoutManager(new LinearLayoutManager(v.getContext(), LinearLayoutManager.VERTICAL, false));


        //query = mFirestore.collection("carrito/"+idUs+"/pedido").whereEqualTo("id_user", mAuth.getCurrentUser().getUid());


        query = mFirestore.collection("compras/"+codigoCliente+"/"+id).whereEqualTo("id_user",codigoCliente);




        FirestoreRecyclerOptions<Productos> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Productos>().setQuery(query, Productos.class).build();

        mAdapter = new ArticulosDetallesAdapter(firestoreRecyclerOptions,getActivity().getSupportFragmentManager());
        mAdapter.notifyDataSetChanged();
        mRecycler.setAdapter(mAdapter);



    }




    @Override
    public void onStart() {
        super.onStart();
        mAdapter.startListening();

        totalprecio();


    }
    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();

    }






}