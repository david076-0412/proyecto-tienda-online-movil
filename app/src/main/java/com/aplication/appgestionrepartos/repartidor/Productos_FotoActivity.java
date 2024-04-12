package com.aplication.appgestionrepartos.repartidor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.aplication.appgestionrepartos.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Productos_FotoActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnCamara,btnGuardar;
    ImageView imgView;

    Bitmap bitmap;

    String rutaImagen;

    String mAbsolutePath = "";

    final int PHOTO_CONST= 1;

    StorageReference storageReference;

    String storage_path = "evidencia_productos/*";


    private static final int COD_SEL_STORAGE = 200;
    private static final int COD_SEL_IMAGE = 300;

    private Uri image_url;
    String photo = "photo";


    ProgressDialog progressDialog;

    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;



    String id,nameCli;


    String productoEvi;

    String iduseruu;

    String clientecodigo;

    private Toolbar mToolbar;

    private ActionBar mActionBar;


    String  pedidoinfo;




    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos_foto);

        mToolbar = findViewById(R.id.toolbar);
        this.setTitle("");
        setSupportActionBar(mToolbar);

        progressDialog = new ProgressDialog(this);
        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        storageReference = FirebaseStorage.getInstance().getReference();


        productoEvi = getIntent().getStringExtra("id_producto");





        pedidoinfo = getIntent().getStringExtra("cliente");






        id = getIntent().getStringExtra("id_pet");
        nameCli = getIntent().getStringExtra("name");

        iduseruu = getIntent().getStringExtra("user");
        clientecodigo = getIntent().getStringExtra("id_user");






        btnCamara = findViewById(R.id.btnCamara);


        btnCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirCamara();
            }
        });


        imgView = findViewById(R.id.imageView);

        imgView.setRotation(90);



        btnGuardar = findViewById(R.id.btnGuardar);



        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                uploadPhoto();





            }
        });



        verificarPermisos();



    }










    private void abrirCamara(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager()) != null){
            File imagenArchivo = null;

            try {
                imagenArchivo = crearImagen();

            }catch (IOException ex){
                Log.e("Error", ex.toString());
            }


            if (imagenArchivo != null){
                Uri fotoUri = FileProvider.getUriForFile(Productos_FotoActivity.this,"com.aplication.appgestionrepartos", imagenArchivo);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fotoUri);
                startActivityForResult(intent, 1);

            }


        }
    }




    private File crearImagen() throws IOException{


        String nombreImagen ="foto_";
        File directorio = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imagen = File.createTempFile(nombreImagen,".jpg",directorio);

        rutaImagen = imagen.getAbsolutePath();

        return imagen;

    }





    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1 && resultCode == RESULT_OK) {



            Bitmap imgBitmap = BitmapFactory.decodeFile(rutaImagen);



            imgView.setImageBitmap(imgBitmap);


        }


        if(resultCode == RESULT_OK){
            if (requestCode == COD_SEL_IMAGE){
                image_url = data.getData();
                subirPhoto(image_url);


            }
        }




        super.onActivityResult(requestCode, resultCode, data);






    }

    private void uploadPhoto() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("image/*");
        startActivityForResult(i, COD_SEL_IMAGE);
    }





    private void subirPhoto(Uri image_url) {

        progressDialog.setMessage("Subiendo foto");
        progressDialog.show();
        DocumentReference id = mFirestore.collection("productos").document();
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
                            map.put("producto", download_uri);
                            mFirestore.collection("productos").document(productoEvi).update(map);

                            Toast.makeText(Productos_FotoActivity.this, "Foto actualizada", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Productos_FotoActivity.this, "Error al cargar foto", Toast.LENGTH_SHORT).show();
            }
        });





    }





    private void verificarPermisos(){


        int PermisosCamera = ContextCompat.checkSelfPermission(Productos_FotoActivity.this, Manifest.permission.CAMERA);

        int PermisosStorageWrite = ContextCompat.checkSelfPermission(Productos_FotoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        int PermisosStorageRead = ContextCompat.checkSelfPermission(Productos_FotoActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

            if (PermisosCamera == PackageManager.PERMISSION_GRANTED && PermisosStorageWrite == PackageManager.PERMISSION_GRANTED && PermisosStorageRead == PackageManager.PERMISSION_GRANTED){

                abrirCamara();

            }else{

                requestPermissions(new String[]{Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200);

            }


        }else{

            abrirCamara();


        }






    }










    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.btnCamara){
            verificarPermisos();
        }else if (id == R.id.btnGuardar){

        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.calculadora_drawer, menu);
        return true;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.volver_inicio:








                if (pedidoinfo.equals("cliente")){

                    Intent i = new Intent(Productos_FotoActivity.this, InfoClienteActivity.class);

                    i.putExtra("id_pet",id);
                    i.putExtra("name",nameCli);

                    overridePendingTransition(R.anim.left_in, R.anim.left_out);


                    startActivity(i);

                    return true;

                }else if (pedidoinfo.equals("pedido")){

                    Intent i = new Intent(Productos_FotoActivity.this, DetallesProductosRepartidorActivity.class);

                    i.putExtra("id_user",clientecodigo);
                    i.putExtra("name",nameCli);
                    i.putExtra("user",iduseruu);

                    overridePendingTransition(R.anim.left_in, R.anim.left_out);


                    startActivity(i);

                    return true;


                }










        }


        return super.onOptionsItemSelected(item);
    }







}