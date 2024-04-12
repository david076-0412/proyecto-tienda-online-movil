package com.aplication.appgestionrepartos.cliente;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.aplication.appgestionrepartos.R;
import com.aplication.appgestionrepartos.adapter.CarritoProductoAdapter;
import com.aplication.appgestionrepartos.chat.LoginActivity;
import com.aplication.appgestionrepartos.interfaz.InterfazClienteActivity;
import com.aplication.appgestionrepartos.login.OnboardingActivity;
import com.aplication.appgestionrepartos.model.Productos;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class CarritoActivity extends AppCompatActivity {


    Button btn_next, btn_exit;


    ImageView btn_eliminar;


    CarritoProductoAdapter mAdapter;

    RecyclerView mRecycler;
    TextView mTextviewPagoTotal,mTextViewtotal_producto, mTextViewpreciototal;


    FirebaseFirestore mFirestore;

    TextView RepartidoresCliente;


    FirebaseAuth mAuth;
    SearchView search_view;
    Query query;


    private String totalcliente;

    private double precio_totalpet;

    private double precio_vacunapet;

    private String nombreCarrito;

    private String idPedido;
    private String pedidosCliente;

    private double precio_finalCliente;

    private SwipeRefreshLayout swipeRefreshLayout;


    String idd;



//-------------------------
    private String codigoP;


    private String data = "";

    int pageHeight = 1120;
    int pagewidth = 1200;

    // creating a bitmap variable
    // for storing our images
    Bitmap bmp, scaledbmp;

    float[] prices = new float[]{0,200,450,325,500};


    private ArrayList<Productos> lista;


    double total_precioyy,total_precio;



    ProgressDialog progressDialog;


    private Toolbar mToolbar;

    //---------------------------------------------------------------------

    String idrepartidor, nomrepartidor,aperepartidor, celrepartidor;

    //---------------------------------------------------------------------



    TextView mTextViewRepartidores;


    String idUs;


    double precioproducto, precio_totalproductos, precio_totalhh;


    int contador = 0;

    String id;



    LinearLayout linearLayout_recycleview, linearLayout_vacio;


    SmsManager smsManager;



    PendingIntent sentPI;




    private int cantidadtotal;






    @SuppressLint({"NotifyDataSetChanged", "MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);
        id = getIntent().getStringExtra("id_pet");
        String pro = getIntent().getStringExtra("id_pro");


        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        search_view = findViewById(R.id.search);
        mTextViewtotal_producto = findViewById(R.id.total_precio);

        mTextViewRepartidores = findViewById(R.id.repartidoresseleccion);



        progressDialog = new ProgressDialog(this);



        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.pizzahead);
        scaledbmp = Bitmap.createScaledBitmap(bmp,1200,510,false);



        lista = new ArrayList<>();


        smsManager = SmsManager.getDefault();





        swipeRefreshLayout = findViewById(R.id.swipe);

        btn_next = findViewById(R.id.btn_next);

        btn_eliminar = findViewById(R.id.btn_eliminar);




        mToolbar = findViewById(R.id.toolbar);
        this.setTitle("");
        setSupportActionBar(mToolbar);


        idrepartidor = getIntent().getStringExtra("id_re");

        nomrepartidor = getIntent().getStringExtra("nom_repar");

        aperepartidor = getIntent().getStringExtra("ape_repar");

        celrepartidor = getIntent().getStringExtra("cel_repar");


        idUs = mAuth.getCurrentUser().getUid();



        linearLayout_recycleview = findViewById(R.id.linearLayout_compras);


        linearLayout_vacio = findViewById(R.id.linearLayout_vacio);



        verificarPermisos();



        mFirestore.collection("acumulador/"+idUs+"/Cliente").document("pedido").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {


                double conteo_productogg = documentSnapshot.getDouble("cont_productos");

                int conteo_productojj = (int) conteo_productogg;


                if (conteo_productojj == 0 ){


                    linearLayout_recycleview.setVisibility(View.GONE);

                    linearLayout_vacio.setVisibility(View.VISIBLE);

                    btn_next.setText("Se necesita que realices una compra");
                    btn_next.setEnabled(false);

                    btn_next.setBackground(getDrawable(R.drawable.ripple_effect_vacio));


                }else{


                    linearLayout_recycleview.setVisibility(View.VISIBLE);

                    linearLayout_vacio.setVisibility(View.GONE);


                    btn_next.setText("FINALIZAR >>");
                    btn_next.setEnabled(true);


                    repartidorselec(idUs);


                    totalprecio(idUs);





                    mTextViewRepartidores.setText("Seleccionar Repartidores");




                    btn_next.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {



                            //Intent i = new Intent(CarritoActivity.this, PedidoActivity.class);


                            Intent i = new Intent(CarritoActivity.this, Pagos_CarritoActivity.class);



                            if (mRecycler.getAdapter() != null){

                                if (mRecycler.getAdapter().getItemCount() == 0){


                                    Toast.makeText(CarritoActivity.this, "Lista Vacia", Toast.LENGTH_SHORT).show();


                                }else if(mRecycler.getAdapter().getItemCount() == 1){

                                    String idUser = mAuth.getCurrentUser().getUid();


                                    mFirestore.collection("Cliente").document(idUser).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot5) {

                                            String idRepartidor = documentSnapshot5.getString("id_repartidor");

                                            String nombreRepartidor = documentSnapshot5.getString("repartidor_nombre");

                                            String apellidoRepartidor = documentSnapshot5.getString("repartidor_apellido");

                                            String celularRepartidor = documentSnapshot5.getString("celularrepartidor");



                                            if (idRepartidor.isEmpty() && nombreRepartidor.isEmpty() && apellidoRepartidor.isEmpty() && celularRepartidor.isEmpty()){

                                                startActivity(new Intent(CarritoActivity.this, Lista_RepartidoresActivity.class));
                                                overridePendingTransition(R.anim.left_in, R.anim.left_out);
                                                finish();



                                            }else if (!idRepartidor.isEmpty() && nombreRepartidor.isEmpty() && apellidoRepartidor.isEmpty() && celularRepartidor.isEmpty()){

                                                startActivity(new Intent(CarritoActivity.this, Lista_RepartidoresActivity.class));
                                                overridePendingTransition(R.anim.left_in, R.anim.left_out);
                                                finish();



                                            }else if (!idRepartidor.isEmpty() && !nombreRepartidor.isEmpty() && apellidoRepartidor.isEmpty() && celularRepartidor.isEmpty()){

                                                startActivity(new Intent(CarritoActivity.this, Lista_RepartidoresActivity.class));
                                                overridePendingTransition(R.anim.left_in, R.anim.left_out);
                                                finish();



                                            }else if (!idRepartidor.isEmpty() && !nombreRepartidor.isEmpty() && !apellidoRepartidor.isEmpty() && celularRepartidor.isEmpty()){

                                                startActivity(new Intent(CarritoActivity.this, Lista_RepartidoresActivity.class));
                                                overridePendingTransition(R.anim.left_in, R.anim.left_out);
                                                finish();



                                            }else if (!idRepartidor.isEmpty() && !nombreRepartidor.isEmpty() && !apellidoRepartidor.isEmpty() && !celularRepartidor.isEmpty()){


                                                mFirestore.collection("carrito/"+idUs+"/pedido").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        for(QueryDocumentSnapshot snapshot : task.getResult()){




                                                            successMessageCompras(i,v, snapshot.getId());








                                                        }
                                                    }
                                                });






                                            }







                                        }
                                    });













                                }else if (mRecycler.getAdapter().getItemCount() >= 2){



                                    String idUser = mAuth.getCurrentUser().getUid();


                                    mFirestore.collection("Cliente").document(idUser).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot5) {

                                            String idRepartidor = documentSnapshot5.getString("id_repartidor");

                                            String nombreRepartidor = documentSnapshot5.getString("repartidor_nombre");

                                            String apellidoRepartidor = documentSnapshot5.getString("repartidor_apellido");

                                            String celularRepartidor = documentSnapshot5.getString("celularrepartidor");



                                            if (idRepartidor.isEmpty() && nombreRepartidor.isEmpty() && apellidoRepartidor.isEmpty() && celularRepartidor.isEmpty()){

                                                startActivity(new Intent(CarritoActivity.this, Lista_RepartidoresActivity.class));
                                                overridePendingTransition(R.anim.left_in, R.anim.left_out);
                                                finish();



                                            }else if (!idRepartidor.isEmpty() && nombreRepartidor.isEmpty() && apellidoRepartidor.isEmpty() && celularRepartidor.isEmpty()){

                                                startActivity(new Intent(CarritoActivity.this, Lista_RepartidoresActivity.class));
                                                overridePendingTransition(R.anim.left_in, R.anim.left_out);
                                                finish();



                                            }else if (!idRepartidor.isEmpty() && !nombreRepartidor.isEmpty() && apellidoRepartidor.isEmpty() && celularRepartidor.isEmpty()){

                                                startActivity(new Intent(CarritoActivity.this, Lista_RepartidoresActivity.class));
                                                overridePendingTransition(R.anim.left_in, R.anim.left_out);
                                                finish();



                                            }else if (!idRepartidor.isEmpty() && !nombreRepartidor.isEmpty() && !apellidoRepartidor.isEmpty() && celularRepartidor.isEmpty()){

                                                startActivity(new Intent(CarritoActivity.this, Lista_RepartidoresActivity.class));
                                                overridePendingTransition(R.anim.left_in, R.anim.left_out);
                                                finish();



                                            }else if (!idRepartidor.isEmpty() && !nombreRepartidor.isEmpty() && !apellidoRepartidor.isEmpty() && !celularRepartidor.isEmpty()){


                                                mFirestore.collection("carrito/"+idUs+"/pedido").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        for(QueryDocumentSnapshot snapshot1 : task.getResult()){


                                                            successMessage(i,v, snapshot1.getId());



                                                        }
                                                    }
                                                });





                                            }







                                        }
                                    });

















                                }




                            }












                        }
                    });











                }






            }
        });






        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();

                swipeRefreshLayout.setRefreshing(false);



            }
        });



        setUpRecyclerView();


        search_view();













    }




    public void eliminarrepartidor(View view){


        Map<String, Object> mapR = new HashMap<>();
        mapR.put("repartidor_nombre", "");
        mapR.put("repartidor_apellido","");
        mapR.put("id_repartidor","");
        mapR.put("celularrepartidor","");


        mFirestore.collection("Cliente").document(idUs).update(mapR);


        refresh();



        repartidorselec(idUs);


    }



    public void repartidorselec(String idUser){



        mFirestore.collection("Cliente").document(idUser).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot2) {


                String nombreRepartidor = documentSnapshot2.getString("repartidor_nombre");

                String apellidoRepartidor = documentSnapshot2.getString("repartidor_apellido");



                String usuarioRepartidores = nombreRepartidor + " " + apellidoRepartidor;






                if (nombreRepartidor.isEmpty() && apellidoRepartidor.isEmpty()){

                    mTextViewRepartidores.setText("Seleccionar Repartidores");
                    btn_eliminar.setVisibility(View.INVISIBLE);



                }else if (!nombreRepartidor.isEmpty() && !apellidoRepartidor.isEmpty()){

                    mTextViewRepartidores.setText(usuarioRepartidores);
                    btn_eliminar.setVisibility(View.VISIBLE);



                }


            }
        });





    }





    private void totalprecio(String idUser){



        mFirestore.collection("carrito/"+idUser+"/pedido").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot snapshot : task.getResult()){

                    precioproducto = snapshot.getDouble("vaccine_price");


                    precio_totalproductos += precioproducto;




                    BigDecimal cc = new BigDecimal(precio_totalproductos);

                    MathContext mc = new MathContext(3);




                    mTextViewtotal_producto.setText(""+cc.round(mc));






                }
            }
        });



    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_carrito_drawer, menu);
        return true;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.volver_inicio:


                startActivity(new Intent(CarritoActivity.this, InterfazClienteActivity.class));
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
                finish();


                return true;


        }


        return super.onOptionsItemSelected(item);
    }

















    public void successMessage(Intent i, View view, String idt) {




        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
        dialogo1.setTitle("AVISO");
        dialogo1.setMessage("¿Estas seguro que deseas concluir con la compra?");
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {



                Toast.makeText(getApplicationContext(), "Se realizo su pedido con exito", Toast.LENGTH_SHORT).show();

                String idUser = mAuth.getCurrentUser().getUid();


                mFirestore.collection("carrito/"+idUser+"/pedido").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(QueryDocumentSnapshot snapshot : task.getResult()){
                            mFirestore.collection("carrito/"+idUser+"/pedido").document(snapshot.getId()).delete();
                        }
                    }
                });




                i.putExtra("id_user", idUser);
                startActivity(i);



                pedidocarrito(view);



                acumulador(view);





                mFirestore.collection("Compraseditado").document(idt).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {


                        double cantidadPRR = documentSnapshot.getDouble("cantidadPro");

                        int cantidadPT = (int) cantidadPRR;


                        updateComprasEditado(cantidadPT, idt, view);


                    }
                });



                Map<String, Object> mapCP = new HashMap<>();



                int cantidadproductouu = 0;



                mapCP.put("cont_productos", cantidadproductouu);



                mFirestore.collection("acumulador/"+idUs+"/Cliente").document("pedido").update(mapCP);




                mFirestore.collection("acumulador/"+idUs+"/Cliente").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot snapshot : task.getResult()){

                            Map<String, Object> mapC = new HashMap<>();



                            mapC.put("producto", 0);



                            mFirestore.collection("acumulador/"+idUs+"/Cliente").document(snapshot.getId()).update(mapC);


                        }
                    }
                });






                enviosms();






            }
        });
        dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {

            }
        });


        dialogo1.show();








    }











    public void successMessageCompras(Intent i, View view, String idt) {





        AlertDialog.Builder dialogo2 = new AlertDialog.Builder(this);
        dialogo2.setTitle("AVISO");
        dialogo2.setMessage("¿Estas seguro que deseas concluir con la compra?");
        dialogo2.setCancelable(false);
        dialogo2.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo2, int id) {


                Toast.makeText(getApplicationContext(), "Se realizo su pedido con exito", Toast.LENGTH_SHORT).show();

                String idUser = mAuth.getCurrentUser().getUid();


                mFirestore.collection("carrito/"+idUser+"/pedido").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(QueryDocumentSnapshot snapshot : task.getResult()){
                            mFirestore.collection("carrito/"+idUser+"/pedido").document(snapshot.getId()).delete();
                        }
                    }
                });




                i.putExtra("id_user", idUser);
                startActivity(i);



                pedidocarritocompras(view, idt);



                acumulador(view);





                mFirestore.collection("Compraseditado").document(idt).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {


                        double cantidadPRR = documentSnapshot.getDouble("cantidadPro");

                        int cantidadPT = (int) cantidadPRR;


                        updateComprasEditado(cantidadPT, idt, view);


                    }
                });



                Map<String, Object> mapCP = new HashMap<>();


                int cantidadproductouu = 0;


                mapCP.put("cont_productos", cantidadproductouu);


                mFirestore.collection("acumulador/"+idUs+"/Cliente").document("pedido").update(mapCP);



                mFirestore.collection("acumulador/"+idUs+"/Cliente").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot snapshot : task.getResult()){

                            Map<String, Object> mapC = new HashMap<>();



                            mapC.put("producto", 0);



                            mFirestore.collection("acumulador/"+idUs+"/Cliente").document(snapshot.getId()).update(mapC);


                        }
                    }
                });





                enviosms();





            }
        });
        dialogo2.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo2, int id) {

            }
        });
        dialogo2.show();








    }







    private void updateComprasEditado(int agepet, String id, View v) {


        mFirestore.collection("Compraseditado").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {


                Map<String, Object> mapCA = new HashMap<>();




                //-----------------------------------------------------------

                String cantidadproducto = documentSnapshot.getString("age");

                int cantidadproductopda = Integer.parseInt(cantidadproducto);

                int agepetCA = cantidadproductopda - agepet;

                String agepetCE = Integer.toString(agepetCA);

                mapCA.put("age", agepetCE);

                //-----------------------------------------------------------


                mFirestore.collection("Compraseditado").document(id).update(mapCA);





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





    private void refresh(){
        Toast.makeText(getApplicationContext(), "Refreshed", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), CarritoActivity.class));
        overridePendingTransition(0,0);
        finish();
        overridePendingTransition(0,0);


    }



    @SuppressLint("NotifyDataSetChanged")
    private void setUpRecyclerView() {

        mRecycler = findViewById(R.id.recyclerViewSingle);
        mRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));





        query = mFirestore.collection("carrito/"+idUs+"/pedido").whereEqualTo("id_user", mAuth.getCurrentUser().getUid());
        //query = mFirestore.collection("carrito");

        FirestoreRecyclerOptions<Productos> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Productos>().setQuery(query, Productos.class).build();

        mAdapter = new CarritoProductoAdapter(firestoreRecyclerOptions);
        mAdapter.notifyDataSetChanged();
        mRecycler.setAdapter(mAdapter);



    }




    public void acumulador(View view){


        String idUser = mAuth.getCurrentUser().getUid();

        mFirestore.collection("acumulador/"+idUser+"/Cliente").document("pedido").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {


                double codigoPP=documentSnapshot.getDouble("codigoP");
                int codigoP = (int) codigoPP;


                int codigoPI= codigoP+1;



                String codigoPS = Integer.toString(codigoPI);


                //Toast.makeText(CarritoActivity.this,"codigoP: "+codigoPS+", codigoS: "+codigoPQ,Toast.LENGTH_SHORT).show();



                HashMap<String, Object> map = new HashMap<>();
                map.put("id_user",idUser);
                map.put("codigoP", codigoPI);


                mFirestore.collection("acumulador/"+idUser+"/Cliente").document("pedido").update(map);






            }
        });








    }





    public void enviosms(){



        String idUser = mAuth.getCurrentUser().getUid();



        mFirestore.collection("Cliente").document(idUser).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot5) {





                String celular_administrador = documentSnapshot5.getString("celular_administrador");


                String tarjeta_credito = documentSnapshot5.getString("tarjeta_credito");


                String email_administrador = documentSnapshot5.getString("email_administrador");



                String email_cliente = documentSnapshot5.getString("email");

                String password_cliente = documentSnapshot5.getString("password");



                String nombre_cliente = documentSnapshot5.getString("name");

                String apellido_cliente = documentSnapshot5.getString("apellido");



                if (celular_administrador.isEmpty()){

                    String mensajePedido = "El cliente "
                            +nombre_cliente+" "+ apellido_cliente+ " "+"realizo una compra"+" "+"el pago esta en espera"
                            + " "+"debe de confirmar si se realizo el pago del cliente";


                    String SENT = "SMS_SENT";

                    sentPI = PendingIntent.getBroadcast(CarritoActivity.this, 0, new Intent(SENT), 0);






                    smsManager.sendTextMessage("936137207",null,mensajePedido
                            ,sentPI,null);


                    Toast.makeText(CarritoActivity.this, "Mensaje Enviado", Toast.LENGTH_SHORT).show();


                }else if (!celular_administrador.isEmpty()){
                    String mensajePedido = "El cliente "
                            +nombre_cliente+" "+ apellido_cliente+ " "+"realizo una compra"+" "+"el pago esta en espera"
                            + " "+"debe de confirmar si se realizo el pago del cliente";


                    String SENT = "SMS_SENT";

                    sentPI = PendingIntent.getBroadcast(CarritoActivity.this, 0, new Intent(SENT), 0);


                    smsManager.sendTextMessage(celular_administrador,null,mensajePedido
                            ,sentPI,null);


                    Toast.makeText(CarritoActivity.this, "Mensaje Enviado", Toast.LENGTH_SHORT).show();

                }











                /*


                //------------------------------------------------------------------------





                    try {



                    String stringSenderEmail = email_cliente;
                    String stringPasswordSenderEmail = password_cliente;



                    String stringReceiverEmail = email_administrador;











                    String stringHost = "smtp.gmail.com";


                    Properties properties = System.getProperties();


                    properties.put("mail.smtp.host", stringHost);

                    properties.put("mail.smtp.port", "465");

                    properties.put("mail.smtp.ssl.enable", "true");

                    properties.put("mail.smtp.auth", "true");



                    javax.mail.Session session = Session.getInstance(properties, new Authenticator() {

                        @Override

                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(stringSenderEmail, stringPasswordSenderEmail);

                        }

                    });


                    MimeMessage mimeMessage = new MimeMessage(session);

                    mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(stringReceiverEmail));


                    mimeMessage.setSubject("Subject: Android App email");

                    mimeMessage.setText("Hello Programmer, \n\nProgrammer World has sent you this 2nd email. \n\n Cheers!\nProgrammer World");


                    Thread thread = new Thread(new Runnable() {

                        @Override


                        public void run() {

                            try {

                                Transport.send(mimeMessage);


                            } catch (MessagingException e) {
                                e.printStackTrace();

                            }

                        }

                    });

                    thread.start();


                } catch (AddressException e) {
                    e.printStackTrace();

                } catch (MessagingException e) {
                    e.printStackTrace();

                }







                //---------------------------------------------------------------------------



*/

















            }
        });






    }














    public void pedidocarritocompras(View view, String idcli){



        String idUser = mAuth.getCurrentUser().getUid();

        DocumentReference id = mFirestore.collection("pedidofinal").document();


        mFirestore.collection("pedido/"+idUser+"/cliente").document(idcli).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {




                double precio_totalpet = documentSnapshot.getDouble("vaccine_price");





                //nombreProducto = snapshot.getString("name");

                String estadopet = "Pendiente";






                String dia = obtenerdiaactual("America/Lima");

                int diad = Integer.parseInt(dia);

                int diaent = diad;

                String diape = String.valueOf(diaent);



                String mes = obtenermesactual("America/Lima");

                String ano = obteneranoactual("America/Lima");


                String fecha = obtenerFechaActual("America/Lima");


                String hora = obtenerHoraActual("America/Lima");

                String hora_actual = hora;


                String fecha_actual=fecha;


                String fecha_entrega = diape+"-"+mes+"-"+ano;





                mFirestore.collection("acumulador/"+idUser+"/Cliente").document("pedido").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {




                        mFirestore.collection("Usuario/Cliente/"+idUser).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                for (QueryDocumentSnapshot snapshot2 : task.getResult()){


                                    String nombreCarritoP = snapshot2.getString("name");

                                    String apellidoCarritoP = snapshot2.getString("apellido");



                                    double codigoPP=documentSnapshot.getDouble("codigoP");

                                    int codigoPL = (int) codigoPP;


                                    codigoP = "C00"+String.valueOf(codigoPL);


                                    HashMap<String, Object> mapS = new HashMap<>();
                                    mapS.put("id_user",idUser);
                                    mapS.put("id",id.getId());

                                    mapS.put("name",fecha_entrega);

                                    mapS.put("fecha_actual",fecha_actual);
                                    mapS.put("hora",hora_actual);
                                    mapS.put("nombre", nombreCarritoP);
                                    mapS.put("apellido", apellidoCarritoP);
                                    mapS.put("vaccine_price", precio_totalpet);
                                    mapS.put("color",estadopet);
                                    mapS.put("codigo", codigoP);
                                    mapS.put("pago", "en espera");





                                    mFirestore.collection("comprasfinal/"+idUser+"/cliente").document(codigoP).set(mapS);

                                    mFirestore.collection("comprasfinal/"+idUser+"/cliente").document(codigoP).update(mapS);







                                    mFirestore.collection("comprasfinal/"+idUser+"/cliente").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                            for (QueryDocumentSnapshot snapshot : task.getResult()){

                                                double precio_producto = snapshot.getDouble("vaccine_price");


                                                precio_totalhh = precio_totalhh + precio_producto;



                                                Double totalpreciopt = Double.parseDouble(mTextViewtotal_producto.getText().toString().trim());

                                                HashMap<String, Object> mapSR = new HashMap<>();

                                                mapSR.put("precio_producto", precio_totalpet);



                                                mFirestore.collection("comprasfinal/"+idUser+"/MensajePago").document(codigoP).set(mapSR);



                                                HashMap<String, Object> mapSQ = new HashMap<>();


                                                mapSQ.put("precio_total", totalpreciopt);
                                                mapSQ.put("mensaje_pago","Pendiente Cobrar:");




                                                mFirestore.collection("comprasfinal/"+idUser+"/PagoPedido").document("cliente").set(mapSQ);





                                            }



                                        }
                                    });





                                    mFirestore.collection("Usuario/Cliente/"+idUser).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            for (QueryDocumentSnapshot snapshot4 : task.getResult()){

                                                nombreCarrito = snapshot4.getString("name");
                                                HashMap<String, Object> map = new HashMap<>();
                                                map.put("id_user",idUser);
                                                map.put("id",id.getId());
                                                map.put("name",fecha_entrega);
                                                map.put("fecha_actual",fecha_actual);
                                                map.put("hora",hora_actual);
                                                map.put("nombre", nombreCarrito);
                                                map.put("vaccine_price", precio_totalpet);
                                                map.put("color",estadopet);
                                                map.put("codigo", codigoP);



                                                mFirestore.collection("pedidofinal").document(fecha_actual).set(map);

                                                mFirestore.collection("pedidofinal").document(fecha_actual).update(map);


                                                //Log.d("mercaderia",snapshot.getId()+"=>"+snapshot.getData());




                                            }



                                        }
                                    });











                                }
                            }
                        });












                    }
                });






































            }
        });







    }


















    public void pedidocarrito(View view){



        String idUser = mAuth.getCurrentUser().getUid();

        DocumentReference id = mFirestore.collection("pedidofinal").document();


        mFirestore.collection("pedido/"+idUser+"/cliente").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (QueryDocumentSnapshot snapshot : task.getResult()) {



                        precio_vacunapet = snapshot.getDouble("vaccine_price");




                        //nombreProducto = snapshot.getString("name");

                        String estadopet = "Pendiente";







                        String dia = obtenerdiaactual("America/Lima");

                        int diad = Integer.parseInt(dia);

                        int diaent = diad;

                        String diape = String.valueOf(diaent);



                        String mes = obtenermesactual("America/Lima");

                        String ano = obteneranoactual("America/Lima");


                        String fecha = obtenerFechaActual("America/Lima");


                        String hora = obtenerHoraActual("America/Lima");

                        String hora_actual = hora;


                        String fecha_actual=fecha;


                        String fecha_entrega = diape+"-"+mes+"-"+ano;




                        precio_totalpet = precio_totalpet + precio_vacunapet;







                        //mTextViewtotal_producto.setText(format.format(0.0));




                        //mTextViewtotal_producto.setText(format.format(precio_totalpet));




                        //data = snapshot.getData().toString();





                        mFirestore.collection("acumulador/"+idUser+"/Cliente").document("pedido").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot1) {




                                mFirestore.collection("Usuario/Cliente/"+idUser).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        for (QueryDocumentSnapshot snapshot2 : task.getResult()){


                                            String nombreCarritoP = snapshot2.getString("name");

                                            String apellidoCarritoP = snapshot2.getString("apellido");



                                            double codigoPP=documentSnapshot1.getDouble("codigoP");

                                            int codigoPL = (int) codigoPP;


                                            codigoP = "C00"+String.valueOf(codigoPL);


                                            HashMap<String, Object> mapS = new HashMap<>();
                                            mapS.put("id_user",idUser);
                                            mapS.put("id",id.getId());

                                            mapS.put("name",fecha_entrega);

                                            mapS.put("fecha_actual",fecha_actual);
                                            mapS.put("hora",hora_actual);
                                            mapS.put("nombre", nombreCarritoP);
                                            mapS.put("apellido", apellidoCarritoP);

                                            mapS.put("vaccine_price", precio_totalpet);
                                            mapS.put("color",estadopet);
                                            mapS.put("codigo", codigoP);
                                            mapS.put("pago", "en espera");








                                            mFirestore.collection("comprasfinal/"+idUser+"/cliente").document(codigoP).set(mapS);

                                            mFirestore.collection("comprasfinal/"+idUser+"/cliente").document(codigoP).update(mapS);




                                            mFirestore.collection("comprasfinal/"+idUser+"/cliente").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                                    for (QueryDocumentSnapshot snapshot : task.getResult()){

                                                        double precio_producto = snapshot.getDouble("vaccine_price");


                                                        precio_totalhh = precio_totalhh + precio_producto;



                                                        Double totalpreciopt = Double.parseDouble(mTextViewtotal_producto.getText().toString().trim());

                                                        HashMap<String, Object> mapSR = new HashMap<>();

                                                        mapSR.put("precio_producto", precio_totalpet);



                                                        mFirestore.collection("comprasfinal/"+idUser+"/MensajePago").document(codigoP).set(mapSR);



                                                        HashMap<String, Object> mapSQ = new HashMap<>();


                                                        mapSQ.put("precio_total", totalpreciopt);
                                                        mapSQ.put("mensaje_pago","Pendiente Cobrar:");




                                                        mFirestore.collection("comprasfinal/"+idUser+"/PagoPedido").document("cliente").set(mapSQ);











                                                    }



                                                }
                                            });











                                            mFirestore.collection("Usuario/Cliente/"+idUser).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    for (QueryDocumentSnapshot snapshot4 : task.getResult()){

                                                        nombreCarrito = snapshot4.getString("name");
                                                        HashMap<String, Object> map = new HashMap<>();
                                                        map.put("id_user",idUser);
                                                        map.put("id",id.getId());
                                                        map.put("name",fecha_entrega);
                                                        map.put("fecha_actual",fecha_actual);
                                                        map.put("hora",hora_actual);
                                                        map.put("nombre", nombreCarrito);
                                                        map.put("vaccine_price", precio_totalpet);
                                                        map.put("color",estadopet);
                                                        map.put("codigo", codigoP);

                                                        mFirestore.collection("pedidofinal").document(fecha_actual).set(map);

                                                        mFirestore.collection("pedidofinal").document(fecha_actual).update(map);


                                                        //Log.d("mercaderia",snapshot.getId()+"=>"+snapshot.getData());




                                                    }



                                                }
                                            });








                                        }
                                    }
                                });







                            }
                        });






            }
        }
    });





}





    private void verificarPermisos(){



       if (ActivityCompat.checkSelfPermission(CarritoActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){

           ActivityCompat.requestPermissions(CarritoActivity.this, new String[]{Manifest.permission.SEND_SMS},1);


       }







    }















/*


    private void createPDF(){


        PdfDocument myPdfDocument = new PdfDocument();
        Paint myPaint = new Paint();
        Paint titlePaint = new Paint();

        PdfDocument.PageInfo myPageInfo1 = new PdfDocument.PageInfo.Builder(1200,2010,1).create();
        PdfDocument.Page myPage1 = myPdfDocument.startPage(myPageInfo1);
        Canvas canvas = myPage1.getCanvas();

        canvas.drawBitmap(scaledbmp,0,0,myPaint);
        titlePaint.setTextAlign(Paint.Align.CENTER);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        titlePaint.setTextSize(70);
        canvas.drawText("Dimond Pizza", pagewidth/2,270,titlePaint);

        myPaint.setColor(Color.rgb(0,113,188));
        myPaint.setTextSize(30f);
        myPaint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("Call: 022-41414242",1160,40, myPaint);
        canvas.drawText("022-86868787",1160,80,myPaint);

        titlePaint.setTextAlign(Paint.Align.CENTER);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.ITALIC));
        titlePaint.setTextSize(70);
        canvas.drawText("Invoice",pagewidth/2, 500, titlePaint);


        myPaint.setTextAlign(Paint.Align.LEFT);
        myPaint.setTextSize(35f);
        myPaint.setColor(Color.BLACK);

        canvas.drawText("Customer Name:"+"producto",20,590,myPaint);
        canvas.drawText("Contact No:"+"7542158",20,640,myPaint);

        myPaint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("Invoice No:"+"232425",pagewidth-20, 590, myPaint);

        canvas.drawText("Date: "+"23-09-2022",pagewidth-20,640,myPaint);

        canvas.drawText("Time: "+"07:58",pagewidth-20,690,myPaint);


        myPaint.setStyle(Paint.Style.STROKE);
        myPaint.setStrokeWidth(2);
        canvas.drawRect(20,780,pagewidth-20,860,myPaint);

        myPaint.setTextAlign(Paint.Align.LEFT);
        myPaint.setStyle(Paint.Style.FILL);
        canvas.drawText("Si. No", 40,830,myPaint);
        canvas.drawText("Item Descripcion", 200, 830, myPaint);
        canvas.drawText("Price",700,830,myPaint);
        canvas.drawText("Qty.", 900, 830, myPaint);
        canvas.drawText("Total", 1050,830,myPaint);

        canvas.drawLine(180,790,180,840,myPaint);
        canvas.drawLine(680,790,680,840,myPaint);
        canvas.drawLine(880,790,880,840,myPaint);
        canvas.drawLine(1030,790,1030,840,myPaint);



        canvas.drawText("1.",40,950,myPaint);




        canvas.drawText("dato1",200,950,myPaint);



        canvas.drawText("dato2",700,950,myPaint);
        canvas.drawText("dato3.",900,950,myPaint);






        myPaint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("952.0",pagewidth-40, 950, myPaint);
        myPaint.setTextAlign(Paint.Align.LEFT);






        canvas.drawText("2", 40, 1050, myPaint);
        canvas.drawText("dato4.",200,1050,myPaint);
        canvas.drawText("dato5.",700,1050,myPaint);
        canvas.drawText("dato6.",900,1050, myPaint);





        myPaint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("dato6.",pagewidth-40,1050,myPaint);
        myPaint.setTextAlign(Paint.Align.LEFT);






        canvas.drawLine(680,1200,pagewidth-20,1200,myPaint);
        canvas.drawText("SubTotal", 700,1250, myPaint);
        canvas.drawText(":",900,1250,myPaint);
        myPaint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("500",pagewidth-40,1250,myPaint);


        myPaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("Tax(12%)",700,1300,myPaint);
        canvas.drawText(":",900,1300,myPaint);
        myPaint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("dato7.x%",pagewidth-40,1300,myPaint);
        myPaint.setTextAlign(Paint.Align.LEFT);

        myPaint.setColor(Color.rgb(247,147,30));
        canvas.drawRect(680,1350,pagewidth-20,1450,myPaint);


        myPaint.setColor(Color.BLACK);
        myPaint.setTextSize(50f);
        myPaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("Total",700,1415,myPaint);
        myPaint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("date8",pagewidth-40,1415,myPaint);


        myPdfDocument.finishPage(myPage1);

        File file = new File(this.getExternalFilesDir("/"),"Archivo.pdf");

        try{
            myPdfDocument.writeTo(new FileOutputStream(file));
        } catch (IOException e){
            e.printStackTrace();
        }



        myPdfDocument.close();


    }

*/





    private void search_view() {
        search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                textSearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                textSearch(s);
                return false;
            }
        });
    }

    public void textSearch(String s){
        FirestoreRecyclerOptions<Productos> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Productos>()
                        .setQuery(query.orderBy("name")
                                .startAt(s).endAt(s+"~"), Productos.class).build();
        mAdapter = new CarritoProductoAdapter(firestoreRecyclerOptions);
        mAdapter.startListening();
        mRecycler.setAdapter(mAdapter);
    }


    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.startListening();

    }
    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();

    }








}