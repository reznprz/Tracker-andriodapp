package com.example.macbookpro.tracker;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class addscreen extends Fragment {
    InputStream inputStream;
    String line, result = null;
    //String[] data;
    TextView textView;
    //ArrayList<bussclassinfo> businfo = new ArrayList<>();
    AutoCompleteTextView upc;
    TextView Time;
    TextView Time_txt;
    TextView Odameterone;
    TextView Odameterone_txt;
    TextView Odametertwo;
    TextView Odametertwo_txt;
    TextView Vehicle;
    TextView Vehicle_txt;
    TextView Note;
    TextView input_Note_txt;
    Button btn_save;
    Button btn_cancel;
    ProgressDialog pDialog;
    ConfirmationAsync confirmationAsync;
    Dialog dialog;
    AutoCompleteTextView mTextView;
    String[] data;
    ListView listView;
    public static String valid_eplid;

    public static ArrayList<String> businfostringonly= new ArrayList<>();


    ArrayAdapter<String> adapter;
    AutoCompleteTextView bus;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.addscreen, container, false);

        Time = (TextView) view.findViewById(R.id.Time);
        Time_txt = (TextView) view.findViewById(R.id.Time_txt);
        Odameterone = (TextView) view.findViewById(R.id.Odameter_begin);
        Odameterone_txt = (TextView) view.findViewById(R.id.Odameter_begin_txt);
        Odametertwo = (TextView) view.findViewById(R.id.Odameter_end);
        Odametertwo_txt = (TextView) view.findViewById(R.id.Odameter_end_txt);
        Vehicle = (TextView) view.findViewById(R.id.Vehicle);
        Vehicle_txt = (TextView) view.findViewById(R.id.Vehicle_txt);
        Note = (TextView) view.findViewById(R.id.Note);
        input_Note_txt = (TextView) view.findViewById(R.id.id_inputnote_txt);
        btn_save = (Button) view.findViewById(R.id.btn_save);
        btn_cancel = (Button) view.findViewById(R.id.btn_cancel);




       // bus = (AutoCompleteTextView) view.findViewById(R.id.id_autotext);



        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Ntime = Time_txt.getText().toString();
                String Nodameterone = Odameterone_txt.getText().toString();
                String Nodametertwo = Odametertwo_txt.getText().toString();
                String Nvehicle = Vehicle_txt.getText().toString();
                String Nnote = input_Note_txt.getText().toString();
                String Eml = layout_main2.Empid;
                Toast.makeText(getContext(),Eml,Toast.LENGTH_SHORT).show();
                //Toast.makeText(this, ""+empid, Toast.LENGTH_SHORT).show();

                //Toast.makeText(getContext(),"Driver"+n,Toast.LENGTH_SHORT).show();



                confirmationAsync = new addscreen.ConfirmationAsync();
                confirmationAsync.execute(Eml,Ntime, Nodameterone, Nodametertwo, Nvehicle, Nnote);
                getFragmentManager().beginTransaction().replace(R.id.Main_Fragment,new reviewscreen()).commit();

            }


        });
        TextView clickTextView = (TextView) view.findViewById(R.id.Time_txt);
        clickTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getFragmentManager(), "date picker");
            }

        });
        clickTextView = (TextView) view.findViewById(R.id.Vehicle_txt);
        clickTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getLayoutInflater().getContext());
                final View rootview = getLayoutInflater().inflate(R.layout.layout_choosebus, null);
                mBuilder.setView(rootview);
                dialog = mBuilder.create();
                dialog.show();
                listView = rootview.findViewById(R.id.listviewbus);
                getData();
                adapter=new ArrayAdapter<String>(getLayoutInflater().getContext(), android.R.layout.simple_list_item_1,data);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        TextView textView = (TextView) view.findViewById(R.id.text2);
                        TextView txt = (TextView) getActivity().findViewById(R.id.Vehicle_txt);
                        txt.setText(businfostringonly.get(position));
                        dialog.dismiss();
                    }
                });
            }
        });
        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));
        return view;
    }

    private class ConfirmationAsync extends AsyncTask<String, Void, String> {
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
                String data = URLEncoder.encode("Eml", "UTF-8") + "=" + URLEncoder.encode(params[0], "UTF-8");
                data += "&" + URLEncoder.encode("Ntime", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8");
                data += "&" + URLEncoder.encode("Nodameterone", "UTF-8") + "=" + URLEncoder.encode(params[2], "UTF-8");
                data += "&" + URLEncoder.encode("Nodametertwo", "UTF-8") + "=" + URLEncoder.encode(params[3], "UTF-8");
                data += "&" + URLEncoder.encode("Nvehicle", "UTF-8") + "=" + URLEncoder.encode(params[4], "UTF-8");
                data += "&" + URLEncoder.encode("Nnote", "UTF-8") + "=" + URLEncoder.encode(params[5], "UTF-8");

                URL url = new URL("http://dmis.club/dmis_app/rijan/busdataregis.php");
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
                Log.d("result", result);
            response(result);
            pDialog.dismiss();
        }
    }

    private void response(String result) {

        try {
            JSONObject jsonObject = new JSONObject(result);
            if (jsonObject != null) {
                String response = jsonObject.getString("response");
                String message = jsonObject.getString("message");
                Log.i("response", response);
                if (response.equals("true")) {
                    String confirmation_id = jsonObject.getString("sale_id");
                    Log.i("QUERY", confirmation_id);
                    Toast.makeText(getContext(), "SALE ID IS:" + confirmation_id, Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                    getFragmentManager().beginTransaction().replace(R.id.Main_Fragment, new reviewscreen()).commit();

                } else {
                    Toast.makeText(getContext(), "NOT WORKING", Toast.LENGTH_SHORT).show();
                    //String confirmation_id = jsonObject.getString("query");
                    //Log.i("QUERY", confirmation_id);

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
        return s.substring(0, s.length() - 1);
    }

    private void getData() {
        try {
            URL url = new URL("http://dmis.club/dmis_app/rijan/allbus.php");
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

            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                data[i]=jsonObject.getString("Busnameid");
                businfostringonly.add(data[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("file note read",""+result);
        }

    }

}
