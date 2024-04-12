package com.aplication.appgestionrepartos.administrador;

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
import android.widget.Toast;

import com.aplication.appgestionrepartos.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class ActualizarProveedorActivity extends AppCompatActivity {


    ImageView photo_proveedor;

    Button btn_actualizar, btn_regresar;

    Button btn_cu_photo, btn_r_photo;





    EditText nombre, apellido, dni, direccion, email, empresa, celular;

    private FirebaseFirestore mfirestore;
    private FirebaseAuth mAuth;


    StorageReference storageReference;
    String storage_path = "usuarios/*";


    String storage_path_name;


    private static final int COD_SEL_STORAGE = 200;
    private static final int COD_SEL_IMAGE = 300;

    private Uri image_url;
    String photo = "photo";
    String idd;

    ProgressDialog progressDialog;


    String id;




    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_proveedor);


        id = getIntent().getStringExtra("id_proveedor");

        mfirestore = FirebaseFirestore.getInstance();

        mAuth = FirebaseAuth.getInstance();

        storageReference = FirebaseStorage.getInstance().getReference();




        progressDialog = new ProgressDialog(this);







        nombre = findViewById(R.id.nombre);

        apellido = findViewById(R.id.apellido);

        dni = findViewById(R.id.dni);

        direccion = findViewById(R.id.direccion);

        email = findViewById(R.id.email);

        empresa = findViewById(R.id.empresa);

        celular = findViewById(R.id.celular);



        photo_proveedor = findViewById(R.id.perfil_photo);

        btn_cu_photo = findViewById(R.id.btn_subir_photo);
        btn_r_photo = findViewById(R.id.btn_eliminar_photo);


        btn_actualizar = findViewById(R.id.btn_actualizar);


        btn_regresar = findViewById(R.id.btn_regresar);


        btn_regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(ActualizarProveedorActivity.this, ProveedoresActivity.class);

                startActivity(i);



            }
        });





        btn_cu_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadPhoto();
            }
        });




        btn_r_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("photo", "");
                mfirestore.collection("Proveedores").document(id).update(map);

                //Toast.makeText(CreatePetActivity.this, "Foto eliminada", Toast.LENGTH_SHORT).show();
            }
        });









        if (id == null || id == ""){






        }else{

            idd = id;

            getPet(id);





            btn_actualizar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {



                    String nombrepropet = nombre.getText().toString().trim();
                    String nombreproveedor = ucFirst(nombrepropet);
                    String apellidopet = apellido.getText().toString().trim();
                    String apellidoproveedor = ucFirst(apellidopet);
                    String dniproveedor = dni.getText().toString().trim();
                    String direccionproveedor = direccion.getText().toString().trim();
                    String emailproveedor = email.getText().toString().trim();
                    String empresaproveedor = empresa.getText().toString().trim();
                    String celularproveedor = "+51"+celular.getText().toString().trim();




                    if(nombreproveedor.isEmpty() && apellidoproveedor.isEmpty() && dniproveedor.isEmpty() && direccionproveedor.isEmpty() && emailproveedor.isEmpty() && empresaproveedor.isEmpty() && celularproveedor.isEmpty()){

                        Toast.makeText(view.getContext(), "Ingresar los datos", Toast.LENGTH_SHORT).show();

                        nombre.setError("El campo esta vacio");
                        apellido.setError("El campo esta vacio");
                        dni.setError("El campo esta vacio");
                        direccion.setError("Este campo esta vacio");
                        email.setError("Este campo esta vacio");
                        empresa.setError("Este campo esta vacio");
                        celular.setError("Este campo esta vacio");



                    }else if (!nombreproveedor.isEmpty() && apellidoproveedor.isEmpty() && dniproveedor.isEmpty() && direccionproveedor.isEmpty() && emailproveedor.isEmpty() && empresaproveedor.isEmpty() && celularproveedor.isEmpty()){



                        apellido.setError("El campo esta vacio");
                        dni.setError("El campo esta vacio");
                        direccion.setError("Este campo esta vacio");
                        email.setError("Este campo esta vacio");
                        empresa.setError("Este campo esta vacio");
                        celular.setError("Este campo esta vacio");



                    }else if (!nombreproveedor.isEmpty() && !apellidoproveedor.isEmpty() && dniproveedor.isEmpty() && direccionproveedor.isEmpty() && emailproveedor.isEmpty() && empresaproveedor.isEmpty() && celularproveedor.isEmpty()){


                        dni.setError("El campo esta vacio");
                        direccion.setError("Este campo esta vacio");
                        email.setError("Este campo esta vacio");
                        empresa.setError("Este campo esta vacio");
                        celular.setError("Este campo esta vacio");


                    }else if (!nombreproveedor.isEmpty() && !apellidoproveedor.isEmpty() && dni.length()<8 && direccionproveedor.isEmpty() && emailproveedor.isEmpty() && empresaproveedor.isEmpty() && celularproveedor.isEmpty()){

                        dni.setError("El dni debe tener un maximo de 8 digitos");
                        direccion.setError("Este campo esta vacio");
                        email.setError("Este campo esta vacio");
                        empresa.setError("Este campo esta vacio");
                        celular.setError("Este campo esta vacio");


                    }else if (!nombreproveedor.isEmpty() && !apellidoproveedor.isEmpty() && dni.length()>=8 && direccionproveedor.isEmpty() && emailproveedor.isEmpty() && empresaproveedor.isEmpty() && celularproveedor.isEmpty()){


                        direccion.setError("Este campo esta vacio");
                        email.setError("Este campo esta vacio");
                        empresa.setError("Este campo esta vacio");
                        celular.setError("Este campo esta vacio");


                    }else if (!nombreproveedor.isEmpty() && !apellidoproveedor.isEmpty() && dni.length()>=8 && !direccionproveedor.isEmpty() && emailproveedor.isEmpty() && empresaproveedor.isEmpty() && celularproveedor.isEmpty()){



                        email.setError("Este campo esta vacio");
                        empresa.setError("Este campo esta vacio");
                        celular.setError("Este campo esta vacio");


                    }else if (!nombreproveedor.isEmpty() && !apellidoproveedor.isEmpty() && dni.length()>=8 && !direccionproveedor.isEmpty() && !emailproveedor.isEmpty() && empresaproveedor.isEmpty() && celularproveedor.isEmpty()){





                        empresa.setError("Este campo esta vacio");
                        celular.setError("Este campo esta vacio");


                    }else if (!nombreproveedor.isEmpty() && !apellidoproveedor.isEmpty() && dni.length()>=8 && !direccionproveedor.isEmpty() && !emailproveedor.isEmpty() && !empresaproveedor.isEmpty() && !celularproveedor.isEmpty()){



                        updatePet(nombreproveedor, apellidoproveedor, dniproveedor, direccionproveedor, emailproveedor, empresaproveedor, celularproveedor, id);


                        Intent i = new Intent(ActualizarProveedorActivity.this, ProveedoresActivity.class);

                        startActivity(i);


                    }













                }
            });










        }















    }





    public static String ucFirst(String str) {
        if (str == null || str.isEmpty()) {
            return "";
        } else {
            return  Character.toUpperCase(str.charAt(0)) + str.substring(1, str.length()).toLowerCase();
        }
    }






    public void updatePet(String nombreproveedor, String apellidoproveedor, String dniproveedor, String direccionproveedor, String emailproveedor, String empresaproveedor, String celularproveedor, String id) {


        Map<String, Object> map = new HashMap<>();

        map.put("nombre", nombreproveedor);
        map.put("apellido", apellidoproveedor);
        map.put("dni", dniproveedor);
        map.put("direccion", direccionproveedor);
        map.put("email",emailproveedor);
        map.put("empresa", empresaproveedor);
        map.put("celular",celularproveedor);




        mfirestore.collection("Proveedores").document(id).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(ActualizarProveedorActivity.this, "Proveedor Actualizado", Toast.LENGTH_SHORT).show();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ActualizarProveedorActivity.this, "Error al Actualizar Proveedor", Toast.LENGTH_SHORT).show();


            }
        });













    }




    private void getPet(String id){
        mfirestore.collection("Proveedores").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String nombreProveedor = documentSnapshot.getString("nombre");
                String apellidoProveedor = documentSnapshot.getString("apellido");
                String dniProveedor = documentSnapshot.getString("dni");
                String direccionProveedor = documentSnapshot.getString("direccion");
                String emailProveedor = documentSnapshot.getString("email");
                String empresaProveedor = documentSnapshot.getString("empresa");
                String celularProveedor = documentSnapshot.getString("celular");
                String photoProveedor = documentSnapshot.getString("photo");


                nombre.setText(nombreProveedor);
                apellido.setText(apellidoProveedor);
                dni.setText(dniProveedor);
                direccion.setText(direccionProveedor);
                email.setText(emailProveedor);
                empresa.setText(empresaProveedor);
                celular.setText(celularProveedor);



                try {
                    if(!photoProveedor.equals("")){
                        Toast toast = Toast.makeText(getApplicationContext(), "Cargando foto", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP,0,200);
                        toast.show();


                        // Picasso.with(CreatePetActivity.this)
                        //        .load(photoPet)
                        //        .resize(150, 150)
                        //        .into(photo_pet);

                        Glide.with(ActualizarProveedorActivity.this)
                                .load(photoProveedor)
                                .circleCrop()
                                .error(R.drawable.ic_launcher_background)
                                .into(photo_proveedor);



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
        progressDialog.setMessage("Actualizando foto");
        progressDialog.show();


        DocumentReference codigoid = mfirestore.collection("Proveedores").document();


        String rute_storage_photo = storage_path + storage_path_name + "" + photo + "" + mAuth.getUid() +""+ codigoid.getId();
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





                            mfirestore.collection("Proveedores").document(id).update(map);




                            Toast.makeText(ActualizarProveedorActivity.this, "Foto actualizada", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ActualizarProveedorActivity.this, "Error al cargar foto", Toast.LENGTH_SHORT).show();
            }
        });
    }















}