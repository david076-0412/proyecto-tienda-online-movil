package com.aplication.appgestionrepartos.model;

public class Productos {


    String name,id;
    int age;
    String color, photo;
    Double vaccine_price;

    public Productos(){}


    public Productos(String name, String id, int age, String color, String photo, Double vaccine_price) {
        this.name = name;
        this.age = age;
        this.color = color;
        this.photo = photo;
        this.vaccine_price = vaccine_price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
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

    public Double getVaccine_price() {
        return vaccine_price;
    }

    public void setVaccine_price(Double vaccine_price) {
        this.vaccine_price = vaccine_price;

    }

    public void addOne(){
        this.age++;
    }

    public void removeOne(){
        this.age--;
    }



}
