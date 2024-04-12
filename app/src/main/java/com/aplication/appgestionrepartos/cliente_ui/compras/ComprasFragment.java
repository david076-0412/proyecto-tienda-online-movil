package com.aplication.appgestionrepartos.cliente_ui.compras;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.aplication.appgestionrepartos.R;




import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aplication.appgestionrepartos.adapter.PedidoComprasAdapter;
import com.aplication.appgestionrepartos.adapter.PedidoFinalAdapter;
import com.aplication.appgestionrepartos.adapter.PedidoFinalRepartidorAdapter;
import com.aplication.appgestionrepartos.cliente.ListadoProductosActivity;
import com.daimajia.swipe.SwipeLayout;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.aplication.appgestionrepartos.model.Pedido;


public class ComprasFragment extends Fragment {


    Button btn_add, btn_add_fragment;
    PedidoComprasAdapter mAdapter;

    RecyclerView mRecycler;
    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;
    SearchView search_view;
    Query query;



    @SuppressLint("NotifyDataSetChanged")

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

    }







    @SuppressLint("MissingInflatedId")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_compras, container, false);






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
        mRecycler.setLayoutManager(new LinearLayoutManager(v.getContext(), LinearLayoutManager.VERTICAL, false));





        query = mFirestore.collection("comprasfinal/"+idUser+"/cliente").whereEqualTo("id_user",idUser);


        FirestoreRecyclerOptions<Pedido> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Pedido>().setQuery(query, Pedido.class).build();

        mAdapter = new PedidoComprasAdapter(firestoreRecyclerOptions,getActivity());
        mAdapter.notifyDataSetChanged();
        mRecycler.setAdapter(mAdapter);


        mAdapter.setOnItemClickListener(new PedidoComprasAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Pedido pedido = documentSnapshot.toObject(Pedido.class);
                String id = documentSnapshot.getId();
                String path = documentSnapshot.getReference().getPath();



                //Toast.makeText(ComprasPedidoActivity.this, "Position: " + position + " ID: " + id , Toast.LENGTH_SHORT).show();







                Intent i = new Intent(v.getContext(), ListadoProductosActivity.class);

                i.putExtra("codigo",id);

                getActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);

                getActivity().startActivity(i);



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
        mAdapter = new PedidoComprasAdapter(firestoreRecyclerOptions,getActivity());
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