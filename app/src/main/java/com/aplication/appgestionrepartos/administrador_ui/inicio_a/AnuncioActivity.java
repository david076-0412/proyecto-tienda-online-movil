package com.aplication.appgestionrepartos.administrador_ui.inicio_a;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aplication.appgestionrepartos.R;
import com.aplication.appgestionrepartos.interfaz.InterfazAdministradorActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class AnuncioActivity extends AppCompatActivity {


    String titulo_anuncio;


    ProgressDialog progressDialog;

    StorageReference storageReference;
    String storage_path = "usuarios/*";


    private static final int COD_SEL_STORAGE = 200;
    private static final int COD_SEL_IMAGE = 300;

    private Uri image_url;
    String photo = "photo";


    Button btn_subir_anuncio, btn_regresar;


    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;


    EditText textTitulo;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anuncio);



        titulo_anuncio = getIntent().getStringExtra("titulo");


        progressDialog = new ProgressDialog(this);

        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        storageReference = FirebaseStorage.getInstance().getReference();




        btn_subir_anuncio = findViewById(R.id.btn_subir_anuncio);



        btn_subir_anuncio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                uploadPhoto();



            }
        });


        btn_regresar = findViewById(R.id.btn_regresar);


        btn_regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(AnuncioActivity.this, InterfazAdministradorActivity.class);


                startActivity(intent);

                finish();



            }
        });





        textTitulo = findViewById(R.id.titulo);


        textTitulo.setEnabled(false);

        textTitulo.setText(titulo_anuncio);





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


        String rute_storage_photo = storage_path + "" + titulo_anuncio;
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
                            map.put("imagen", download_uri);
                            map.put("titulo", titulo_anuncio);
                            mFirestore.collection("galeria/fotos/cliente").document(titulo_anuncio).set(map);
                            Toast.makeText(AnuncioActivity.this, "Foto actualizada", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AnuncioActivity.this, "Error al cargar foto", Toast.LENGTH_SHORT).show();
            }
        });





    }

















}