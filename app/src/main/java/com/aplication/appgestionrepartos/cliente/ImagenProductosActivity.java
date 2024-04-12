package com.aplication.appgestionrepartos.cliente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.aplication.appgestionrepartos.R;
import com.aplication.appgestionrepartos.adapter.SliderProductosAdapter;
import com.aplication.appgestionrepartos.model.SliderItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ImagenProductosActivity extends AppCompatActivity {


    private ViewPager2 svCarrusel;

    private ArrayList<SliderItem> lista;
    private SliderProductosAdapter sliderAdapter;

    FirebaseFirestore mFirestore;

    String nombreproducto;

    String codigoid;

    TextView textProducto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_imagen_productos);

        codigoid = getIntent().getStringExtra("id_pet");

        nombreproducto = getIntent().getStringExtra("nombre");

        mFirestore = FirebaseFirestore.getInstance();


        svCarrusel = findViewById(R.id.svCarrusel);

        lista = new ArrayList<>();

        loadData();

        textProducto = (TextView) findViewById(R.id.textProducto);




    }





    private void loadData() {

        //Toast.makeText(ImagenProductosActivity.this, nombreproducto, Toast.LENGTH_SHORT).show();

        mFirestore.collection("galeria/productos/"+nombreproducto).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){



                    SliderItem sliderItem = documentSnapshot.toObject(SliderItem.class);

                    String nombre = documentSnapshot.getString("nombre");


                    SliderItem model = new SliderItem();

                    model.setImagen(sliderItem.getImagen());

                    //model.setTitulo(nombre);


                    textProducto.setText(nombre);


                    lista.add(model);




                    sliderAdapter = new SliderProductosAdapter(lista, svCarrusel);
                    svCarrusel.setAdapter(sliderAdapter);


                    svCarrusel.setOffscreenPageLimit(3);
                    svCarrusel.setClipChildren(false);
                    svCarrusel.setClipToPadding(false);


                    svCarrusel.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

                    CompositePageTransformer transformer = new CompositePageTransformer();
                    transformer.addTransformer(new MarginPageTransformer(40));
                    transformer.addTransformer(new ViewPager2.PageTransformer() {
                        @Override
                        public void transformPage(@NonNull View page, float position) {
                            float r = 1 - Math.abs(position);
                            page.setScaleY(0.85f + r * 0.14f);
                        }
                    });


                    svCarrusel.setPageTransformer(transformer);



                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ImagenProductosActivity.this, "Fail to load slider data..", Toast.LENGTH_SHORT).show();
            }
        });





    }










}