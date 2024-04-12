package com.aplication.appgestionrepartos.settings;

import android.content.Intent;
import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;


import com.aplication.appgestionrepartos.R;
import com.aplication.appgestionrepartos.interfaz.InterfazAdministradorActivity;
import com.aplication.appgestionrepartos.interfaz.InterfazClienteActivity;
import com.aplication.appgestionrepartos.interfaz.InterfazRepartidorActivity;
import com.aplication.appgestionrepartos.settings.InfoAppActivity;


public class SettingsFragment extends PreferenceFragmentCompat {


    String id, usuario;


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        setPreferencesFromResource(R.xml.preferences, rootKey);

        id = getActivity().getIntent().getStringExtra("id_user");

        usuario = getActivity().getIntent().getStringExtra("Usuario");





        Preference myPerf = (Preference)findPreference("contact_preference");

        myPerf.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                if (usuario.equals("Cliente")){

                    Intent i = new Intent(getContext(), InfoAppActivity.class);
                    i.putExtra("id_user",id);
                    i.putExtra("Usuario",usuario);
                    startActivity(i);
                }else if (usuario.equals("Repartidor")){
                    Intent i = new Intent(getContext(), InfoAppActivity.class);
                    i.putExtra("id_user",id);
                    i.putExtra("Usuario",usuario);
                    startActivity(i);

                }else if (usuario.equals("Administrador")){
                    Intent i = new Intent(getContext(), InfoAppActivity.class);
                    i.putExtra("id_user",id);
                    i.putExtra("Usuario",usuario);
                    startActivity(i);
                }






                return true;
            }
        });

        Preference myInicio = (Preference) findPreference("volver_preference");

        myInicio.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {


                if (usuario.equals("Cliente")){

                    Intent i = new Intent(getContext(), InterfazClienteActivity.class);
                    i.putExtra("id_user",id);
                    startActivity(i);
                }else if (usuario.equals("Repartidor")){
                    Intent i = new Intent(getContext(), InterfazRepartidorActivity.class);
                    i.putExtra("id_user",id);
                    startActivity(i);

                }else if (usuario.equals("Administrador")){
                    Intent i = new Intent(getContext(), InterfazAdministradorActivity.class);
                    i.putExtra("id_user",id);
                    startActivity(i);
                }







                return true;
            }
        });


    }
}