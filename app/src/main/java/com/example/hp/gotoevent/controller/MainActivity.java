package com.example.hp.gotoevent.controller;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.hp.gotoevent.R;

public class MainActivity extends AppCompatActivity {
    // Write a message to the database

   private RelativeLayout relativeLayout;
   private ImageView transitionLogo;
   private static int SPLASH_TIME_OUT = 8000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setViewUI();
        //((TransitionDrawable) transitionLogo.getDrawable()).startTransition(2000);
         transitionLogo.setBackgroundResource(R.drawable.animation_list);
         AnimationDrawable frameAnimation = (AnimationDrawable) transitionLogo.getBackground();
        frameAnimation.start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent HomeIntent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(HomeIntent);
                finish();
            }
        },SPLASH_TIME_OUT);
    }


    private void setViewUI(){
        transitionLogo = (ImageView) findViewById(R.id.transitionLogo);
    }

    private  void addArtist(){
        //String name  =  nameTxt.getText().toString();
      //  String id = databaseReference.push().getKey();
      //  Artist artist = new Artist(id,name,23);
       // databaseReference.child(id).setValue(artist);
       // Log.i("create new A rtist : " ,artist.toString());

    }

}
