package com.example.hp.gotoevent.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by fatima on 18/12/2018.
 */

public class MessageUtil {


    public static void shortToast(Context context , String message){
        Toast.makeText(context , message,Toast.LENGTH_SHORT).show();
    }

    public static void  longToast(Context context , String message){
        Toast.makeText(context , message,Toast.LENGTH_LONG).show();
    }
}
