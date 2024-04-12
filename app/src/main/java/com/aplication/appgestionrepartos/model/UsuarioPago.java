package com.aplication.appgestionrepartos.model;


import com.aplication.appgestionrepartos.R;
public class UsuarioPago {

    String id, name, apellido,estado_pago, prueba, photo;

    public UsuarioPago(){}


    public UsuarioPago(String name, String apellido, String estado_pago, String prueba, String photo) {
        this.name = name;
        this.apellido = apellido;
        this.estado_pago = estado_pago;
        this.prueba = prueba;
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

    public String getEstado_pago() {
        return estado_pago;
    }

    public void setEstado_pago(String estado_pago) {
        this.estado_pago = estado_pago;
    }

    public String getPrueba() {
        return prueba;
    }

    public void setPrueba(String prueba) {
        this.prueba = prueba;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }


    @Override
    public String toString() {
        return name + " "+apellido;
    }
}
