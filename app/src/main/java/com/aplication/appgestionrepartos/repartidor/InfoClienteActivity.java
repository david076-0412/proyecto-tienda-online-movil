package com.aplication.appgestionrepartos.repartidor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aplication.appgestionrepartos.R;
import com.aplication.appgestionrepartos.interfaz.InterfazRepartidorActivity;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;



//import com.squareup.picasso.Picasso;


import java.util.HashMap;

public class InfoClienteActivity extends AppCompatActivity implements OnMapReadyCallback{



    private GoogleMap mMap;


    ImageView photo_pet;
    LinearLayout linearLayout_image_btn;
    Button btn_atras;

    ImageView photo_petfirma;
    Button btn_paint_photo, btn_delete_photo, btn_productos_foto,btn_delete_producto;
    LinearLayout linearLayout_image_btnfirma;


    ImageView photo_petproducto;


    TextView nombre, apellido, dni, direccion, email;
    private FirebaseFirestore mfirestore;
    private FirebaseAuth mAuth;

    StorageReference storageReference;
    String storage_path = "usuarios/*";

    private static final int COD_SEL_STORAGE = 200;
    private static final int COD_SEL_IMAGE = 300;

    private Uri image_url;


    String photo = "photo";
    String idd,nombreClien;

    private String idPerfil;
    private String nombrePerfil;

    ProgressDialog progressDialog;

    private String nombreCliente;

    public final static String EXTRA_MESSAGE = "com.aplication.appgestionrepartos.repartidor";


    String id;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_cliente);


        this.setTitle("Usuario");
        progressDialog = new ProgressDialog(this);
        id = getIntent().getStringExtra("id_pet");
        String nameCli = getIntent().getStringExtra("name");



        mfirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();


        nombre = findViewById(R.id.nombre);
        nombre.setEnabled(false);


        apellido = findViewById(R.id.apellido);
        apellido.setEnabled(false);


        dni = findViewById(R.id.dni);
        dni.setEnabled(false);

        direccion = findViewById(R.id.direccion);
        direccion.setEnabled(false);

        email = findViewById(R.id.correo);
        email.setEnabled(false);



        verificarPermisos();



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        linearLayout_image_btn = findViewById(R.id.images_btn);

        photo_pet = findViewById(R.id.pet_photo);

        photo_petfirma = findViewById(R.id.firma_photo);


        photo_petproducto = findViewById(R.id.producto_photo);



        btn_atras = findViewById(R.id.btn_atras);


        linearLayout_image_btnfirma = findViewById(R.id.images_btnfirma);



        btn_paint_photo = findViewById(R.id.btn_photo1);

        btn_delete_photo = findViewById(R.id.btn_remove_photo1);




        btn_productos_foto = findViewById(R.id.btn_photo_producto);

        btn_delete_producto = findViewById(R.id.btn_remove_photo_producto);









        btn_paint_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //startActivity(new Intent(InfoClienteActivity.this, MainPaintActivity.class));


                Intent i = new Intent(InfoClienteActivity.this, PaintActivity.class);

                nombreCliente = nombre.getText().toString();
                i.putExtra("id_firma", nombreCliente);

                i.putExtra("id_pet", id);
                i.putExtra("name", nameCli);


                i.putExtra("cliente","cliente");




                overridePendingTransition(R.anim.left_in, R.anim.left_out);

                startActivity(i);

            }
        });


        btn_delete_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idUser = mAuth.getCurrentUser().getUid();


                HashMap<String, Object> map = new HashMap<>();
                map.put("firma", "");
                mfirestore.collection("firmas").document(nombreClien).update(map);
                //mfirestore.collection("Cliente").document(nombre).update(map);
                Toast.makeText(InfoClienteActivity.this, "Foto eliminada", Toast.LENGTH_SHORT).show();
            }
        });




        btn_productos_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i = new Intent(InfoClienteActivity.this, Productos_FotoActivity.class);

                nombreCliente = nombre.getText().toString();
                i.putExtra("id_producto", nombreCliente);

                i.putExtra("id_pet", id);
                i.putExtra("name", nameCli);



                i.putExtra("cliente","cliente");



                HashMap<String, Object> map = new HashMap<>();
                map.put("nombre",nombreCliente);


                mfirestore.collection("productos").document(nombreCliente).set(map);

                overridePendingTransition(R.anim.left_in, R.anim.left_out);

                startActivity(i);




            }
        });




        btn_delete_producto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String idUser = mAuth.getCurrentUser().getUid();


                HashMap<String, Object> map = new HashMap<>();
                map.put("producto", "");
                mfirestore.collection("productos").document(nombreClien).update(map);
                //mfirestore.collection("Cliente").document(nombre).update(map);
                Toast.makeText(InfoClienteActivity.this, "Foto eliminada", Toast.LENGTH_SHORT).show();




            }
        });




        if (id == null || id == "") {


            btn_atras.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String nombreRe = nombre.getText().toString().trim();
                    String apellidoRe = apellido.getText().toString().trim();
                    String dniRe = dni.getText().toString().trim();
                    String direccionRe = direccion.getText().toString().trim();



                    if (nombreRe.isEmpty() && apellidoRe.isEmpty() && dniRe.isEmpty() && direccionRe.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Datos no obtenidos", Toast.LENGTH_SHORT).show();


                    } else {
                        startActivity(new Intent(InfoClienteActivity.this, InterfazRepartidorActivity.class));
                        overridePendingTransition(R.anim.left_in, R.anim.left_out);

                        finish();
                    }


                }
            });

        } else {

            idd = id;
            btn_atras.setText("REGRESAR");
            getPetPhoto(id);
            nombreClien = nameCli;
            getPetFirma(nameCli);
            getPetProductos_Fotos(nameCli);





            btn_atras.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String nombreRe = nombre.getText().toString().trim();
                    String apellidoRe = apellido.getText().toString().trim();
                    String dniRe = dni.getText().toString().trim();
                    String direccionRe = direccion.getText().toString().trim();



                    if (nombreRe.isEmpty() && apellidoRe.isEmpty() && dniRe.isEmpty() && direccionRe.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Datos no obtenidos", Toast.LENGTH_SHORT).show();


                    } else {
                        startActivity(new Intent(InfoClienteActivity.this, InterfazRepartidorActivity.class));
                        overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        finish();

                    }


                }
            });

        }

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }


        //mMap.setMyLocationEnabled(true);




        ubicacionCliente(googleMap);




    }






    public void ubicacionCliente(GoogleMap googleMap){



        mfirestore.collection("Cliente").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot2) {


                double latitudCliente = documentSnapshot2.getDouble("latitud");

                double longitudCliente = documentSnapshot2.getDouble("longitud");


                String direccionCliente = documentSnapshot2.getString("direccion");





                LatLng direccionCli = new LatLng(latitudCliente, longitudCliente);


                mMap.addMarker(new MarkerOptions()
                                .position(direccionCli)
                                .title(direccionCliente))
                        .setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));


                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(direccionCli)
                        .zoom(7)
                        .bearing(90)
                        .tilt(90)
                        .build();


                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                mMap.moveCamera(CameraUpdateFactory.newLatLng(direccionCli));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(direccionCli, (float) 18));











            }
        });










    }









    private void getPetPhoto(String id){

        mfirestore.collection("Cliente").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String nombreRe = documentSnapshot.getString("name");
                String apellidoRe = documentSnapshot.getString("apellido");
                String dniRe = documentSnapshot.getString("dni");
                String direccionRe = documentSnapshot.getString("direccion");
                String emailRe = documentSnapshot.getString("email");


                String photoRe = documentSnapshot.getString("photo");


                nombre.setText(nombreRe);
                apellido.setText(apellidoRe);
                dni.setText(dniRe);
                direccion.setText(direccionRe);
                email.setText(emailRe);

                try {
                    if(!photoRe.equals("")){
                        Toast toast = Toast.makeText(getApplicationContext(), "Cargando foto", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP,0,200);
                        toast.show();


                        // Picasso.with(CreatePetActivity.this)
                        //        .load(photoPet)
                        //        .resize(150, 150)
                        //        .into(photo_pet);

                        Glide.with(InfoClienteActivity.this)
                                .load(photoRe)
                                .circleCrop()
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






    private void getPetFirma(String nombreClien){

        mfirestore.collection("firmas").document(nombreClien).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String firmaRe = documentSnapshot.getString("firma");


                try {

                    if(!firmaRe.equals("")){
                        //Toast toast = Toast.makeText(getApplicationContext(), "Cargando foto", Toast.LENGTH_SHORT);
                        //toast.setGravity(Gravity.TOP,0,200);
                        //toast.show();


                        Glide.with(InfoClienteActivity.this)
                                .load(firmaRe)
                                .circleCrop()
                                .into(photo_petfirma);



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












    private void getPetProductos_Fotos(String nombreClien){

        mfirestore.collection("productos").document(nombreClien).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String productoRe = documentSnapshot.getString("producto");


                try {

                    if(!productoRe.equals("")){
                        //Toast toast = Toast.makeText(getApplicationContext(), "Cargando foto", Toast.LENGTH_SHORT);
                        //toast.setGravity(Gravity.TOP,0,200);
                        //toast.show();


                        Glide.with(InfoClienteActivity.this)
                                .load(productoRe)
                                .circleCrop()
                                .into(photo_petproducto);



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




    private void verificarPermisos(){


        int PermisosLocation = ContextCompat.checkSelfPermission(InfoClienteActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);




        if (PermisosLocation == PackageManager.PERMISSION_GRANTED){

            //Toast.makeText(OnboardingActivity.this, "Permiso SMS Concedido", Toast.LENGTH_SHORT).show();

            //Toast.makeText(OnboardingActivity.this, "Permiso Location Concedido", Toast.LENGTH_SHORT).show();







        }else{

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);

        }


    }











}