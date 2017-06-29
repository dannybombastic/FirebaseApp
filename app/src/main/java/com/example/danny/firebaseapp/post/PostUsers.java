package com.example.danny.firebaseapp.post;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vspc-PoisonRiver on 28/01/2017.
 */
@IgnoreExtraProperties
public class PostUsers {

    private String name;
    private String year;
    private String motor;
    private String price;
    private String marca;
    private String modelo;
    private String localidad;
    private String pic_one;
    private String pic_two;
    private String pic_three;
    private String email;
    private boolean comb;
    private boolean dis;
    private String desc;
    private String tel;
    private String tofind;


    public PostUsers() {


    }

    public PostUsers(String motor, String price, String marca, String modelo, String localidad, String pic_one, String pic_two, String pic_three, String email,String telefono, boolean comb, String desc,String year,String tofind) {
        this.motor = motor;
        this.price = price;
        this.marca = marca;
        this.modelo = modelo;
        this.localidad = localidad;
        this.pic_one = pic_one;
        this.pic_two = pic_two;
        this.pic_three = pic_three;
        this.email = email;
        this.comb = comb;
        this.desc = desc;
        this.tel = telefono;
        this.year = year;
        this.tofind = tofind;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToFind() {
        return tofind;
    }

    public void setToFind(String toFind) {
        this.tofind = toFind;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public boolean isComb() {
        return comb;
    }

    public void setComb(boolean comb) {
        this.comb = comb;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }





    public boolean isDis() {
        return dis;
    }

    public void setDis(boolean dis) {
        this.dis = dis;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotor() {
        return this.motor;
    }

    public void setMotor(String motor) {
        this.motor = motor;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getPic_one() {
        return pic_one;
    }

    public void setPic_one(String pic_one) {
        this.pic_one = pic_one;
    }

    public String getPic_two() {
        return pic_two;
    }

    public void setPic_two(String pic_two) {
        this.pic_two = pic_two;
    }

    public String getPic_three() {
        return pic_three;
    }

    public void setPic_three(String pic_three) {
        this.pic_three = pic_three;
    }




    @Exclude
    public Map<String,Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("year",this.year);
        result.put("name",this.name);
        result.put("pic_one",this.pic_one);
        result.put("pic_two",this.pic_two);
        result.put("pic_three",this.pic_three);
        result.put("tel",this.tel);
        result.put("desc",this.desc);
        result.put("localidad",this.localidad);
        result.put("toFind",this.tofind);
        result.put("comb",this.comb);
        result.put("price",this.price);



        return result;
    }
}
