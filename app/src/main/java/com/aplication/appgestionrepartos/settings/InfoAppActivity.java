package com.aplication.appgestionrepartos.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.aplication.appgestionrepartos.R;

public class InfoAppActivity extends AppCompatActivity {

    Button btn_atras;

    String id,usuario;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infoapp);


        id = getIntent().getStringExtra("id_user");

        usuario = getIntent().getStringExtra("Usuario");


        btn_atras = findViewById(R.id.btn_atras);

        btn_atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(InfoAppActivity.this, SettingsActivity.class);
                i.putExtra("id_user",id);
                i.putExtra("Usuario",usuario);

                startActivity(i);



            }
        });





    }
}