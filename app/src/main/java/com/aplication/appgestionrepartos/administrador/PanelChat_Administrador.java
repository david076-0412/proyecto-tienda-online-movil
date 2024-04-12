package com.aplication.appgestionrepartos.administrador;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.aplication.appgestionrepartos.R;
import com.aplication.appgestionrepartos.chat.ContactosAdministradorActivity;
import com.aplication.appgestionrepartos.chat.ContactosClienteActivity;
import com.aplication.appgestionrepartos.chat.ContactosRepartidorActivity;
import com.aplication.appgestionrepartos.cliente.PanelChat_Cliente;
import com.aplication.appgestionrepartos.interfaz.InterfazAdministradorActivity;
import com.aplication.appgestionrepartos.repartidor.PanelChat_RepartidorActivity;

public class PanelChat_Administrador extends AppCompatActivity {


    String Usuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_chat_administrador);

        Usuario = getIntent().getStringExtra("Us");


    }




    public void contactos_cliente(View view){


        Intent icc = new Intent(PanelChat_Administrador.this, ContactosClienteActivity.class);


        icc.putExtra("Us",Usuario);

        startActivity(icc);


        Toast.makeText(PanelChat_Administrador.this, ""+Usuario, Toast.LENGTH_SHORT).show();




        overridePendingTransition(R.anim.left_in, R.anim.left_out);

        finish();


    }



    public void contactos_repartidor(View view){




        Intent icr = new Intent(PanelChat_Administrador.this, ContactosRepartidorActivity.class);


        icr.putExtra("Us",Usuario);


        Toast.makeText(PanelChat_Administrador.this, ""+Usuario, Toast.LENGTH_SHORT).show();





        startActivity(icr);




        overridePendingTransition(R.anim.left_in, R.anim.left_out);

        finish();


    }




    public void cerrar_sesion_chat_administrador(View view){

        startActivity(new Intent(PanelChat_Administrador.this, InterfazAdministradorActivity.class));

        overridePendingTransition(R.anim.left_in, R.anim.left_out);

        finish();


    }



}