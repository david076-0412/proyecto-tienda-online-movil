package com.aplication.appgestionrepartos.cliente_ui.inicio;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.aplication.appgestionrepartos.R;

import com.aplication.appgestionrepartos.adapter.InicioAdapter;
import com.aplication.appgestionrepartos.adapter.InicioPetAdapter;
import com.aplication.appgestionrepartos.adapter.PetAdapter;
import com.aplication.appgestionrepartos.adapter.SliderAdapter;


import com.aplication.appgestionrepartos.cliente.DetalleActivity;
import com.aplication.appgestionrepartos.model.SliderItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import com.aplication.appgestionrepartos.model.Pet;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class InicioFragment extends Fragment {


    //----------------------------------------------------


    private SliderView svCarrusel;
    private SliderAdapter sliderAdapter;
    private ArrayList<SliderItem> lista;

    //-----------------------------------------------------



    InicioPetAdapter mAdapter;
    RecyclerView mRecycler;
    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;
    Query query;










    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirestore = FirebaseFirestore.getInstance();

        lista = new ArrayList<>();



    }



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_inicio, container, false);

        listadoProductos(view);

        return view;

    }


    private void listadoProductos(View v){

        mRecycler = v.findViewById(R.id.rcvLista_Productos);

        mRecycler.setLayoutManager(new LinearLayoutManager(v.getContext(),LinearLayoutManager.VERTICAL, false));



        query = mFirestore.collection("Compraseditado");



        FirestoreRecyclerOptions<Pet> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Pet>().setQuery(query, Pet.class).build();


        mAdapter = new InicioPetAdapter(firestoreRecyclerOptions);
        mAdapter.notifyDataSetChanged();



        mRecycler.addItemDecoration(new DividerItemDecoration(v.getContext(), LinearLayoutManager.VERTICAL));
        mRecycler.setItemAnimator(new DefaultItemAnimator());

        mRecycler.setAdapter(mAdapter);


        mAdapter.setOnItemClickListener(new InicioPetAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Pet pet = documentSnapshot.toObject(Pet.class);
                String id = documentSnapshot.getId();
                String path = documentSnapshot.getReference().getPath();



                //Toast.makeText(ComprasPedidoActivity.this, "Position: " + position + " ID: " + id , Toast.LENGTH_SHORT).show();


                Intent i = new Intent(v.getContext(), DetalleActivity.class);

                i.putExtra("id_pet",id);
                startActivity(i);



            }
        });


    }



    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        loadData();
    }


    private void init(View v){
        svCarrusel = v.findViewById(R.id.svCarrusel);

    }






    private void loadData() {

        mAuth = FirebaseAuth.getInstance();
        String idUser = mAuth.getCurrentUser().getUid();



        mFirestore.collection("galeria/fotos/cliente").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){





                    SliderItem sliderItem = documentSnapshot.toObject(SliderItem.class);
                    String nombre = documentSnapshot.getString("titulo");


                    SliderItem model = new SliderItem();

                    model.setImagen(sliderItem.getImagen());
                    model.setTitulo(nombre);


                    lista.add(model);




                    sliderAdapter = new SliderAdapter(getContext(),lista);


                    svCarrusel.setSliderAdapter(sliderAdapter);
                    svCarrusel.setIndicatorAnimation(IndicatorAnimationType.WORM);
                    svCarrusel.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                    svCarrusel.setAutoCycleDirection(svCarrusel.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
                    svCarrusel.setIndicatorSelectedColor(Color.WHITE);
                    svCarrusel.setIndicatorUnselectedColor(Color.GRAY);
                    svCarrusel.setScrollTimeInSec(4);
                    svCarrusel.setAutoCycle(true);
                    svCarrusel.startAutoCycle();






                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Fail to load slider data..", Toast.LENGTH_SHORT).show();
            }
        });





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