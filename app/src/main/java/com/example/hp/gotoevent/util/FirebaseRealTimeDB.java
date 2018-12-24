package com.example.hp.gotoevent.util;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.hp.gotoevent.bean.Event;
import com.example.hp.gotoevent.bean.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by fatima on 18/12/2018.
 */

public class FirebaseRealTimeDB {

    static FirebaseDatabase database = FirebaseDatabase.getInstance();
    static  DatabaseReference databaseReference;
    static List<Object> objects = new ArrayList<>();

    public static StorageReference initStorageRef(){
        FirebaseStorage firebaseStorage= FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();
        return storageReference;
    }

    public static void query(){
        dbGetRef("event");
        System.out.println("**/*/*/*/*/*/*/*/*/**/**" + databaseReference.orderByChild("lieu"));
    }

    public static String generatID(){
        String id = databaseReference.push().getKey();
        return id;
    }
    private static void dbGetRef(String tableName){
        databaseReference = database.getReference(tableName);
    }

    public static void save(Object objet , String tableName ){
        dbGetRef(tableName);
        databaseReference.child(generatID()).setValue(objet);

    }


    public static List<Object> addToList (String  tableName){
        dbGetRef(tableName);
        query();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                List<Object> objects1 = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    Object obj = dataSnapshot1.getValue(Object.class);
                    objects1.add(obj);
                }
                objects = objects1;
            }
             @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("***********  Faild ***************************************");
            }
        });
        return objects;
    }

}
