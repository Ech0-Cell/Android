package com.example.rest;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Packages extends AppCompatActivity {

    private TextView panel1Name, panel1Price, panel1Data, panel1Minutes, panel1Sms;
    private TextView panel2Name, panel2Price, panel2Data, panel2Minutes, panel2Sms;
    private TextView panel3Name, panel3Price, panel3Data, panel3Minutes, panel3Sms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_packages);

        initializeViews();

        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        fetchPackages();
    }

    private void initializeViews() {
        panel1Name = findViewById(R.id.panel1_name);
        panel1Price = findViewById(R.id.panel1_price);
        panel1Data = findViewById(R.id.panel1_data);
        panel1Minutes = findViewById(R.id.panel1_minutes);
        panel1Sms = findViewById(R.id.panel1_sms);

        panel2Name = findViewById(R.id.panel2_name);
        panel2Price = findViewById(R.id.panel2_price);
        panel2Data = findViewById(R.id.panel2_data);
        panel2Minutes = findViewById(R.id.panel2_minutes);
        panel2Sms = findViewById(R.id.panel2_sms);

        panel3Name = findViewById(R.id.panel3_name);
        panel3Price = findViewById(R.id.panel3_price);
        panel3Data = findViewById(R.id.panel3_data);
        panel3Minutes = findViewById(R.id.panel3_minutes);
        panel3Sms = findViewById(R.id.panel3_sms);
    }

    private void fetchPackages() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://aom-vmovtyc2ya-uc.a.run.app/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PackageService service = retrofit.create(PackageService.class);

        service.getPackages().enqueue(new Callback<List<Package>>() {
            @Override
            public void onResponse(Call<List<Package>> call, Response<List<Package>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    updateUI(response.body());
                } else {
                    Toast.makeText(Packages.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Package>> call, Throwable t) {
                Toast.makeText(Packages.this, "Network error", Toast.LENGTH_SHORT).show();
                Log.e("Packages", "Network error", t);
            }
        });
    }

    private void updateUI(List<Package> packages) {
        if (packages.size() > 0) {
            Package package1 = packages.get(0);
            panel1Name.setText(package1.getName());
            panel1Price.setText(String.format("%s₺", package1.getPrice()));
            panel1Data.setText(String.format("Data: %s GB", package1.getDataAmount()));
            panel1Minutes.setText(String.format("Minutes: %s Min", package1.getMinAmount()));
            panel1Sms.setText(String.format("SMS: %s", package1.getSmsAmount()));
        }

        if (packages.size() > 1) {
            Package package2 = packages.get(1);
            panel2Name.setText(package2.getName());
            panel2Price.setText(String.format("%s₺", package2.getPrice()));
            panel2Data.setText(String.format("Data: %s GB", package2.getDataAmount()));
            panel2Minutes.setText(String.format("Minutes: %s Min", package2.getMinAmount()));
            panel2Sms.setText(String.format("SMS: %s", package2.getSmsAmount()));
        }

        if (packages.size() > 2) {
            Package package3 = packages.get(2);
            panel3Name.setText(package3.getName());
            panel3Price.setText(String.format("%s₺", package3.getPrice()));
            panel3Data.setText(String.format("Data: %s GB", package3.getDataAmount()));
            panel3Minutes.setText(String.format("Minutes: %s Min", package3.getMinAmount()));
            panel3Sms.setText(String.format("SMS: %s", package3.getSmsAmount()));
        }
    }
}
