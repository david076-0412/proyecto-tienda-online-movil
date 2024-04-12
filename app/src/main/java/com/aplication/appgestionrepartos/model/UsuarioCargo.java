package com.aplication.appgestionrepartos.model;

import com.aplication.appgestionrepartos.R;

public class UsuarioCargo {

    String id, name, apellido;


    public UsuarioCargo(String id, String name, String apellido){
        this.id=id;
        this.name=name;
        this.apellido= apellido;

    }






    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }



    @Override
    public String toString() {
        return name+" "+apellido;
    }
}
