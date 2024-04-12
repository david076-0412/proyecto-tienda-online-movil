package com.aplication.appgestionrepartos.administrador_ui.inicio_a;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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


import com.aplication.appgestionrepartos.model.SliderItem;
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

import com.aplication.appgestionrepartos.model.Pet;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class InicioAFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;


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


    Button btn_subir_anuncio;









    public static InicioAFragment newInstance(String param1, String param2) {
        InicioAFragment fragment = new InicioAFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirestore = FirebaseFirestore.getInstance();

        lista = new ArrayList<>();

        storageReference = FirebaseStorage.getInstance().getReference();


        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }




    }






    @SuppressLint("MissingInflatedId")

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_inicioa, container, false);


        progressDialog = new ProgressDialog(view.getContext());



        btn_subir_anuncio = view.findViewById(R.id.btn_subir_anuncio);

        btn_subir_anuncio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final EditText resetMail = new EditText(view.getContext());





                AlertDialog.Builder dialogo2 = new AlertDialog.Builder(view.getContext());
                dialogo2.setTitle("AVISO!");
                dialogo2.setMessage("TITULO");
                dialogo2.setView(resetMail);
                dialogo2.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo2, int id) {



                        String mail = resetMail.getText().toString().trim();

                        String mailpet = ucFirst(mail);



                        if (mail.isEmpty()){


                            resetMail.setError("El campo esta vacio");


                        }else{


                            Intent intent = new Intent(view.getContext(), AnuncioActivity.class);


                            intent.putExtra("titulo", mailpet);



                            startActivity(intent);






                        }








                    }
                });
                dialogo2.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo2, int id) {



                    }
                });


                dialogo2.show();












            }
        });






        listadoProductos(view);





        return view;





    }




    public static String ucFirst(String str) {
        if (str == null || str.isEmpty()) {
            return "";
        } else {
            return  Character.toUpperCase(str.charAt(0)) + str.substring(1, str.length()).toLowerCase();
        }
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