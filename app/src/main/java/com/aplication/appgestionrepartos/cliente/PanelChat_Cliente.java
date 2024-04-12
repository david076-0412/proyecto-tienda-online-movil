package com.aplication.appgestionrepartos.cliente;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.aplication.appgestionrepartos.administrador.PanelChat_Administrador;
import com.aplication.appgestionrepartos.chat.ContactosAdministradorActivity;
import com.aplication.appgestionrepartos.R;
import com.aplication.appgestionrepartos.chat.ContactosRepartidorActivity;
import com.aplication.appgestionrepartos.interfaz.InterfazClienteActivity;
import com.aplication.appgestionrepartos.interfaz.InterfazRepartidorActivity;


public class PanelChat_Cliente extends AppCompatActivity {



    String Usuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_chat_cliente);


        Usuario = getIntent().getStringExtra("Us");






    }


    public void contactos_administrador(View view){


        Intent ica = new Intent(PanelChat_Cliente.this,ContactosAdministradorActivity.class);

        ica.putExtra("Us",Usuario);

        startActivity(ica);


        Toast.makeText(PanelChat_Cliente.this, ""+Usuario, Toast.LENGTH_SHORT).show();



        overridePendingTransition(R.anim.left_in, R.anim.left_out);


        finish();


    }



    public void contactos_repartidor(View view){


        Intent icr = new Intent(PanelChat_Cliente.this,ContactosRepartidorActivity.class);

        icr.putExtra("Us",Usuario);
        startActivity(icr);


        Toast.makeText(PanelChat_Cliente.this, ""+Usuario, Toast.LENGTH_SHORT).show();



        overridePendingTransition(R.anim.left_in, R.anim.left_out);


        finish();


    }




    public void cerrar_sesion_chat_cliente(View view){

        startActivity(new Intent(PanelChat_Cliente.this, InterfazClienteActivity.class));

        overridePendingTransition(R.anim.left_in, R.anim.left_out);

        finish();


    }



}