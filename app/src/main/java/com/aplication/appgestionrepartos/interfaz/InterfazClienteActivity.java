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
import android.net.Uri;
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
import com.aplication.appgestionrepartos.cliente.Lista_RepartidoresActivity;
import com.aplication.appgestionrepartos.cliente.CarritoActivity;
import com.aplication.appgestionrepartos.cliente.Quejas_SugerenciasActivity;
import com.aplication.appgestionrepartos.cliente.ChatBotActivity;
import com.aplication.appgestionrepartos.cliente.PanelChat_Cliente;
import com.aplication.appgestionrepartos.cliente.CalculadoraActivity;
import com.aplication.appgestionrepartos.R;
import com.aplication.appgestionrepartos.cliente.PerfilActivity;
import com.aplication.appgestionrepartos.chat.LoginActivity;
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

import com.aplication.appgestionrepartos.databinding.ActivityInterfazClienteBinding;
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


public class InterfazClienteActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityInterfazClienteBinding binding;


    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;

    StorageReference storageReference;
    String storage_path = "usuarios/*";

    private static final int COD_SEL_STORAGE = 200;
    private static final int COD_SEL_IMAGE = 300;

    String photo = "photo";

    String id;

    private DrawerLayout drawerLayout;

    private String url;


    FloatingActionButton fab, fab1, fab2, fab3;
    Animation fabOpen, fabClose, rotateForward, rotateBackward;

    boolean isOpen = false;


    private String codigoid;

    BroadcastReceiver broadcastReceiver = null;

    NetworkChangeListener networkChangeListener;

    String idUser;


    TextView textCartItemCount;
    //int mCartItemCount = 0;


    private View parentView;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        broadcastReceiver = new InternetReceiver();
        networkChangeListener = new NetworkChangeListener();


        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        id = getIntent().getStringExtra("id_user");

        idUser = mAuth.getCurrentUser().getUid();

        drawerLayout=(DrawerLayout) findViewById(R.id.drawer_layout);
        storageReference = FirebaseStorage.getInstance().getReference();



        binding = ActivityInterfazClienteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        setSupportActionBar(binding.appBarInterfazCliente.toolbar);



        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fab_chat);
        fab2 = (FloatingActionButton) findViewById(R.id.fab_chatbot);
        fab3 = (FloatingActionButton) findViewById(R.id.fab_whatsapp);


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


                mFirestore.collection("Cliente").document(idUser).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {


                        String ClienteUsuario = documentSnapshot.getString("Usuario");


                        Intent intent = new Intent(InterfazClienteActivity.this, PanelChat_Cliente.class);

                        intent.putExtra("Us",ClienteUsuario);


                        startActivity(intent);





                    }
                });










            }
        });


        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(InterfazClienteActivity.this, ChatBotActivity.class);
                startActivity(intent);

                //Toast.makeText(InterfazClienteActivity.this, "chatbot", Toast.LENGTH_SHORT).show();

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


       /*
        binding.appBarInterfazCliente.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();



            }
        });
        */









        DrawerLayout drawer = binding.drawerLayout;

        NavigationView navigationView = binding.navView;




        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_inicio, R.id.nav_productos, R.id.nav_compras, R.id.nav_ubicacion)
                .setOpenableLayout(drawer)
                .build();


        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_interfaz_cliente);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


    }


    private void animationFab(){
        if (isOpen){
            fab.startAnimation(rotateForward);
            fab1.startAnimation(fabClose);
            fab2.startAnimation(fabClose);
            fab3.startAnimation(fabClose);
            fab1.setClickable(false);
            fab2.setClickable(false);
            fab3.setClickable(false);
            isOpen = false;

        }else{

            fab.startAnimation(rotateBackward);
            fab1.startAnimation(fabOpen);
            fab2.startAnimation(fabOpen);
            fab3.startAnimation(fabOpen);
            fab1.setClickable(true);
            fab2.setClickable(true);
            fab3.setClickable(true);
            isOpen = true;


        }





    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.interfaz_cliente, menu);



        final MenuItem menuItem = menu.findItem(R.id.bolsaCompras);

        View actionView = menuItem.getActionView();
        textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);

        setupBadge();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });


        return true;

    }



    private void setupBadge() {



        mFirestore.collection("acumulador/"+idUser+"/Cliente").document("pedido").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                double conteoproductos = documentSnapshot.getDouble("cont_productos");

                int conteoproductosyy = (int) conteoproductos;


                if (textCartItemCount != null) {
                    if (conteoproductosyy == 0) {
                        if (textCartItemCount.getVisibility() != View.GONE) {
                            textCartItemCount.setVisibility(View.GONE);
                        }
                    } else {
                        textCartItemCount.setText(String.valueOf(Math.min(conteoproductosyy, 99)));
                        if (textCartItemCount.getVisibility() != View.VISIBLE) {
                            textCartItemCount.setVisibility(View.VISIBLE);
                        }
                    }
                }






            }
        });










    }




    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        String idUser = mAuth.getCurrentUser().getUid();


        switch (item.getItemId()){

            case R.id.calculadora:

                startActivity(new Intent(InterfazClienteActivity.this, CalculadoraActivity.class));
                finish();


                return true;

            case R.id.bolsaCompras:



                mFirestore.collection("Cliente").document(idUser).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot5) {

                        String idRepartidor = documentSnapshot5.getString("id_repartidor");

                        String nombreRepartidor = documentSnapshot5.getString("repartidor_nombre");

                        String apellidoRepartidor = documentSnapshot5.getString("repartidor_apellido");

                        String celularRepartidor = documentSnapshot5.getString("celularrepartidor");



                        if (idRepartidor.isEmpty() && nombreRepartidor.isEmpty() && apellidoRepartidor.isEmpty() && celularRepartidor.isEmpty()){

                            startActivity(new Intent(InterfazClienteActivity.this, Lista_RepartidoresActivity.class));
                            overridePendingTransition(R.anim.left_in, R.anim.left_out);
                            finish();



                        }else if (!idRepartidor.isEmpty() && nombreRepartidor.isEmpty() && apellidoRepartidor.isEmpty() && celularRepartidor.isEmpty()){

                            startActivity(new Intent(InterfazClienteActivity.this, Lista_RepartidoresActivity.class));
                            overridePendingTransition(R.anim.left_in, R.anim.left_out);
                            finish();



                        }else if (!idRepartidor.isEmpty() && !nombreRepartidor.isEmpty() && apellidoRepartidor.isEmpty() && celularRepartidor.isEmpty()){

                            startActivity(new Intent(InterfazClienteActivity.this, Lista_RepartidoresActivity.class));
                            overridePendingTransition(R.anim.left_in, R.anim.left_out);
                            finish();



                        }else if (!idRepartidor.isEmpty() && !nombreRepartidor.isEmpty() && !apellidoRepartidor.isEmpty() && celularRepartidor.isEmpty()){

                            startActivity(new Intent(InterfazClienteActivity.this, Lista_RepartidoresActivity.class));
                            overridePendingTransition(R.anim.left_in, R.anim.left_out);
                            finish();



                        }else if (!idRepartidor.isEmpty() && !nombreRepartidor.isEmpty() && !apellidoRepartidor.isEmpty() && !celularRepartidor.isEmpty()){


                            startActivity(new Intent(InterfazClienteActivity.this, CarritoActivity.class));
                            overridePendingTransition(R.anim.left_in, R.anim.left_out);
                            finish();


                        }







                    }
                });





                return true;






            case R.id.action_perfil:

                startActivity(new Intent(InterfazClienteActivity.this, PerfilActivity.class));
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
                finish();

                return true;


            case R.id.action_quejas_sugerencias:



                mFirestore.collection("Usuario/Cliente/"+idUser).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot snapshot : task.getResult()){

                            codigoid = snapshot.getString("id");


                            Intent i = new Intent(InterfazClienteActivity.this, Quejas_SugerenciasActivity.class);

                            i.putExtra("id_user",codigoid);

                            overridePendingTransition(R.anim.left_in, R.anim.left_out);

                            startActivity(i);

                            finish();






                        }
                    }
                });




                return true;



            case R.id.action_configuracion:


                Intent it = new Intent(InterfazClienteActivity.this, SettingsActivity.class);

                it.putExtra("id_user",id);
                it.putExtra("Usuario","Cliente");
                startActivity(it);



                return true;





            case R.id.action_cerrar_sesion:



                AlertDialog.Builder dialogo2 = new AlertDialog.Builder(this);
                dialogo2.setTitle("AVISO");
                dialogo2.setMessage("¿Deseas cerrar sesion?");
                dialogo2.setCancelable(false);
                dialogo2.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo2, int id) {


                        startActivity(new Intent(InterfazClienteActivity.this, LoginActivity.class));
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

                        mFirestore.collection("Cliente").document(idUser).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
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
                                                                            Toast.makeText(InterfazClienteActivity.this, "Usuario eliminado", Toast.LENGTH_SHORT).show();

                                                                            mFirestore.collection("Cliente").document(idUser).delete();
                                                                            mFirestore.collection("Usuario/Cliente/"+idUser).document(idUser).delete();

                                                                            mFirestore.collection("acumulador/"+idUser+"/Cliente").document("pedido").delete();
                                                                            mFirestore.collection("acumulador/conteo/cliente").document(idUser).delete();
                                                                            mFirestore.collection("pago").document(idUser).delete();


                                                                            FirebaseAuth.getInstance().signOut();
                                                                            startActivity(new Intent(InterfazClienteActivity.this, LoginActivity.class));
                                                                            finish();


                                                                        }else{

                                                                            Toast.makeText(InterfazClienteActivity.this, "Error al autenticar", Toast.LENGTH_SHORT).show();


                                                                        }



                                                                    }
                                                                });

                                                    }else{
                                                        Toast.makeText(InterfazClienteActivity.this, "Error al autenticar", Toast.LENGTH_SHORT).show();
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













                return true;



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
    protected void onStart(){
        super.onStart();
        Internetstatus();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        loadData();



    }







    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);

    }


    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }


    private void loadData(){

        String idUser = mAuth.getCurrentUser().getUid();


        mFirestore.collection("Usuario/Cliente/"+idUser).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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

                    Glide.with(InterfazClienteActivity.this)
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
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_interfaz_cliente);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }



}