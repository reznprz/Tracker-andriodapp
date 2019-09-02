package com.example.macbookpro.tracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    AutoCompleteTextView source;
    AutoCompleteTextView destination;
    Button button;
    static List<String> list=new ArrayList<>();
    static List<Stops> stops=new ArrayList<>();
    int sourceposition=0;
    int destinationposition=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        source=findViewById(R.id.autoCompleteTextView);
        destination=findViewById(R.id.autoCompleteTextView2);
        button=findViewById(R.id.buttonsearch);
        Intent intent=new Intent(this,MyIntentService.class);
        intent.setAction("ACTION_GETDATA_DATABASE");
        startService(intent);
        source.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sourceposition=position;
            }
        });
        destination.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                destinationposition=position;
            }
        });
        source.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                source.showDropDown();
            }
        });
        destination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                destination.showDropDown();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.select_dialog_singlechoice,list);
        source.setThreshold(1);
        source.setAdapter(adapter);
        destination.setThreshold(1);
        destination.setAdapter(adapter);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentservice=new Intent(SearchActivity.this,MyIntentService.class);
                intentservice.setAction("ACTION_REFORMAT_LIST");
                intentservice.putExtra("sindex",sourceposition);
                intentservice.putExtra("dindex",destinationposition);
                startService(intentservice);
                Intent intent=new Intent(SearchActivity.this,HomeActivity.class);
                startActivity(intent);

            }
        });
    }
}
