package com.aplication.appgestionrepartos.cliente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
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
import com.aplication.appgestionrepartos.interfaz.InterfazClienteActivity;
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

public class Quejas_SugerenciasActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

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


    private Spinner mSpinnerDepartamento;
    private String itemDepartamento;
    String[] departamentos = {"Repartidor", "Administrador"};

    private Spinner mSpinnerFuncionario_Atendio_repartidor;

    List<UsuarioCargo> PersonalRepartidor;
    private String itemPersonalRepartidor;


    private Spinner mSpinnerFuncionario_Atendio_administrador;

    List<UsuarioCargo> PersonalAdministrador;

    private String itemPersonalAdministrador;




    private String id;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quejas_sugerencias);

        progressDialog = new ProgressDialog(this);

        id = getIntent().getStringExtra("id_user");


        mfirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        linearLayout_image_btn = findViewById(R.id.images_btn);

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
        loadPersonalRepartidor();



        mSpinnerFuncionario_Atendio_administrador = (Spinner) findViewById(R.id.spn_funcionario_atendio_administrador);

        mSpinnerFuncionario_Atendio_administrador.setOnItemSelectedListener(this);
        loadPersonalAdministrador();

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





                    } else if (!nombreus.isEmpty() && apellidous.isEmpty() && celularus.isEmpty() && emailus.isEmpty() && direccionus.isEmpty() && descripcion_tramite_experienciaus.isEmpty() && descripcion_fisicaus.isEmpty()) {


                        apellido.setError("Este campo esta vacio");
                        celular.setError("Este campo esta vacio");
                        email.setError("Este campo esta vacio");
                        direccion.setError("La direccion debe existir");
                        descripcion_tramite.setError("Debe ingresar una descripcion del tramite");
                        descripcion_fisica.setError("Debe ingresar una descripcion fisica del repartidor");



                    }else if (!nombreus.isEmpty() && !apellidous.isEmpty() && celularus.isEmpty() && emailus.isEmpty() && direccionus.isEmpty() && descripcion_tramite_experienciaus.isEmpty() && descripcion_fisicaus.isEmpty()) {

                        celular.setError("Este campo esta vacio");
                        email.setError("Este campo esta vacio");
                        direccion.setError("La direccion debe existir");
                        descripcion_tramite.setError("Debe ingresar una descripcion del tramite");
                        descripcion_fisica.setError("Debe ingresar una descripcion fisica del repartidor");


                    }else if (!nombreus.isEmpty() && !apellidous.isEmpty() && !celularus.isEmpty() && emailus.isEmpty() && direccionus.isEmpty() && descripcion_tramite_experienciaus.isEmpty() && descripcion_fisicaus.isEmpty()) {

                        email.setError("Este campo esta vacio");
                        direccion.setError("La direccion debe existir");
                        descripcion_tramite.setError("Debe ingresar una descripcion del tramite");
                        descripcion_fisica.setError("Debe ingresar una descripcion fisica del repartidor");


                    }else if (!nombreus.isEmpty() && !apellidous.isEmpty() && !celularus.isEmpty() && !emailus.isEmpty() && direccionus.isEmpty() && descripcion_tramite_experienciaus.isEmpty() && descripcion_fisicaus.isEmpty()) {



                        direccion.setError("La direccion debe existir");
                        descripcion_tramite.setError("Debe ingresar una descripcion del tramite");
                        descripcion_fisica.setError("Debe ingresar una descripcion fisica del repartidor");



                    }else if (!nombreus.isEmpty() && !apellidous.isEmpty() && !celularus.isEmpty() && !emailus.isEmpty() && !direccionus.isEmpty() && descripcion_tramite_experienciaus.isEmpty() && descripcion_fisicaus.isEmpty()) {


                        descripcion_tramite.setError("Debe ingresar una descripcion del tramite");
                        descripcion_fisica.setError("Debe ingresar una descripcion fisica del repartidor");




                    }else if (!nombreus.isEmpty() && !apellidous.isEmpty() && !celularus.isEmpty() && !emailus.isEmpty() && !direccionus.isEmpty() && !descripcion_tramite_experienciaus.isEmpty() && descripcion_fisicaus.isEmpty()) {

                        descripcion_fisica.setError("Debe ingresar una descripcion fisica del repartidor");


                    } else{





                        if (itemDepartamento == "Repartidor"){

                            postUsRepartidor(nombreus, apellidous, celularus,emailus,direccionus,descripcion_tramite_experienciaus,descripcion_fisicaus);

                            startActivity(new Intent(Quejas_SugerenciasActivity.this, InterfazClienteActivity.class));

                            overridePendingTransition(R.anim.left_in, R.anim.left_out);

                            finish();




                        }

                        if (itemDepartamento == "Administrador"){

                            postUsAdministrador(nombreus, apellidous, celularus,emailus,direccionus,descripcion_tramite_experienciaus,descripcion_fisicaus);

                            startActivity(new Intent(Quejas_SugerenciasActivity.this, InterfazClienteActivity.class));
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
                startActivity(new Intent(Quejas_SugerenciasActivity.this, InterfazClienteActivity.class));
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
                finish();
            }
        });




    }



    private void postUsRepartidor(String nombreus, String apellidous, String celularus, String emailus, String direccionus, String descripcion_tramite_experienciaus, String descripcion_fisicaus) {

        progressDialog.setMessage("Cargando");
        progressDialog.show();


        String idUser = mAuth.getCurrentUser().getUid();
        DocumentReference id = mfirestore.collection(itemIncidencia).document();




        if (itemIncidencia == "Quejas"){



            Map<String, Object> map = new HashMap<>();
            map.put("id_user", idUser);
            map.put("id", id.getId());
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


            map.put("departamento", "Repartidor");


            map.put("cantidad_quejas", 1);




            mfirestore.collection("Incidencias").document(id.getId()).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(getApplicationContext(), itemIncidencia+" Enviada", Toast.LENGTH_SHORT).show();
                    finish();



                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Error al ingresar", Toast.LENGTH_SHORT).show();
                }
            });


            mfirestore.collection("Quejas").document(id.getId()).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    //Toast.makeText(getApplicationContext(), "Quejas Enviada", Toast.LENGTH_SHORT).show();
                    //finish();



                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //Toast.makeText(getApplicationContext(), "Error al ingresar las quejas", Toast.LENGTH_SHORT).show();
                }
            });







        }else if (itemIncidencia == "Sugerencias"){


            Map<String, Object> map = new HashMap<>();
            map.put("id_user", idUser);
            map.put("id", id.getId());
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


            map.put("departamento", "Repartidor");

            map.put("cantidad_sugerencias", 1);






            mfirestore.collection("Incidencias").document(id.getId()).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(getApplicationContext(), itemIncidencia+" Enviada", Toast.LENGTH_SHORT).show();
                    finish();



                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Error al ingresar", Toast.LENGTH_SHORT).show();
                }
            });



            mfirestore.collection("Sugerencias").document(id.getId()).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    //Toast.makeText(getApplicationContext(), itemIncidencia+" Enviada", Toast.LENGTH_SHORT).show();
                    //finish();



                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //Toast.makeText(getApplicationContext(), "Error al ingresar", Toast.LENGTH_SHORT).show();
                }
            });








        }


































        progressDialog.dismiss();



    }



    private void postUsAdministrador(String nombreus, String apellidous, String celularus, String emailus, String direccionus, String descripcion_tramite_experienciaus, String descripcion_fisicaus) {

        progressDialog.setMessage("Cargando");
        progressDialog.show();


        String idUser = mAuth.getCurrentUser().getUid();
        DocumentReference id = mfirestore.collection(itemIncidencia).document();



        if (itemIncidencia == "Quejas"){



            Map<String, Object> map = new HashMap<>();
            map.put("id_user", idUser);
            map.put("id", id.getId());
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


            map.put("cantidad_quejas", 1);




            mfirestore.collection("Incidencias").document(id.getId()).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(getApplicationContext(), "Quejas Enviada", Toast.LENGTH_SHORT).show();
                    finish();



                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Error al ingresar", Toast.LENGTH_SHORT).show();
                }
            });




            mfirestore.collection("Quejas").document(id.getId()).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    //Toast.makeText(getApplicationContext(), "Quejas Enviada", Toast.LENGTH_SHORT).show();
                    //finish();



                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //Toast.makeText(getApplicationContext(), "Error al ingresar las quejas", Toast.LENGTH_SHORT).show();
                }
            });






        }else if (itemIncidencia == "Sugerencias"){


            Map<String, Object> map = new HashMap<>();
            map.put("id_user", idUser);
            map.put("id", id.getId());
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


            map.put("cantidad_sugerencias", 1);





            mfirestore.collection("Incidencias").document(id.getId()).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(getApplicationContext(), "Sugerencias Enviada", Toast.LENGTH_SHORT).show();
                    finish();



                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Error al ingresar la sugerencia", Toast.LENGTH_SHORT).show();
                }
            });



            mfirestore.collection("Sugerencias").document(id.getId()).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    //Toast.makeText(getApplicationContext(), itemIncidencia+" Enviada", Toast.LENGTH_SHORT).show();
                    //finish();



                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //Toast.makeText(getApplicationContext(), "Error al ingresar", Toast.LENGTH_SHORT).show();
                }
            });




        }












        progressDialog.dismiss();



    }















    public void loadPersonalRepartidor(){


        mfirestore.collection("Repartidor").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot snapshot : task.getResult()){
                    String codigoidre = snapshot.getId();
                    String nombrerepartidor = snapshot.getString("name").toString();
                    String apellidorepartidor = snapshot.getString("apellido").toString();



                    PersonalRepartidor.add(new UsuarioCargo(codigoidre,nombrerepartidor,apellidorepartidor));





                }


                ArrayAdapter<UsuarioCargo> arrayAdapterRepartidor = new ArrayAdapter<>(Quejas_SugerenciasActivity.this, android.R.layout.simple_spinner_dropdown_item, PersonalRepartidor);
                mSpinnerFuncionario_Atendio_repartidor.setAdapter(arrayAdapterRepartidor);












            }







        });





    }



    public void loadPersonalAdministrador(){




        mfirestore.collection("Administrador").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot snapshot1 : task.getResult()){
                    String codigoidad = snapshot1.getId();
                    String nombreadministrador = snapshot1.getString("name").toString();
                    String apellidoadministrador = snapshot1.getString("apellido").toString();



                    PersonalAdministrador.add(new UsuarioCargo(codigoidad,nombreadministrador, apellidoadministrador));






                }


                ArrayAdapter<UsuarioCargo> arrayAdapterAdministrador = new ArrayAdapter<>(Quejas_SugerenciasActivity.this, android.R.layout.simple_spinner_dropdown_item, PersonalAdministrador);
                mSpinnerFuncionario_Atendio_administrador.setAdapter(arrayAdapterAdministrador);


            }
        });





    }







    public void loadDatos(String id){


        mfirestore.collection("Cliente").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {



                String nombreDatos = documentSnapshot.getString("name");
                String apellidoDatos = documentSnapshot.getString("apellido");
                String celularDatos = documentSnapshot.getString("celular");

                String direccionDatos = documentSnapshot.getString("direccion");
                String emailDatos = documentSnapshot.getString("email");


                String photoDatos = documentSnapshot.getString("photo");


                nombre.setText(nombreDatos);
                apellido.setText(apellidoDatos);
                celular.setText(celularDatos);

                direccion.setText(direccionDatos);
                email.setText(emailDatos);



                try {
                    if(!photoDatos.equals("")){
                        //Toast toast = Toast.makeText(getApplicationContext(), "Cargando foto", Toast.LENGTH_SHORT);
                        //toast.setGravity(Gravity.TOP,0,200);
                        //toast.show();


                        // Picasso.with(CreatePetActivity.this)
                        //        .load(photoPet)
                        //        .resize(150, 150)
                        //        .into(photo_pet);

                        Glide.with(Quejas_SugerenciasActivity.this)
                                .load(photoDatos)
                                .circleCrop()
                                .into(photo_perfil);



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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        itemIncidencia = mSpinnerIncidencia.getSelectedItem().toString();
        itemDepartamento = mSpinnerDepartamento.getSelectedItem().toString();


        if (itemDepartamento == "Repartidor"){

            mSpinnerFuncionario_Atendio_administrador.setEnabled(false);


            mSpinnerFuncionario_Atendio_repartidor.setEnabled(true);




        }else if (itemDepartamento == "Administrador"){

            mSpinnerFuncionario_Atendio_administrador.setEnabled(true);


            mSpinnerFuncionario_Atendio_repartidor.setEnabled(false);


        }




        mfirestore.collection("Repartidor").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot snapshot : task.getResult()){
                    String codigoidre = snapshot.getId();
                    String nombrerepartidor = snapshot.getString("name").toString();
                    String apellidorepartidor = snapshot.getString("apellido").toString();


                    itemPersonalRepartidor = mSpinnerFuncionario_Atendio_repartidor.getSelectedItem().toString();



                }



            }



        });







        mfirestore.collection("Administrador").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot snapshot1 : task.getResult()){
                    String codigoidad = snapshot1.getId();
                    String nombreadministrador = snapshot1.getString("name").toString();
                    String apellidoadministrador = snapshot1.getString("apellido").toString();

                    itemPersonalAdministrador = mSpinnerFuncionario_Atendio_administrador.getSelectedItem().toString();

                }



            }

        });












    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }











}