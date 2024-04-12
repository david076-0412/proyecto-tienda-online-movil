package com.aplication.appgestionrepartos.administrador;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import com.aplication.appgestionrepartos.interfaz.InterfazAdministradorActivity;
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
import com.google.firebase.storage.UploadTask;
//import com.squareup.picasso.Picasso;


import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class CreatePetActivity extends AppCompatActivity {

    ImageView photo_pet;
    Button btn_add, btn_atras;
    Button btn_cu_photo, btn_r_photo;
    LinearLayout linearLayout_image_btn;
    EditText name, age, color, precio_vacuna;
    private FirebaseFirestore mfirestore;
    private FirebaseAuth mAuth;


    StorageReference storageReference;
    String storage_path = "productos/*";


    String storage_path_name;


    private static final int COD_SEL_STORAGE = 200;
    private static final int COD_SEL_IMAGE = 300;

    private Uri image_url;
    String photo = "photo";
    String idd;

    ProgressDialog progressDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pet);

        this.setTitle("Producto");
        progressDialog = new ProgressDialog(this);
        String id = getIntent().getStringExtra("id_pet");
        mfirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        linearLayout_image_btn = findViewById(R.id.images_btn);


        name = findViewById(R.id.nombre);
        age = findViewById(R.id.edad);
        color = findViewById(R.id.color);
        precio_vacuna = findViewById(R.id.precio_vacuna);
        photo_pet = findViewById(R.id.pet_photo);
        btn_cu_photo = findViewById(R.id.btn_photo);
        btn_r_photo = findViewById(R.id.btn_remove_photo);

        btn_add = findViewById(R.id.btn_add);
        btn_atras = findViewById(R.id.btn_atras);

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
                mfirestore.collection("pda").document(idd).update(map);
                mfirestore.collection("Compraseditado").document(idd).update(map);
                Toast.makeText(CreatePetActivity.this, "Foto eliminada", Toast.LENGTH_SHORT).show();
            }
        });


        if (id == null || id == ""){


            linearLayout_image_btn.setVisibility(View.GONE);

            btn_add.setText("AGREGAR");

            btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    String namepro = name.getText().toString().trim();

                    String namepet = ucFirst(namepro);


                    String agepet = age.getText().toString().trim();
                    String colorpet = color.getText().toString().trim();
                    Double precio_vacunapet = Double.parseDouble(precio_vacuna.getText().toString().trim());

                    if(namepet.isEmpty() && agepet.isEmpty() && colorpet.isEmpty()){
                        Toast.makeText(getApplicationContext(), "Ingresar los datos", Toast.LENGTH_SHORT).show();
                        name.setError("El campo esta vacio");
                        age.setError("El campo esta vacio");
                        color.setError("El campo esta vacio");

                    }else{
                        postPet(namepet, agepet, colorpet, precio_vacunapet);
                    }
                }
            });

        }else{

            idd = id;
            btn_add.setText("ACTUALIZAR");
            getPet(id);




            btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    String namepro = name.getText().toString().trim();

                    String namepet = ucFirst(namepro);


                    String agepet = age.getText().toString().trim();
                    String colorpet = color.getText().toString().trim();
                    Double precio_vacunapet = Double.parseDouble(precio_vacuna.getText().toString().trim());

                    if(namepet.isEmpty() && agepet.isEmpty() && colorpet.isEmpty()){
                        Toast.makeText(getApplicationContext(), "Ingresar los datos", Toast.LENGTH_SHORT).show();
                        name.setError("El campo esta vacio");
                        age.setError("El campo esta vacio");
                        color.setError("El campo esta vacio");

                    }else{
                        updatePet(namepet, agepet, colorpet, precio_vacunapet, id);
                        startActivity(new Intent(CreatePetActivity.this, InterfazAdministradorActivity.class));
                        overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        finish();
                    }
                }
            });



        }

        btn_atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(CreatePetActivity.this, InterfazAdministradorActivity.class));
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
                finish();
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


        String namepet = name.getText().toString().trim();


        storage_path_name = namepet+"/*";


        DocumentReference codigoid = mfirestore.collection("galeria/productos/"+namepet).document();


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

                            HashMap<String, Object> mapG = new HashMap<>();
                            mapG.put("imagen", download_uri);



                            mapG.put("id", codigoid.getId());
                            mapG.put("nombre", namepet);




                            //------------------------------------------------------------------------------------
                            mfirestore.collection("galeria/productos/"+namepet).document(codigoid.getId()).set(mapG);


                            mfirestore.collection("galeria/productos/"+namepet).document(codigoid.getId()).update(mapG);



                            //------------------------------------------------------------------------------------


                            mfirestore.collection("pda").document(idd).update(map);


                            mfirestore.collection("Compraseditado").document(idd).update(map);


                            //-----------------------------------------------------------------------

                            mfirestore.collection("calificacion").document(idd).update(map);

                            //-----------------------------------------------------------------------


                            Toast.makeText(CreatePetActivity.this, "Foto actualizada", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CreatePetActivity.this, "Error al cargar foto", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void updatePet(String namepet, String agepet, String colorpet, Double precio_vacunapet, String id) {

        Map<String, Object> map = new HashMap<>();
        map.put("name", namepet);
        map.put("age", agepet);
        map.put("color", colorpet);
        map.put("vaccine_price", precio_vacunapet);

        mfirestore.collection("pda").document(id).update(map);


        Map<String, Object> mapR = new HashMap<>();
        mapR.put("name", namepet);
        mapR.put("age", agepet);
        mapR.put("color", colorpet);
        mapR.put("vaccine_price", precio_vacunapet);


        mapR.put("cantidadR", agepet);

        int valor = 0;

        mapR.put("cantidadPro", valor);


       mfirestore.collection("Compraseditado").document(namepet).update(mapR);

       //----------------------------------------------------------------------

       //mfirestore.collection("calificacion").document(id).update(mapR);

        //---------------------------------------------------------------------


    }







    private void postPet(String namepet, String agepet, String colorpet, Double precio_vacunapet) {
        String idUser = mAuth.getCurrentUser().getUid();
        DocumentReference id = mfirestore.collection("pda").document();

        Map<String, Object> map = new HashMap<>();
        map.put("id_user", idUser);
        map.put("id", id.getId());
        map.put("name", namepet);
        map.put("age", agepet);
        map.put("color", colorpet);
        map.put("vaccine_price", precio_vacunapet);


        mfirestore.collection("pda").document(namepet).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "Producto Creado", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error al ingresar", Toast.LENGTH_SHORT).show();
            }
        });



        mfirestore.collection("Compraseditado").document(namepet).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "Producto Creado", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error al ingresar", Toast.LENGTH_SHORT).show();
            }
        });




        Map<String, Object> mapE = new HashMap<>();
        mapE.put("producto", 0);



        mfirestore.collection("acumulador/"+idUser+"/Cliente").document(namepet).set(mapE);
        mfirestore.collection("acumulador/"+idUser+"/Cliente").document(namepet).update(mapE);


        mfirestore.collection("acumulador/"+idUser+"/Cliente").document("pedido").set(mapE);
        mfirestore.collection("acumulador/"+idUser+"/Cliente").document("pedido").update(mapE);






    }



    public static String ucFirst(String str) {
        if (str == null || str.isEmpty()) {
            return "";
        } else {
            return  Character.toUpperCase(str.charAt(0)) + str.substring(1, str.length()).toLowerCase();
        }
    }






    private void getPet(String id){
        mfirestore.collection("Compraseditado").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                DecimalFormat format = new DecimalFormat("0.00");
                String namePet = documentSnapshot.getString("name");
                String agePet = documentSnapshot.getString("age");
                String colorPet = documentSnapshot.getString("color");
                Double precio_vacunapet = documentSnapshot.getDouble("vaccine_price");
                String photoPet = documentSnapshot.getString("photo");

                name.setText(namePet);
                age.setText(agePet);
                color.setText(colorPet);



                precio_vacuna.setText(String.valueOf(precio_vacunapet));
                try {
                    if(!photoPet.equals("")){
                        Toast toast = Toast.makeText(getApplicationContext(), "Cargando foto", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP,0,200);
                        toast.show();


                        // Picasso.with(CreatePetActivity.this)
                        //        .load(photoPet)
                        //        .resize(150, 150)
                        //        .into(photo_pet);

                        Glide.with(CreatePetActivity.this)
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