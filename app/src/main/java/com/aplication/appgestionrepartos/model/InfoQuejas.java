package com.aplication.appgestionrepartos.model;
import com.aplication.appgestionrepartos.R;
public class InfoQuejas {


    String id, departamento;


    public InfoQuejas(String id, String departamento) {
        this.id = id;
        this.departamento = departamento;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }


    @Override
    public String toString() {
        return departamento;
    }
}
