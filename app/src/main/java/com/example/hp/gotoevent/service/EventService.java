package com.example.hp.gotoevent.service;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.example.hp.gotoevent.bean.Event;
import com.example.hp.gotoevent.bean.Role;
import com.example.hp.gotoevent.controller.NewEventActivity;
import com.example.hp.gotoevent.util.FirebaseRealTimeDB;
import com.example.hp.gotoevent.util.MessageUtil;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class EventService {

    private  UserService userService = new UserService();

    public String isFile(Uri filePath) {
        if(filePath != null ){
            String fileID = UUID.randomUUID().toString();
            return fileID;
        }
        else return null;
    }

    private void uploadFile(final Context context, Uri filePath){
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Uploadion ... ");
        progressDialog.show();
        String fileID = isFile(filePath);
        StorageReference ref = FirebaseRealTimeDB.initStorageRef().child("image/"+ fileID);
        ref.putFile(filePath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        MessageUtil.shortToast(context,"Upload");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        MessageUtil.shortToast(context,"Failed "+e.getMessage());
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                        progressDialog.setMessage("upload "+(int) progress + "%");
                    }
                });
    }

    private LatLng getLatLng(Intent intent){
        Bundle bundle = intent.getExtras();
        double latitude = bundle.getDouble("latitude");
        double longitude = bundle.getDouble("longitude");
        LatLng latLng = new LatLng(latitude, longitude);
        return latLng;
    }

    public void save(String title, String description, String place, Date date,Intent intent,Uri filePath,final Context context){
        uploadFile(context,filePath);
        LatLng latLng = getLatLng(intent);
        Event event = new Event(title,description,place,latLng,date,isFile(filePath));
        List<Role> roles = new ArrayList<>();
        Role adminRole = new Role(1,userService.connectedUser().getUid());
        roles.add(adminRole);
        event.setRoles(roles);
        FirebaseRealTimeDB.save(event , "event");
    }

    public Date generateDate(Calendar myCalendar,int heure,int min){
        Date date = new Date();
        date.setHours(heure);
        date.setMinutes(min);
        date.setMonth(myCalendar.getTime().getMonth()+1);
        date.setYear(myCalendar.getTime().getYear()+1900);
        return date;
    }



    private Event clone(Event event, Event destinationEvent){
        destinationEvent.setName(event.getName());
        destinationEvent.setDate(event.getDate());
        destinationEvent.setLatLng(event.getLatLng());
        destinationEvent.setRoles(event.getRoles());
        destinationEvent.setImgID(event.getImgID());
        destinationEvent.setLieu(event.getLieu());
        return destinationEvent;
    }

}
