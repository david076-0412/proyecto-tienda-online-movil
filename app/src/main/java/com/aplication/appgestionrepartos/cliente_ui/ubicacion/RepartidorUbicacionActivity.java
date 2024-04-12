package com.aplication.appgestionrepartos.cliente_ui.ubicacion;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.aplication.appgestionrepartos.R;
import com.aplication.appgestionrepartos.chat.LoginActivity;
import com.aplication.appgestionrepartos.cliente.PerfilActivity;
import com.aplication.appgestionrepartos.interfaz.InterfazClienteActivity;


import com.aplication.appgestionrepartos.login.OnboardingActivity;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.aplication.appgestionrepartos.databinding.ActivityMapsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class RepartidorUbicacionActivity extends AppCompatActivity implements OnMapReadyCallback {


    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    private Toolbar mToolbar;


    FirebaseFirestore mFirestore;

    FirebaseAuth mAuth;

    String idRepartidor;


    EditText Nombre,Apellido, direccion, celular;


    ImageView photo_Repartidor;

    String idUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repartidor_ubicacion);

        mToolbar = findViewById(R.id.toolbar);
        this.setTitle("");
        setSupportActionBar(mToolbar);

        idRepartidor = getIntent().getStringExtra("idRepartidor");


        mFirestore = FirebaseFirestore.getInstance();

        mAuth = FirebaseAuth.getInstance();

        idUser = mAuth.getCurrentUser().getUid();


        Nombre = findViewById(R.id.nombre);
        Nombre.setEnabled(false);

        Apellido =findViewById(R.id.apellido);
        Apellido.setEnabled(false);



        direccion = findViewById(R.id.direccion);
        direccion.setEnabled(false);


        celular = findViewById(R.id.celular);
        celular.setEnabled(false);



        photo_Repartidor = findViewById(R.id.perfil_photo);



        verificarPermisos();





        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        infoRepartidor(idUser);



    }



    public void infoRepartidor(String idUser){


        mFirestore.collection("Cliente").document(idUser).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String nombreRepartidor = documentSnapshot.getString("repartidor_nombre");

                String apellidoRepartidor = documentSnapshot.getString("repartidor_apellido");


                String celularRepartidor = documentSnapshot.getString("celularrepartidor");


                String direccionRepartidor = documentSnapshot.getString("direccion_repartidor");


                String photoRepartidor = documentSnapshot.getString("photo_repartidor");





                Nombre.setText(nombreRepartidor);

                Apellido.setText(apellidoRepartidor);


                direccion.setText(direccionRepartidor);



                celular.setText(celularRepartidor);



                try {
                    if(!photoRepartidor.equals("")){
                        //Toast toast = Toast.makeText(getApplicationContext(), "Cargando foto", Toast.LENGTH_SHORT);
                        //toast.setGravity(Gravity.TOP,0,200);
                        //toast.show();


                        // Picasso.with(CreatePetActivity.this)
                        //        .load(photoPet)
                        //        .resize(150, 150)
                        //        .into(photo_pet);

                        Glide.with(RepartidorUbicacionActivity.this)
                                .load(photoRepartidor)
                                .circleCrop()
                                .into(photo_Repartidor);



                    }

                }catch (Exception e){
                    Log.v("Error", "e: " + e);
                }











            }
        });



    }





    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }


        mMap.setMyLocationEnabled(true);


        ubicacionRepartidor(googleMap);

        ubicacionCliente(googleMap);




    }




    public void ubicacionCliente(GoogleMap googleMap){



            mFirestore.collection("Cliente").document(idUser).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
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
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(direccionCli, (float) 12));











                }
            });










    }


    public void ubicacionRepartidor(GoogleMap googleMap){

        mFirestore.collection("Repartidor").document(idRepartidor).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {


                double LatitudRepartidor = documentSnapshot.getDouble("latitud");



                double LongitudRepartidor = documentSnapshot.getDouble("longitud");



                String DireccionRepartidor = documentSnapshot.getString("direccion");




                // Add a marker in Sydney and move the camera
                LatLng sydney = new LatLng(LatitudRepartidor, LongitudRepartidor);

                mMap.addMarker(new MarkerOptions()
                                .position(sydney)
                                .title(DireccionRepartidor))
                        .setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));




                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(sydney)
                        .zoom(7)
                        .bearing(90)
                        .tilt(90)
                        .build();


                //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, (float) 20));












            }


        });




    }







    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_carrito_drawer, menu);
        return true;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.volver_inicio:


                startActivity(new Intent(RepartidorUbicacionActivity.this, InterfazClienteActivity.class));
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
                finish();


                return true;


        }


        return super.onOptionsItemSelected(item);
    }






    private void verificarPermisos(){


        int PermisosLocation = ContextCompat.checkSelfPermission(RepartidorUbicacionActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);




        if (PermisosLocation == PackageManager.PERMISSION_GRANTED){

            //Toast.makeText(OnboardingActivity.this, "Permiso SMS Concedido", Toast.LENGTH_SHORT).show();

            //Toast.makeText(OnboardingActivity.this, "Permiso Location Concedido", Toast.LENGTH_SHORT).show();







        }else{

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);

        }


    }










}