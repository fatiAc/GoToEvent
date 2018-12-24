package com.example.hp.gotoevent.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.hp.gotoevent.R;
import com.example.hp.gotoevent.bean.Event;
import com.example.hp.gotoevent.service.EventService;

import java.util.List;

public class DisplayEventsActivity extends AppCompatActivity {


    private EventService eventService = new EventService();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_events);

        ListView listView = (ListView) findViewById(R.id.eventListView);

    }


}
