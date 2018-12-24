package com.example.hp.gotoevent.controller;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;

import com.example.hp.gotoevent.R;
import com.example.hp.gotoevent.bean.Event;
import com.example.hp.gotoevent.service.EventService;
import com.example.hp.gotoevent.service.UserService;
import com.example.hp.gotoevent.util.FirebaseRealTimeDB;
import com.example.hp.gotoevent.util.MessageUtil;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class NewEventActivity extends AppCompatActivity implements Serializable {


    private EditText descriptionTxt;
    private EditText titleTxt;
    private EditText adresTxt;
    private EditText eventDat;
    private EditText timeTxt;
    private Button chooseImgBtn, validBtn,addLocationBtn;
    private ImageView imageView;

    private Uri filePath;

    private EventService eventService = new EventService();
    private final int PICK_IMAGE_REQUEST = 71;
    final Calendar myCalendar = Calendar.getInstance();
    private Event event;
    private int heure,min;
    private UserService userService = new UserService();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        setUIView();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
        heure = myCalendar.get(Calendar.HOUR_OF_DAY);
        min = myCalendar.get(Calendar.MINUTE);
       timeTxt.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               TimePickerDialog timePickerDialog = new TimePickerDialog(NewEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                   @Override
                   public void onTimeSet(TimePicker timePicker, int houre, int minute) {
                       timeTxt.setText(houre + ":" + minute);
                       heure = houre;
                       min = minute;
                   }
               },heure,min,false
               );
               timePickerDialog.show();
           }
       });

        eventDat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(NewEventActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        descriptionTxt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction() & MotionEvent.ACTION_MASK){
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });


        chooseImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  chooseImg();
            }
        });

        validBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateFormAndSave();
            }
        });

        addLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // saveValues();
                Intent intent = new Intent(NewEventActivity.this, EventLocationActivity.class);
                intent.putExtra("event" , initEvent());
                startActivity(intent);
            }
        });
    }

    private void validateFormAndSave(){
        boolean isOk = true;
        String title = titleTxt.getText().toString();
        String description = descriptionTxt.getText().toString();
        String addresse = adresTxt.getText().toString();
        Date date = eventService.generateDate(myCalendar,heure,min);
        if(title.equals("") || title == null){
            isOk = false;
            titleTxt.setError("required");
        }
        if(description.equals("") || description == null){
            isOk = false;
            descriptionTxt.setError("required");
        }
        if (addresse.equals("") || addresse == null){
            isOk = false;
            adresTxt.setError("required");
        }
        if(date.equals("") || date == null){
            isOk = false;
            eventDat.setError("required");
        }
       /* String imgID = eventService.isFile(filePath);
        if(imgID == null){
            isOk = false;
        }*/
        if(isOk == true){
            eventService.save(title,description, addresse, date,getIntent(),filePath,NewEventActivity.this);
            MessageUtil.longToast(NewEventActivity.this,"Vous avez bien créer un evenement :) ");
        }


    }


    private String[] initEvent(){
        String myEvent[] = {titleTxt.getText().toString(),descriptionTxt.getText().toString(), myCalendar.getTime().toString() ,};
        return myEvent;
    }



    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        eventDat.setText(sdf.format(myCalendar.getTime()));
        Date d = myCalendar.getTime();
        System.out.println("haaaaahwa date =========== " +d);
    }

    private void chooseImg() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select picture ") , PICK_IMAGE_REQUEST);
    }


    private void setUIView(){
        userService.connectedUser();
        chooseImgBtn = (Button) findViewById(R.id.addImgBtn);
        validBtn = (Button) findViewById(R.id.valideeBtn);
        addLocationBtn = (Button) findViewById(R.id.addLocationBtn);
        imageView = (ImageView) findViewById(R.id.imgView);
        descriptionTxt = (EditText) findViewById(R.id.descriptionTxt);
        eventDat = (EditText) findViewById(R.id.dateTxt);
        titleTxt = (EditText) findViewById(R.id.titleTxt);
        adresTxt = (EditText) findViewById(R.id.adresTxt);
        timeTxt = (EditText) findViewById(R.id.timeTxt);
        Bundle b = getIntent().getExtras();
        if(b != null){
            boolean getParam = b.getBoolean("getParam");
            if(getParam){
                Bundle bundle = getIntent().getExtras();
                String event[] = bundle.getStringArray("myEvent");
                descriptionTxt.setText(event[1]);
                eventDat.setText(event[2]);
                titleTxt.setText(event[0]);
                boolean isAddress = (boolean) bundle.get("isNull");
                if(isAddress){
                    adresTxt.setText((String) bundle.get("address"));
                }
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            filePath = data.getData();
            System.out.println("hahwa file dyali"+filePath.toString());
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                System.out.println("fil Notfound ================");
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("exception ================");
                e.printStackTrace();
            }
        }
    }



}
