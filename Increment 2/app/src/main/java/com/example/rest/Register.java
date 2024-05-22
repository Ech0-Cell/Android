package com.example.rest;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

public class Register extends AppCompatActivity {

    private TextView tvLogin;
    private EditText etUsername, etSurname, etEmail, etPhoneNumber, etPassword, etConfirmPassword;
    private CheckBox cbAcceptTerms, cbAcceptDataProcessing;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvLogin = findViewById(R.id.tv_back_to_login);
        etUsername = findViewById(R.id.et_register_username);
        etSurname = findViewById(R.id.et_register_surname);
        etEmail = findViewById(R.id.et_register_email);
        etPhoneNumber = findViewById(R.id.et_register_phone_number);
        etPassword = findViewById(R.id.et_register_password);
        etConfirmPassword = findViewById(R.id.et_register_confirm_password);
        cbAcceptTerms = findViewById(R.id.cb_accept_terms);
        cbAcceptDataProcessing = findViewById(R.id.cb_accept_data_processing);
        btnRegister = findViewById(R.id.btn_register);

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String surname = etSurname.getText().toString();
                String email = etEmail.getText().toString();
                String phoneNumber = etPhoneNumber.getText().toString();
                String password = etPassword.getText().toString();
                String confirmPassword = etConfirmPassword.getText().toString();

                if (!password.equals(confirmPassword)) {
                    Toast.makeText(Register.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!cbAcceptTerms.isChecked() || !cbAcceptDataProcessing.isChecked()) {
                    Toast.makeText(Register.this, "Please accept the terms and conditions", Toast.LENGTH_SHORT).show();
                    return;
                }

                new RegisterTask().execute(username, surname, email, phoneNumber, password);
            }
        });
    }

    private class RegisterTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String username = params[0];
            String surname = params[1];
            String email = params[2];
            String phoneNumber = params[3];
            String password = params[4];
            String packageID = "2";
            String securityKey = "0000";

            try {
                URL url = new URL("https://aom-vmovtyc2ya-uc.a.run.app/api/customer/register");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("msisdn", phoneNumber);
                jsonParam.put("packageID", packageID);
                jsonParam.put("name", username);
                jsonParam.put("surname", surname);
                jsonParam.put("email", email);
                jsonParam.put("password", password);
                jsonParam.put("securityKey", securityKey);

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
            Intent intent = new Intent(Register.this, Login.class);
            startActivity(intent);
        }
    }
}
