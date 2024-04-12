package com.aplication.appgestionrepartos.model;




public class Quejas_Sugerencias {

    String id, nombre, incidencia, departamento, celular;


    public Quejas_Sugerencias(){}



    public Quejas_Sugerencias(String nombre, String incidencia, String departamento, String celular) {
        this.nombre = nombre;
        this.incidencia = incidencia;
        this.departamento = departamento;
        this.celular = celular;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIncidencia() {
        return incidencia;
    }

    public void setIncidencia(String incidencia) {
        this.incidencia = incidencia;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }
}
