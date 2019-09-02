package com.example.macbookpro.tracker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class SignupActivity extends AppCompatActivity {

    TextView firstname;
    TextView lastname;
    TextView email;
    TextView employeeid;
    TextView password;
    Button Done;
    ProgressDialog pDialog;
    RegisterAsync registerAsync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        firstname = findViewById(R.id.editText1);
        lastname = findViewById(R.id.editText2);
        email = findViewById(R.id.editText3);
        password = findViewById(R.id.editText4);
        employeeid = findViewById(R.id.editText5);
        Done = findViewById(R.id.button5);
        Done.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            String FName = firstname.getText().toString();
            String LName = lastname.getText().toString();
            String Eml = email.getText().toString();
            String Pawd = password.getText().toString();
            String eplid = employeeid.getText().toString();
            registerAsync = new RegisterAsync();
            Log.d("tag",FName);

            registerAsync.execute(FName, LName, Eml, Pawd, eplid);
        }
    });
}

private class RegisterAsync extends AsyncTask<String,Void,String> {
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(SignupActivity.this);
        pDialog.setMessage("Registering in ...");
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {
        String Response = "";
        try {
            String data = URLEncoder.encode("firstname", "UTF-8") + "=" + URLEncoder.encode(params[0], "UTF-8");
            data += "&" + URLEncoder.encode("lastname", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8");
            data += "&" + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(params[2], "UTF-8");
            data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(params[3], "UTF-8");
            data += "&" + URLEncoder.encode("employeeid", "UTF-8") + "=" + URLEncoder.encode(params[4], "UTF-8");
            Log.d("tag","1");

            URL url = new URL("http://dmis.club/dmis_app/rijan/register.php");
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
        Log.d("tag","2");
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
                Log.d("tag",""+response);
                Log.i("response", response);
                if (response.equals("true")){
                    Intent homeactivity = new Intent(SignupActivity.this, LogInActivity.class);
                    startActivity(homeactivity);
                    Toast.makeText(SignupActivity.this,message,Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(SignupActivity.this,message,Toast.LENGTH_SHORT).show();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
