package com.example.hp.gotoevent.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.hp.gotoevent.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button displayBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setUIView();
        displayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayInfo();
            }
        });

    }

    private void displayInfo(){
        FirebaseUser user = mAuth.getCurrentUser();
        mAuth = FirebaseAuth.getInstance();
        System.out.println("hahya smiya dyal user ================  " +user.getDisplayName());
        System.out.println("hahya metadata dyal user ================  " +user.getMetadata());
        System.out.println("hahya proveder dyal user ================  " +user.getProviderId());
        System.out.println("hahya id dyal user ================  " +user.getUid());
        System.out.println("hahya smiya dyal user ================  " +user.getDisplayName());

    }
    private void setUIView(){
        displayBtn = (Button) findViewById(R.id.displayBtn);

    }
}
