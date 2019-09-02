package com.example.macbookpro.tracker;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class reviewscreen extends Fragment {

    TextView notereiew;
    InputStream inputStream;
    String line, result = null;
    TextView milagetxt;
    String[] data;
    String[] data1;
    Button milagebtn;
    public static ArrayList<String> odameter= new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reviewscreen, container, false);

        notereiew =(TextView)view.findViewById(R.id.id_notereviewt_txt);
        milagetxt =(TextView) view.findViewById(R.id.mtxt);
        milagebtn = (Button) view.findViewById(R.id.button6);

        milagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
                TextView txt = (TextView) getActivity().findViewById(R.id.mtxt);
                String text="";
                for(int x=0;x<odameter.size();x++){
                    text +=odameter.get(x);
                }
                txt.setText(text);

            }
        });

        return view;
    }
    private void getData() {
        try {
            URL url = new URL("http://dmis.club/dmis_app/rijan/odameterbegin.php");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            inputStream = new BufferedInputStream(con.getInputStream());

        } catch (Exception e) {
            e.printStackTrace();
        }

        //READ INPUT STREAM CONTENT INto string
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();

            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line + "\n");
            }
            inputStream.close();
            result = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            JSONArray jsonArray = new JSONArray(result);
            JSONObject jsonObject = null;
            data = new String[jsonArray.length()];
            data1 = new String[jsonArray.length()];

            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                data[i]=jsonObject.getString("Odametersone");
                //data[i]=jsonObject.getString("Odametersone");

                data1[i]=jsonObject.getString("Odametersttwo");
                odameter.add(data[i]);

            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("file note read",""+result);
        }

    }

}



