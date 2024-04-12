package com.aplication.appgestionrepartos.pago;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.aplication.appgestionrepartos.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Pago_Anuncio_OficialActivity extends AppCompatActivity {


    private FirebaseFirestore mfirestore;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pago_anuncio_oficial);

        mfirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();


    }







}