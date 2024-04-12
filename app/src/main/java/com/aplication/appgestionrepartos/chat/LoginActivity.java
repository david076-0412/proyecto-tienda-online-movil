package com.aplication.appgestionrepartos.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.aplication.appgestionrepartos.interfaz.InterfazRepartidorActivity;
import com.aplication.appgestionrepartos.pago.Pago_Anuncio_OficialActivity;
import com.aplication.appgestionrepartos.R;
import com.aplication.appgestionrepartos.interfaz.InterfazAdministradorActivity;
import com.aplication.appgestionrepartos.interfaz.InterfazClienteActivity;
import com.aplication.appgestionrepartos.login.RegisterActivity;
import com.aplication.appgestionrepartos.no_internet_connection.NetworkChangeListener;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class LoginActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    ProgressDialog progressDialog;

    Button btn_login, btn_register, btn_login_anonymous;
    EditText email, password;
    FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;

    private String emailUser, passUser;


    private Spinner mSpinner;

    private String item;

    String[] Usuarios = {"Seleccionar", "Cliente", "Repartidor", "Administrador"};


    Switch active;

    NetworkChangeListener networkChangeListener;


    TextView mTextViewrestaurar_contrasena;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.setTitle("Login");
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        networkChangeListener = new NetworkChangeListener();

        email = findViewById(R.id.correo);





        password = findViewById(R.id.contrasena);








        btn_login = findViewById(R.id.btn_ingresar);




        active = findViewById(R.id.active);













        mSpinner = (Spinner) findViewById(R.id.spn);





        mSpinner.setOnItemSelectedListener(this);

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, Usuarios);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinner.setAdapter(arrayAdapter);


        btn_register = findViewById(R.id.btn_register);



        //btn_login_anonymous = findViewById(R.id.btn_anonymous);


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                emailUser = email.getText().toString().trim();
                passUser = password.getText().toString().trim();


                if (emailUser.isEmpty() && passUser.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Ingresar los datos", Toast.LENGTH_SHORT).show();
                    email.setError("El campo esta vacio");
                    password.setError("la contraseña debe tener mas de 6 digitos");

                } else {


                    if (item == "Seleccionar") {

                        Toast.makeText(LoginActivity.this, "Seleccione un Usuario", Toast.LENGTH_SHORT).show();


                    } else {


                        if (item == "Cliente" || item == "Repartidor" || item == "Administrador") {


                            loginUser(emailUser, passUser);


                        } else {

                            Toast.makeText(LoginActivity.this, "No se detecto ninguna opcion", Toast.LENGTH_SHORT).show();

                        }


                    }


                }


            }
        });


        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        /*

        btn_login_anonymous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginAnonymous();
            }
        });
        */





        mTextViewrestaurar_contrasena = findViewById(R.id.olvidaste_contrasena);


        mTextViewrestaurar_contrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextViewClicked();


            }
        });










        email.setTranslationX(800);

        email.setAlpha(0);

        email.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();





        password.setTranslationX(800);

        password.setAlpha(0);

        password.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();




        mTextViewrestaurar_contrasena.setTranslationX(800);

        mTextViewrestaurar_contrasena.setAlpha(0);

        mTextViewrestaurar_contrasena.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();







        mSpinner.setTranslationX(800);

        mSpinner.setAlpha(0);

        mSpinner.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();




        active.setTranslationX(800);

        active.setAlpha(0);

        active.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();




        btn_login.setTranslationX(800);

        btn_login.setAlpha(0);

        btn_login.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();




        btn_register.setTranslationX(800);

        btn_register.setAlpha(0);

        btn_register.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();








    }


    public void TextViewClicked() {


        final EditText resetMail = new EditText(LoginActivity.this);

        AlertDialog.Builder dialogo2 = new AlertDialog.Builder(LoginActivity.this);
        dialogo2.setTitle("Reset Password");
        dialogo2.setMessage("Enter Your Email To Received Reset Link.");
        dialogo2.setView(resetMail);
        dialogo2.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo2, int id) {



                String mail = resetMail.getText().toString().trim();
                mAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(LoginActivity.this, "Reset Link Sent To Your Email.", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this, "Error! Reset Link is Not Sent"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });








            }
        });
        dialogo2.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo2, int id) {



            }
        });


        dialogo2.show();



    }








    private void loginAnonymous() {
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            //startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this, "Error al acceder", Toast.LENGTH_SHORT).show();
                    }
                });
    }






    private void loginUser(String emailUser, String passUser) {


        mAuth.signInWithEmailAndPassword(emailUser, passUser).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {


                if (task.isSuccessful()) {


                    if (active.isChecked()) {




                        String idUser = mAuth.getCurrentUser().getUid();


                        mFirestore.collection("Cliente").document(idUser).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot1) {


                                String usuariocliente = documentSnapshot1.getString("Usuario");

                                String emailcliente = documentSnapshot1.getString("email");

                                String estadoprueba = documentSnapshot1.getString("prueba");

                                String estadopago = documentSnapshot1.getString("estado_pago");


                                mFirestore.collection("Repartidor").document(idUser).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot2) {


                                        String usuariorepartidor = documentSnapshot2.getString("Usuario");

                                        String emailrepartidor = documentSnapshot2.getString("email");


                                        mFirestore.collection("Administrador").document(idUser).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot3) {


                                                String usuarioadministrador = documentSnapshot3.getString("Usuario");

                                                String emailadministrador = documentSnapshot3.getString("email");


                                                if (estadoprueba == null && estadopago == null) {

                                                    repartidoracceso(idUser, emailrepartidor, emailUser, passUser);

                                                    administradoracceso(idUser, emailadministrador, emailUser, passUser);


                                                } else if (estadoprueba.equals("si")) {


                                                    cargaLoading(idUser, emailcliente, emailUser, passUser);


                                                } else if (estadoprueba.equals("no")) {


                                                    Intent intent = new Intent(LoginActivity.this, Pago_Anuncio_OficialActivity.class);

                                                    startActivity(intent);

                                                    finish();


                                                } else if (estadoprueba.equals("si") && estadopago.equals("aceptado")) {

                                                    cargaLoading(idUser, emailcliente, emailUser, passUser);




                                                } else if (estadoprueba.equals("no") && estadopago.equals("no aceptado")) {


                                                    Intent intent = new Intent(LoginActivity.this, Pago_Anuncio_OficialActivity.class);

                                                    startActivity(intent);

                                                    finish();


                                                }


                                            }
                                        });


                                    }
                                });


                            }
                        });


                    } else {

                        String idUser = mAuth.getCurrentUser().getUid();

                        mFirestore.collection("Cliente").document(idUser).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot1) {


                                String usuariocliente = documentSnapshot1.getString("Usuario");

                                String emailcliente = documentSnapshot1.getString("email");

                                String estadoprueba = documentSnapshot1.getString("prueba");

                                String estadopago = documentSnapshot1.getString("estado_pago");


                                mFirestore.collection("Repartidor").document(idUser).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot2) {


                                        String usuariorepartidor = documentSnapshot2.getString("Usuario");

                                        String emailrepartidor = documentSnapshot2.getString("email");


                                        mFirestore.collection("Administrador").document(idUser).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot3) {


                                                String usuarioadministrador = documentSnapshot3.getString("Usuario");

                                                String emailadministrador = documentSnapshot3.getString("email");


                                                if (estadoprueba == null && estadopago == null) {

                                                    repartidoracceso(idUser, emailrepartidor, emailUser, passUser);

                                                    administradoracceso(idUser, emailadministrador, emailUser, passUser);


                                                } else if (estadoprueba.equals("si")) {


                                                    cargaLoading(idUser, emailcliente, emailUser, passUser);


                                                } else if (estadoprueba.equals("no")) {


                                                    Intent intent = new Intent(LoginActivity.this, Pago_Anuncio_OficialActivity.class);

                                                    startActivity(intent);

                                                    finish();


                                                } else if (estadoprueba.equals("si") && estadopago.equals("aceptado")) {

                                                    cargaLoading(idUser, emailcliente, emailUser, passUser);




                                                } else if (estadoprueba.equals("no") && estadopago.equals("no aceptado")) {


                                                    Intent intent = new Intent(LoginActivity.this, Pago_Anuncio_OficialActivity.class);

                                                    startActivity(intent);

                                                    overridePendingTransition(R.anim.left_in, R.anim.left_out);

                                                    finish();


                                                }


                                            }
                                        });


                                    }
                                });


                            }
                        });


                    }


                } else {
                    Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, "Error al inciar sesión", Toast.LENGTH_SHORT).show();
            }
        });


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

    public static String obtenerdiaactual(String zonaHoraria) {
        String formato = "dd";
        return obtenerFechaConFormato(formato, zonaHoraria);

    }

    public static String obtenermesactual(String zonaHoraria) {
        String formato = "MM";

        return obtenerFechaConFormato(formato, zonaHoraria);
    }


    public static String obteneranoactual(String zonaHoraria) {
        String formato = "yyyy";

        return obtenerFechaConFormato(formato, zonaHoraria);

    }


    public static String obtenerHoraActual(String zonaHoraria) {
        String formato = "HH:mm:ss";
        return obtenerFechaConFormato(formato, zonaHoraria);
    }


    public void cargaLoading(String idUser, String emailcliente, String emailUser, String passUser) {

        final LoadingDialog loadingDialog = new LoadingDialog(LoginActivity.this);

        loadingDialog.showLoading();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                clienteacceso(idUser, emailcliente, emailUser, passUser);


                loadingDialog.disMiss();



            }
        },5000);


    }




    public void clienteacceso(String idUser, String emailcliente, String emailUser, String passUser){

        if (emailcliente != null && emailUser != null && emailcliente.equals(emailUser) && item == "Cliente"){





            mFirestore.collection("Usuario/Cliente/"+idUser).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (QueryDocumentSnapshot snapshot : task.getResult()){
                        String nombrecliente = snapshot.getString("name");
                        String apellidocliente = snapshot.getString("apellido");
                        UserDetails.username = nombrecliente + " " + apellidocliente;
                        UserDetails.password = passUser;





                        //String diaactual = snapshot.getString("dia_actual");
                        //String mesactual = snapshot.getString("mes_actual");


                        String diaactual = obtenerdiaactual("America/Lima");

                        String mesactual = obtenermesactual("America/Lima");



                        //String diaultimo = snapshot.getString("fecha_vencimiento_dia");





                        String id = mAuth.getCurrentUser().getUid();
                        Map<String, Object> mapC = new HashMap<>();
                        mapC.put("dia_actual", diaactual);
                        mapC.put("mes_actual",mesactual);



                        Calendar calendar = Calendar.getInstance();

                        String diaultimo = String.valueOf(calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

                        String diaprimero = String.valueOf(calendar.getActualMinimum(Calendar.DAY_OF_MONTH));


                        int messiguientemm = calendar.getActualMaximum(Calendar.MONTH);


                        String messiguiente = String.valueOf(messiguientemm+1);



                        mapC.put("fecha_vencimiento_dia", diaultimo);
                        mapC.put("fecha_vencimiento_mes", "12");



                        mapC.put("fecha_vencimiento_dia_mes", diaprimero);
                        mapC.put("fecha_vencimiento_mes_ultimo", messiguiente);




                        String fechaactual = obtenerFechaActual("America/Lima");

                        mapC.put("fecha_actual", fechaactual);





                        mFirestore.collection("Cliente").document(idUser).update(mapC);
                        mFirestore.collection("Usuario/Cliente/"+idUser).document("registro").update(mapC);





                    }

                }
            });


            startActivity(new Intent(LoginActivity.this, InterfazClienteActivity.class));
            //startActivity(new Intent(LoginActivity.this, ClienteActivity.class));
            Toast.makeText(LoginActivity.this, "Bienvenido", Toast.LENGTH_SHORT).show();

            overridePendingTransition(R.anim.left_in, R.anim.left_out);








        }else if (emailcliente != null && emailUser != null && emailcliente.equals(emailUser) && item == "Repartidor"){


            Toast.makeText(LoginActivity.this, "usuario incorrecto", Toast.LENGTH_SHORT).show();



        }else if (emailcliente != null && emailUser != null && emailcliente.equals(emailUser) && item == "Administrador"){

            Toast.makeText(LoginActivity.this, "usuario incorrecto", Toast.LENGTH_SHORT).show();


        }else if(emailcliente == null && emailUser == null){

            Toast.makeText(LoginActivity.this, "usuario no existente", Toast.LENGTH_SHORT).show();

        }





    }




    public void repartidoracceso(String idUser, String emailrepartidor, String emailUser, String passUser){


        if (emailrepartidor != null && emailUser != null && emailrepartidor.equals(emailUser) && item == "Repartidor"){







            mFirestore.collection("Usuario/Repartidor/"+idUser).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (QueryDocumentSnapshot snapshot : task.getResult()){
                        String nombrerepartidor = snapshot.getString("name");
                        String apellidorepartidor = snapshot.getString("apellido");

                        UserDetails.username = nombrerepartidor + " " + apellidorepartidor;
                        UserDetails.password = passUser;







                    }

                }
            });



            startActivity(new Intent(LoginActivity.this, InterfazRepartidorActivity.class));

            overridePendingTransition(R.anim.left_in, R.anim.left_out);









        }else if (emailrepartidor != null && emailUser != null && emailrepartidor.equals(emailUser) && item == "Cliente"){


            Toast.makeText(LoginActivity.this, "usuario incorrecto", Toast.LENGTH_SHORT).show();


        }else if (emailrepartidor != null && emailUser != null && emailrepartidor.equals(emailUser) && item == "Administrador"){


            Toast.makeText(LoginActivity.this, "usuario incorrecto", Toast.LENGTH_SHORT).show();


        }else if (emailrepartidor == null && emailUser == null){


            Toast.makeText(LoginActivity.this, "usuario no existente", Toast.LENGTH_SHORT).show();

        }






    }





    public void administradoracceso(String idUser, String emailadministrador, String emailUser, String passUser){


        if (emailadministrador != null && emailUser != null && emailadministrador.equals(emailUser) && item == "Administrador"){





            mFirestore.collection("Usuario/Administrador/"+idUser).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (QueryDocumentSnapshot snapshot : task.getResult()){
                        String nombreadministrador = snapshot.getString("name");
                        String apellidoadministrador = snapshot.getString("apellido");

                        UserDetails.username = nombreadministrador + " " + apellidoadministrador;
                        UserDetails.password = passUser;
                    }

                }
            });




            startActivity(new Intent(LoginActivity.this, InterfazAdministradorActivity.class));

            overridePendingTransition(R.anim.left_in, R.anim.left_out);







        }else if (emailadministrador != null && emailUser != null && emailadministrador.equals(emailUser) && item == "Cliente"){

            Toast.makeText(LoginActivity.this, "usuario incorrecto", Toast.LENGTH_SHORT).show();

        }else if (emailadministrador != null && emailUser != null && emailadministrador.equals(emailUser) && item == "Repartidor"){


            Toast.makeText(LoginActivity.this, "usuario incorrecto", Toast.LENGTH_SHORT).show();



        }else if (emailadministrador == null && emailUser == null){

            Toast.makeText(LoginActivity.this, "usuario no existente", Toast.LENGTH_SHORT).show();

        }





    }







    /*






    public void toastCorrecto(String msg) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.custom_toast_ok, (ViewGroup) findViewById(R.id.ll_custom_toast_ok));
        TextView txtMensaje = view.findViewById(R.id.txtMensajeToast1);
        txtMensaje.setText(msg);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM, 0, 200);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();
    }

    public void toastIncorrecto(String msg) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.custom_toast_error, (ViewGroup) findViewById(R.id.ll_custom_toast_error));
        TextView txtMensaje = view.findViewById(R.id.txtMensajeToast2);
        txtMensaje.setText(msg);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM, 0, 200);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();
    }


*/







    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        item = mSpinner.getSelectedItem().toString();

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