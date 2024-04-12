package com.aplication.appgestionrepartos.cliente;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.aplication.appgestionrepartos.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Pagos_CarritoActivity extends AppCompatActivity {



    FirebaseFirestore mFirestore;

    FirebaseAuth mAuth;


    TextView mTextViewtransferencia, mTextViewCelular, mTextViewHorario_Atencion, mTextViewHora_Atencion;


    Button btn_pedido_total;

    String idUser;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagos_carrito);


        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();


        idUser = mAuth.getCurrentUser().getUid();



        mTextViewtransferencia = findViewById(R.id.n_cuenta_bancaria);

        mTextViewCelular = findViewById(R.id.celular_administrador);


        btn_pedido_total = findViewById(R.id.btn_pedido_total);

        mTextViewHorario_Atencion = findViewById(R.id.horario_atencion);

        mTextViewHora_Atencion = findViewById(R.id.hora_atencion);




        mFirestore.collection("Cliente").document(idUser).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot1) {




                String numero_cuenta_bancaria_administrador = documentSnapshot1.getString("tarjeta_credito_administrador");



                String celular_administrador = documentSnapshot1.getString("celular_administrador");






                if (numero_cuenta_bancaria_administrador.isEmpty() && celular_administrador.isEmpty()){



                    mFirestore.collection("acumulador/"+idUser+"/Cliente").document("pedido").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot2) {


                            String cuenta_administrador_defecto = documentSnapshot2.getString("tarjeta_credito_administrador");


                            mTextViewtransferencia.setText(cuenta_administrador_defecto);


                            String celular_administrador_defecto = documentSnapshot2.getString("celular_administrador");



                            mTextViewCelular.setText(celular_administrador_defecto);



                        }
                    });





                }else if (!numero_cuenta_bancaria_administrador.isEmpty()){



                    mTextViewtransferencia.setText(numero_cuenta_bancaria_administrador);



                    mFirestore.collection("acumulador/"+idUser+"/Cliente").document("pedido").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot3) {



                            String celular_defecto = documentSnapshot3.getString("celular_administrador");



                            mTextViewCelular.setText(celular_defecto);



                        }
                    });




                }else if (celular_administrador !=null){



                    mFirestore.collection("acumulador/"+idUser+"/Cliente").document("pedido").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot4) {



                            String cuenta_defecto = documentSnapshot4.getString("tarjeta_credito_administrador");



                            mTextViewCelular.setText(cuenta_defecto);



                        }
                    });







                    mTextViewCelular.setText(celular_administrador);










                }else if (!numero_cuenta_bancaria_administrador.isEmpty() && !celular_administrador.isEmpty()){



                    mTextViewtransferencia.setText(numero_cuenta_bancaria_administrador);



                    mTextViewCelular.setText(celular_administrador);



                }








            }
        });













        mFirestore.collection("acumulador/"+idUser+"/Cliente").document("pedido").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot2) {


                String horario_atencion = documentSnapshot2.getString("horario_atencion");

                String hora_atencion = documentSnapshot2.getString("hora_atencion");


                mTextViewHorario_Atencion.setText(horario_atencion);

                mTextViewHora_Atencion.setText(hora_atencion);






            }
        });










        btn_pedido_total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i = new Intent(Pagos_CarritoActivity.this, PedidoActivity.class);


                overridePendingTransition(R.anim.left_in, R.anim.left_out);

                startActivity(i);




            }
        });







    }











}