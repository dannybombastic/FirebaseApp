package com.example.danny.firebaseapp.usuarios;

import android.icu.util.Output;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by danny on 03/01/2017.
 */
@IgnoreExtraProperties

public class Users {
    private String email;
    private String name;
    private String pic;




    public Users(){


    }


    public Users(String email, String name,String pic) {
        this.pic =  pic;
        this.email = email;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }


@Override
    public boolean equals(Object obj) {
    boolean ban = false;
    if(obj != null) {
        ban = this.email.equalsIgnoreCase(((Users) obj).getEmail());
    }
        return ban;
    }


    @Exclude
    public Map<String,Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("email",this.email);
        result.put("name",this.name);
        result.put("pic",this.pic);



     return result;
    }
}
