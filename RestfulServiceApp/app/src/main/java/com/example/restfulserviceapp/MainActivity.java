package com.example.restfulserviceapp;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {

    TextView textViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewResult = findViewById(R.id.text_view_result);
        callUniversityService();
    }

    private void callUniversityService() {
        String url = "http://universities.hipolabs.com/search?name=bahce";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            StringBuilder formattedResult = new StringBuilder();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String name = jsonObject.getString("name");
                                String country = jsonObject.getString("country");
                                formattedResult.append("Name: ").append(name).append("\nCountry: ").append(country).append("\n\n");
                            }

                            textViewResult.setText(formattedResult.toString());
                        } catch (Exception e) {
                            textViewResult.setText("Failed to parse the JSON response!");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                String errorMessage = "Error: " + error.getMessage();
                // Display the error message (e.g., using a Toast)
                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();

                //textViewResult.setText("Failed to get response from the service!");
            }
        });

        Volley.newRequestQueue(this).add(stringRequest);
    }
}