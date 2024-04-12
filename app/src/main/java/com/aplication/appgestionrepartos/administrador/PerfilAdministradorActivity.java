package com.aplication.appgestionrepartos.administrador;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.aplication.appgestionrepartos.R;
import com.aplication.appgestionrepartos.cliente.PerfilActivity;
import com.aplication.appgestionrepartos.interfaz.InterfazAdministradorActivity;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class PerfilAdministradorActivity extends AppCompatActivity {


    ImageView perfil;
    Button btn_actualizar, btn_regresar;
    Button btn_subir_photo,btn_eliminar_photo;

    LinearLayout linearLayout_image_btn;
    EditText name, apellido, dni, direccion, email, celular, password, tarjeta;
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

    private String nombrePerfil, nombrePerfil1;


    Button btn_restaurar_contrasena;




    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_administrador);


        this.setTitle("PERFIL");
        progressDialog = new ProgressDialog(this);
        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        idUser = mAuth.getCurrentUser().getUid();
        storageReference = FirebaseStorage.getInstance().getReference();


        String id = getIntent().getStringExtra("id_pet");



        linearLayout_image_btn = findViewById(R.id.images_btn);
        name = findViewById(R.id.nombre);
        apellido = findViewById(R.id.apellido);
        dni = findViewById(R.id.dni);
        direccion = findViewById(R.id.direccion);
        email = findViewById(R.id.correo);

        celular = findViewById(R.id.celular);

        password= findViewById(R.id.contrasena);


        tarjeta = findViewById(R.id.tarjeta);





        perfil = findViewById(R.id.perfil_photo);








        btn_subir_photo = findViewById(R.id.btn_subir_photo);

        btn_subir_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPhoto();
            }
        });


        btn_eliminar_photo = findViewById(R.id.btn_eliminar_photo);

        btn_eliminar_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mFirestore.collection("Usuario/Administrador/"+idUser).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot snapshot : task.getResult()){
                            nombrePerfil = snapshot.getString("name");
                            HashMap<String, Object> map = new HashMap<>();

                            map.put("photo", "");

                            mFirestore.collection("Administrador").document(idUser).update(map);
                            mFirestore.collection("Usuario/Administrador/"+idUser).document("registro").update(map);


                            Toast.makeText(PerfilAdministradorActivity.this, "Foto eliminada", Toast.LENGTH_SHORT).show();



                        }
                    }
                });

            }
        });

        btn_regresar = findViewById(R.id.btn_regresar);

        btn_regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PerfilAdministradorActivity.this, InterfazAdministradorActivity.class));

                overridePendingTransition(R.anim.left_in, R.anim.left_out);

                finish();


            }
        });


        btn_actualizar = findViewById(R.id.btn_actualizar);



        btn_actualizar.setText("ACTUALIZAR");


        getPerfil(idUser);


        btn_actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String namepet = name.getText().toString().trim();
                String namePerfil = ucFirst(namepet);

                String apellidopet = apellido.getText().toString().trim();
                String apellidoPerfil = ucFirst(apellidopet);


                String dniPerfil = dni.getText().toString().trim();
                String direccionPerfil = direccion.getText().toString().trim();
                String emailPerfil = email.getText().toString().trim();

                String celularPerfil = celular.getText().toString().trim();

                String passwordPerfil = password.getText().toString().trim();

                String tarjetaPerfil = tarjeta.getText().toString().trim();



                if(namePerfil.isEmpty() && apellidoPerfil.isEmpty() && dniPerfil.isEmpty() && direccionPerfil.isEmpty() && emailPerfil.isEmpty() && celularPerfil.isEmpty() && passwordPerfil.isEmpty() && tarjetaPerfil.isEmpty()){

                    Toast.makeText(getApplicationContext(), "Ingresar los datos", Toast.LENGTH_SHORT).show();


                }else{

                    updatePerfil(namePerfil, apellidoPerfil, dniPerfil, direccionPerfil, emailPerfil, celularPerfil, passwordPerfil, idUser, tarjetaPerfil);
                    startActivity(new Intent(PerfilAdministradorActivity.this, InterfazAdministradorActivity.class));

                    overridePendingTransition(R.anim.left_in, R.anim.left_out);

                    finish();
                }
            }
        });



        btn_restaurar_contrasena = findViewById(R.id.btn_restaurar_contrasena);

        btn_restaurar_contrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String emailPerfil = email.getText().toString().trim();

                AlertDialog.Builder dialogo2 = new AlertDialog.Builder(PerfilAdministradorActivity.this);
                dialogo2.setTitle("Reset Password");
                dialogo2.setMessage("Enter Your Email To Received Reset Link.");
                dialogo2.setCancelable(false);
                dialogo2.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo2, int id) {



                        mAuth.sendPasswordResetEmail(emailPerfil).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(PerfilAdministradorActivity.this, "Reset Link Sent To Your Email.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(PerfilAdministradorActivity.this, "Error! Reset Link is Not Sent"+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });








                    }
                });
                dialogo2.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo2, int id) {


                    }
                });


                dialogo2.show();



            }
        });




    }



    public static String ucFirst(String str) {
        if (str == null || str.isEmpty()) {
            return "";
        } else {
            return  Character.toUpperCase(str.charAt(0)) + str.substring(1, str.length()).toLowerCase();
        }
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
                subirPhoto(image_url);

            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    private void subirPhoto(Uri image_url) {


        mFirestore.collection("Usuario/Administrador/"+idUser).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot snapshot1 : task.getResult()){
                    nombrePerfil1 = snapshot1.getString("name");

                    progressDialog.setMessage("Actualizando foto");
                    progressDialog.show();
                    DocumentReference id = mFirestore.collection("Usuario/Administrador/"+idUser).document();
                    String rute_storage_photo = storage_path + "" + photo + "" + mAuth.getUid() +""+ id.getId();
                    StorageReference reference = storageReference.child(rute_storage_photo);
                    reference.putFile(image_url).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful());
                            if (uriTask.isSuccessful()){
                                uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String download_uri = uri.toString();
                                        HashMap<String, Object> map = new HashMap<>();
                                        map.put("photo", download_uri);
                                        mFirestore.collection("Administrador").document(idUser).update(map);
                                        mFirestore.collection("Usuario/Administrador/"+idUser).document("registro").update(map);
                                        Toast.makeText(PerfilAdministradorActivity.this, "Foto actualizada", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();

                                    }
                                });
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(PerfilAdministradorActivity.this, "Error al cargar foto", Toast.LENGTH_SHORT).show();
                        }
                    });






                }
            }
        });



    }



    public void tarjeta_credito_sheet(View view){
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                PerfilAdministradorActivity.this,R.style.BottomSheetDialogTheme
        );
        View bottomSheetView = LayoutInflater.from(PerfilAdministradorActivity.this)
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

                tarjeta.setText(tarjeta_registro);

                //Toast.makeText(RegisterActivity.this, "Share...", Toast.LENGTH_SHORT).show();

                bottomSheetDialog.dismiss();

            }
        });

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();




    }








    public void updatePerfil(String namePerfil, String apellidoPerfil, String dniPerfil, String direccionPerfil, String emailPerfil, String celularPerfil, String passwordPerfil, String id, String tarjetaPerfil){

        Map<String, Object> map = new HashMap<>();
        map.put("name", namePerfil);
        map.put("apellido", apellidoPerfil);
        map.put("dni", dniPerfil);
        map.put("direccion", direccionPerfil);





        map.put("email", emailPerfil);

        final FirebaseUser usuario1 = FirebaseAuth.getInstance().getCurrentUser();

        AuthCredential credential1 = EmailAuthProvider.getCredential(emailPerfil,passwordPerfil);
        usuario1.reauthenticate(credential1).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                usuario1.updateEmail(emailPerfil).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            DocumentReference reference = mFirestore.collection("Administrador").document(idUser);
                            reference.update("email",emailPerfil).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(PerfilAdministradorActivity.this, "Contraseña Actualizada", Toast.LENGTH_SHORT).show();
                                }
                            });

                            DocumentReference reference1 = mFirestore.collection("Usuario/Administrador/"+idUser).document("registro");

                            reference1.update("email", emailPerfil).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    //Toast.makeText(PerfilActivity.this, "", Toast.LENGTH_SHORT).show();
                                }
                            });


                        }
                    }
                });


            }
        });











        String celularNarea1=celularPerfil;


        map.put("celular",celularNarea1);








        map.put("password",passwordPerfil);

        final FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();

        AuthCredential credential = EmailAuthProvider.getCredential(emailPerfil,passwordPerfil);
        usuario.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                usuario.updatePassword(passwordPerfil).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            DocumentReference reference = mFirestore.collection("Administrador").document(idUser);
                            reference.update("password",passwordPerfil).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(PerfilAdministradorActivity.this, "Contraseña Actualizada", Toast.LENGTH_SHORT).show();
                                }
                            });

                            DocumentReference reference1 = mFirestore.collection("Usuario/Administrador/"+idUser).document("registro");

                            reference1.update("password", passwordPerfil).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    //Toast.makeText(PerfilActivity.this, "", Toast.LENGTH_SHORT).show();
                                }
                            });


                        }
                    }
                });


            }
        });



        map.put("tarjeta_credito",tarjetaPerfil);



























        mFirestore.collection("Administrador").document(id).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
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




        mFirestore.collection("Usuario/Administrador/"+idUser).document("registro").update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
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









        DatabaseReference dbRefC = FirebaseDatabase.getInstance().getReference().child("users");




        Map<String, Object> mapC = new HashMap<>();
        mapC.put("name", namePerfil);
        mapC.put("apellido",apellidoPerfil);
        mapC.put("dni", dniPerfil);
        mapC.put("direccion", direccionPerfil);
        mapC.put("email", emailPerfil);


        String celularNarea2=celularPerfil;


        mapC.put("celular",celularNarea2);

        mapC.put("password",passwordPerfil);


        String usuarioPerfil = namePerfil + " " + apellidoPerfil;


        dbRefC.child(usuarioPerfil).setValue(mapC);







    }



    private void getPerfil(String id){

        mFirestore.collection("Administrador").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String namePer = documentSnapshot.getString("name");
                String apellidoPer = documentSnapshot.getString("apellido");
                String dniPer = documentSnapshot.getString("dni");
                String direccionPer = documentSnapshot.getString("direccion");
                String emailPer = documentSnapshot.getString("email");
                String passwordPer = documentSnapshot.getString("password");

                String celularPer = documentSnapshot.getString("celular");
                String tarjetaPer = documentSnapshot.getString("tarjeta_credito");


                String photoPer = documentSnapshot.getString("photo");


                name.setText(namePer);
                apellido.setText(apellidoPer);
                dni.setText(dniPer);
                direccion.setText(direccionPer);
                email.setText(emailPer);

                tarjeta.setText(tarjetaPer);


                celular.setText(celularPer);

                password.setText(passwordPer);


                try {
                    if(!photoPer.equals("")){
                        Toast toast = Toast.makeText(getApplicationContext(), "Cargando foto", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP,0,200);
                        toast.show();


                        // Picasso.with(CreatePetActivity.this)
                        //        .load(photoPet)
                        //        .resize(150, 150)
                        //        .into(photo_pet);

                        Glide.with(PerfilAdministradorActivity.this)
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









    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }






}