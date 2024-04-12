package com.aplication.appgestionrepartos.repartidor_ui.inicio_r;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aplication.appgestionrepartos.R;
import com.aplication.appgestionrepartos.adapter.InicioPetAdapter;
import com.aplication.appgestionrepartos.adapter.SliderAdapter;
import com.aplication.appgestionrepartos.model.Pet;
import com.aplication.appgestionrepartos.model.SliderItem;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class InicioRFragment extends Fragment {


    private SliderView svCarrusel;
    private SliderAdapter sliderAdapter;


    RecyclerView mRecycler;

    FirebaseFirestore mFirestore;

    InicioPetAdapter mAdapter;
    FirebaseAuth mAuth;
    Query query;


    private ArrayList<SliderItem> lista;



    StorageReference storageReference;
    String storage_path = "galeria/*";


    String storage_path_name;


    private static final int COD_SEL_STORAGE = 200;
    private static final int COD_SEL_IMAGE = 300;

    private Uri image_url;
    String photo = "photo";


    ProgressDialog progressDialog;


    EditText tituloAnuncio;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirestore = FirebaseFirestore.getInstance();

        lista = new ArrayList<>();

        storageReference = FirebaseStorage.getInstance().getReference();







    }







    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_inicior, container, false);


        progressDialog = new ProgressDialog(view.getContext());




        listadoProductos(view);




        return view;
    }




    private void listadoProductos(View v){

        mRecycler = v.findViewById(R.id.rcvLista_Productos);

        mRecycler.setLayoutManager(new LinearLayoutManager(v.getContext(),LinearLayoutManager.VERTICAL,false));

        query = mFirestore.collection("pda");

        FirestoreRecyclerOptions<Pet> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Pet>().setQuery(query, Pet.class).build();

        mAdapter = new InicioPetAdapter(firestoreRecyclerOptions);
        mAdapter.notifyDataSetChanged();
        mRecycler.setAdapter(mAdapter);





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
                    String nombre = documentSnapshot.getString("nombre");


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









