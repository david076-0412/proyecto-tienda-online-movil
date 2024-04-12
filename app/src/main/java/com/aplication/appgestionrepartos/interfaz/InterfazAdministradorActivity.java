package com.aplication.appgestionrepartos.interfaz;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.aplication.appgestionrepartos.settings.SettingsActivity;
import com.aplication.appgestionrepartos.administrador.ProveedoresActivity;
import com.aplication.appgestionrepartos.pago.ClientesRegistradosActivity;
import com.aplication.appgestionrepartos.administrador.Quejas_Sugerencias_FinalActivity;
import com.aplication.appgestionrepartos.administrador.PanelChat_Administrador;
import com.aplication.appgestionrepartos.administrador.PerfilAdministradorActivity;
import com.aplication.appgestionrepartos.R;
import com.aplication.appgestionrepartos.chat.LoginActivity;
import com.aplication.appgestionrepartos.databinding.ActivityInterfazAdministradorBinding;
import com.aplication.appgestionrepartos.model.Usuarios;
import com.aplication.appgestionrepartos.no_internet_connection.InternetReceiver;
import com.aplication.appgestionrepartos.no_internet_connection.NetworkChangeListener;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class InterfazAdministradorActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityInterfazAdministradorBinding binding;


    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;

    StorageReference storageReference;
    String storage_path = "usuarios/*";

    private static final int COD_SEL_STORAGE = 200;
    private static final int COD_SEL_IMAGE = 300;

    String photo = "photo";

    String id;

    String codigoid;

    private DrawerLayout drawerLayout;


    private String url;




    FloatingActionButton fab, fab1, fab2, fab3;
    Animation fabOpen, fabClose, rotateForward, rotateBackward;

    boolean isOpen = false;


    BroadcastReceiver broadcastReceiver = null;

    NetworkChangeListener networkChangeListener;


    String idUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();


        broadcastReceiver = new InternetReceiver();
        networkChangeListener = new NetworkChangeListener();

        id = getIntent().getStringExtra("id_user");


        idUser = mAuth.getCurrentUser().getUid();

        drawerLayout=(DrawerLayout) findViewById(R.id.drawer_layout);
        storageReference = FirebaseStorage.getInstance().getReference();



        binding = ActivityInterfazAdministradorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarInterfazAdministrador.toolbar);







        /*
        binding.appBarInterfazAdministrador.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        */


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fab_chat);
        //fab2 = (FloatingActionButton) findViewById(R.id.fab_chatbot);
        //fab3 = (FloatingActionButton) findViewById(R.id.fab_whatsapp);


        fabOpen = AnimationUtils.loadAnimation(this,R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(this,R.anim.fab_close);

        rotateForward = AnimationUtils.loadAnimation(this, R.anim.rotate_forward);
        rotateBackward = AnimationUtils.loadAnimation(this, R.anim.rotate_backwawrd);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationFab();

            }
        });


        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mFirestore.collection("Administrador").document(idUser).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot2) {


                        String AdministradorUsuario = documentSnapshot2.getString("Usuario");


                        Intent intent = new Intent(InterfazAdministradorActivity.this, PanelChat_Administrador.class);

                        intent.putExtra("Us",AdministradorUsuario);


                        startActivity(intent);



                        //Toast.makeText(InterfazAdministradorActivity.this, ""+AdministradorUsuario, Toast.LENGTH_SHORT).show();




                    }
                });






            }
        });


        /*

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(InterfazAdministradorActivity.this, "chatbot", Toast.LENGTH_SHORT).show();
            }
        });


        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                url="https://wa.link/kf1q9x";

                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);


                //Toast.makeText(InterfazClienteActivity.this, "Whatsapp", Toast.LENGTH_SHORT).show();
            }
        });

        */





        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;




        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_inicio_A, R.id.nav_productos_a, R.id.nav_pedidos, R.id.nav_pagos, R.id.nav_monitoreo)
                .setOpenableLayout(drawer)
                .build();





        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_interfaz_administrador);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


    }

    private void animationFab(){
        if (isOpen){
            fab.startAnimation(rotateForward);
            fab1.startAnimation(fabClose);
            //fab2.startAnimation(fabClose);
            //fab3.startAnimation(fabClose);
            fab1.setClickable(false);
            //fab2.setClickable(false);
            //fab3.setClickable(false);
            isOpen = false;

        }else{

            fab.startAnimation(rotateBackward);
            fab1.startAnimation(fabOpen);
            //fab2.startAnimation(fabOpen);
            //fab3.startAnimation(fabOpen);
            fab1.setClickable(true);
            //fab2.setClickable(true);
            //fab3.setClickable(true);
            isOpen = true;


        }





    }









    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.interfaz_administrador, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        String idUser = mAuth.getCurrentUser().getUid();


        switch (item.getItemId()){


            case R.id.action_perfil:

                startActivity(new Intent(InterfazAdministradorActivity.this, PerfilAdministradorActivity.class));
                finish();

                return true;


            case R.id.action_configuracion:


                Intent it = new Intent(InterfazAdministradorActivity.this, SettingsActivity.class);

                it.putExtra("id_user",id);
                it.putExtra("Usuario","Administrador");
                startActivity(it);



                return true;



            case R.id.action_proveedores:

                startActivity(new Intent(InterfazAdministradorActivity.this, ProveedoresActivity.class));
                finish();


                return true;



            case R.id.action_quejas_sugerencias:

                startActivity(new Intent(InterfazAdministradorActivity.this, Quejas_Sugerencias_FinalActivity.class));

                finish();




                    return true;



            case R.id.action_pago_suscripcion:

                Intent i = new Intent(InterfazAdministradorActivity.this, ClientesRegistradosActivity.class);

                startActivity(i);

                finish();





                return true;






            case R.id.action_cerrar_sesion:


                AlertDialog.Builder dialogo2 = new AlertDialog.Builder(this);
                dialogo2.setTitle("AVISO");
                dialogo2.setMessage("¿Deseas cerrar sesion?");
                dialogo2.setCancelable(false);
                dialogo2.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo2, int id) {


                        startActivity(new Intent(InterfazAdministradorActivity.this, LoginActivity.class));
                        mAuth.signOut();
                        overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        finish();




                    }
                });
                dialogo2.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo2, int id) {



                    }
                });


                dialogo2.show();



                return true;



            case R.id.action_eliminar_cuenta:



                AlertDialog.Builder dialogo5 = new AlertDialog.Builder(this);
                dialogo5.setTitle("AVISO");
                dialogo5.setMessage("¿Deseas eliminar tu cuenta?");
                dialogo5.setCancelable(false);
                dialogo5.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo5, int id) {




                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        mFirestore.collection("Administrador").document(idUser).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot5) {

                                String emailUspt = documentSnapshot5.getString("email");

                                String passwordUspt = documentSnapshot5.getString("password");





                                AuthCredential credential = EmailAuthProvider
                                        .getCredential(emailUspt,passwordUspt);



                                if (user != null){
                                    user.reauthenticate(credential)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if (task.isSuccessful()){
                                                        user.delete()
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {

                                                                        if (task.isSuccessful()){
                                                                            Toast.makeText(InterfazAdministradorActivity.this, "Usuario eliminado", Toast.LENGTH_SHORT).show();

                                                                            mFirestore.collection("Administrador").document(idUser).delete();
                                                                            mFirestore.collection("Usuario/Administrador/"+idUser).document(idUser).delete();

                                                                            mFirestore.collection("Compraseditador").document().delete();

                                                                            mFirestore.collection("pda").document().delete();


                                                                            mFirestore.collection("galeria").document().delete();




                                                                            FirebaseAuth.getInstance().signOut();
                                                                            startActivity(new Intent(InterfazAdministradorActivity.this, LoginActivity.class));
                                                                            finish();


                                                                        }else{

                                                                            Toast.makeText(InterfazAdministradorActivity.this, "Error al autenticar", Toast.LENGTH_SHORT).show();


                                                                        }



                                                                    }
                                                                });

                                                    }else{
                                                        Toast.makeText(InterfazAdministradorActivity.this, "Error al autenticar", Toast.LENGTH_SHORT).show();
                                                    }




                                                }
                                            });

                                }






                            }
                        });















                    }
                });
                dialogo5.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo5, int id) {



                    }
                });


                dialogo5.show();
















                return true;



            case R.id.action_cerrar_aplicacion:


                AlertDialog.Builder dialogo3 = new AlertDialog.Builder(this);
                dialogo3.setTitle("AVISO");
                dialogo3.setMessage("¿Deseas cerrar la Aplicación por completo?");
                dialogo3.setCancelable(false);
                dialogo3.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo3, int id) {



                        finishAffinity();


                    }
                });
                dialogo3.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo3, int id) {



                    }
                });


                dialogo3.show();


                return  true;




            default:

                return super.onOptionsItemSelected(item);

        }


    }



    public static boolean conexionDisponible(Context context) {
        ConnectivityManager connectMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        try {
            if (connectMgr != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Network[] networks = connectMgr.getAllNetworks();
                    if (networks != null) {
                        for (Network net : networks) {
                            NetworkInfo infonet = connectMgr.getNetworkInfo(net);
                            if (infonet.getState() == NetworkInfo.State.CONNECTED) {
                                return true;
                            }
                        }
                    }
                } else {
                    NetworkInfo[] netInfo = connectMgr.getAllNetworkInfo(); // deprecated en api 23
                    if (netInfo != null) {
                        for (NetworkInfo net : netInfo) {
                            if (net.getState() == NetworkInfo.State.CONNECTED) {
                                return true;
                            }
                        }
                    }
                }
            }
            return false;
        }catch (Exception e){
            NetworkInfo activeNetwork = connectMgr.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnected();

            //Para saber si está conectado al wifi o al 3g
//          if (activeNetwork != null) { // connected to the internet
//              if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
//                  // connected to wifi
//                  Toast.makeText(context, activeNetwork.getTypeName(), Toast.LENGTH_SHORT).show();
//              } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
//                  // connected to the mobile provider's data plan
//                  Toast.makeText(context, activeNetwork.getTypeName(), Toast.LENGTH_SHORT).show();
//              }
//          } else {
//              // not connected to the internet
//          }
        }
    }








    public void Internetstatus(){
        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }


    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStart(){
        super.onStart();
        Internetstatus();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        loadData();

    }


    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }

    private void loadData(){
        String idUser = mAuth.getCurrentUser().getUid();


        mFirestore.collection("Usuario/Administrador/"+idUser).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot snapshot : task.getResult()){


                    String nombreRe = snapshot.getString("name");
                    String apellidoRe = snapshot.getString("apellido");

                    String correonRe = snapshot.getString("email");

                    String photoRe = snapshot.getString("photo");

                    final View vistaHeader = binding.navView.getHeaderView(0);


                    final TextView tvNombre = vistaHeader.findViewById(R.id.tvNombre),
                            tvCorreo = vistaHeader.findViewById(R.id.tvCorreo);

                    final ImageView imgFoto = vistaHeader.findViewById(R.id.imgFotoPerfil);

                    Usuarios u = new Usuarios();


                    tvNombre.setText(nombreRe+" "+apellidoRe);
                    tvCorreo.setText(correonRe);


                    Glide.with(InterfazAdministradorActivity.this)
                            .load(photoRe)
                            .circleCrop()
                            .error(R.mipmap.ic_launcher_round)
                            .into(imgFoto);


                }
            }
        });





    }










    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_interfaz_administrador);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}