package com.aplication.appgestionrepartos.administrador_ui.pedidos;



import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.view.View;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;


import com.aplication.appgestionrepartos.R;
import com.aplication.appgestionrepartos.adapter.ContactosPedidoFinalAdapter;
import com.aplication.appgestionrepartos.administrador.PedidoFinalActivity;
import com.aplication.appgestionrepartos.model.Usuarios;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;


public class PedidosAFragment extends Fragment {


    ContactosPedidoFinalAdapter mAdapter;

    RecyclerView mRecycler;
    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;
    SearchView search_view;
    Query query;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

    }









    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_pedidosa, container, false);


        search_view = view.findViewById(R.id.search);

        setUpRecyclerView(view);



        search_view();




        return view;


    }




    @SuppressLint("NotifyDataSetChanged")
    public void setUpRecyclerView(View v) {


        mRecycler = v.findViewById(R.id.recyclerViewSingle);
        mRecycler.setLayoutManager(new LinearLayoutManager(v.getContext(), LinearLayoutManager.VERTICAL,false));

        query = mFirestore.collection("Cliente");


        FirestoreRecyclerOptions<Usuarios> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Usuarios>().setQuery(query, Usuarios.class).build();

        mAdapter = new ContactosPedidoFinalAdapter(firestoreRecyclerOptions);
        mAdapter.notifyDataSetChanged();
        mRecycler.setAdapter(mAdapter);


        mAdapter.setOnItemClickListener(new ContactosPedidoFinalAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Usuarios usuarios = documentSnapshot.toObject(Usuarios.class);
                String id = documentSnapshot.getId();
                String path = documentSnapshot.getReference().getPath();



                //Toast.makeText(v.getContext(), "Position: " + position + " ID: " + id, Toast.LENGTH_SHORT).show();




                Intent i = new Intent(v.getContext(), PedidoFinalActivity.class);


                i.putExtra("codigoid",id);

                startActivity(i);






            }
        });







    }




    public void search_view() {
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
        mAdapter = new ContactosPedidoFinalAdapter(firestoreRecyclerOptions);
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