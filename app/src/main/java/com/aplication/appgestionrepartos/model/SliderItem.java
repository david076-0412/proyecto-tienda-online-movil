package com.aplication.appgestionrepartos.model;

public class SliderItem {
    private String titulo;
    private String imagen;

    public SliderItem() {
    }

    public SliderItem(String imagen, String titulo) {
        this.imagen = imagen;
        this.titulo = titulo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}