package com.aplication.appgestionrepartos.cliente_ui.compras;

import androidx.annotation.NonNull;
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
import android.widget.RatingBar;
import android.widget.Toast;

import com.aplication.appgestionrepartos.R;
import com.aplication.appgestionrepartos.cliente.ListadoProductosActivity;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DecimalFormat;

public class DetallesComprasActivity extends AppCompatActivity {


    ImageView photo_pet;
    Button btn_atras;

    LinearLayout linearLayout_image_btn;
    EditText name, age, color, precio_vacuna;
    RatingBar ratingBar;


    EditText totalcliente, totalestrellas, totalcalificacion;



    private FirebaseFirestore mfirestore;
    private FirebaseAuth mAuth;

    StorageReference storageReference;
    String storage_path = "productos/*";

    private static final int COD_SEL_STORAGE = 200;
    private static final int COD_SEL_IMAGE = 300;

    private Uri image_url;
    String photo = "photo";
    String idd;

    ProgressDialog progressDialog;


    String idProducto;

    String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_compras);


        this.setTitle("Detalle");
        progressDialog = new ProgressDialog(this);
        id = getIntent().getStringExtra("codigo");

        idProducto = getIntent().getStringExtra("id_pro");

        mfirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        linearLayout_image_btn = findViewById(R.id.images_btn);






        getPet(id, idProducto);


        name = findViewById(R.id.nombre);
        name.setEnabled(false);


        age = findViewById(R.id.edad);
        age.setEnabled(false);


        color = findViewById(R.id.color);
        color.setEnabled(false);


        precio_vacuna = findViewById(R.id.precio_vacuna);
        precio_vacuna.setEnabled(false);






        photo_pet = findViewById(R.id.pet_photo);


        btn_atras = findViewById(R.id.btn_atras);



        btn_atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i = new Intent(DetallesComprasActivity.this, ListadoProductosActivity.class);

                i.putExtra("codigo",id);


                startActivity(i);


            }
        });







    }









    private void getPet(String id, String idProducto){

        String idUser = mAuth.getCurrentUser().getUid();

        mfirestore.collection("compras/"+idUser+"/"+id).document(idProducto).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                DecimalFormat format = new DecimalFormat("0.00");
                String namePet = documentSnapshot.getString("name");
                Double agePet = documentSnapshot.getDouble("age");
                String colorPet = documentSnapshot.getString("color");
                Double precio_vacunapet = documentSnapshot.getDouble("vaccine_price");
                String photoPet = documentSnapshot.getString("photo");


                name.setText(namePet);
                age.setText(String.valueOf(agePet));
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

                        Glide.with(DetallesComprasActivity.this)
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










}