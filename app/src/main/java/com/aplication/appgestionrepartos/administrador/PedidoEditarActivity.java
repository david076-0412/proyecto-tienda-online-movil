package com.aplication.appgestionrepartos.administrador;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.aplication.appgestionrepartos.R;
import com.aplication.appgestionrepartos.interfaz.InterfazAdministradorActivity;
import com.aplication.appgestionrepartos.model.Pedido;
import com.aplication.appgestionrepartos.model.Usuarios;
import com.bumptech.glide.Glide;
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
//import com.squareup.picasso.Picasso;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PedidoEditarActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    ImageView photo_pet;
    Button btn_add, btn_atras;
    Button btn_cu_photo, btn_r_photo;
    LinearLayout linearLayout_image_btn;
    EditText name, nombre, apellido, color, precio_vacuna, hora;
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


    private Spinner mSpinner;

    private String item;

    String[] Estado = {"Pendiente", "Entregado","Enviado"};



    private Spinner mSpinnerPago;

    private String itempago;

    String[] EstadoPago = {"en espera", "Pagado"};


    private String codigoCliente;

    private String iddt;



    private int dia,mes,ano;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido_editar);

        this.setTitle("Producto Total");
        progressDialog = new ProgressDialog(this);

        String id = getIntent().getStringExtra("id_user");

        iddt = getIntent().getStringExtra("id_user");

        mfirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        linearLayout_image_btn = findViewById(R.id.images_btn);


        codigoCliente = getIntent().getStringExtra("user");


        name = findViewById(R.id.nombre);

        hora = findViewById(R.id.hora);
        hora.setEnabled(false);



        nombre = findViewById(R.id.edad);
        apellido = findViewById(R.id.apellido);

        color = findViewById(R.id.color);
        precio_vacuna = findViewById(R.id.precio_vacuna);
        photo_pet = findViewById(R.id.pet_photo);
        //btn_cu_photo = findViewById(R.id.btn_photo);
        //btn_r_photo = findViewById(R.id.btn_remove_photo);

        mSpinner = (Spinner) findViewById(R.id.spn);
        mSpinner.setOnItemSelectedListener(this);


        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, Estado);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinner.setAdapter(arrayAdapter);



        mSpinnerPago = (Spinner) findViewById(R.id.spn_pago);
        mSpinnerPago.setOnItemSelectedListener(this);


        ArrayAdapter arrayAdapterPago = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, EstadoPago);
        arrayAdapterPago.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinnerPago.setAdapter(arrayAdapterPago);




        btn_add = findViewById(R.id.btn_add);
        btn_atras = findViewById(R.id.btn_atras);

        /*
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
                mfirestore.collection("pet").document(idd).update(map);
                Toast.makeText(PedidoEditarActivity.this, "Foto eliminada", Toast.LENGTH_SHORT).show();
            }
        });
*/

        if (id == null || id == ""){

            Toast.makeText(getApplicationContext(), "Datos no obtenido", Toast.LENGTH_SHORT).show();

        }else{

            idd = id;
            btn_add.setText("ACTUALIZAR");
            getPet(id);

            btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String namepet = name.getText().toString().trim();
                    String nombrepet = nombre.getText().toString().trim();
                    String apellidopet = apellido.getText().toString().trim();

                    //String colorpet = color.getText().toString().trim();
                    Double precio_vacunapet = Double.parseDouble(precio_vacuna.getText().toString().trim());



                    if(namepet.isEmpty() && nombrepet.isEmpty() && apellidopet.isEmpty()){
                        Toast.makeText(getApplicationContext(), "Ingresar los datos", Toast.LENGTH_SHORT).show();
                        name.setError("El campo esta vacio");
                        nombre.setError("El campo esta vacio");
                        apellido.setError("El campo esta vacio");
                        //mSpinner.setError("El campo esta vacio");

                    }else{

                        updatePet(namepet, nombrepet, apellidopet, item, precio_vacunapet, id);
                        startActivity(new Intent(PedidoEditarActivity.this, InterfazAdministradorActivity.class));

                        overridePendingTransition(R.anim.left_in, R.anim.left_out);

                        finish();

                    }
                }
            });



        }

        btn_atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(PedidoEditarActivity.this, InterfazAdministradorActivity.class));

                overridePendingTransition(R.anim.left_in, R.anim.left_out);

                finish();
            }
        });



    }



    public void Fecha(View v){

        if (v==name){
            final Calendar c = Calendar.getInstance();
            dia = c.get(Calendar.DAY_OF_MONTH);
            mes = c.get(Calendar.MONTH);
            ano = c.get(Calendar.YEAR);

            DatePickerDialog datePickerDialog = new DatePickerDialog(PedidoEditarActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {





                    String fdm = String.valueOf(dayOfMonth);

                    String mt = String.valueOf(month+1);

                    String yr = String.valueOf(year);

                    String fecha_entrega = fdm+"-"+mt+"-"+yr;

                    name.setText(fecha_entrega);




                }
            },dia,mes,ano);

            datePickerDialog.show();


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


            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }



    private void updatePet(String namepet, String nombrepet, String apellidopet, String colorpet, Double precio_vacunapet, String id) {
        Map<String, Object> map = new HashMap<>();




        map.put("name", namepet);

        map.put("nombre", nombrepet);
        map.put("apellido", apellidopet);

        map.put("color", item);
        map.put("vaccine_price", precio_vacunapet);

        map.put("pago",itempago);





        mfirestore.collection("comprasfinal/"+codigoCliente+"/cliente").document(id).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    }


    private void getPet(String id){

        final List<Pedido> Estados = new ArrayList<>();

        mfirestore.collection("comprasfinal/"+codigoCliente+"/cliente").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                DecimalFormat format = new DecimalFormat("0.00");
                String namePet = documentSnapshot.getString("name");
                String nombrePet = documentSnapshot.getString("nombre");
                String apellidoPet = documentSnapshot.getString("apellido");

                String horaPet = documentSnapshot.getString("hora");


                Double precio_vacunapet = documentSnapshot.getDouble("vaccine_price");

                name.setText(namePet);
                nombre.setText(nombrePet);
                apellido.setText(apellidoPet);

                //color.setText(colorPet);
                precio_vacuna.setText(format.format(precio_vacunapet));

                hora.setText(horaPet);



                mfirestore.collection("comprasfinal/"+codigoCliente+"/cliente").document(iddt).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {


                        String estadoPet = documentSnapshot.getString("color");


                        mSpinner.setSelection(getIndex(mSpinner,estadoPet));


                        String estadoPago = documentSnapshot.getString("pago");

                        mSpinnerPago.setSelection(getIndex(mSpinnerPago, estadoPago));




                    }
                });









                mfirestore.collection("Usuario/Cliente/"+codigoCliente).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot snapshot : task.getResult()){

                            String photoPet = snapshot.getString("photo");


                            try {
                                if(!photoPet.equals("")){
                                    Toast toast = Toast.makeText(getApplicationContext(), "Cargando foto", Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.TOP,0,200);
                                    toast.show();


                                    // Picasso.with(CreatePetActivity.this)
                                    //        .load(photoPet)
                                    //        .resize(150, 150)
                                    //        .into(photo_pet);

                                    Glide.with(PedidoEditarActivity.this)
                                            .load(photoPet)
                                            .circleCrop()
                                            .error(R.drawable.ic_launcher_background)
                                            .into(photo_pet);



                                }

                            }catch (Exception e){
                                Log.v("Error", "e: " + e);
                            }





                        }
                    }
                });










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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        item = mSpinner.getSelectedItem().toString();

        itempago = mSpinnerPago.getSelectedItem().toString();

    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }






}