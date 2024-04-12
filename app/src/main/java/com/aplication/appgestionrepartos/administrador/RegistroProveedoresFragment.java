package com.aplication.appgestionrepartos.administrador;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.aplication.appgestionrepartos.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;


public class RegistroProveedoresFragment extends Fragment {


    ImageView photo_proveedor;

    Button btn_registrar;

    Button btn_cu_photo, btn_r_photo;



    LinearLayout linearLayout_image_btn;

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



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        id = getActivity().getIntent().getStringExtra("id_proveedor");

        mfirestore = FirebaseFirestore.getInstance();

        mAuth = FirebaseAuth.getInstance();

        storageReference = FirebaseStorage.getInstance().getReference();







    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registro_proveedores, container, false);

        progressDialog = new ProgressDialog(view.getContext());


        linearLayout_image_btn = view.findViewById(R.id.images_btn);






        nombre = view.findViewById(R.id.nombre);

        apellido = view.findViewById(R.id.apellido);

        dni = view.findViewById(R.id.dni);

        direccion = view.findViewById(R.id.direccion);

        email = view.findViewById(R.id.email);

        empresa = view.findViewById(R.id.empresa);

        celular = view.findViewById(R.id.celular);



        photo_proveedor = view.findViewById(R.id.perfil_photo);

        btn_cu_photo = view.findViewById(R.id.btn_subir_photo);
        btn_r_photo = view.findViewById(R.id.btn_eliminar_photo);

        btn_registrar = view.findViewById(R.id.btn_registrar);



        /*


        btn_cu_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadPhoto();
            }
        });

        */


        btn_r_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("photo", "");
                //mfirestore.collection("Proveedores").document(idd).update(map);

                //Toast.makeText(CreatePetActivity.this, "Foto eliminada", Toast.LENGTH_SHORT).show();
            }
        });




        if (id == null || id == ""){



            btn_registrar.setText("REGISTRAR");

            linearLayout_image_btn.setVisibility(View.GONE);



            btn_registrar.setOnClickListener(new View.OnClickListener() {
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



                        postPet(nombreproveedor, apellidoproveedor, dniproveedor, direccionproveedor, emailproveedor, empresaproveedor, celularproveedor);

                        nombre.setText("");
                        apellido.setText("");
                        dni.setText("");
                        direccion.setText("");
                        email.setText("");
                        empresa.setText("");
                        celular.setText("");



                    }













                }
            });










        }else{




            String nombrepropet = nombre.getText().toString().trim();
            String nombreproveedor = ucFirst(nombrepropet);
            String apellidopet = apellido.getText().toString().trim();
            String apellidoproveedor = ucFirst(apellidopet);
            String dniproveedor = dni.getText().toString().trim();
            String direccionproveedor = direccion.getText().toString().trim();
            String emailproveedor = email.getText().toString().trim();
            String empresaproveedor = empresa.getText().toString().trim();
            String celularproveedor = celular.getText().toString().trim();
















        }












        return view;

    }






    public static String ucFirst(String str) {
        if (str == null || str.isEmpty()) {
            return "";
        } else {
            return  Character.toUpperCase(str.charAt(0)) + str.substring(1, str.length()).toLowerCase();
        }
    }





    public void postPet(String nombreproveedor, String apellidoproveedor, String dniproveedor, String direccionproveedor, String emailproveedor, String empresaproveedor, String celularproveedor) {

        String idUser = mAuth.getCurrentUser().getUid();

        DocumentReference id = mfirestore.collection("Proveedores").document();



        Map<String, Object> map = new HashMap<>();

        map.put("id_user", idUser);
        map.put("id", id.getId());
        map.put("nombre", nombreproveedor);
        map.put("apellido", apellidoproveedor);
        map.put("dni", dniproveedor);
        map.put("direccion", direccionproveedor);
        map.put("email",emailproveedor);
        map.put("empresa", empresaproveedor);
        map.put("celular",celularproveedor);




        mfirestore.collection("Proveedores").document(idUser).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                Toast.makeText(getContext(), "Proveedor Creado", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error al Crear al Proveedor", Toast.LENGTH_SHORT).show();

            }
        });













    }






}