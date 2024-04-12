package com.aplication.appgestionrepartos.cliente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
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
//import com.squareup.picasso.Picasso;


import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class AgregarCompraActivity extends AppCompatActivity {

    ImageView photo_pet;
    Button btn_add,btn_regresar;

    LinearLayout linearLayout_image_btn;
    TextView name, color, precio_vacuna,photocod;


    NumberPicker age;


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


    private int agepet;

    private int iaget;


    String idUs;


    double cantidad_producto;

    double cantidad_productogg;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_compra);

        this.setTitle("AGREGAR PEDIDO");
        progressDialog = new ProgressDialog(this);
        String id = getIntent().getStringExtra("id_pet");
        mfirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        linearLayout_image_btn = findViewById(R.id.images_btn);
        name = findViewById(R.id.nombre);


        age = (NumberPicker) findViewById(R.id.edad);


        NumberPickerCantidad(id);

        idUs = mAuth.getCurrentUser().getUid();


        color = findViewById(R.id.color);
        precio_vacuna = findViewById(R.id.precio_vacuna);
        photo_pet = findViewById(R.id.pet_photo);
        photocod = findViewById(R.id.codphoto);


        btn_add = findViewById(R.id.btn_anadir);
        btn_regresar = findViewById(R.id.btn_regresar);

        btn_regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(AgregarCompraActivity.this, InterfazClienteActivity.class));
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
                finish();




            }
        });




        if (id == null || id == ""){
            linearLayout_image_btn.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "Datos no encontrados", Toast.LENGTH_SHORT).show();


        }else{

            idd = id;
            btn_add.setText("Añadir");
            getPet(id);

            btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String namepet = name.getText().toString().trim();
                    String colorpet = color.getText().toString().trim();
                    Double precio_vacunapet = Double.parseDouble(precio_vacuna.getText().toString().trim());
                    String photopet_cod = photocod.getText().toString().trim();


                    if (namepet.isEmpty() && colorpet.isEmpty()) {

                        Toast.makeText(getApplicationContext(), "Datos no obtenidos", Toast.LENGTH_SHORT).show();


                    } else {



                        if (agepet == 0){



                            Toast.makeText(getApplicationContext(), "El producto se a agotado", Toast.LENGTH_SHORT).show();





                        }else{



                            String idUs = mAuth.getCurrentUser().getUid();




                            mfirestore.collection("acumulador/"+idUs+"/Cliente").document("pedido").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {



                                    cantidad_producto = documentSnapshot.getDouble("producto");


                                    cantidad_productogg = cantidad_producto + 1;



                                    Map<String, Object> mapE = new HashMap<>();
                                    mapE.put("producto", cantidad_productogg);
                                    mapE.put("producto", cantidad_productogg);


                                    mfirestore.collection("acumulador/"+idUs+"/Cliente").document("pedido").update(mapE);




                                    if (cantidad_productogg <= 1){


                                        postPet(namepet, agepet, colorpet, precio_vacunapet, photopet_cod);




                                    }else if (cantidad_productogg >= 2){


                                        postPro(namepet, agepet, colorpet, precio_vacunapet, photopet_cod);





                                    }







                                }
                            });






                            updateComprasEditado(agepet, id);

                            Map<String, Object> mapAA = new HashMap<>();
                            mapAA.put("cantidadPro", agepet);

                            mfirestore.collection("Compraseditado").document(id).update(mapAA);






                            try {



                                mfirestore.collection("Cliente").document(idUs).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot5) {

                                        String idRepartidor = documentSnapshot5.getString("id_repartidor");

                                        String nombreRepartidor = documentSnapshot5.getString("repartidor_nombre");

                                        String apellidoRepartidor = documentSnapshot5.getString("repartidor_apellido");

                                        String celularRepartidor = documentSnapshot5.getString("celularrepartidor");



                                        if (idRepartidor.isEmpty() && nombreRepartidor.isEmpty() && apellidoRepartidor.isEmpty() && celularRepartidor.isEmpty()){

                                            startActivity(new Intent(AgregarCompraActivity.this, Lista_RepartidoresActivity.class));

                                            overridePendingTransition(R.anim.left_in, R.anim.left_out);

                                            finish();



                                        }else if (!idRepartidor.isEmpty() && nombreRepartidor.isEmpty() && apellidoRepartidor.isEmpty() && celularRepartidor.isEmpty()){

                                            startActivity(new Intent(AgregarCompraActivity.this, Lista_RepartidoresActivity.class));

                                            overridePendingTransition(R.anim.left_in, R.anim.left_out);

                                            finish();



                                        }else if (!idRepartidor.isEmpty() && !nombreRepartidor.isEmpty() && apellidoRepartidor.isEmpty() && celularRepartidor.isEmpty()){

                                            startActivity(new Intent(AgregarCompraActivity.this, Lista_RepartidoresActivity.class));

                                            overridePendingTransition(R.anim.left_in, R.anim.left_out);

                                            finish();



                                        }else if (!idRepartidor.isEmpty() && !nombreRepartidor.isEmpty() && !apellidoRepartidor.isEmpty() && celularRepartidor.isEmpty()){

                                            startActivity(new Intent(AgregarCompraActivity.this, Lista_RepartidoresActivity.class));

                                            overridePendingTransition(R.anim.left_in, R.anim.left_out);

                                            finish();



                                        }else if (!idRepartidor.isEmpty() && !nombreRepartidor.isEmpty() && !apellidoRepartidor.isEmpty() && !celularRepartidor.isEmpty()){


                                            Intent i = new Intent(AgregarCompraActivity.this, CarritoActivity.class);

                                            i.putExtra("id_pet", id);
                                            i.putExtra("id_pro",id);

                                            startActivity(i);

                                            overridePendingTransition(R.anim.left_in, R.anim.left_out);

                                            finish();




                                        }







                                    }
                                });

                            }catch(IndexOutOfBoundsException e){

                                e.printStackTrace();


                            }






                        }











                    }

                }
            });




        }

    }


    
    public void NumberPickerCantidad(String id){


        
        mfirestore.collection("Compraseditado").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {


                String cantidadPR = documentSnapshot.getString("age");


                int agepetcantidad = Integer.parseInt(cantidadPR);

                age.setMinValue(0);
                age.setMaxValue(agepetcantidad);



            }
        });
        




        age.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                //agepet = Integer.parseInt(age.getText().toString().trim());
                agepet = newVal;


            }
        });
        
        
        
        
    }



    public void galeriadetalles(View view){


        String namepet = name.getText().toString().trim();

        //Toast.makeText(AgregarCompraActivity.this, namepet, Toast.LENGTH_SHORT).show();


        Intent intent = new Intent(AgregarCompraActivity.this,ImagenProductosActivity.class);
        intent.putExtra("id_pet",idd);
        intent.putExtra("nombre", namepet);

        overridePendingTransition(R.anim.left_in, R.anim.left_out);

        startActivity(intent);




        //Toast.makeText(AgregarCompraActivity.this, idd, Toast.LENGTH_SHORT).show();



    }


    private void postPro(String namepet, int agepet, String colorpet, Double precio_vacunapet, String photopet_cod ) {


        String idUser = mAuth.getCurrentUser().getUid();
        DocumentReference id = mfirestore.collection("carrito/"+idUser+"/pedido").document();


        mfirestore.collection("acumulador/"+idUser+"/Cliente").document("pedido").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                double codigoPP=documentSnapshot.getDouble("codigoP");

                int codigoPL = (int) codigoPP;

                int codigoPR = codigoPL+1;

                String codigoP = "C00"+String.valueOf(codigoPR);




                Map<String, Object> mapC = new HashMap<>();
                mapC.put("id_user", idUser);
                mapC.put("id", id.getId());
                mapC.put("name", namepet);
                mapC.put("age", agepet);
                mapC.put("color", colorpet);
                mapC.put("codigo", codigoP);


                DecimalFormat format = new DecimalFormat("0.00");



                Double total_precio_vacunapet = Double.valueOf(format.format(precio_vacunapet * agepet));









                mapC.put("vaccine_price", total_precio_vacunapet);
                mapC.put("photo", photopet_cod);










                mfirestore.collection("compras/"+idUser+"/"+codigoP).document(namepet).set(mapC).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {



                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error al ingresar", Toast.LENGTH_SHORT).show();
                    }
                });



                mfirestore.collection("compras/"+idUser+"/"+codigoP).document(namepet).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        Double cant_productos = documentSnapshot.getDouble("cant_producto");

                        mfirestore.collection("acumulador/"+idUser+"/Cliente").document("pedido").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {


                                double conte_productos = documentSnapshot.getDouble("cont_productos");


                                int conteo_productosyy = (int) conte_productos;



                                Map<String, Object> mapPA = new HashMap<>();

                                mapPA.put("cont_productos", conteo_productosyy);



                                mfirestore.collection("acumulador/"+idUser+"/Cliente").document("pedido").update(mapPA).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {


                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), "Error al ingresar", Toast.LENGTH_SHORT).show();
                                    }
                                });




                            }
                        });














                    }
                });





            }
        });








        Map<String, Object> map = new HashMap<>();
        map.put("id_user", idUser);
        map.put("id", id.getId());
        map.put("name", namepet);
        map.put("age", agepet);
        map.put("color", colorpet);

        Double total_precio_vacunapet = precio_vacunapet * agepet;
        map.put("vaccine_price", total_precio_vacunapet);
        map.put("photo", photopet_cod);








        mfirestore.collection("carrito/"+idUser+"/pedido").document(namepet).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error al ingresar", Toast.LENGTH_SHORT).show();
            }
        });







        mfirestore.collection("pedido/"+idUser+"/cliente").document(namepet).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error al ingresar", Toast.LENGTH_SHORT).show();
            }
        });














    }








    private void postPet(String namepet, int agepet, String colorpet, Double precio_vacunapet, String photopet_cod ) {


        String idUser = mAuth.getCurrentUser().getUid();
        DocumentReference id = mfirestore.collection("carrito/"+idUser+"/pedido").document();


        mfirestore.collection("acumulador/"+idUser+"/Cliente").document("pedido").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                double codigoPP=documentSnapshot.getDouble("codigoP");

                int codigoPL = (int) codigoPP;

                int codigoPR = codigoPL+1;

                String codigoP = "C00"+String.valueOf(codigoPR);




                Map<String, Object> mapC = new HashMap<>();
                mapC.put("id_user", idUser);
                mapC.put("id", id.getId());
                mapC.put("name", namepet);
                mapC.put("age", agepet);
                mapC.put("color", colorpet);
                mapC.put("codigo", codigoP);


                DecimalFormat format = new DecimalFormat("0.00");



                Double total_precio_vacunapet = Double.valueOf(format.format(precio_vacunapet * agepet));









                mapC.put("vaccine_price", total_precio_vacunapet);
                mapC.put("photo", photopet_cod);










                mfirestore.collection("compras/"+idUser+"/"+codigoP).document(namepet).set(mapC).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {



                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error al ingresar", Toast.LENGTH_SHORT).show();
                    }
                });



                mfirestore.collection("compras/"+idUser+"/"+codigoP).document(namepet).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        Double cant_productos = documentSnapshot.getDouble("cant_producto");

                        mfirestore.collection("acumulador/"+idUser+"/Cliente").document("pedido").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {


                                double conte_productos = documentSnapshot.getDouble("cont_productos");


                                int conteo_productosyy = (int) conte_productos + 1;



                                Map<String, Object> mapPA = new HashMap<>();

                                mapPA.put("cont_productos", conteo_productosyy);



                                mfirestore.collection("acumulador/"+idUser+"/Cliente").document("pedido").update(mapPA).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {


                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), "Error al ingresar", Toast.LENGTH_SHORT).show();
                                    }
                                });




                            }
                        });














                    }
                });





            }
        });








        Map<String, Object> map = new HashMap<>();
        map.put("id_user", idUser);
        map.put("id", id.getId());
        map.put("name", namepet);
        map.put("age", agepet);
        map.put("color", colorpet);

        Double total_precio_vacunapet = precio_vacunapet * agepet;
        map.put("vaccine_price", total_precio_vacunapet);
        map.put("photo", photopet_cod);








        mfirestore.collection("carrito/"+idUser+"/pedido").document(namepet).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error al ingresar", Toast.LENGTH_SHORT).show();
            }
        });







        mfirestore.collection("pedido/"+idUser+"/cliente").document(namepet).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error al ingresar", Toast.LENGTH_SHORT).show();
            }
        });














    }






    private void updateComprasEditado(int agepet, String id ) {




        mfirestore.collection("Compraseditado").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                Map<String, Object> mapCA = new HashMap<>();




                //-----------------------------------------------------------

                String cantidadproducto = documentSnapshot.getString("age");

                int cantidadproductopda = Integer.parseInt(cantidadproducto);

                int agepetCA = cantidadproductopda - agepet;

                String agepetCE = Integer.toString(agepetCA);

                //mapCA.put("age", agepetCE);
                mapCA.put("cantidadR", agepetCA);

                //-----------------------------------------------------------


                mfirestore.collection("Compraseditado").document(id).update(mapCA).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {



                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error al actualizar", Toast.LENGTH_SHORT).show();
                    }
                });




            }
        });








    }







    private void getPet(String id){
        mfirestore.collection("Compraseditado").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                DecimalFormat format = new DecimalFormat("0.00");
                String namePet = documentSnapshot.getString("name");
                //int agePet = documentSnapshot.getLong("age");
                String colorPet = documentSnapshot.getString("color");
                Double precio_vacunapet = documentSnapshot.getDouble("vaccine_price");
                String photoPet = documentSnapshot.getString("photo");
                String photoPet_cod = documentSnapshot.getString("photo");


                name.setText(namePet);
                //age.setText(agePet);
                color.setText(colorPet);
                precio_vacuna.setText(format.format(precio_vacunapet));
                photocod.setText(photoPet_cod);

                try {
                    if(!photoPet.equals("")){
                        //Toast toast = Toast.makeText(getApplicationContext(), "Cargando foto", Toast.LENGTH_SHORT);
                        //toast.setGravity(Gravity.TOP,0,200);
                        //toast.show();


                        // Picasso.with(CreatePetActivity.this)
                        //        .load(photoPet)
                        //        .resize(150, 150)
                        //        .into(photo_pet);

                        Glide.with(AgregarCompraActivity.this)
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