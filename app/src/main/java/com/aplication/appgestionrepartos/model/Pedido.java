package com.aplication.appgestionrepartos.model;

public class Pedido {

    public boolean isSelected;
    String name, codigo, color, nombre, pago, hora;
    Double vaccine_price;


    int cantidad;


    public Pedido(){}

    public Pedido(String codigo, String color) {

        this.codigo = codigo;
        this.color = color;
    }

    public Pedido(String name, String codigo, String color, String nombre, String pago, String hora, Double vaccine_price) {
        this.name = name;
        this.codigo = codigo;
        this.color = color;
        this.nombre = nombre;
        this.pago = pago;
        this.hora = hora;
        this.vaccine_price = vaccine_price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public Double getVaccine_price() {
        return vaccine_price;
    }

    public void setVaccine_price(Double vaccine_price) {
        this.vaccine_price = vaccine_price;
    }


    public String getPago() {
        return pago;
    }

    public void setPago(String pago) {
        this.pago = pago;
    }


    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    @Override
    public String toString() {
        return  color;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }





}
