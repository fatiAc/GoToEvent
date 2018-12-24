package com.example.hp.gotoevent.service;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.hp.gotoevent.bean.User;
import com.example.hp.gotoevent.controller.SingUpActivity;
import com.example.hp.gotoevent.util.FirebaseRealTimeDB;
import com.example.hp.gotoevent.util.HashageUtil;
import com.example.hp.gotoevent.util.MessageUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

/**
 * Created by fatima on 18/12/2018.
 */

public class UserService {

    public void saveUser(String nom, String prenom , String email){
            User user = new User(nom, prenom ,email);
            FirebaseRealTimeDB.save(user, "user");
    }


    public FirebaseUser connectedUser(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
       return user;
    }
}
