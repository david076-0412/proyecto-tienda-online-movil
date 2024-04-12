package com.aplication.appgestionrepartos.administrador;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.IDNA;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import com.aplication.appgestionrepartos.cliente.Quejas_SugerenciasActivity;
import com.aplication.appgestionrepartos.interfaz.InterfazClienteActivity;
import com.aplication.appgestionrepartos.model.InfoQuejas;
import com.aplication.appgestionrepartos.model.UsuarioCargo;
import com.aplication.appgestionrepartos.model.Usuarios;
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
import com.google.rpc.BadRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InfoQuejas_SugerenciasActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{


    ImageView photo_perfil;
    Button btn_enviar, btn_regresar;
    LinearLayout linearLayout_image_btn;


    EditText nombre,apellido,celular,email,direccion,descripcion_tramite, descripcion_fisica;


    private FirebaseFirestore mfirestore;
    private FirebaseAuth mAuth;


    StorageReference storageReference;
    String storage_path = "usuarios/*";

    private static final int COD_SEL_STORAGE = 200;
    private static final int COD_SEL_IMAGE = 300;

    private Uri image_url;
    String photo = "photo";
    String idd;

    ProgressDialog progressDialog;


    private Spinner mSpinnerIncidencia;
    List<InfoQuejas> Incidencias;
    private String itemIncidencia;


    private Spinner mSpinnerDepartamento;

    List<InfoQuejas> Departamento;
    private String itemDepartamento;



    private Spinner mSpinnerFuncionario_Atendio_repartidor;

    List<InfoQuejas> PersonalRepartidor;
    private String itemPersonalRepartidor;


    private Spinner mSpinnerFuncionario_Atendio_administrador;

    List<InfoQuejas> PersonalAdministrador;

    private String itemPersonalAdministrador;





    private String id;






    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_quejas_sugerencias);


        progressDialog = new ProgressDialog(this);

        id = getIntent().getStringExtra("id_qs");


        mfirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        linearLayout_image_btn = findViewById(R.id.images_btn);

        PersonalRepartidor = new ArrayList<>();
        PersonalAdministrador = new ArrayList<>();


        Incidencias = new ArrayList<>();
        Departamento = new ArrayList<>();

        photo_perfil = findViewById(R.id.usuario_photo);


        nombre = findViewById(R.id.nombre);
        nombre.setEnabled(false);


        apellido = findViewById(R.id.apellido);
        apellido.setEnabled(false);


        celular = findViewById(R.id.celular);
        celular.setEnabled(false);


        email= findViewById(R.id.email);
        email.setEnabled(false);


        direccion = findViewById(R.id.direccion);
        direccion.setEnabled(false);




        mSpinnerIncidencia = (Spinner) findViewById(R.id.spn_incidencia);
        mSpinnerIncidencia.setOnItemSelectedListener(this);
        loadIncidencias(id);
        mSpinnerIncidencia.setEnabled(false);



        mSpinnerDepartamento = (Spinner) findViewById(R.id.spn_usuario);
        mSpinnerDepartamento.setOnItemSelectedListener(this);
        mSpinnerDepartamento.setEnabled(false);
        loadDepartamento(id);






        mSpinnerFuncionario_Atendio_repartidor = (Spinner) findViewById(R.id.spn_funcionario_atendio_repartidor);
        mSpinnerFuncionario_Atendio_repartidor.setOnItemSelectedListener(this);
        mSpinnerFuncionario_Atendio_repartidor.setEnabled(false);


        mSpinnerFuncionario_Atendio_administrador = (Spinner) findViewById(R.id.spn_funcionario_atendio_administrador);
        mSpinnerFuncionario_Atendio_administrador.setOnItemSelectedListener(this);
        mSpinnerFuncionario_Atendio_administrador.setEnabled(false);





        mfirestore.collection("Incidencias").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot6) {

                String tipodepartamento = documentSnapshot6.getString("departamento");

                if (tipodepartamento.equals("Repartidor")){


                    loadPersonalRepartidor(id);




                }else if (tipodepartamento.equals("Administrador")){

                    loadPersonalAdministrador(id);




                }





            }
        });










        descripcion_tramite = findViewById(R.id.tramite_experiencia_personal);
        descripcion_tramite.setEnabled(false);


        descripcion_fisica = findViewById(R.id.descripcion_fisica);
        descripcion_fisica.setEnabled(false);




        loadDatos(id);

        btn_regresar = findViewById(R.id.btn_atras);



        btn_regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InfoQuejas_SugerenciasActivity.this, Quejas_Sugerencias_FinalActivity.class));

                overridePendingTransition(R.anim.left_in, R.anim.left_out);

                finish();
            }
        });






    }




    public void loadPersonalRepartidor(String codigoidre){


        mfirestore.collection("Incidencias").document(codigoidre).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot4) {

                String repartidorencargado = documentSnapshot4.getString("nombre").toString();

                PersonalRepartidor.add(new InfoQuejas(codigoidre,repartidorencargado));


                ArrayAdapter<InfoQuejas> arrayAdapterRepartidor = new ArrayAdapter<>(InfoQuejas_SugerenciasActivity.this, android.R.layout.simple_spinner_dropdown_item, PersonalRepartidor);
                mSpinnerFuncionario_Atendio_repartidor.setAdapter(arrayAdapterRepartidor);

            }
        });







    }





    public void loadPersonalAdministrador(String codigoidam){


        mfirestore.collection("Incidencias").document(codigoidam).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot4) {

                String administradorencargado = documentSnapshot4.getString("nombre").toString();

                PersonalAdministrador.add(new InfoQuejas(codigoidam,administradorencargado));

                ArrayAdapter<InfoQuejas> arrayAdapterAdministrador = new ArrayAdapter<>(InfoQuejas_SugerenciasActivity.this, android.R.layout.simple_spinner_dropdown_item, PersonalAdministrador);
                mSpinnerFuncionario_Atendio_administrador.setAdapter(arrayAdapterAdministrador);



            }
        });










    }





    public void loadDepartamento(String codigoidin){


        mfirestore.collection("Incidencias").document(codigoidin).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot5) {

                String tipoDepartamento = documentSnapshot5.getString("departamento").toString();

                Departamento.add(new InfoQuejas(codigoidin,tipoDepartamento));


                ArrayAdapter<InfoQuejas> arrayAdapterDepartamento = new ArrayAdapter<>(InfoQuejas_SugerenciasActivity.this, android.R.layout.simple_spinner_dropdown_item, Departamento);
                mSpinnerDepartamento.setAdapter(arrayAdapterDepartamento);


            }




        });




    }



    public void loadIncidencias(String codigoidin){


        mfirestore.collection("Incidencias").document(codigoidin).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot2) {

                String tipoincidencia = documentSnapshot2.getString("incidencia").toString();

                Incidencias.add(new InfoQuejas(codigoidin,tipoincidencia));


                ArrayAdapter<InfoQuejas> arrayAdapterIncidencias = new ArrayAdapter<>(InfoQuejas_SugerenciasActivity.this, android.R.layout.simple_spinner_dropdown_item, Incidencias);
                mSpinnerIncidencia.setAdapter(arrayAdapterIncidencias);


            }




        });




    }






    public void loadDatos(String id){


        mfirestore.collection("Incidencias").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {



                String nombreDatos = documentSnapshot.getString("cliente");
                String apellidoDatos = documentSnapshot.getString("apellido");
                String celularDatos = documentSnapshot.getString("celular");

                String direccionDatos = documentSnapshot.getString("direccion");
                String emailDatos = documentSnapshot.getString("email");


                String descripcion_tramiteDatos = documentSnapshot.getString("descripcion_tramite");

                String descripcion_fisicaDatos = documentSnapshot.getString("descripcion_fisica");







                nombre.setText(nombreDatos);
                apellido.setText(apellidoDatos);
                celular.setText(celularDatos);

                direccion.setText(direccionDatos);
                email.setText(emailDatos);

                descripcion_tramite.setText(descripcion_tramiteDatos);

                descripcion_fisica.setText(descripcion_fisicaDatos);













            }



        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error al obtener los datos", Toast.LENGTH_SHORT).show();
            }
        });





    }














    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long idt) {

        itemIncidencia = mSpinnerIncidencia.getSelectedItem().toString();
        itemDepartamento = mSpinnerDepartamento.getSelectedItem().toString();


    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }








}