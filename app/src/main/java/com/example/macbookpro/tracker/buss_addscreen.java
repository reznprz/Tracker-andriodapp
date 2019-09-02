package com.example.macbookpro.tracker;

import android.app.ProgressDialog;
import android.os.AsyncTask;
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
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class buss_addscreen extends Fragment {

    TextView id_busname;
    TextView id_busname_txt;
    TextView id_Lice;
    TextView id_Lice_txt;
    TextView id_make;
    TextView id_make_txt;
    TextView id_color;
    TextView id_color_txt;
    Button id_create;
    ProgressDialog pDialog;
    ConfirmationAsync confirmationAsync;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bus_addscreen, container, false);
        id_busname=(TextView)view.findViewById(R.id.id_busname);
        id_busname_txt=(TextView)view.findViewById(R.id.id_busname_txt);
        id_Lice=(TextView)view.findViewById(R.id.id_Lice);
        id_Lice_txt=(TextView)view.findViewById(R.id.id_Lice_txt);
        id_make=(TextView)view.findViewById(R.id.id_make);
        id_make_txt=(TextView)view.findViewById(R.id.id_make_txt);
        id_color=(TextView)view.findViewById(R.id.id_color);
        id_color_txt=(TextView)view.findViewById(R.id.id_color_txt);
        id_create= (Button)view.findViewById(R.id.id_create_btn);

        id_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Nbus = id_busname_txt.getText().toString();
                String Nplate = id_Lice_txt.getText().toString();

                String Ncolor = id_color_txt.getText().toString();
                String Nmake = id_make_txt.getText().toString();

                confirmationAsync =new ConfirmationAsync();
                confirmationAsync.execute(Nbus, Nplate,Ncolor, Nmake);


            }


        });

        return view;
    }
    private class ConfirmationAsync extends AsyncTask<String,Void,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage("!! PLEASE WAIT !!");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String Response = "";
            try {
                String data = URLEncoder.encode("Nbus", "UTF-8") + "=" + URLEncoder.encode(params[0], "UTF-8");
                data += "&" + URLEncoder.encode("Nplate", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8");
                data += "&" + URLEncoder.encode("Ncolor", "UTF-8") + "=" + URLEncoder.encode(params[2], "UTF-8");
                data += "&" + URLEncoder.encode("Nmake", "UTF-8") + "=" + URLEncoder.encode(params[3], "UTF-8");

                URL url = new URL("http://dmis.club/dmis_app/rijan/buscongi.php");
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(data);
                writer.flush();
                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String s;
                while ((s = rd.readLine()) != null) {
                    Response += s;
                }
                writer.close();
                rd.close();
                Log.i("Response ", Response);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return Response;
        }

        protected void onPostExecute(String result) {
            if (pDialog.isShowing())
                Log.d("result",result);
            response(result);
            pDialog.dismiss();
        }
    }

    private void response(String result) {

        try {
            JSONObject jsonObject = new JSONObject(result);
            if (jsonObject != null) {
                String response = jsonObject.getString("response");
                String message=jsonObject.getString("message");
                Log.i("response", response);
                if (response.equals("true")){
                    pDialog.dismiss();
                    getFragmentManager().beginTransaction().replace(R.id.Main_Fragment,new reviewscreen()).commit();

                }
                else {
                    Toast.makeText(getContext(),"NOT WORKING",Toast.LENGTH_SHORT).show();
                    String confirmation_id=jsonObject.getString("query");
                    Log.i("QUERY",confirmation_id);

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public String method(String s) {
        if (s == null || s.length() == 0) {
            return s;
        }
        return s.substring(0, s.length()-1);
    }
}

