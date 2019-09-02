package com.example.macbookpro.tracker;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;

public class layout_main2 extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    public static String id;
    public static String Empid;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main2);
        Empid=getIntent().getStringExtra("Empid");

        BottomNavigationView btnnav = findViewById(R.id.nagivatoin);
        btnnav.setOnNavigationItemSelectedListener(navListener);
        //Toast.makeText(this, ""+empid, Toast.LENGTH_SHORT).show();




        FloatingActionButton fab = findViewById(R.id.addbtn_float);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = null;
                FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.Main_Fragment, new addscreen());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        Bundle b = getIntent().getExtras();
        //id =b.getString("Empid");
        //Toast.makeText(this, ""+id, Toast.LENGTH_SHORT).show();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selecedFragment = null;
            switch (menuItem.getItemId()){
                case R.id.nav_addbus:
                    selecedFragment=new buss_addscreen();
                    break;
                case R.id.nav_review:
                    selecedFragment=new reviewscreen();
                    break;
                case R.id.nav_search:
                    selecedFragment= new searchscreen();
                    break;

            }
            getSupportFragmentManager().beginTransaction().replace(R.id.Main_Fragment, selecedFragment).commit();

            return true;
        }
    };


    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayofMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayofMonth);
        String currentDateString = DateFormat.getDateInstance().format(c.getTime());
        TextView textView = (TextView) findViewById(R.id.Time_txt);
        textView.setText(currentDateString);

    }
}
