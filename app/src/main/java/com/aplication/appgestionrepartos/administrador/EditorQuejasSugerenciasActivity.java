package com.aplication.appgestionrepartos.administrador;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.aplication.appgestionrepartos.model.InfoQuejas;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditorQuejasSugerenciasActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

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
    private String itemIncidencia;
    String[] incidencias = {"Quejas", "Sugerencias"};
    List<InfoQuejas> Incidencias;

    private Spinner mSpinnerDepartamento;
    private String itemDepartamento;
    String[] departamentos = {"Repartidor", "Administrador"};
    List<InfoQuejas> Departamento;



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
        setContentView(R.layout.activity_editor_quejas_sugerencias);



        progressDialog = new ProgressDialog(this);

        id = getIntent().getStringExtra("id_user");


        mfirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        linearLayout_image_btn = findViewById(R.id.images_btn);


        Incidencias = new ArrayList<>();
        Departamento = new ArrayList<>();

        PersonalRepartidor = new ArrayList<>();
        PersonalAdministrador = new ArrayList<>();

        photo_perfil = findViewById(R.id.usuario_photo);

        nombre = findViewById(R.id.nombre);

        apellido = findViewById(R.id.apellido);

        celular = findViewById(R.id.celular);

        email= findViewById(R.id.email);

        direccion = findViewById(R.id.direccion);




        mSpinnerIncidencia = (Spinner) findViewById(R.id.spn_incidencia);
        mSpinnerIncidencia.setOnItemSelectedListener(this);


        ArrayAdapter arrayAdapterIncidencia = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, incidencias);
        arrayAdapterIncidencia.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinnerIncidencia.setAdapter(arrayAdapterIncidencia);


        mSpinnerFuncionario_Atendio_repartidor = (Spinner) findViewById(R.id.spn_funcionario_atendio_repartidor);
        mSpinnerFuncionario_Atendio_repartidor.setOnItemSelectedListener(this);
        loadPersonalRepartidor(id);


        mSpinnerFuncionario_Atendio_administrador = (Spinner) findViewById(R.id.spn_funcionario_atendio_administrador);

        mSpinnerFuncionario_Atendio_administrador.setOnItemSelectedListener(this);

        loadPersonalAdministrador(id);



        mfirestore.collection("Incidencias").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

               String DepartamentoIn = documentSnapshot.getString("departamento");


               if (DepartamentoIn.equals("Repartidor")){

                   mSpinnerFuncionario_Atendio_repartidor.setEnabled(true);
                   mSpinnerFuncionario_Atendio_administrador.setEnabled(false);


               }else if (DepartamentoIn.equals("Administrador")){
                   mSpinnerFuncionario_Atendio_administrador.setEnabled(true);
                   mSpinnerFuncionario_Atendio_repartidor.setEnabled(false);








               }




            }
        });






        mSpinnerDepartamento = (Spinner) findViewById(R.id.spn_usuario);
        mSpinnerDepartamento.setOnItemSelectedListener(this);


        ArrayAdapter arrayAdapterDepartamento = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, departamentos);
        arrayAdapterDepartamento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinnerDepartamento.setAdapter(arrayAdapterDepartamento);











        descripcion_tramite = findViewById(R.id.tramite_experiencia_personal);

        descripcion_fisica = findViewById(R.id.descripcion_fisica);





        loadDatos(id);


        btn_enviar = findViewById(R.id.btn_enviar);





        if (id == null || id == ""){

            linearLayout_image_btn.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "Datos no encontrados", Toast.LENGTH_SHORT).show();


        }else{


            btn_enviar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String nombreus = nombre.getText().toString().trim();
                    String apellidous = apellido.getText().toString().trim();
                    String celularus = celular.getText().toString().trim();
                    String emailus = email.getText().toString().trim();
                    String direccionus = direccion.getText().toString().trim();
                    String descripcion_tramite_experienciaus = descripcion_tramite.getText().toString().trim();
                    String descripcion_fisicaus = descripcion_fisica.getText().toString().trim();


                    if (nombreus.isEmpty() && apellidous.isEmpty() && celularus.isEmpty() && emailus.isEmpty() && direccionus.isEmpty() && descripcion_tramite_experienciaus.isEmpty() && descripcion_fisicaus.isEmpty()) {

                        Toast.makeText(getApplicationContext(), "Datos no obtenidos", Toast.LENGTH_SHORT).show();

                        nombre.setError("Este campo esta vacio");
                        apellido.setError("Este campo esta vacio");
                        celular.setError("Este campo esta vacio");
                        email.setError("Este campo esta vacio");
                        direccion.setError("La direccion debe existir");
                        descripcion_tramite.setError("Debe ingresar una descripcion del tramite");
                        descripcion_fisica.setError("Debe ingresar una descripcion fisica del repartidor");





                    } else if (nombreus.isEmpty()){

                        nombre.setError("Este campo esta vacio");



                    }else if (apellidous.isEmpty()){
                        apellido.setError("Este campo esta vacio");

                    }else if (celularus.isEmpty()){
                        celular.setError("Este campo esta vacio");

                    }else if (emailus.isEmpty()){
                        email.setError("Este campo esta vacio");

                    }else if (direccionus.isEmpty()){
                        direccion.setError("La direccion debe existir");

                    }else if (descripcion_tramite_experienciaus.isEmpty() && descripcion_fisicaus.isEmpty()){
                        descripcion_tramite.setError("Debe ingresar una descripcion del tramite");
                        descripcion_fisica.setError("Debe ingresar una descripcion fisica del repartidor");


                    }else if(descripcion_tramite_experienciaus.isEmpty()){
                        descripcion_tramite.setError("Debe ingresar una descripcion del tramite");


                    }else if (descripcion_fisicaus.isEmpty()){

                        descripcion_fisica.setError("Debe ingresar una descripcion fisica del repartidor");

                    }else{





                        if (itemDepartamento == "Repartidor"){

                            UpdateUsRepartidor(nombreus, apellidous, celularus,emailus,direccionus,descripcion_tramite_experienciaus,descripcion_fisicaus, id);

                            startActivity(new Intent(EditorQuejasSugerenciasActivity.this, Quejas_Sugerencias_FinalActivity.class));
                            overridePendingTransition(R.anim.left_in, R.anim.left_out);
                            finish();




                        }

                        if (itemDepartamento == "Administrador"){

                            UpdateUsAdministrador(nombreus, apellidous, celularus,emailus,direccionus,descripcion_tramite_experienciaus,descripcion_fisicaus, id);

                            startActivity(new Intent(EditorQuejasSugerenciasActivity.this, Quejas_Sugerencias_FinalActivity.class));

                            overridePendingTransition(R.anim.left_in, R.anim.left_out);

                            finish();


                        }










                    }
















                }
            });




        }



        btn_regresar = findViewById(R.id.btn_atras);



        btn_regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditorQuejasSugerenciasActivity.this, Quejas_Sugerencias_FinalActivity.class));

                overridePendingTransition(R.anim.left_in, R.anim.left_out);

                finish();
            }
        });















    }


    public void loadPersonalRepartidor(String codigoidre){


        mfirestore.collection("Repartidor").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot snapshot2 : task.getResult()){

                    String repartidorencargado = snapshot2.getString("name").toString();

                    PersonalRepartidor.add(new InfoQuejas(codigoidre,repartidorencargado));
                }


                ArrayAdapter<InfoQuejas> arrayAdapterRepartidor = new ArrayAdapter<>(EditorQuejasSugerenciasActivity.this, android.R.layout.simple_spinner_dropdown_item, PersonalRepartidor);
                mSpinnerFuncionario_Atendio_repartidor.setAdapter(arrayAdapterRepartidor);

            }
        });






    }





    public void loadPersonalAdministrador(String codigoidam){


        mfirestore.collection("Administrador").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot snapshot : task.getResult()){

                    String administradorencargado = snapshot.getString("name").toString();

                    PersonalAdministrador.add(new InfoQuejas(codigoidam,administradorencargado));

                }


                ArrayAdapter<InfoQuejas> arrayAdapterAdministrador = new ArrayAdapter<>(EditorQuejasSugerenciasActivity.this, android.R.layout.simple_spinner_dropdown_item, PersonalAdministrador);
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


                ArrayAdapter<InfoQuejas> arrayAdapterDepartamento = new ArrayAdapter<>(EditorQuejasSugerenciasActivity.this, android.R.layout.simple_spinner_dropdown_item, Departamento);
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


                ArrayAdapter<InfoQuejas> arrayAdapterIncidencias = new ArrayAdapter<>(EditorQuejasSugerenciasActivity.this, android.R.layout.simple_spinner_dropdown_item, Incidencias);
                mSpinnerIncidencia.setAdapter(arrayAdapterIncidencias);


            }




        });




    }









    private void UpdateUsRepartidor(String nombreus, String apellidous, String celularus, String emailus, String direccionus, String descripcion_tramite_experienciaus, String descripcion_fisicaus, String id) {

        progressDialog.setMessage("Actualizando...");
        progressDialog.show();



        Map<String, Object> map = new HashMap<>();

        map.put("cliente", nombreus);
        map.put("apellido", apellidous);
        map.put("celular", celularus);

        map.put("email", emailus);
        map.put("direccion", direccionus);


        map.put("nombre", itemPersonalRepartidor);


        map.put("incidencia", itemIncidencia);
        map.put("departamento", "Repartidor");


        map.put("descripcion_tramite", descripcion_tramite_experienciaus);
        map.put("descripcion_fisica", descripcion_fisicaus);







        mfirestore.collection("Incidencias").document(id).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), itemIncidencia+" Actualizada", Toast.LENGTH_SHORT).show();
                finish();



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error al ingresar", Toast.LENGTH_SHORT).show();
            }
        });






    }




    private void UpdateUsAdministrador(String nombreus, String apellidous, String celularus, String emailus, String direccionus, String descripcion_tramite_experienciaus, String descripcion_fisicaus, String id) {

        progressDialog.setMessage("Actualizando...");
        progressDialog.show();




        Map<String, Object> map = new HashMap<>();

        map.put("cliente", nombreus);
        map.put("apellido", apellidous);
        map.put("celular", celularus);

        map.put("email", emailus);
        map.put("direccion", direccionus);


        map.put("nombre", itemPersonalAdministrador);


        map.put("incidencia", itemIncidencia);
        map.put("departamento", "Administrador");


        map.put("descripcion_tramite", descripcion_tramite_experienciaus);
        map.put("descripcion_fisica", descripcion_fisicaus);







        mfirestore.collection("Incidencias").document(id).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), itemIncidencia+" Actualizada", Toast.LENGTH_SHORT).show();
                finish();



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error al ingresar", Toast.LENGTH_SHORT).show();
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



                String incidenciaPet = documentSnapshot.getString("incidencia");


                mSpinnerIncidencia.setSelection(getIndex(mSpinnerIncidencia,incidenciaPet));


                String departamentoPet = documentSnapshot.getString("departamento");

                mSpinnerDepartamento.setSelection(getIndex(mSpinnerDepartamento, departamentoPet));



                String personalRepartidorPet = documentSnapshot.getString("nombre");


                mSpinnerFuncionario_Atendio_repartidor.setSelection(getIndex(mSpinnerFuncionario_Atendio_repartidor, personalRepartidorPet));






                String personalAdministradorPet = documentSnapshot.getString("nombre");

                mSpinnerFuncionario_Atendio_administrador.setSelection(getIndex(mSpinnerFuncionario_Atendio_administrador, personalAdministradorPet));










            }



        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error al obtener los datos", Toast.LENGTH_SHORT).show();
            }
        });





    }



    private int getIndex(Spinner spinner, String s){

        for (int i=0; i< spinner.getCount(); i++){

            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(s)){
                return i;

            }

        }


        return 0;
    }






    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        itemIncidencia = mSpinnerIncidencia.getSelectedItem().toString();
        itemDepartamento = mSpinnerDepartamento.getSelectedItem().toString();









        if (itemDepartamento == "Repartidor"){

            mSpinnerFuncionario_Atendio_administrador.setEnabled(false);


            mSpinnerFuncionario_Atendio_repartidor.setEnabled(true);

            mfirestore.collection("Repartidor").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (QueryDocumentSnapshot snapshot5 : task.getResult()){
                        String codigoidre = snapshot5.getId();
                        String nombrerepartidor = snapshot5.getString("name").toString();
                        String apellidorepartidor = snapshot5.getString("apellido").toString();


                        itemPersonalRepartidor = mSpinnerFuncionario_Atendio_repartidor.getSelectedItem().toString();



                    }



                }



            });





        } else if (itemDepartamento == "Administrador"){

            mSpinnerFuncionario_Atendio_administrador.setEnabled(true);


            mSpinnerFuncionario_Atendio_repartidor.setEnabled(false);

            mfirestore.collection("Administrador").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (QueryDocumentSnapshot snapshot7 : task.getResult()){
                        String codigoidad = snapshot7.getId();
                        String nombreadministrador = snapshot7.getString("name").toString();
                        String apellidoadministrador = snapshot7.getString("apellido").toString();

                        itemPersonalAdministrador = mSpinnerFuncionario_Atendio_administrador.getSelectedItem().toString();

                    }



                }

            });



        }









    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }













}