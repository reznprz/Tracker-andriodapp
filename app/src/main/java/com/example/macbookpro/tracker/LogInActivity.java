package com.example.macbookpro.tracker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class LogInActivity extends AppCompatActivity {

    Button Login;
    EditText Name;
    EditText Password;
    Button Register;
    ProgressDialog pDialog;
    private RegisterAsync registerAsync;
    //private ProgressDialog pDialog;
    private String fname,lname,empno,password;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        Login=findViewById(R.id.button3);
        Name=findViewById(R.id.editText);
        Password=findViewById(R.id.editText7);
        Register = findViewById(R.id.button4);

        FloatingActionButton fab1 = findViewById(R.id.btn_float);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LogInActivity.this,SearchActivity.class);
                startActivity(intent);
            }
        });

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LogInActivity.this,SignupActivity.class);
                startActivity(intent);
            }
        });


        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });




        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                empno=Name.getText().toString();
                password=Password.getText().toString();
                fname="";
                lname="";



                registerAsync =new RegisterAsync();
                registerAsync.execute(empno,password);
                 String empopattern="";

                 Intent intent=new Intent(LogInActivity.this,layout_main2.class);
                 startActivity(intent);

            }
        });
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LogInActivity.this,SignupActivity.class);
                startActivity(intent);
            }
        });
    }
    private class RegisterAsync extends AsyncTask<String,Void,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LogInActivity.this);
            pDialog.setMessage("Loging in ...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String Response = "";
            try {
                String data = URLEncoder.encode("employeeid", "UTF-8") + "=" + URLEncoder.encode(params[0], "UTF-8");

                data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8");


                URL url = new URL("http://dmis.club/dmis_app/rijan/login.php");
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


                if (response.equals("found")){

                    String input = Name.getText().toString();

                    Bundle bundle = new Bundle();
                    bundle.putString("Empid", input);

                    Intent homeactivity = new Intent(LogInActivity.this, layout_main2.class);
                    homeactivity.putExtras(bundle);
                    startActivity(homeactivity);


                    Toast.makeText(LogInActivity.this,input,Toast.LENGTH_SHORT).show();



                }
                else {
                    Toast.makeText(LogInActivity.this,message,Toast.LENGTH_SHORT).show();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class SecondFragment {
    }
}
