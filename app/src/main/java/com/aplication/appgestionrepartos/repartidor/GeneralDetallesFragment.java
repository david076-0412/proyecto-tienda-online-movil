package com.aplication.appgestionrepartos.repartidor;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import androidx.annotation.NonNull;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aplication.appgestionrepartos.R;
import com.aplication.appgestionrepartos.administrador.PedidoEditarActivity;
import com.aplication.appgestionrepartos.model.Pedido;
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


import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;


public class GeneralDetallesFragment extends Fragment implements AdapterView.OnItemSelectedListener{



    ImageView photo_pet;
    Button btn_add;
    Button btn_cu_photo, btn_r_photo;
    LinearLayout linearLayout_image_btn;

    TextView txtobservacion_cliente;

    EditText name, nombre, apellido, color, precio_vacuna, hora,observacion_cliente,observacion_repartidor;
    FirebaseFirestore mfirestore;
    FirebaseAuth mAuth;


    StorageReference storageReference;
    String storage_path = "productos/*";

    public static final int COD_SEL_STORAGE = 200;
    public static final int COD_SEL_IMAGE = 300;

    public Uri image_url;
    String photo = "photo";
    String idd;

    ProgressDialog progressDialog;


    public Spinner mSpinner;

    public String item;

    String[] Estado = {"Pendiente", "Entregado","Enviado"};



    public Spinner mSpinnerPago;

    public String itempago;

    String[] EstadoPago = {"en espera", "Pagado"};


    public String codigoCliente;

    public String iddt,id;



    public int dia,mes,ano;







    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        id = getActivity().getIntent().getStringExtra("user");

        iddt = getActivity().getIntent().getStringExtra("id_user");

        mfirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();




    }



    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_general_detalles, container, false);




        progressDialog = new ProgressDialog(view.getContext());


        linearLayout_image_btn = view.findViewById(R.id.images_btn);


        codigoCliente = getActivity().getIntent().getStringExtra("id_user");


        name = (EditText) view.findViewById(R.id.nombre);

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obtenerFecha(view);
            }
        });


        hora = view.findViewById(R.id.hora);
        hora.setEnabled(false);



        nombre = view.findViewById(R.id.edad);
        nombre.setEnabled(false);


        apellido = view.findViewById(R.id.apellido);
        apellido.setEnabled(false);



        color = view.findViewById(R.id.color);
        precio_vacuna = view.findViewById(R.id.precio_vacuna);

        observacion_cliente = view.findViewById(R.id.observacion_cliente);

        txtobservacion_cliente = view.findViewById(R.id.txtobservaciones_cliente);


        observacion_repartidor = view.findViewById(R.id.observacion_repartidor);


        mfirestore.collection("comprasfinal/"+codigoCliente+"/cliente").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String nombrecliente = documentSnapshot.getString("nombre");

                String apellidocliente = documentSnapshot.getString("apellido");


                txtobservacion_cliente.setText("observaciones de "+nombrecliente+" "+apellidocliente);


            }
        });





        photo_pet = view.findViewById(R.id.pet_photo);


        //btn_cu_photo = findViewById(R.id.btn_photo);
        //btn_r_photo = findViewById(R.id.btn_remove_photo);

        mSpinner = (Spinner) view.findViewById(R.id.spn);
        mSpinner.setOnItemSelectedListener(this);


        ArrayAdapter arrayAdapter = new ArrayAdapter(view.getContext(), android.R.layout.simple_spinner_dropdown_item, Estado);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinner.setAdapter(arrayAdapter);



        mSpinnerPago = (Spinner) view.findViewById(R.id.spn_pago);
        mSpinnerPago.setOnItemSelectedListener(this);


        ArrayAdapter arrayAdapterPago = new ArrayAdapter(view.getContext(), android.R.layout.simple_spinner_dropdown_item, EstadoPago);
        arrayAdapterPago.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinnerPago.setAdapter(arrayAdapterPago);




        btn_add = view.findViewById(R.id.btn_add);




        if (id == null || id == ""){

            Toast.makeText(view.getContext(), "Datos no obtenido", Toast.LENGTH_SHORT).show();

        }else{

            idd = id;
            btn_add.setText("ACTUALIZAR");
            getPet(id, view);

            btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String namepet = name.getText().toString().trim();
                    String nombrepet = nombre.getText().toString().trim();
                    String apellidopet = apellido.getText().toString().trim();

                    //String colorpet = color.getText().toString().trim();
                    Double precio_vacunapet = Double.parseDouble(precio_vacuna.getText().toString().trim());

                    String observacion_clientepet = observacion_cliente.getText().toString().trim();
                    String observacion_repartidorpet =observacion_repartidor.getText().toString().trim();


                    if(namepet.isEmpty() && nombrepet.isEmpty() && apellidopet.isEmpty() && observacion_clientepet.isEmpty() && observacion_repartidorpet.isEmpty()){
                        Toast.makeText(view.getContext(), "Ingresar los datos", Toast.LENGTH_SHORT).show();
                        name.setError("El campo esta vacio");
                        nombre.setError("El campo esta vacio");
                        apellido.setError("El campo esta vacio");
                        //mSpinner.setError("El campo esta vacio");

                        observacion_cliente.setError("EL campo esta vacio");
                        observacion_repartidor.setError("El campo esta vacio");

                    }else if (namepet.isEmpty() && !nombrepet.isEmpty() && !apellidopet.isEmpty() && !observacion_clientepet.isEmpty() && !observacion_repartidorpet.isEmpty()){
                        name.setError("El campo esta vacio");


                    }else if (!namepet.isEmpty() && nombrepet.isEmpty() && !apellidopet.isEmpty() && !observacion_clientepet.isEmpty() && !observacion_repartidorpet.isEmpty()) {

                        nombre.setError("El campo esta vacio");


                    }else if (!namepet.isEmpty() && !nombrepet.isEmpty() && apellidopet.isEmpty() && !observacion_clientepet.isEmpty() && !observacion_repartidorpet.isEmpty()) {

                        apellido.setError("El campo esta vacio");


                    }else if (!namepet.isEmpty() && !nombrepet.isEmpty() && !apellidopet.isEmpty() && observacion_clientepet.isEmpty() && !observacion_repartidorpet.isEmpty()) {

                        observacion_cliente.setError("EL campo esta vacio");


                    }else if (!namepet.isEmpty() && !nombrepet.isEmpty() && !apellidopet.isEmpty() && !observacion_clientepet.isEmpty() && observacion_repartidorpet.isEmpty()) {

                        observacion_repartidor.setError("El campo esta vacio");


                    }else if (!namepet.isEmpty() && !nombrepet.isEmpty() && !apellidopet.isEmpty() && observacion_clientepet.isEmpty() && observacion_repartidorpet.isEmpty()) {


                        observacion_cliente.setError("EL campo esta vacio");
                        observacion_repartidor.setError("El campo esta vacio");


                    }

                    else if (!namepet.isEmpty() && !nombrepet.isEmpty() && !apellidopet.isEmpty() && !observacion_clientepet.isEmpty() && !observacion_repartidorpet.isEmpty()) {


                        updatePet(view,namepet, nombrepet, apellidopet, item, precio_vacunapet, observacion_clientepet,observacion_repartidorpet,id);


                        //startActivity(new Intent(view.getContext(), InterfazAdministradorActivity.class));

                        //getActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);

                        //getActivity().finish();


                    }



                }
            });



        }






        return view;




    }


    @SuppressLint("SimpleDateFormat")
    public static String obtenerFechaConFormato(String formato, String zonaHoraria) {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat(formato);
        sdf.setTimeZone(TimeZone.getTimeZone(zonaHoraria));
        return sdf.format(date);
    }

    public static String obtenerFechaActual(String zonaHoraria) {
        String formato = "dd-MM-yyyy";
        return obtenerFechaConFormato(formato, zonaHoraria);
    }

    public static String obtenerdiaactual(String zonaHoraria){
        String formato = "dd";
        return obtenerFechaConFormato(formato, zonaHoraria);

    }
    public static String obtenermesactual(String zonaHoraria){
        String formato = "MM";

        return obtenerFechaConFormato(formato, zonaHoraria);
    }


    public static String obteneranoactual(String zonaHoraria){
        String formato = "yyyy";

        return obtenerFechaConFormato(formato,zonaHoraria);

    }











    public void updatePet(View v,String namepet, String nombrepet, String apellidopet, String colorpet, Double precio_vacunapet, String observacion_cliente, String observacion_repartidor, String id) {


        Map<String, Object> map = new HashMap<>();

        map.put("name", namepet);

        map.put("nombre", nombrepet);
        map.put("apellido", apellidopet);

        map.put("color", item);
        map.put("vaccine_price", precio_vacunapet);

        map.put("observacion_cliente", observacion_cliente);
        map.put("observacion_repartidor",observacion_repartidor);



        map.put("pago",itempago);


        mfirestore.collection("comprasfinal/"+codigoCliente+"/cliente").document(id).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(v.getContext(), "Actualizado exitosamente", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(v.getContext(), "Error al actualizar", Toast.LENGTH_SHORT).show();
            }
        });



        Map<String, Object> mapT = new HashMap<>();


        mapT.put("precio_producto", precio_vacunapet);



        mfirestore.collection("comprasfinal/"+codigoCliente+"/MensajePago").document(id).update(mapT);






    }


    public void getPet(String id, View v){

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





                BigDecimal cc = new BigDecimal(precio_vacunapet);

                MathContext mc = new MathContext(3);

                //precio_vacuna.setText(String.valueOf(precio_vacunapet));

                precio_vacuna.setText(""+cc.round(mc));



                hora.setText(horaPet);




                mfirestore.collection("comprasfinal/"+codigoCliente+"/cliente").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
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
                                    Toast toast = Toast.makeText(v.getContext(), "Cargando foto", Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.TOP,0,200);
                                    toast.show();


                                    // Picasso.with(CreatePetActivity.this)
                                    //        .load(photoPet)
                                    //        .resize(150, 150)
                                    //        .into(photo_pet);

                                    Glide.with(v.getContext())
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
                Toast.makeText(v.getContext(), "Error al obtener los datos", Toast.LENGTH_SHORT).show();
            }
        });
    }



    public int getIndex(Spinner spinner, String s){

        for (int i=0; i< spinner.getCount(); i++){

            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(s)){
                return i;

            }

        }


        return 0;
    }






    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        item = mSpinner.getSelectedItem().toString();

        itempago = mSpinnerPago.getSelectedItem().toString();

    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public void obtenerFecha(View v){

        if (v==name){


            String dia = obtenerdiaactual("America/Lima");

            int diad = Integer.parseInt(dia);







            String mes = obtenermesactual("America/Lima");
            int mesd = Integer.parseInt(mes);


            String ano = obteneranoactual("America/Lima");
            int anod = Integer.parseInt(ano);




            final Calendar c = Calendar.getInstance();
            //dia = c.get(Calendar.DAY_OF_MONTH);
            //mes = c.get(Calendar.MONTH);
            //ano = c.get(Calendar.YEAR);

            DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {





                    String fdm = String.valueOf(dayOfMonth);

                    String mt = String.valueOf(month+1);

                    String yr = String.valueOf(year);

                    String fecha_entrega = fdm+"-"+mt+"-"+yr;




                    name.setText(fecha_entrega);




                }

            },diad,mesd,anod);

            datePickerDialog.show();


        }




    }





}