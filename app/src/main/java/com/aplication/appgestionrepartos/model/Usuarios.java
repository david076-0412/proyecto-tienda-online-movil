package com.aplication.appgestionrepartos.model;

import com.aplication.appgestionrepartos.R;

public class Usuarios {

    String id, name, apellido, email, dni, direccion, photo;

    public Usuarios(){}

    public Usuarios(String id,String name,String apellido){
        this.id=id;
        this.name=name;
        this.apellido=apellido;
    }



    public Usuarios(String name, String email, String dni, String direccion, String photo) {
        this.name = name;
        this.email = email;
        this.dni = dni;
        this.direccion = direccion;
        this.photo = photo;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return name+" "+apellido;
    }
}
