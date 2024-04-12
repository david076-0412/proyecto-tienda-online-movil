package com.aplication.appgestionrepartos.cliente_ui.productos;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.aplication.appgestionrepartos.R;
import com.aplication.appgestionrepartos.adapter.ClienteProductoAdapter;
import com.aplication.appgestionrepartos.adapter.PedidoComprasAdapter;
import com.aplication.appgestionrepartos.cliente.AgregarCompraActivity;
import com.aplication.appgestionrepartos.cliente.ComprasPedidoActivity;
import com.aplication.appgestionrepartos.cliente.ListadoProductosActivity;
import com.aplication.appgestionrepartos.model.Pedido;
import com.aplication.appgestionrepartos.model.Pet;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ProductosFragment extends Fragment {

    Button btn_add, btn_add_fragment, btn_exit;
    ClienteProductoAdapter mAdapter;
    RecyclerView mRecycler;
    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;
    SearchView search_view;
    Query query;

    private SwipeRefreshLayout swipeRefreshLayout;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();



    }



    @SuppressLint("MissingInflatedId")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_productos, container, false);


        swipeRefreshLayout = view.findViewById(R.id.swipe);

        mRecycler = view.findViewById(R.id.recyclerViewSingleCliente);
        mRecycler.setLayoutManager(new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL,false));
        //Query query = mFirestore.collection("pet").whereEqualTo("id_user", mAuth.getCurrentUser().getUid());


        query = mFirestore.collection("Compraseditado");



        FirestoreRecyclerOptions<Pet> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Pet>().setQuery(query, Pet.class).build();

        mAdapter = new ClienteProductoAdapter(firestoreRecyclerOptions,getActivity());
        mAdapter.notifyDataSetChanged();
        mRecycler.setAdapter(mAdapter);


        mAdapter.setOnItemClickListener(new ClienteProductoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Pet pet = documentSnapshot.toObject(Pet.class);
                String id = documentSnapshot.getId();
                String path = documentSnapshot.getReference().getPath();



                //Toast.makeText(ComprasPedidoActivity.this, "Position: " + position + " ID: " + id , Toast.LENGTH_SHORT).show();


                Intent i = new Intent(view.getContext(), AgregarCompraActivity.class);

                i.putExtra("id_pet", id);
                view.getContext().startActivity(i);


            }
        });









        search_view = view.findViewById(R.id.search);


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


        return view;

    }



    public void textSearch(String s){
        FirestoreRecyclerOptions<Pet> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Pet>()
                        .setQuery(query.orderBy("name")
                                .startAt(s).endAt(s+"~"), Pet.class).build();
        mAdapter = new ClienteProductoAdapter(firestoreRecyclerOptions, getActivity());
        mAdapter.startListening();
        mRecycler.setAdapter(mAdapter);
    }




    @Override
    public void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }





}