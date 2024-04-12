package com.aplication.appgestionrepartos.repartidor;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aplication.appgestionrepartos.R;
import com.aplication.appgestionrepartos.interfaz.InterfazRepartidorActivity;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;


public class FirmaDetallesFragment extends Fragment {





    ImageView photo_pet;
    LinearLayout linearLayout_image_btn;
    Button btn_atras;

    ImageView photo_petfirma;
    Button btn_paint_photo, btn_delete_photo, btn_productos_foto,btn_delete_producto;
    LinearLayout linearLayout_image_btnfirma;


    ImageView photo_petproducto;


    TextView nombre, apellido, dni, direccion, email;
    private FirebaseFirestore mfirestore;
    private FirebaseAuth mAuth;

    StorageReference storageReference;
    String storage_path = "usuarios/*";

    private static final int COD_SEL_STORAGE = 200;
    private static final int COD_SEL_IMAGE = 300;

    private Uri image_url;


    String photo = "photo";
    String idd,nombreClien;

    private String idPerfil;
    private String nombrePerfil;

    ProgressDialog progressDialog;

    private String nombreCliente;


    public final static String EXTRA_MESSAGE = "com.aplication.appgestionrepartos.repartidor";


    String id;

    String nameCli;

    String iduseruu;




    public FirmaDetallesFragment() {
        // Required empty public constructor
    }


    public static FirmaDetallesFragment newInstance(String param1, String param2) {
        FirmaDetallesFragment fragment = new FirmaDetallesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_firma_detalles, container, false);

        id = getActivity().getIntent().getStringExtra("id_user");
        nameCli = getActivity().getIntent().getStringExtra("name");

        iduseruu = getActivity().getIntent().getStringExtra("user");




        mfirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();


        nombre = view.findViewById(R.id.nombre);
        nombre.setEnabled(false);

        apellido = view.findViewById(R.id.apellido);
        apellido.setEnabled(false);


        dni = view.findViewById(R.id.dni);
        dni.setEnabled(false);

        direccion = view.findViewById(R.id.direccion);
        direccion.setEnabled(false);

        email = view.findViewById(R.id.correo);
        email.setEnabled(false);



        verificarPermisos();




        linearLayout_image_btn = view.findViewById(R.id.images_btn);

        photo_pet = view.findViewById(R.id.pet_photo);

        photo_petfirma = view.findViewById(R.id.firma_photo);


        photo_petproducto = view.findViewById(R.id.producto_photo);



        btn_atras = view.findViewById(R.id.btn_atras);


        linearLayout_image_btnfirma = view.findViewById(R.id.images_btnfirma);



        btn_paint_photo = view.findViewById(R.id.btn_photo1);

        btn_delete_photo = view.findViewById(R.id.btn_remove_photo1);




        btn_productos_foto = view.findViewById(R.id.btn_photo_producto);

        btn_delete_producto = view.findViewById(R.id.btn_remove_photo_producto);









        btn_paint_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //startActivity(new Intent(InfoClienteActivity.this, MainPaintActivity.class));


                Intent i = new Intent(view.getContext(), PaintActivity.class);

                nombreCliente = nombre.getText().toString();
                i.putExtra("id_firma", nombreCliente);

                i.putExtra("id_pet", id);
                i.putExtra("name", nameCli);

                i.putExtra("user",iduseruu);

                i.putExtra("id_user",id);

                i.putExtra("cliente","pedido");






                getActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);

                startActivity(i);

            }
        });


        btn_delete_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                HashMap<String, Object> map = new HashMap<>();
                map.put("firma", "");
                mfirestore.collection("firmas").document(nombreClien).update(map);
                //mfirestore.collection("Cliente").document(nombre).update(map);
                Toast.makeText(view.getContext(), "Foto eliminada", Toast.LENGTH_SHORT).show();
            }
        });




        btn_productos_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(view.getContext(), Productos_FotoActivity.class);

                nombreCliente = nombre.getText().toString();
                i.putExtra("id_producto", nombreCliente);

                i.putExtra("id_pet", id);
                i.putExtra("name", nameCli);

                i.putExtra("user",iduseruu);

                i.putExtra("id_user",id);


                i.putExtra("cliente","pedido");




                getActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);

                startActivity(i);




            }
        });




        btn_delete_producto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                HashMap<String, Object> map = new HashMap<>();
                map.put("producto", "");
                mfirestore.collection("productos").document(nombreClien).update(map);
                //mfirestore.collection("Cliente").document(nombre).update(map);
                Toast.makeText(view.getContext(), "Foto eliminada", Toast.LENGTH_SHORT).show();




            }
        });


        if (id == null || id == "") {


            btn_atras.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String nombreRe = nombre.getText().toString().trim();
                    String apellidoRe = apellido.getText().toString().trim();

                    String dniRe = dni.getText().toString().trim();
                    String direccionRe = direccion.getText().toString().trim();



                    if (nombreRe.isEmpty() && apellidoRe.isEmpty() && dniRe.isEmpty() && direccionRe.isEmpty()) {
                        Toast.makeText(view.getContext(), "Datos no obtenidos", Toast.LENGTH_SHORT).show();


                    } else {
                        //startActivity(new Intent(view.getContext(), InterfazRepartidorActivity.class));
                        //getActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);


                    }


                }
            });

        } else {

            idd = id;
            getPetPhoto(id,view);

            nombreClien = nameCli;
            getPetFirma(nameCli);
            getPetProductos_Fotos(nameCli);





            btn_atras.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String nombreRe = nombre.getText().toString().trim();
                    String apellidoRe = apellido.getText().toString().trim();
                    String dniRe = dni.getText().toString().trim();
                    String direccionRe = direccion.getText().toString().trim();



                    if (nombreRe.isEmpty() && apellidoRe.isEmpty()  && dniRe.isEmpty() && direccionRe.isEmpty()) {
                        Toast.makeText(view.getContext(), "Datos no obtenidos", Toast.LENGTH_SHORT).show();


                    } else {
                        Toast.makeText(view.getContext(), "Actualizacion exitosa", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(view.getContext(), InterfazRepartidorActivity.class));
                        getActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);


                    }


                }
            });

        }







        return view;



    }











    private void getPetPhoto(String id, View v){

        mfirestore.collection("Cliente").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String nombreRe = documentSnapshot.getString("name");

                String apellidoRe = documentSnapshot.getString("apellido");

                String dniRe = documentSnapshot.getString("dni");
                String direccionRe = documentSnapshot.getString("direccion");
                String emailRe = documentSnapshot.getString("email");


                String photoRe = documentSnapshot.getString("photo");


                nombre.setText(nombreRe);
                apellido.setText(apellidoRe);

                dni.setText(dniRe);
                direccion.setText(direccionRe);
                email.setText(emailRe);

                try {
                    if(!photoRe.equals("")){
                        Toast toast = Toast.makeText(v.getContext(), "Cargando foto", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP,0,200);
                        toast.show();


                        // Picasso.with(CreatePetActivity.this)
                        //        .load(photoPet)
                        //        .resize(150, 150)
                        //        .into(photo_pet);

                        Glide.with(getContext())
                                .load(photoRe)
                                .circleCrop()
                                .into(photo_pet);


                    }




                }catch (Exception e){
                    Log.v("Error", "e: " + e);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error al obtener los datos", Toast.LENGTH_SHORT).show();
            }
        });
    }






    private void getPetFirma(String nombreClien){

        mfirestore.collection("firmas").document(nombreClien).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String firmaRe = documentSnapshot.getString("firma");


                try {

                    if(!firmaRe.equals("")){
                        //Toast toast = Toast.makeText(getApplicationContext(), "Cargando foto", Toast.LENGTH_SHORT);
                        //toast.setGravity(Gravity.TOP,0,200);
                        //toast.show();


                        Glide.with(getContext())
                                .load(firmaRe)
                                .circleCrop()
                                .into(photo_petfirma);



                    }



                }catch (Exception e){
                    Log.v("Error", "e: " + e);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error al obtener los datos", Toast.LENGTH_SHORT).show();
            }
        });
    }












    private void getPetProductos_Fotos(String nombreClien){

        mfirestore.collection("productos").document(nombreClien).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String productoRe = documentSnapshot.getString("producto");


                try {

                    if(!productoRe.equals("")){
                        //Toast toast = Toast.makeText(getApplicationContext(), "Cargando foto", Toast.LENGTH_SHORT);
                        //toast.setGravity(Gravity.TOP,0,200);
                        //toast.show();


                        Glide.with(getContext())
                                .load(productoRe)
                                .circleCrop()
                                .into(photo_petproducto);



                    }



                }catch (Exception e){
                    Log.v("Error", "e: " + e);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error al obtener los datos", Toast.LENGTH_SHORT).show();
            }
        });
    }




    private void verificarPermisos(){


        int PermisosLocation = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);




        if (PermisosLocation == PackageManager.PERMISSION_GRANTED){

            //Toast.makeText(OnboardingActivity.this, "Permiso SMS Concedido", Toast.LENGTH_SHORT).show();

            //Toast.makeText(OnboardingActivity.this, "Permiso Location Concedido", Toast.LENGTH_SHORT).show();







        }else{

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);

        }


    }










}