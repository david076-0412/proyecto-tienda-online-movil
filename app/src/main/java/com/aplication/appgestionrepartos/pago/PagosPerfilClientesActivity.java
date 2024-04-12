package com.aplication.appgestionrepartos.pago;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.aplication.appgestionrepartos.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class PagosPerfilClientesActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{


    ImageView perfil;
    Button btn_actualizar, btn_regresar;
    LinearLayout linearLayout_image_btn;
    EditText name, apellido, dni, direccion, email,tarjeta_credito;
    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;

    ProgressDialog progressDialog;

    StorageReference storageReference;
    String storage_path = "usuarios/*";


    private static final int COD_SEL_STORAGE = 200;
    private static final int COD_SEL_IMAGE = 300;

    private Uri image_url;
    String photo = "photo";
    String idd;


    private String idUser;



    private Spinner mSpinnerEstado_Pago;

    private String itemestado_pago;

    String[] Estado_Pago = {"aceptado", "no aceptado"};

    private Spinner mSpinnerPrueba;
    private String itemprueba;


    String[] Prueba = {"si", "no"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagos_perfil_clientes);



        progressDialog = new ProgressDialog(this);
        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        idUser = mAuth.getCurrentUser().getUid();
        storageReference = FirebaseStorage.getInstance().getReference();


        String id = getIntent().getStringExtra("id_ps");



        linearLayout_image_btn = findViewById(R.id.images_btn);


        name = findViewById(R.id.nombre);

        name.setFocusable(false);
        name.setEnabled(false);
        name.setCursorVisible(false);
        name.setKeyListener(null);
        name.setBackgroundColor(Color.TRANSPARENT);


        apellido = findViewById(R.id.apellido);

        apellido.setFocusable(false);
        apellido.setEnabled(false);
        apellido.setCursorVisible(false);
        apellido.setKeyListener(null);
        apellido.setBackgroundColor(Color.TRANSPARENT);


        dni = findViewById(R.id.dni);

        dni.setFocusable(false);
        dni.setEnabled(false);
        dni.setCursorVisible(false);
        dni.setKeyListener(null);
        dni.setBackgroundColor(Color.TRANSPARENT);


        direccion = findViewById(R.id.direccion);

        direccion.setFocusable(false);
        direccion.setEnabled(false);
        direccion.setCursorVisible(false);
        direccion.setKeyListener(null);
        direccion.setBackgroundColor(Color.TRANSPARENT);



        email = findViewById(R.id.correo);

        email.setFocusable(false);
        email.setEnabled(false);
        email.setCursorVisible(false);
        email.setKeyListener(null);
        email.setBackgroundColor(Color.TRANSPARENT);





        tarjeta_credito = findViewById(R.id.tarjeta_credito);





        perfil = findViewById(R.id.perfil_photo);


        mSpinnerEstado_Pago = findViewById(R.id.spnestado_pago);

        mSpinnerEstado_Pago.setOnItemSelectedListener(this);


        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, Estado_Pago);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinnerEstado_Pago.setAdapter(arrayAdapter);


        mSpinnerPrueba = findViewById(R.id.spnprueba);

        mSpinnerPrueba.setOnItemSelectedListener(this);


        ArrayAdapter arrayAdapterPrueba = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, Prueba);
        arrayAdapterPrueba.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinnerPrueba.setAdapter(arrayAdapterPrueba);




        btn_regresar = findViewById(R.id.btn_regresar);

        btn_regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PagosPerfilClientesActivity.this, ClientesRegistradosActivity.class));
                overridePendingTransition(R.anim.left_in, R.anim.left_out);

                finish();
            }
        });


        btn_actualizar = findViewById(R.id.btn_actualizar);



        btn_actualizar.setText("ACTUALIZAR");

        idd=id;

        getPerfil(id);


        btn_actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namePerfil = name.getText().toString().trim();
                String apellidoPerfil = apellido.getText().toString().trim();
                String dniPerfil = dni.getText().toString().trim();
                String direccionPerfil = direccion.getText().toString().trim();
                String emailPerfil = email.getText().toString().trim();
                String tarjetaPerfil = tarjeta_credito.getText().toString().trim();



                if(namePerfil.isEmpty() && apellidoPerfil.isEmpty() && dniPerfil.isEmpty() && direccionPerfil.isEmpty() && emailPerfil.isEmpty() && tarjetaPerfil.isEmpty()){
                   tarjeta_credito.setText("Llenar con tarjeta de credito o numero de cuenta reales");



                }else{
                    updatePerfil(id);
                    startActivity(new Intent(PagosPerfilClientesActivity.this, ClientesRegistradosActivity.class));
                    overridePendingTransition(R.anim.left_in, R.anim.left_out);

                    finish();
                }
            }
        });














    }


    public void tarjeta_credito_sheet(View view){
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                PagosPerfilClientesActivity.this,R.style.BottomSheetDialogTheme
        );
        View bottomSheetView = LayoutInflater.from(PagosPerfilClientesActivity.this)
                .inflate(
                        R.layout.layout_bottom_sheet,
                        (LinearLayout) findViewById(R.id.bottomSheetContainer)
                );
        bottomSheetView.findViewById(R.id.buttonShare).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                EditText tarjeta_credito_sheetRegistro;

                tarjeta_credito_sheetRegistro = bottomSheetView.findViewById(R.id.tarjeta_credito_sheet);


                String tarjeta_registro = tarjeta_credito_sheetRegistro.getText().toString().trim();

                tarjeta_credito.setText(tarjeta_registro);

                //Toast.makeText(RegisterActivity.this, "Share...", Toast.LENGTH_SHORT).show();

            }
        });

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();




    }





    private void uploadPhoto() {
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");

        startActivityForResult(i, COD_SEL_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(resultCode == RESULT_OK){
            if (requestCode == COD_SEL_IMAGE){
                image_url = data.getData();


            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }




    public void updatePerfil(String id){

        Map<String, Object> map = new HashMap<>();

        map.put("estado_pago",itemestado_pago);

        map.put("prueba",itemprueba);

        mFirestore.collection("pago").document(id).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Toast.makeText(getApplicationContext(), "Error al actualizar", Toast.LENGTH_SHORT).show();
            }
        });

        mFirestore.collection("Cliente").document(id).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Toast.makeText(getApplicationContext(), "Error al actualizar", Toast.LENGTH_SHORT).show();
            }
        });


        mFirestore.collection("Usuario/Cliente/"+id).document("registro").update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Toast.makeText(PagosPerfilClientesActivity.this, "Error al guardar", Toast.LENGTH_SHORT).show();
            }
        });









    }



    private void getPerfil(String id){

        mFirestore.collection("Cliente").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String namePer = documentSnapshot.getString("name");
                String apellidoPer = documentSnapshot.getString("apellido");
                String dniPer = documentSnapshot.getString("dni");
                String direccionPer = documentSnapshot.getString("direccion");
                String emailPer = documentSnapshot.getString("email");


                String photoPer = documentSnapshot.getString("photo");


                name.setText(namePer);
                apellido.setText(apellidoPer);
                dni.setText(dniPer);
                direccion.setText(direccionPer);
                email.setText(emailPer);

                String EstadoTarjetaPer = documentSnapshot.getString("estado_pago");
                String PruebaPer = documentSnapshot.getString("prueba");
                String TarjetaPer = documentSnapshot.getString("tarjeta_credito");


                mSpinnerEstado_Pago.setSelection(getIndexEstado(mSpinnerEstado_Pago,EstadoTarjetaPer));
                mSpinnerPrueba.setSelection(getIndexPrueba(mSpinnerPrueba, PruebaPer));

                tarjeta_credito.setText(TarjetaPer);





                try {
                    if(!photoPer.equals("")){
                        //Toast toast = Toast.makeText(getApplicationContext(), "Cargando foto", Toast.LENGTH_SHORT);
                        //toast.setGravity(Gravity.TOP,0,200);
                        //toast.show();


                        // Picasso.with(CreatePetActivity.this)
                        //        .load(photoPet)
                        //        .resize(150, 150)
                        //        .into(photo_pet);

                        Glide.with(PagosPerfilClientesActivity.this)
                                .load(photoPer)
                                .circleCrop()
                                .into(perfil);



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

    private int getIndexEstado(Spinner spinner, String s){

        for (int i=0; i< spinner.getCount(); i++){

            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(s)){
                return i;

            }

        }


        return 0;
    }


    private int getIndexPrueba(Spinner spinner, String s){

        for (int i=0; i< spinner.getCount(); i++){

            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(s)){
                return i;

            }

        }


        return 0;
    }





    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        itemestado_pago = mSpinnerEstado_Pago.getSelectedItem().toString();
        itemprueba = mSpinnerPrueba.getSelectedItem().toString();

    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }





}