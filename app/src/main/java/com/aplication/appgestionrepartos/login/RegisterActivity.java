package com.aplication.appgestionrepartos.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.aplication.appgestionrepartos.chat.LoginActivity;
import com.aplication.appgestionrepartos.R;
import com.aplication.appgestionrepartos.no_internet_connection.NetworkChangeListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Button btn_register, btn_regresar;
    Button btn_cu_photo, btn_r_photo;
    EditText name, apellido, email, dni, password,direccion, celular, tarjeta, latitud,longitud;
    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;




    ImageView photo_pet;


    private String nameUser="";
    private String apellidoUser="";
    private String emailUser="";
    private String dniUser="";
    private String direccionUser="";
    private String celularUser="";
    private String tarjetaUser="";


    private double latitudUser = 0.0;

    private double longitudUser = 0.0;


    private String passUser="";

    private Spinner mSpinner;

    private String item;

    String[] Usuarios = {"Seleccionar", "Cliente", "Repartidor", "Administrador"};

    LinearLayout linearLayout_image_btn;

    ProgressDialog progressDialog;



    NetworkChangeListener networkChangeListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.setTitle("Registro");
        progressDialog = new ProgressDialog(this);


        networkChangeListener = new NetworkChangeListener();


        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        String id = getIntent().getStringExtra("id_pet");
        linearLayout_image_btn = findViewById(R.id.images_btn);
        name = findViewById(R.id.nombre);
        apellido = findViewById(R.id.apellido);
        email = findViewById(R.id.correo);
        dni = findViewById(R.id.dni);
        celular = findViewById(R.id.celular);
        tarjeta = findViewById(R.id.tarjeta_credito);




        password = findViewById(R.id.contrasena);
        direccion = findViewById(R.id.direccion);
        photo_pet = findViewById(R.id.pet_photo);


        latitud = findViewById(R.id.latitud);

        longitud = findViewById(R.id.longitud);





        btn_register = findViewById(R.id.btn_registro);


        btn_regresar = findViewById(R.id.btn_regresar);

        btn_regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
                finish();

            }
        });





        mSpinner = (Spinner) findViewById(R.id.spn);
        mSpinner.setOnItemSelectedListener(this);


        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, Usuarios);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinner.setAdapter(arrayAdapter);



        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        } else {
            locationStart();
        }



        linearLayout_image_btn.setVisibility(View.GONE);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                String nameUs = name.getText().toString().trim();


                nameUser = ucFirst(nameUs);



                String apellidoUs = apellido.getText().toString().trim();

                apellidoUser = ucFirst(apellidoUs);


                emailUser = email.getText().toString().trim();
                dniUser = dni.getText().toString().trim();
                direccionUser = direccion.getText().toString().trim();
                celularUser = celular.getText().toString().trim();
                tarjetaUser = tarjeta.getText().toString().trim();

                passUser = password.getText().toString().trim();


                latitudUser = Double.parseDouble(latitud.getText().toString().trim());

                longitudUser = Double.parseDouble(longitud.getText().toString().trim());


                if (item == "Seleccionar" && nameUser.isEmpty() && apellidoUser.isEmpty() && emailUser.isEmpty() && dniUser.isEmpty() && celularUser.isEmpty() && direccionUser.isEmpty() && tarjetaUser.isEmpty() && passUser.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Complete los datos", Toast.LENGTH_SHORT).show();
                    name.setError("El campo esta vacio");
                    apellido.setError("El campo esta vacio");
                    email.setError("El campo esta vacio");
                    dni.setError("El dni debe tener menos de 9 numeros");
                    direccion.setError("la direccion debe existir");
                    celular.setError("El celular debe tener un maximo de 9 numeros");
                    tarjeta.setError("Introducir tu tarjeta de credito o numero de cuenta");

                    password.setError("la contraseña debe tener mas de 6 digitos");




                }else if (nameUser.isEmpty() && !apellidoUser.isEmpty() && !emailUser.isEmpty() && !dniUser.isEmpty() && !celularUser.isEmpty() && !direccionUser.isEmpty() && !tarjetaUser.isEmpty() && !passUser.isEmpty() && item == "Seleccionar"){

                    name.setError("El campo esta vacio");

                    Toast.makeText(RegisterActivity.this, "Seleccione un Usuario", Toast.LENGTH_SHORT).show();

                }else if (!nameUser.isEmpty() && apellidoUser.isEmpty() && !emailUser.isEmpty() && !dniUser.isEmpty() && !celularUser.isEmpty() && !direccionUser.isEmpty() && !tarjetaUser.isEmpty() && !passUser.isEmpty()&& item == "Seleccionar"){

                    apellido.setError("El campo esta vacio");
                    Toast.makeText(RegisterActivity.this, "Seleccione un Usuario", Toast.LENGTH_SHORT).show();

                }else if (!nameUser.isEmpty() && !apellidoUser.isEmpty() && emailUser.isEmpty() && !dniUser.isEmpty() && !celularUser.isEmpty() && !direccionUser.isEmpty() && !tarjetaUser.isEmpty() && !passUser.isEmpty()&& item == "Seleccionar"){

                    email.setError("El campo esta vacio");
                    Toast.makeText(RegisterActivity.this, "Seleccione un Usuario", Toast.LENGTH_SHORT).show();

                }else if (!nameUser.isEmpty() && !apellidoUser.isEmpty() && !emailUser.isEmpty() && dniUser.isEmpty() && !celularUser.isEmpty() && !direccionUser.isEmpty() && !tarjetaUser.isEmpty() && !passUser.isEmpty()&& item == "Seleccionar"){

                    dni.setError("El dni debe tener menos de 9 numeros");
                    Toast.makeText(RegisterActivity.this, "Seleccione un Usuario", Toast.LENGTH_SHORT).show();

                }else if (!nameUser.isEmpty() && !apellidoUser.isEmpty() && !emailUser.isEmpty() && !dniUser.isEmpty() && !celularUser.isEmpty() && direccionUser.isEmpty() && !tarjetaUser.isEmpty() && !passUser.isEmpty()&& item == "Seleccionar"){

                    direccion.setError("la direccion debe existir");

                    Toast.makeText(RegisterActivity.this, "Seleccione un Usuario", Toast.LENGTH_SHORT).show();

                }else if (!nameUser.isEmpty() && !apellidoUser.isEmpty() && !emailUser.isEmpty() && !dniUser.isEmpty() && celularUser.isEmpty() && !direccionUser.isEmpty() && !tarjetaUser.isEmpty() && !passUser.isEmpty()&& item == "Seleccionar"){

                    celular.setError("El celular debe tener un maximo de 9 numeros");
                    Toast.makeText(RegisterActivity.this, "Seleccione un Usuario", Toast.LENGTH_SHORT).show();

                }else if (!nameUser.isEmpty() && !apellidoUser.isEmpty() && !emailUser.isEmpty() && !dniUser.isEmpty() && !celularUser.isEmpty() && !direccionUser.isEmpty() && tarjetaUser.isEmpty() && !passUser.isEmpty()&& item == "Seleccionar"){

                    tarjeta.setError("Introducir tu tarjeta de credito o numero de cuenta");
                    Toast.makeText(RegisterActivity.this, "Seleccione un Usuario", Toast.LENGTH_SHORT).show();

                }else if (!nameUser.isEmpty() && !apellidoUser.isEmpty() && !emailUser.isEmpty() && !dniUser.isEmpty() && !celularUser.isEmpty() && !direccionUser.isEmpty() && !tarjetaUser.isEmpty() && passUser.isEmpty()&& item == "Seleccionar"){

                    password.setError("la contraseña debe tener mas de 6 digitos");
                    Toast.makeText(RegisterActivity.this, "Seleccione un Usuario", Toast.LENGTH_SHORT).show();

                }else if (!nameUser.isEmpty() && !apellidoUser.isEmpty() && !emailUser.isEmpty() && !dniUser.isEmpty() && !celularUser.isEmpty() && !direccionUser.isEmpty() && !tarjetaUser.isEmpty() && !passUser.isEmpty()&& item == "Seleccionar"){


                    Toast.makeText(RegisterActivity.this, "Seleccione un Usuario", Toast.LENGTH_SHORT).show();

                    name.setError("El campo esta vacio");
                    apellido.setError("El campo esta vacio");
                    email.setError("El campo esta vacio");
                    dni.setError("El dni debe tener menos de 9 numeros");
                    direccion.setError("la direccion debe existir");
                    celular.setError("El celular debe tener un maximo de 9 numeros");
                    tarjeta.setError("Introducir tu tarjeta de credito o numero de cuenta");

                    password.setError("la contraseña debe tener mas de 6 digitos");


                }









                else if (nameUser.isEmpty() && !apellidoUser.isEmpty() && !emailUser.isEmpty() && !dniUser.isEmpty() && !celularUser.isEmpty() && !direccionUser.isEmpty() && !tarjetaUser.isEmpty() && !passUser.isEmpty() && item == "Cliente"){

                    name.setError("El campo esta vacio");



                }else if (!nameUser.isEmpty() && apellidoUser.isEmpty() && !emailUser.isEmpty() && !dniUser.isEmpty() && !celularUser.isEmpty() && !direccionUser.isEmpty() && !tarjetaUser.isEmpty() && !passUser.isEmpty()&& item == "Cliente"){

                    apellido.setError("El campo esta vacio");


                }else if (!nameUser.isEmpty() && !apellidoUser.isEmpty() && emailUser.isEmpty() && !dniUser.isEmpty() && !celularUser.isEmpty() && !direccionUser.isEmpty() && !tarjetaUser.isEmpty() && !passUser.isEmpty()&& item == "Cliente"){

                    email.setError("El campo esta vacio");


                }else if (!nameUser.isEmpty() && !apellidoUser.isEmpty() && !emailUser.isEmpty() && dniUser.isEmpty() && dni.length() == 8 && !celularUser.isEmpty() && !direccionUser.isEmpty() && !tarjetaUser.isEmpty() && !passUser.isEmpty()&& item == "Cliente"){

                    dni.setError("El dni debe tener menos de 9 numeros");


                }else if (!nameUser.isEmpty() && !apellidoUser.isEmpty() && !emailUser.isEmpty() && !dniUser.isEmpty() && !celularUser.isEmpty() && direccionUser.isEmpty() && !tarjetaUser.isEmpty() && !passUser.isEmpty()&& item == "Cliente"){

                    direccion.setError("la direccion debe existir");



                }else if (!nameUser.isEmpty() && !apellidoUser.isEmpty() && !emailUser.isEmpty() && !dniUser.isEmpty() && celularUser.isEmpty() && !direccionUser.isEmpty() && !tarjetaUser.isEmpty() && !passUser.isEmpty()&& item == "Cliente"){

                    celular.setError("El celular debe tener un maximo de 9 numeros");


                }else if (!nameUser.isEmpty() && !apellidoUser.isEmpty() && !emailUser.isEmpty() && !dniUser.isEmpty() && !celularUser.isEmpty() && !direccionUser.isEmpty() && !tarjetaUser.isEmpty() && passUser.isEmpty()&& item == "Cliente"){

                    password.setError("la contraseña debe tener mas de 6 digitos");

                }

                else if (!nameUser.isEmpty() && !apellidoUser.isEmpty() && !emailUser.isEmpty() && !dniUser.isEmpty() && dni.length() == 8 && !celularUser.isEmpty() && !direccionUser.isEmpty() && !tarjetaUser.isEmpty() && !passUser.isEmpty()&& item == "Cliente"){

                    registerUserCliente(nameUser, apellidoUser, emailUser, dniUser, direccionUser, celularUser, tarjetaUser, passUser, latitudUser, longitudUser);
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));

                    overridePendingTransition(R.anim.left_in, R.anim.left_out);





                }













                else if (nameUser.isEmpty() && !apellidoUser.isEmpty() && !emailUser.isEmpty() && !dniUser.isEmpty() && !celularUser.isEmpty() && !direccionUser.isEmpty() && !tarjetaUser.isEmpty() && !passUser.isEmpty() && item == "Repartidor"){

                    name.setError("El campo esta vacio");



                }else if (!nameUser.isEmpty() && apellidoUser.isEmpty() && !emailUser.isEmpty() && !dniUser.isEmpty() && !celularUser.isEmpty() && !direccionUser.isEmpty() && !tarjetaUser.isEmpty() && !passUser.isEmpty()&& item == "Repartidor"){

                    apellido.setError("El campo esta vacio");


                }else if (!nameUser.isEmpty() && !apellidoUser.isEmpty() && emailUser.isEmpty() && !dniUser.isEmpty() && !celularUser.isEmpty() && !direccionUser.isEmpty() && !tarjetaUser.isEmpty() && !passUser.isEmpty()&& item == "Repartidor"){

                    email.setError("El campo esta vacio");


                }else if (!nameUser.isEmpty() && !apellidoUser.isEmpty() && !emailUser.isEmpty() && dniUser.isEmpty() && dni.length() == 8 && !celularUser.isEmpty() && !direccionUser.isEmpty() && !tarjetaUser.isEmpty() && !passUser.isEmpty()&& item == "Repartidor"){

                    dni.setError("El dni debe tener menos de 9 numeros");


                }else if (!nameUser.isEmpty() && !apellidoUser.isEmpty() && !emailUser.isEmpty() && !dniUser.isEmpty() && !celularUser.isEmpty() && direccionUser.isEmpty() && !tarjetaUser.isEmpty() && !passUser.isEmpty()&& item == "Repartidor"){

                    direccion.setError("la direccion debe existir");



                }else if (!nameUser.isEmpty() && !apellidoUser.isEmpty() && !emailUser.isEmpty() && !dniUser.isEmpty() && celularUser.isEmpty() && !direccionUser.isEmpty() && !tarjetaUser.isEmpty() && !passUser.isEmpty()&& item == "Repartidor"){

                    celular.setError("El celular debe tener un maximo de 9 numeros");


                }else if (!nameUser.isEmpty() && !apellidoUser.isEmpty() && !emailUser.isEmpty() && !dniUser.isEmpty() && !celularUser.isEmpty() && !direccionUser.isEmpty() && !tarjetaUser.isEmpty() && passUser.isEmpty()&& item == "Repartidor"){

                    password.setError("la contraseña debe tener mas de 6 digitos");

                }else if (!nameUser.isEmpty() && !apellidoUser.isEmpty() && !emailUser.isEmpty() && !dniUser.isEmpty() && !celularUser.isEmpty() && !direccionUser.isEmpty() && !tarjetaUser.isEmpty() && !passUser.isEmpty()&& item == "Repartidor"){

                    registerUserRepartidor(nameUser, apellidoUser, emailUser, dniUser, direccionUser, celularUser, passUser, latitudUser, longitudUser, tarjetaUser);
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    overridePendingTransition(R.anim.left_in, R.anim.left_out);





                }









                else if (nameUser.isEmpty() && !apellidoUser.isEmpty() && !emailUser.isEmpty() && !dniUser.isEmpty() && !celularUser.isEmpty() && !direccionUser.isEmpty() && !tarjetaUser.isEmpty() && !passUser.isEmpty() && item == "Administrador"){

                    name.setError("El campo esta vacio");



                }else if (!nameUser.isEmpty() && apellidoUser.isEmpty() && !emailUser.isEmpty() && !dniUser.isEmpty() && !celularUser.isEmpty() && !direccionUser.isEmpty() && !tarjetaUser.isEmpty() && !passUser.isEmpty()&& item == "Administrador"){

                    apellido.setError("El campo esta vacio");


                }else if (!nameUser.isEmpty() && !apellidoUser.isEmpty() && emailUser.isEmpty() && !dniUser.isEmpty() && !celularUser.isEmpty() && !direccionUser.isEmpty() && !tarjetaUser.isEmpty() && !passUser.isEmpty()&& item == "Administrador"){

                    email.setError("El campo esta vacio");


                }else if (!nameUser.isEmpty() && !apellidoUser.isEmpty() && !emailUser.isEmpty() && dniUser.isEmpty() && dni.length() == 8 && !celularUser.isEmpty() && !direccionUser.isEmpty() && !tarjetaUser.isEmpty() && !passUser.isEmpty()&& item == "Administrador"){

                    dni.setError("El dni debe tener menos de 9 numeros");


                }else if (!nameUser.isEmpty() && !apellidoUser.isEmpty() && !emailUser.isEmpty() && !dniUser.isEmpty() && !celularUser.isEmpty() && direccionUser.isEmpty() && !tarjetaUser.isEmpty() && !passUser.isEmpty()&& item == "Administrador"){

                    direccion.setError("la direccion debe existir");



                }else if (!nameUser.isEmpty() && !apellidoUser.isEmpty() && !emailUser.isEmpty() && !dniUser.isEmpty() && celularUser.isEmpty() && !direccionUser.isEmpty() && !tarjetaUser.isEmpty() && !passUser.isEmpty()&& item == "Administrador"){

                    celular.setError("El celular debe tener un maximo de 9 numeros");


                }else if (!nameUser.isEmpty() && !apellidoUser.isEmpty() && !emailUser.isEmpty() && !dniUser.isEmpty() && !celularUser.isEmpty() && !direccionUser.isEmpty() && !tarjetaUser.isEmpty() && passUser.isEmpty()&& item == "Administrador"){

                    password.setError("la contraseña debe tener mas de 6 digitos");

                }else if (!nameUser.isEmpty() && !apellidoUser.isEmpty() && !emailUser.isEmpty() && !dniUser.isEmpty() && !celularUser.isEmpty() && !direccionUser.isEmpty() && !tarjetaUser.isEmpty() && !passUser.isEmpty()&& item == "Administrador"){

                    registerUserAdministrador(nameUser, apellidoUser, emailUser, dniUser, direccionUser, celularUser, passUser, latitudUser, longitudUser, tarjetaUser);
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    overridePendingTransition(R.anim.left_in, R.anim.left_out);

                }












            }
        });










    }



    public void tarjeta_credito_sheet(View view){
    final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
            RegisterActivity.this,R.style.BottomSheetDialogTheme
    );
    View bottomSheetView = LayoutInflater.from(RegisterActivity.this)
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


    public static String obtenerHoraActual(String zonaHoraria) {
        String formato = "HH:mm:ss";
        return obtenerFechaConFormato(formato, zonaHoraria);
    }






    private void locationStart() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Localizacion Local = new Localizacion();
        Local.setRegisterActivity(this);
        assert  mlocManager != null;
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) Local);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) Local);

        //mensaje1.setText("Localizacion agregada");

    }









    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationStart();
                return;
            }
        }
    }





    public void setLocation(Location loc) {
        //Obtener la direccion de la calle a partir de la latitud y la longitud
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);


                    String direccionactual = DirCalle.getAddressLine(0);



                    direccion.setText(direccionactual);














                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }





    public class Localizacion implements LocationListener {
        RegisterActivity registerActivity;

        public RegisterActivity getRegisterActivity() {
            return registerActivity;
        }

        public void setRegisterActivity(RegisterActivity registerActivity) {
            this.registerActivity = registerActivity;
        }

        @Override
        public void onLocationChanged(Location loc) {
            // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
            // debido a la deteccion de un cambio de ubicacion

            loc.getLatitude();
            loc.getLongitude();

            String Text = "Mi ubicacion actual es: " + "\n Lat = "
                    + loc.getLatitude() + "\n Long = " + loc.getLongitude();



            try {


                latitud.setText(String.valueOf(loc.getLatitude()));

                longitud.setText(String.valueOf(loc.getLongitude()));




            }catch (NullPointerException e){
                e.printStackTrace();

            }



            this.registerActivity.setLocation(loc);


        }




        @Override
        public void onProviderDisabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es desactivado
            //mensaje1.setText("GPS Desactivado");

            Toast.makeText(RegisterActivity.this, "GPS Desactivado", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onProviderEnabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es activado
            //mensaje1.setText("GPS Activado");

            Toast.makeText(RegisterActivity.this, "GPS Activado", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Log.d("debug", "LocationProvider.AVAILABLE");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                    break;
            }
        }






    }




    public static String ucFirst(String str) {
        if (str == null || str.isEmpty()) {
            return "";
        } else {
            return  Character.toUpperCase(str.charAt(0)) + str.substring(1, str.length()).toLowerCase();
        }
    }








    private void registerUserCliente(String nameUser, String apellidoUser, String emailUser, String dniUser, String direccionUser, String celularUser, String tarjetaUser, String passUser, double latitudUser, double longitudUser) {
        mAuth.createUserWithEmailAndPassword(emailUser, passUser).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                String id = mAuth.getCurrentUser().getUid();
                Map<String, Object> map = new HashMap<>();
                map.put("id", id);
                map.put("name", nameUser);
                map.put("apellido", apellidoUser);
                map.put("email", emailUser);
                map.put("dni", dniUser);
                map.put("direccion", direccionUser);




                String celularNarea=celularUser;



                map.put("celular", celularNarea);
                map.put("tarjeta_credito", tarjetaUser);

                map.put("Usuario", "Cliente");
                map.put("password", passUser);
                map.put("estado_pago","no aceptado");
                map.put("prueba","si");

                String diaactual = obtenerdiaactual("America/Lima");

                String mesactual = obtenermesactual("America/Lima");


                Calendar calendar = Calendar.getInstance();


                String diaultimo = String.valueOf(calendar.getActualMaximum(Calendar.DAY_OF_MONTH));




                map.put("dia_actual",diaactual);

                map.put("mes_actual", mesactual);


                map.put("fecha_vencimiento_dia", diaactual);
                map.put("fecha_vencimiento_mes", "12");



                //-------------------------------------------------

                map.put("latitud", latitudUser);

                map.put("longitud", longitudUser);





                //---------------------------------------------------



                int codigoP = 0;

                int cont_pago_mes = 2;
                int cont_pago_ano = 1;

                int cont_productos = 0;


                HashMap<String, Object> mapA = new HashMap<>();
                mapA.put("id_user",id);


                mapA.put("codigoP", codigoP);



                mapA.put("nombre", nameUser);
                mapA.put("apellido", apellidoUser);


                mapA.put("cont_pago_un_mes", cont_pago_mes);
                mapA.put("cont_pago_un_ano", cont_pago_ano);

                mapA.put("cont_productos", cont_productos);


                String horario_atencion = "Lunes a Viernes";

                String hora_atencion = "9:00 - 18:00";


                mapA.put("horario_atencion", horario_atencion);

                mapA.put("hora_atencion", hora_atencion);


                String tarjeta_credito_defecto = "191-98998982-0-68";

                String celular_administrador_defecto = "+51921736660";


                mapA.put("tarjeta_credito_administrador", tarjeta_credito_defecto);

                mapA.put("celular_administrador", celular_administrador_defecto);


                mapA.put("producto", 0);



                mFirestore.collection("acumulador/"+id+"/Cliente").document("pedido").set(mapA).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, "Error al guardar", Toast.LENGTH_SHORT).show();
                    }
                });






                int codigoS = 1;


                HashMap<String, Object> mapCU = new HashMap<>();
                mapCU.put("id_user",id);


                mapCU.put("codigoS", codigoS);


                mapCU.put("nombre", nameUser);
                mapCU.put("apellido", apellidoUser);




                mFirestore.collection("acumulador/conteo/cliente").document(id).set(mapCU).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, "Error al guardar", Toast.LENGTH_SHORT).show();
                    }
                });









                //-------------------------------------------------------------------------





                DatabaseReference dbRefC = FirebaseDatabase.getInstance().getReference().child("users");



                Map<String, Object> mapC = new HashMap<>();
                mapC.put("id", id);
                mapC.put("nombre", nameUser);
                mapC.put("apellido", apellidoUser);
                mapC.put("email", emailUser);
                mapC.put("dni", dniUser);
                mapC.put("direccion", direccionUser);




                String celularNarea1=celularUser;

                mapC.put("celular",celularNarea1);


                mapC.put("Usuario", "Cliente");
                mapC.put("password", passUser);
                mapC.put("tarjeta_credito", tarjetaUser);

                String usuario = nameUser + " "+ apellidoUser;



                dbRefC.child(usuario).setValue(mapC);


                //-------------------------------------------------------


                map.put("id_repartidor", "");
                map.put("repartidor_nombre", "");
                map.put("repartidor_apellido", "");
                map.put("celularrepartidor", "");


                map.put("tarjeta_credito_administrador", "");
                map.put("celular_administrador", "");



                mFirestore.collection("Cliente").document(id).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(RegisterActivity.this, "Cliente Creado", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, "Error al guardar", Toast.LENGTH_SHORT).show();
                    }
                });



                //------------------------------------------------------------------------


                Map<String, Object> mapP = new HashMap<>();
                mapP.put("id", id);
                mapP.put("name", nameUser);
                mapP.put("apellido", apellidoUser);
                mapP.put("email", emailUser);
                mapP.put("dni", dniUser);
                String celularNarea2=celularUser;

                mapP.put("celular",celularNarea2);
                mapP.put("estado_pago","aceptado");

                mapP.put("tarjeta_credito", tarjetaUser);
                mapP.put("estado_pago","no aceptado");
                mapP.put("prueba","si");



                mapP.put("Usuario", "Cliente");






                mFirestore.collection("pago").document(id).set(mapP).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        //Toast.makeText(RegisterActivity.this, "Cliente Creado", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, "Error al guardar", Toast.LENGTH_SHORT).show();
                    }
                });





                mFirestore.collection("Usuario/Cliente/"+id).document("registro").set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(RegisterActivity.this, "Cliente Creado", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, "Error al guardar", Toast.LENGTH_SHORT).show();
                    }
                });









            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, "Error al registrar", Toast.LENGTH_SHORT).show();
            }
        });
    }












    private void registerUserRepartidor(String nameUser, String apellidoUser, String emailUser, String dniUser, String direccionUser, String celularUser, String passUser, double latitudUser, double longitudUser, String tarjetaUser) {
        mAuth.createUserWithEmailAndPassword(emailUser, passUser).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                String id = mAuth.getCurrentUser().getUid();
                Map<String, Object> map = new HashMap<>();
                map.put("id", id);
                map.put("name", nameUser);
                map.put("apellido", apellidoUser);
                map.put("email", emailUser);
                map.put("dni", dniUser);
                map.put("direccion", direccionUser);


                String celularNarea2=celularUser;

                map.put("celular",celularNarea2);



                map.put("Usuario", "Repartidor");
                map.put("password", passUser);


                map.put("tarjeta_credito",tarjetaUser);





                //------------------------------------------------

                map.put("latitud", latitudUser);
                map.put("longitud", longitudUser);







                //-------------------------------------------------









                DatabaseReference dbRefR = FirebaseDatabase.getInstance().getReference().child("users");



                Map<String, Object> mapR = new HashMap<>();
                mapR.put("id", id);
                mapR.put("name", nameUser);
                mapR.put("email", emailUser);
                mapR.put("dni", dniUser);
                mapR.put("direccion", direccionUser);

                String celularNarea3=celularUser;

                mapR.put("celular",celularNarea3);
                mapR.put("Usuario", "Repartidor");
                mapR.put("password", passUser);
                mapR.put("tarjeta_credito",tarjetaUser);




                String usuario = nameUser + " "+ apellidoUser;


                dbRefR.child(usuario).setValue(mapR);





                mFirestore.collection("Repartidor").document(id).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(RegisterActivity.this, "Repartidor Creado", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, "Error al guardar", Toast.LENGTH_SHORT).show();
                    }
                });




                mFirestore.collection("Usuario/Repartidor/"+id).document("registro").set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(RegisterActivity.this, "Repartidor Creado", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, "Error al guardar", Toast.LENGTH_SHORT).show();
                    }
                });



                HashMap<String, Object> mapP = new HashMap<>();
                map.put("nombre",nameUser);
                map.put("apellido",apellidoUser);
                map.put("id_user", id);


                mFirestore.collection("productos").document(nameUser).set(mapP);

                mFirestore.collection("firmas").document(nameUser).set(mapP);







            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, "Error al registrar", Toast.LENGTH_SHORT).show();
            }
        });
    }




    private void registerUserAdministrador(String nameUser, String apellidoUser, String emailUser, String dniUser, String direccionUser, String celularUser, String passUser, double latitudUser, double longitudUser, String tarjetaUser) {
        mAuth.createUserWithEmailAndPassword(emailUser, passUser).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                String id = mAuth.getCurrentUser().getUid();
                Map<String, Object> map = new HashMap<>();
                map.put("id", id);
                map.put("name", nameUser);
                map.put("apellido", apellidoUser);
                map.put("email", emailUser);
                map.put("dni", dniUser);
                map.put("direccion", direccionUser);

                String celularNarea4=celularUser;


                map.put("celular",celularNarea4);


                map.put("Usuario", "Administrador");
                map.put("password", passUser);

                map.put("tarjeta_credito",tarjetaUser);





                DatabaseReference dbRefA = FirebaseDatabase.getInstance().getReference().child("users");



                Map<String, Object> mapA = new HashMap<>();
                mapA.put("id", id);
                mapA.put("name", nameUser);
                mapA.put("email", emailUser);
                mapA.put("dni", dniUser);
                mapA.put("direccion", direccionUser);

                String celularNarea5=celularUser;

                mapA.put("celular",celularNarea5);
                mapA.put("Usuario", "Administrador");
                mapA.put("password", passUser);
                mapA.put("tarjeta_credito",tarjetaUser);


                String usuario = nameUser + " "+ apellidoUser;


                dbRefA.child(usuario).setValue(mapA);




                //--------------------------------------------------



                map.put("latitud", latitudUser);
                map.put("longitud", longitudUser);



                //----------------------------------------------------






                mFirestore.collection("Administrador").document(id).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(RegisterActivity.this, "Administrador Creado", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, "Error al guardar", Toast.LENGTH_SHORT).show();
                    }
                });




                mFirestore.collection("Usuario/Administrador/"+id).document("registro").set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(RegisterActivity.this, "Administrador Creado", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, "Error al guardar", Toast.LENGTH_SHORT).show();
                    }
                });






            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, "Error al registrar", Toast.LENGTH_SHORT).show();
            }
        });
    }









    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        item = mSpinner.getSelectedItem().toString();



        /*


        if (item == "Cliente"){

            tarjeta.setEnabled(true);


        }else if (item == "Repartidor"){

            tarjeta.setEnabled(false);
            tarjeta.setText("");


        }else if (item == "Administrador"){

            tarjeta.setEnabled(false);
            tarjeta.setText("");



        }

        */





    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }






    @Override
    protected void onStart(){
        super.onStart();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
    }




    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }





}