package com.aplication.appgestionrepartos.cliente;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aplication.appgestionrepartos.R;
import com.aplication.appgestionrepartos.interfaz.InterfazClienteActivity;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
//import com.squareup.picasso.Picasso;


import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class DetalleActivity extends AppCompatActivity {

    ImageView photo_pet;
    Button btn_atras;

    LinearLayout linearLayout_image_btn;
    EditText name, age, color, precio_vacuna;
    RatingBar ratingBar;


    private FirebaseFirestore mfirestore;
    private FirebaseAuth mAuth;

    StorageReference storageReference;
    String storage_path = "productos/*";

    private static final int COD_SEL_STORAGE = 200;
    private static final int COD_SEL_IMAGE = 300;

    private Uri image_url;
    String photo = "photo";
    String idd;

    ProgressDialog progressDialog;


    double calificaciontotalyy;

    double conteocliente;



    double calificaciontotalrr;





    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);

        this.setTitle("Detalle");
        progressDialog = new ProgressDialog(this);
        String id = getIntent().getStringExtra("id_pet");
        mfirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        linearLayout_image_btn = findViewById(R.id.images_btn);

        name = findViewById(R.id.nombre);
        name.setEnabled(false);


        age = findViewById(R.id.edad);
        age.setEnabled(false);


        color = findViewById(R.id.color);
        color.setEnabled(false);


        precio_vacuna = findViewById(R.id.precio_vacuna);
        precio_vacuna.setEnabled(false);


        ratingBar = findViewById(R.id.ratingBar);





        photo_pet = findViewById(R.id.pet_photo);


        btn_atras = findViewById(R.id.btn_atras);



        if (id == null || id == ""){


            linearLayout_image_btn.setVisibility(View.GONE);
            btn_atras.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String namepet = name.getText().toString().trim();
                    String agepet = age.getText().toString().trim();
                    String colorpet = color.getText().toString().trim();
                    Double precio_vacunapet = Double.parseDouble(precio_vacuna.getText().toString().trim());



                    if(namepet.isEmpty() && agepet.isEmpty() && colorpet.isEmpty()){
                        Toast.makeText(getApplicationContext(), "Datos no obtenidos", Toast.LENGTH_SHORT).show();
                        name.setError("El campo esta vacio");
                        age.setError("El campo esta vacio");
                        color.setError("El campo esta vacio");

                    }else{



                        //postPet(namepet, agepet, colorpet, precio_vacunapet);
                    }



                }
            });

        }else{

            idd = id;
            btn_atras.setText("REGRESAR");
            getPet(id);


            String idUser = mAuth.getCurrentUser().getUid();




            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {



                    mfirestore.collection("Compraseditado").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            String nombreDato = documentSnapshot.getString("name");


                            float rating = ratingBar.getRating();

                            Map<String, Object> map = new HashMap<>();

                            map.put("id_user", idUser);
                            map.put("nombre", nombreDato);
                            map.put("estrellas", rating);


                            mfirestore.collection("calificacion/"+id+"/producto").document(idUser).set(map);

                            mfirestore.collection("calificacion/"+id+"/producto").document(idUser).update(map);





                        }
                    });





                }
            });



            btn_atras.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {




                    mfirestore.collection("calificacion/"+id+"/producto").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for (QueryDocumentSnapshot snapshot10 : task.getResult()){





                                mfirestore.collection("acumulador/conteo/cliente").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        for (QueryDocumentSnapshot snapshot12 : task.getResult()){

                                            Double calificacionpt = snapshot10.getDouble("estrellas");


                                            calificaciontotalyy += calificacionpt;


                                            Map<String, Object> mapQ = new HashMap<>();


                                            mapQ.put("total_estrellas", calificaciontotalyy);



                                            Double conteoclienteyy = snapshot12.getDouble("codigoS");


                                            conteocliente += conteoclienteyy;



                                            mapQ.put("total_clientes", conteocliente);




                                            mfirestore.collection("acumulador/"+idUser+"/Cliente").document("pedido").update(mapQ);




                                            mfirestore.collection("acumulador/"+idUser+"/Cliente").document("pedido").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {




                                                    Double total_estrellas = documentSnapshot.getDouble("total_estrellas");

                                                    Double total_clientes = documentSnapshot.getDouble("total_clientes");




                                                    calificaciontotalrr = total_estrellas/total_clientes;



                                                    BigDecimal cr = new BigDecimal(calificaciontotalrr);

                                                    MathContext m = new MathContext(3);


                                                    String calificacionuu = ""+cr.round(m);

                                                    double calificaciontotalgg = Double.parseDouble(calificacionuu);




                                                    Map<String, Object> mapR = new HashMap<>();


                                                    mapR.put("estrellas", calificaciontotalgg);


                                                    mfirestore.collection("Compraseditado").document(id).update(mapR);





                                                }
                                            });



















                                        }
                                    }
                                });










                            }
                        }
                    });









                    startActivity(new Intent(DetalleActivity.this, InterfazClienteActivity.class));
                    finish();




                }
            });

        }

    }






    public void galeriadetalles(View view){


        String namepet = name.getText().toString().trim();

        //Toast.makeText(AgregarCompraActivity.this, namepet, Toast.LENGTH_SHORT).show();


        Intent intent = new Intent(DetalleActivity.this,ImagenProductosActivity.class);
        intent.putExtra("id_pet",idd);
        intent.putExtra("nombre", namepet);
        startActivity(intent);

        overridePendingTransition(R.anim.left_in, R.anim.left_out);


        //Toast.makeText(AgregarCompraActivity.this, idd, Toast.LENGTH_SHORT).show();



    }














    private void updatePet(String namepet, String agepet, String colorpet, Double precio_vacunapet, String id) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", namepet);
        map.put("age", agepet);
        map.put("color", colorpet);
        map.put("vaccine_price", precio_vacunapet);

        mfirestore.collection("Compraseditado").document(id).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "Actualizado exitosamente", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error al actualizar", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void postPet(String namepet, String agepet, String colorpet, Double precio_vacunapet) {
        String idUser = mAuth.getCurrentUser().getUid();
        DocumentReference id = mfirestore.collection("Compraseditado").document();

        Map<String, Object> map = new HashMap<>();
        map.put("id_user", idUser);
        map.put("id", id.getId());
        map.put("name", namepet);
        map.put("age", agepet);
        map.put("color", colorpet);
        map.put("vaccine_price", precio_vacunapet);


        mfirestore.collection("Compraseditado").document(id.getId()).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "Creado exitosamente", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error al ingresar", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void getPet(String id){

        String idUser = mAuth.getCurrentUser().getUid();

        mfirestore.collection("Compraseditado").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                DecimalFormat format = new DecimalFormat("0.00");
                String namePet = documentSnapshot.getString("name");
                String agePet = documentSnapshot.getString("age");
                String colorPet = documentSnapshot.getString("color");
                Double precio_vacunapet = documentSnapshot.getDouble("vaccine_price");
                String photoPet = documentSnapshot.getString("photo");


                name.setText(namePet);
                age.setText(agePet);
                color.setText(colorPet);
                precio_vacuna.setText(format.format(precio_vacunapet));




                //-------------------------------------------------------------------------------------















                //--------------------------------------------------------------------------------










                try {
                    if(!photoPet.equals("")){
                        //Toast toast = Toast.makeText(getApplicationContext(), "Cargando foto", Toast.LENGTH_SHORT);
                        //toast.setGravity(Gravity.TOP,0,200);
                        //toast.show();


                        // Picasso.with(CreatePetActivity.this)
                        //        .load(photoPet)
                        //        .resize(150, 150)
                        //        .into(photo_pet);

                        Glide.with(DetalleActivity.this)
                                .load(photoPet)
                                .circleCrop()
                                .error(R.drawable.ic_launcher_background)
                                .into(photo_pet);



                    }

                }catch (Exception e){
                    Log.v("Error", "e: " + e);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error al obtener los datos", Toast.LENGTH_SHORT).show();
            }
        });
    }






    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }


}