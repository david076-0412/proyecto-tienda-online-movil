package com.aplication.appgestionrepartos.administrador;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;

import com.aplication.appgestionrepartos.R;
import com.aplication.appgestionrepartos.adapter.PetAdapter;
import com.aplication.appgestionrepartos.adapter.ProveedoreAdapter;
import com.aplication.appgestionrepartos.model.Pet;
import com.aplication.appgestionrepartos.model.Proveedor;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class ListaProveedoresFragment extends Fragment {



    ProveedoreAdapter mAdapter;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lista_proveedores, container, false);


        mRecycler = view.findViewById(R.id.recyclerViewSingle);
        mRecycler.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));



        //Query query = mFirestore.collection("pet").whereEqualTo("id_user", mAuth.getCurrentUser().getUid());
        query = mFirestore.collection("Proveedores");

        FirestoreRecyclerOptions<Proveedor> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Proveedor>().setQuery(query, Proveedor.class).build();

        mAdapter = new ProveedoreAdapter(firestoreRecyclerOptions);
        mAdapter.notifyDataSetChanged();
        mRecycler.setAdapter(mAdapter);


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
        FirestoreRecyclerOptions<Proveedor> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Proveedor>()
                        .setQuery(query.orderBy("nombre")
                                .startAt(s).endAt(s+"~"), Proveedor.class).build();
        mAdapter = new ProveedoreAdapter(firestoreRecyclerOptions);
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