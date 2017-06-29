package com.example.danny.firebaseapp.usuarios;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vspc-PoisonRiver on 16/04/2017.
 */
@IgnoreExtraProperties
public class Comentarios {


    private String foto;
    private String cometario;
    private int estrellas;
    private String user;
    private String para;

    public Comentarios() {
    }

    public Comentarios(String foto, String cometario, int estrellas) {
        this.foto = foto;
        this.cometario = cometario;
        this.estrellas = estrellas;
    }

    public String getPara() {
        return para;
    }

    public void setPara(String para) {
        this.para = para;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getCometario() {
        return cometario;
    }

    public void setCometario(String cometario) {
        this.cometario = cometario;
    }

    public int getEstrellas() {
        return estrellas;
    }

    public void setEstrellas(int estrellas) {
        this.estrellas = estrellas;
    }


    @Exclude
    public Map<String,Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("foto",this.foto);
        result.put("comentario",this.cometario);
        result.put("estrellas",this.estrellas);
        result.put("user",this.user);
        return result;
    }

}
