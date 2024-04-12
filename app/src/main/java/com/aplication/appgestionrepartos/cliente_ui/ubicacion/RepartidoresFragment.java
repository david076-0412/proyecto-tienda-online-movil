package com.aplication.appgestionrepartos.cliente_ui.ubicacion;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;

import com.aplication.appgestionrepartos.R;
import com.aplication.appgestionrepartos.adapter.ContactosAdapter;
import com.aplication.appgestionrepartos.model.Usuarios;
import com.aplication.appgestionrepartos.no_internet_connection.NetworkChangeListener;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;


public class RepartidoresFragment extends Fragment {


    Button btn_exit;
    ContactosAdapter mAdapter;

    RecyclerView mRecycler;
    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;
    SearchView search_view;
    Query query;

    NetworkChangeListener networkChangeListener;



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

        View view = inflater.inflate(R.layout.fragment_repartidores, container, false);



        search_view = view.findViewById(R.id.search);


        setUpRecyclerView(view);



        search_view();




        return view;



    }


    @SuppressLint("NotifyDataSetChanged")
    private void setUpRecyclerView(View v) {

        String id = mAuth.getUid();

        String idUser = mAuth.getCurrentUser().getUid();

        mRecycler = v.findViewById(R.id.recyclerViewSingle);
        mRecycler.setLayoutManager(new LinearLayoutManager(v.getContext()));


        query = mFirestore.collection("Repartidor");


        FirestoreRecyclerOptions<Usuarios> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Usuarios>().setQuery(query, Usuarios.class).build();

        mAdapter = new ContactosAdapter(firestoreRecyclerOptions, getActivity(), getActivity().getSupportFragmentManager());
        mAdapter.notifyDataSetChanged();
        mRecycler.setAdapter(mAdapter);


        mAdapter.setOnItemClickListener(new ContactosAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

                Usuarios usuarios = documentSnapshot.toObject(Usuarios.class);
                String id = documentSnapshot.getId();
                String path = documentSnapshot.getReference().getPath();


                Intent i = new Intent(v.getContext(), RepartidorUbicacionActivity.class);



                mFirestore.collection("Usuario/Repartidor/"+id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot snapshot : task.getResult()){
                            String nombrecliente = snapshot.getString("name");
                            String apellidocliente = snapshot.getString("apellido");
                            //Toast.makeText(ContactosClienteActivity.this, nombrecliente, Toast.LENGTH_SHORT).show();


                            i.putExtra("idRepartidor", id);

                            i.putExtra("name",nombrecliente);
                            i.putExtra("apellido", apellidocliente);


                            getActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);


                            String idRepartidor = snapshot.getString("id");

                            String nombreRepartidor = snapshot.getString("name");

                            String apellidoRepartidor = snapshot.getString("apellido");

                            String celularRepartidor = snapshot.getString("celular");
                            String direccionRepartidor = snapshot.getString("direccion");
                            String photo_Repartidor = snapshot.getString("photo");


                            //-------------------------------------------------------


                            Map<String, Object> map = new HashMap<>();
                            map.put("id_repartidor", idRepartidor);
                            map.put("repartidor_nombre", nombreRepartidor);
                            map.put("repartidor_apellido",apellidoRepartidor);
                            map.put("celularrepartidor",celularRepartidor);
                            map.put("direccion_repartidor",direccionRepartidor);
                            map.put("photo_repartidor",photo_Repartidor);




                            mFirestore.collection("Cliente").document(idUser).update(map);
                            mFirestore.collection("Usuario/Cliente/"+idUser).document("registro").update(map);












                            startActivity(i);


                        }
                    }
                });








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
        FirestoreRecyclerOptions<Usuarios> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Usuarios>()
                        .setQuery(query.orderBy("name")
                                .startAt(s).endAt(s+"~"), Usuarios.class).build();
        mAdapter = new ContactosAdapter(firestoreRecyclerOptions, getActivity(), getActivity().getSupportFragmentManager());
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