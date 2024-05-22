package com.example.rest;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Login extends AppCompatActivity {

    private TextView tvSignup;
    private EditText etPhone, etPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvSignup = findViewById(R.id.tv_signup);
        etPhone = findViewById(R.id.et_phone_number);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);

        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the register activity
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = etPhone.getText().toString();
                String password = etPassword.getText().toString();
                new LoginTask().execute(phone, password);
            }
        });
    }

    private class LoginTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String msisdn = params[0];
            String password = params[1];
            try {
                URL url = new URL("https://aom-vmovtyc2ya-uc.a.run.app/api/customer/login");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("msisdn", msisdn);
                jsonParam.put("password", password);

                Log.i("JSON", jsonParam.toString());
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
                writer.write(jsonParam.toString());
                writer.flush();
                writer.close();

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null) {
                        response.append(line);
                    }
                    in.close();
                    return response.toString();
                } else {
                    return "Error: " + responseCode;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return e.toString();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject response = new JSONObject(result);
                String msisdn = response.getString("msisdn");
                String token = response.getString("token");
                Toast.makeText(Login.this, "Login successful. MSISDN: " + msisdn, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Login.this, Usages.class);
                intent.putExtra("TOKEN_KEY", token);
                intent.putExtra("NUMBER_KEY", msisdn);
                startActivity(intent);
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(Login.this, "Login failed. Please try again.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
