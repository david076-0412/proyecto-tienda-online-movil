package com.aplication.appgestionrepartos.repartidor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.aplication.appgestionrepartos.R;
import com.aplication.appgestionrepartos.administrador.PanelChat_Administrador;
import com.aplication.appgestionrepartos.chat.ContactosAdministradorActivity;
import com.aplication.appgestionrepartos.chat.ContactosClienteActivity;
import com.aplication.appgestionrepartos.cliente.PanelChat_Cliente;
import com.aplication.appgestionrepartos.interfaz.InterfazRepartidorActivity;

public class PanelChat_RepartidorActivity extends AppCompatActivity {



    String Usuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_chat_repartidor);


        Usuario = getIntent().getStringExtra("Us");



    }


    public void contactos_cliente(View view){


        Intent icc = new Intent(PanelChat_RepartidorActivity.this, ContactosClienteActivity.class);


        icc.putExtra("Us",Usuario);

        startActivity(icc);


        Toast.makeText(PanelChat_RepartidorActivity.this, ""+Usuario, Toast.LENGTH_SHORT).show();



        overridePendingTransition(R.anim.left_in, R.anim.left_out);

        finish();


    }



    public void contactos_administrador(View view){


        Intent ica = new Intent(PanelChat_RepartidorActivity.this, ContactosAdministradorActivity.class);

        ica.putExtra("Us",Usuario);


        Toast.makeText(PanelChat_RepartidorActivity.this, ""+Usuario, Toast.LENGTH_SHORT).show();



        startActivity(ica);



        overridePendingTransition(R.anim.left_in, R.anim.left_out);

        finish();


    }




    public void cerrar_sesion_chat_repartidor(View view){

        startActivity(new Intent(PanelChat_RepartidorActivity.this, InterfazRepartidorActivity.class));

        overridePendingTransition(R.anim.left_in, R.anim.left_out);

        finish();


    }



}