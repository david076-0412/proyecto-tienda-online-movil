package com.aplication.appgestionrepartos.model;

import android.widget.RatingBar;

public class Pet {
    String name, age, color, photo;
    float estrellas;
    Double vaccine_price;

    public Pet(){}

    public Pet(String name, String age, String color, String photo, float estrellas, Double vaccine_price) {
        this.name = name;
        this.age = age;
        this.color = color;
        this.photo = photo;
        this.estrellas = estrellas;
        this.vaccine_price = vaccine_price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }


    public float getEstrellas() {
        return estrellas;
    }

    public void setEstrellas(float estrellas) {
        this.estrellas = estrellas;
    }

    public Double getVaccine_price() {
        return vaccine_price;
    }

    public void setVaccine_price(Double vaccine_price) {
        this.vaccine_price = vaccine_price;
    }


}
