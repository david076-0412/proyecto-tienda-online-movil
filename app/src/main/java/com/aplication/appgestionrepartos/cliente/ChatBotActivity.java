package com.aplication.appgestionrepartos.cliente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;


import com.aplication.appgestionrepartos.R;
import com.aplication.appgestionrepartos.adapter.MessageAdapter;
import com.aplication.appgestionrepartos.interfaz.InterfazClienteActivity;
import com.aplication.appgestionrepartos.model.ResponseMessage;
import com.aplication.appgestionrepartos.no_internet_connection.NetworkChangeListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


import androidx.appcompat.app.ActionBar;


import androidx.appcompat.widget.Toolbar;



public class ChatBotActivity extends AppCompatActivity {


    EditText userInput;
    RecyclerView recyclerView;
    MessageAdapter messageAdapter;
    List<ResponseMessage> responseMessageList;

    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;


    private Toolbar mToolbar;

    private ActionBar mActionBar;


    private String nombrecliente,apellidocliente;

    private String nombrerepartidor,apellidorepartidor,celularrepartidor;

    private String hora_atencion,horario_atencion;


    NetworkChangeListener networkChangeListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot);


        userInput = findViewById(R.id.userInput);
        userInput.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        userInput.setSingleLine(true);

        userInput.setNestedScrollingEnabled(false);

        networkChangeListener = new NetworkChangeListener();

        recyclerView = findViewById(R.id.conversation);
        responseMessageList = new ArrayList<>();

        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();


        String id = getIntent().getStringExtra("id_pet");





        mToolbar = findViewById(R.id.toolbar);
        this.setTitle("");
        setSupportActionBar(mToolbar);




        messageAdapter = new MessageAdapter(responseMessageList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(messageAdapter);



        String mensajeIntro = "Bienvenido al asistente virtual \uD83E\uDD16 mi nombre es Dante";


        ResponseMessage responseMessage1 = new ResponseMessage(mensajeIntro, true);
        responseMessageList.add(responseMessage1);


        String mensajeOpciones = "OPCIONES DE AYUDA \n"
                +"1.COMPRAR LA MEMBRESIA\n"
                +"2.DIRECCIÓN DE LA TIENDA\n"
                +"3.COMUNICARME CON LOS REPARTIDORES\n"
                +"4.INFORMACIÓN DE LA APLICACION\n"
                +"5.TENGO PROBLEMAS CON MI PEDIDO\n"
                +"6.HORARIOS DE ATENCIÓN\n"
                +"7.NECESITO CANCELAR MI PEDIDO\n"
                +"8.MAS OPCIONES";


        ResponseMessage responseMessage2 = new ResponseMessage(mensajeOpciones, true);
        responseMessageList.add(responseMessage2);




        String mensajeAyuda = "ELIGE UNA DE LA OPCIONES ESCRIBIENDO POR NUMERO DE ORDEN\n"
                +"COMO SE MUESTRA EN LA LISTA DE ARRIBA";

        ResponseMessage responseMessage3 = new ResponseMessage(mensajeAyuda, true);
        responseMessageList.add(responseMessage3);








        userInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                if (i == EditorInfo.IME_ACTION_SEND) {


                    String mensajeInput = userInput.getText().toString().trim();


                    secuenciabot(mensajeInput);



                    if (!isLastVisible())

                        recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);

                }
                return false;
            }
        });




    }


    boolean isLastVisible() {
        LinearLayoutManager layoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
        int pos = layoutManager.findLastCompletelyVisibleItemPosition();
        int numItems = recyclerView.getAdapter().getItemCount();
        return (pos >= numItems);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chatbot_drawer, menu);
        return true;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.volver_inicio:

                startActivity(new Intent(ChatBotActivity.this, InterfazClienteActivity.class));

                overridePendingTransition(R.anim.left_in, R.anim.left_out);

                finish();


                return true;


        }


        return super.onOptionsItemSelected(item);
    }








    public boolean secuenciabot(String mensajeInput){

        switch (mensajeInput){



            case "":


                userInput.setText("");
                userInput.setError("Introducir mensaje");


                return true;





            case "HOLA":


                String idUser = mAuth.getCurrentUser().getUid();


                mFirestore.collection("Usuario/Cliente/"+idUser).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot snapshot :task.getResult()){
                            nombrecliente = snapshot.getString("name");

                            apellidocliente = snapshot.getString("apellido");


                            ResponseMessage responseMessage4 = new ResponseMessage(userInput.getText().toString(), true);
                            responseMessageList.add(responseMessage4);


                            String mensaje="HOLA AMIGO(A) "+ nombrecliente+" "+apellidocliente;

                            ResponseMessage responseMessage5 = new ResponseMessage(mensaje.toString(), false);

                            //ResponseMessage responseMessage5 = new ResponseMessage(userInput.getText().toString(), false);

                            responseMessageList.add(responseMessage5);

                            messageAdapter.notifyDataSetChanged();

                            userInput.setText("");




                        }
                    }
                });










                return true;






            case "1":

                ResponseMessage responseMessage6 = new ResponseMessage(userInput.getText().toString(), true);
                responseMessageList.add(responseMessage6);

                String mensajeOpcionUno="HOLA AMIGO(A)\n"
                        +"MEMBRESIA SIMPLE: S/.120 \n"
                        +"MEMBRESIA MEDIUM: S/220 \n"
                        +"MEMBRESIA PREMIUM: S/350 \n"
                        +"DEBE LLAMAR A ESTE NUMERO DE TELEFONO:\n"
                        +"+51921736660\n"
                        +"PARA ACORDAR CON EL NIVEL DE MEMBRESIA,\n"
                        +"GRACIAS POR TU INTERES";

                ResponseMessage responseMessage7 = new ResponseMessage(mensajeOpcionUno.toString(), false);

                //ResponseMessage responseMessage7 = new ResponseMessage(userInput.getText().toString(), false);

                responseMessageList.add(responseMessage7);

                messageAdapter.notifyDataSetChanged();

                userInput.setText("");





                return true;




            case "2":


                ResponseMessage responseMessage8 = new ResponseMessage(userInput.getText().toString(), true);
                responseMessageList.add(responseMessage8);

                String mensajeOpcionDos="MZA. F LOTE. 04 ASC. PRO CASA HUERTA E IND. PE (ALT OVALO DE ANCON)\n" + "LIMA / LIMA / ANCON \n";

                ResponseMessage responseMessage9 = new ResponseMessage(mensajeOpcionDos.toString(), false);

                //ResponseMessage responseMessage7 = new ResponseMessage(userInput.getText().toString(), false);

                responseMessageList.add(responseMessage9);

                messageAdapter.notifyDataSetChanged();

                userInput.setText("");




                return true;



            case "3":


                String idUs = mAuth.getCurrentUser().getUid();



                mFirestore.collection("Usuario/Cliente/"+idUs).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot snapshot :task.getResult()){

                            nombrerepartidor = snapshot.getString("repartidor_nombre");

                            apellidorepartidor = snapshot.getString("repartidor_apellido");


                            celularrepartidor = snapshot.getString("celularrepartidor");


                            ResponseMessage responseMessage10 = new ResponseMessage(userInput.getText().toString(), true);
                            responseMessageList.add(responseMessage10);



                            String mensajeOpcionTres="El Repartidor asignado es \n"+nombrerepartidor+" "+ apellidorepartidor+"\n"+"Número de celular:\n"+
                                    celularrepartidor;



                            ResponseMessage responseMessage11 = new ResponseMessage(mensajeOpcionTres.toString(), false);

                            //ResponseMessage responseMessage7 = new ResponseMessage(userInput.getText().toString(), false);

                            responseMessageList.add(responseMessage11);

                            messageAdapter.notifyDataSetChanged();

                            userInput.setText("");




                        }
                    }
                });




                return true;



            case "4":


                ResponseMessage responseMessage12 = new ResponseMessage(userInput.getText().toString(), true);
                responseMessageList.add(responseMessage12);

                String mensajeOpcionCuatro="Esta aplicación que esta compuesta por tres aplicaciones en una son:\n" +
                        "    -Aplicación de compras\n" +
                        "    -Aplicación de Gestión de Repartos\n" +
                        "    -Aplicación de supervisión y monitoreo\n";

                ResponseMessage responseMessage13 = new ResponseMessage(mensajeOpcionCuatro.toString(), false);

                //ResponseMessage responseMessage7 = new ResponseMessage(userInput.getText().toString(), false);

                responseMessageList.add(responseMessage13);

                messageAdapter.notifyDataSetChanged();

                userInput.setText("");








                return true;



            case "5":



                ResponseMessage responseMessage14 = new ResponseMessage(userInput.getText().toString(), true);
                responseMessageList.add(responseMessage14);

                String mensajeOpcionCinco="Debe de comunicarse con el repartidor reponsable de su pedido. Tambien puede contactarse con el administrador a cargo de su pago desde el chat de la aplicación";

                ResponseMessage responseMessage15 = new ResponseMessage(mensajeOpcionCinco.toString(), false);

                //ResponseMessage responseMessage7 = new ResponseMessage(userInput.getText().toString(), false);

                responseMessageList.add(responseMessage15);

                messageAdapter.notifyDataSetChanged();

                userInput.setText("");



                return true;



            case "6":


                String idUse = mAuth.getCurrentUser().getUid();



                mFirestore.collection("acumulador/"+idUse+"/Cliente").document("pedido").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {


                        hora_atencion = documentSnapshot.getString("hora_atencion");

                        horario_atencion = documentSnapshot.getString("horario_atencion");





                        ResponseMessage responseMessage16 = new ResponseMessage(userInput.getText().toString(), true);
                        responseMessageList.add(responseMessage16);



                        String mensajeOpcionSeis="El Horario de atención es :\n"+horario_atencion+"\n"+hora_atencion+"\n";



                        ResponseMessage responseMessage17 = new ResponseMessage(mensajeOpcionSeis.toString(), false);

                        //ResponseMessage responseMessage7 = new ResponseMessage(userInput.getText().toString(), false);

                        responseMessageList.add(responseMessage17);

                        messageAdapter.notifyDataSetChanged();

                        userInput.setText("");





                    }
                });







                return true;



            case "7":


                ResponseMessage responseMessage18 = new ResponseMessage(userInput.getText().toString(), true);
                responseMessageList.add(responseMessage18);

                String mensajeOpcionSiete="Se puede cancelar el pedido solicito desde la misma aplicación o llamar al administrador a cargo para que lo haga";

                ResponseMessage responseMessage19 = new ResponseMessage(mensajeOpcionSiete.toString(), false);

                //ResponseMessage responseMessage7 = new ResponseMessage(userInput.getText().toString(), false);

                responseMessageList.add(responseMessage19);

                messageAdapter.notifyDataSetChanged();

                userInput.setText("");







                return true;






            case "8":



                ResponseMessage responseMessage23 = new ResponseMessage(userInput.getText().toString(), true);
                responseMessageList.add(responseMessage23);

                String mensajeOpcionesMas = "OPCIONES DE AYUDA\n"
                        +"9.PROBLEMAS CON EL PEDIDO\n"
                        +"10.PROBLEMAS CON LA APP\n"
                        +"11.PROBLEMAS CON EL REPARTIDOR\n"
                        +"12.DEVOLUCIÓN DEL PEDIDO\n"
                        +"13.VOLVER A LAS OPCIONES ANTERIORES";

                ResponseMessage responseMessage24 = new ResponseMessage(mensajeOpcionesMas.toString(), false);

                //ResponseMessage responseMessage24 = new ResponseMessage(userInput.getText().toString(), false);

                responseMessageList.add(responseMessage24);

                messageAdapter.notifyDataSetChanged();

                userInput.setText("");







                return true;


            case "9":



                ResponseMessage responseMessage20 = new ResponseMessage(userInput.getText().toString(), true);
                responseMessageList.add(responseMessage20);

                String mensajeOpcionesNueve = "Cuando hay un problema con el pedido se debe poner en contacto con el repartidor asignado o con el administrador para la devolución del producto y del dinero\n";

                ResponseMessage responseMessage21 = new ResponseMessage(mensajeOpcionesNueve.toString(), false);

                //ResponseMessage responseMessage24 = new ResponseMessage(userInput.getText().toString(), false);

                responseMessageList.add(responseMessage21);

                messageAdapter.notifyDataSetChanged();

                userInput.setText("");



                return true;




            case "10":


                ResponseMessage responseMessage25 = new ResponseMessage(userInput.getText().toString(), true);
                responseMessageList.add(responseMessage25);

                String mensajeOpcionesDiez = "Para enviar las quejas sobre el funcionamiento de la aplicación debe de escoger la opción del Whatsapp que se encuentra en la parte inferior derecha de la ventana.\n"
                        +"Eso lo enviara con el creador de la misma aplicación";

                ResponseMessage responseMessage26 = new ResponseMessage(mensajeOpcionesDiez.toString(), false);

                //ResponseMessage responseMessage24 = new ResponseMessage(userInput.getText().toString(), false);

                responseMessageList.add(responseMessage26);

                messageAdapter.notifyDataSetChanged();

                userInput.setText("");




                return true;





            case "11":


                String idU = mAuth.getCurrentUser().getUid();



                mFirestore.collection("Usuario/Cliente/"+idU).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot snapshot :task.getResult()){

                            nombrerepartidor = snapshot.getString("repartidor_nombre");

                            apellidorepartidor = snapshot.getString("repartidor_apellido");


                            celularrepartidor = snapshot.getString("celularrepartidor");


                            ResponseMessage responseMessage29 = new ResponseMessage(userInput.getText().toString(), true);
                            responseMessageList.add(responseMessage29);

                            String mensajeOpcionesOnce = "El Repartidor con el cual se hara la queja es \n"+nombrerepartidor+" "+ apellidorepartidor+"\n"+"Número de celular:\n"+
                                    celularrepartidor;;

                            ResponseMessage responseMessage30 = new ResponseMessage(mensajeOpcionesOnce.toString(), false);

                            //ResponseMessage responseMessage24 = new ResponseMessage(userInput.getText().toString(), false);

                            responseMessageList.add(responseMessage30);

                            messageAdapter.notifyDataSetChanged();

                            userInput.setText("");





                        }
                    }
                });











                return true;




            case "12":


                ResponseMessage responseMessage31 = new ResponseMessage(userInput.getText().toString(), true);
                responseMessageList.add(responseMessage31);

                String mensajeOpcionesDoce = "Se debe de comunicar con el administrado a cargo para que autorice la devolución del pago del producto junto con la mercancia entregada al cliente";

                ResponseMessage responseMessage32 = new ResponseMessage(mensajeOpcionesDoce.toString(), false);

                //ResponseMessage responseMessage24 = new ResponseMessage(userInput.getText().toString(), false);

                responseMessageList.add(responseMessage32);

                messageAdapter.notifyDataSetChanged();

                userInput.setText("");




                return true;








            case "13":


                ResponseMessage responseMessage27 = new ResponseMessage(userInput.getText().toString(), true);
                responseMessageList.add(responseMessage27);

                String mensajeOpcionesVolver = "OPCIONES DE AYUDA \n"
                        +"1.COMPRAR LA MEMBRESIA\n"
                        +"2.DIRECCIÓN DE LA TIENDA\n"
                        +"3.COMUNICARME CON LOS REPARTIDORES\n"
                        +"4.INFORMACIÓN DE LA APLICACION\n"
                        +"5.TENGO PROBLEMAS CON MI PEDIDO\n"
                        +"6.HORARIOS DE ATENCIÓN\n"
                        +"7.NECESITO CANCELAR MI PEDIDO\n"
                        +"8.MAS OPCIONES";

                ResponseMessage responseMessage28 = new ResponseMessage(mensajeOpcionesVolver.toString(), false);

                //ResponseMessage responseMessage28 = new ResponseMessage(userInput.getText().toString(), false);

                responseMessageList.add(responseMessage28);

                messageAdapter.notifyDataSetChanged();

                userInput.setText("");



                return true;




            default:






                ResponseMessage responseMessage29 = new ResponseMessage(userInput.getText().toString(), true);
                responseMessageList.add(responseMessage29);

                String mensajeError = "No logro entender, porfavor eliga una de las opciones " +
                        "mencionadas anteriormente";

                ResponseMessage responseMessage30 = new ResponseMessage(mensajeError.toString(), false);

                //ResponseMessage responseMessage30 = new ResponseMessage(userInput.getText().toString(), false);

                responseMessageList.add(responseMessage30);

                messageAdapter.notifyDataSetChanged();

                userInput.setText("");




        }



        return false;




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