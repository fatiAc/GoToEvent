package com.example.hp.gotoevent.controller;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hp.gotoevent.R;
import com.example.hp.gotoevent.service.BaseActivity;
import com.example.hp.gotoevent.util.FirebaseRealTimeDB;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class HomeActivity extends BaseActivity {

    private static final String TAG = "EmailPassword";
    private Button toSingUp;
    private SignInButton singInGoogleBtn;
    private Button seConnecteBtn;
    private EditText emailTxt;
    private EditText paswrdTxt;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setUIView();

        seConnecteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();

            }
        });
        toSingUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent HomeIntent = new Intent(HomeActivity.this, SingUpActivity.class);
                startActivity(HomeIntent);
            }
        });

    }


    private void signIn() {
        String email = emailTxt.getText().toString();
        String password = paswrdTxt.getText().toString();
        Log.d(TAG, "signIn:" + email);
        if (!validateForm(email,password)) {
            return;
        }
        showProgressDialog();
        // [START sign_in_with_email]
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(HomeActivity.this, NewEventActivity.class);
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {
                            // mStatusTextView.setText(R.string.auth_failed);
                        }
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }

    private boolean validateForm(String email,String password) {
        boolean valid = true;
        if (TextUtils.isEmpty(email)) {
            emailTxt.setError("Required.");
            valid = false;
        } else {
            emailTxt.setError(null);
        }
        if (TextUtils.isEmpty(password)) {
            paswrdTxt.setError("Required.");
            valid = false;
        } else {
            paswrdTxt.setError(null);
        }
        return valid;
    }

    private void setUIView(){
        toSingUp = (Button) findViewById(R.id.signUpBtn);
        seConnecteBtn = (Button) findViewById(R.id.submitBtn);
        emailTxt = (EditText) findViewById(R.id.emailTxt);
        paswrdTxt = (EditText) findViewById(R.id.paswrdTxt);
        singInGoogleBtn = (SignInButton) findViewById(R.id.singInGoogleBtn);
    }







}
