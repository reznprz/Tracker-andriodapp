package com.example.macbookpro.tracker;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by macbookpro on 3/10/18.
 */

class MyIntentService extends IntentService {
    private DBHelper dbHelper;
    static List<Stops> stop = new ArrayList<>();

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public MyIntentService() {
        super("This is intentservice.");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        stop.clear();
        if (intent.getAction() == "ACTION_GETDATA_DATABASE") {
            dbHelper = new DBHelper(this);
            try {
                dbHelper.CopyDataBaseFromAsset();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            dbHelper.openDataBase();
            dbHelper.Get_ContactDetails("1");


        } else if (intent.getAction() == "ACTION_REFORMAT_LIST") {
            int source = intent.getIntExtra("sindex", 0);
            int destination = intent.getIntExtra("dindex", 0);
            int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            if (destination > source) {
                int i = 0;
                for (int s = source; s <= destination; s++) {
                    stop.add(SearchActivity.stops.get(s));
                    stop.get(i).setTime(stop.get(i).getTime());
                    i = i + 1;
                }

            } else if (destination < source) {
                int i = 0;
                for (int s = source; s < SearchActivity.stops.size(); s++) {
                    stop.add(SearchActivity.stops.get(s));
                    stop.get(i).setTime(stop.get(i).getTime());
                    i = i + 1;
                }
                for (int x = 0; x < source; x++) {
                    stop.add(SearchActivity.stops.get(x));
                    stop.get(i).setTime( stop.get(i).getTime());
                    i = i + 1;
                }

            } else {
                Toast.makeText(this, "You're where you want to be. Congrats", Toast.LENGTH_LONG).show();
            }
        }

    }

    public int runBinarySearchRecursively(
            int[] sortedArray, int key, int low, int high) {
        int middle = (low + high) / 2;

        if (high < low) {
            return -1;
        }

        if (key == sortedArray[middle]) {
            return middle;
        } else if (key < sortedArray[middle]) {
            return runBinarySearchRecursively(
                    sortedArray, key, low, middle - 1);
        } else {
            return runBinarySearchRecursively(
                    sortedArray, key, middle + 1, high);
        }
    }
}
