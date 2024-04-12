package com.aplication.appgestionrepartos.administrador_ui.pagos;

import androidx.annotation.NonNull;
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
import android.widget.Toast;

import com.aplication.appgestionrepartos.R;
import com.aplication.appgestionrepartos.interfaz.InterfazAdministradorActivity;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DetallesClienteActivity extends AppCompatActivity {


    ImageView perfil;
    Button btn_regresar;

    EditText name, apellido, dni, direccion, email, celular, tarjeta;
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




    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_cliente);



        this.setTitle("PERFIL");
        progressDialog = new ProgressDialog(this);
        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        idUser = mAuth.getCurrentUser().getUid();
        storageReference = FirebaseStorage.getInstance().getReference();


        String id = getIntent().getStringExtra("id_pago_cliente");



        name = findViewById(R.id.nombre);

        name.setEnabled(false);


        apellido = findViewById(R.id.apellido);
        apellido.setEnabled(false);

        dni = findViewById(R.id.dni);
        dni.setEnabled(false);

        direccion = findViewById(R.id.direccion);
        direccion.setEnabled(false);

        email = findViewById(R.id.correo);
        email.setEnabled(false);

        celular = findViewById(R.id.celular);
        celular.setEnabled(false);


        tarjeta = findViewById(R.id.tarjeta_credito);
        tarjeta.setEnabled(false);



        perfil = findViewById(R.id.perfil_photo);





        btn_regresar = findViewById(R.id.btn_regresar);

        btn_regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetallesClienteActivity.this, InterfazAdministradorActivity.class));
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
                finish();
            }
        });


        idd=id;

        getPerfil(idd);








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

                String celularPer = documentSnapshot.getString("celular");



                String tarjetaPer = documentSnapshot.getString("tarjeta_credito");


                String photoPer = documentSnapshot.getString("photo");


                name.setText(namePer);
                apellido.setText(apellidoPer);
                dni.setText(dniPer);
                direccion.setText(direccionPer);
                email.setText(emailPer);

                celular.setText(celularPer);

                tarjeta.setText(tarjetaPer);



                try {
                    if(!photoPer.equals("")){
                        Toast toast = Toast.makeText(getApplicationContext(), "Cargando foto", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP,0,200);
                        toast.show();


                        // Picasso.with(CreatePetActivity.this)
                        //        .load(photoPet)
                        //        .resize(150, 150)
                        //        .into(photo_pet);

                        Glide.with(DetallesClienteActivity.this)
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










}