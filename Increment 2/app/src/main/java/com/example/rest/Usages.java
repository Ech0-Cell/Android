package com.example.rest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Usages extends AppCompatActivity {

    private Button btnBuyAddi;
    private TextView tvVoiceUsage, tvDataUsage, tvSmsUsage;

    private ProgressBar pbData, pbSms, pbVoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_usages);
        String receivedToken = getIntent().getStringExtra("TOKEN_KEY");
        String receivedNumber = getIntent().getStringExtra("NUMBER_KEY");
        Log.d("Usages", "Received access token: " + receivedToken);
        btnBuyAddi = findViewById(R.id.btn_buy_additional_package);
        tvDataUsage = findViewById(R.id.tv_data_usage);
        tvSmsUsage = findViewById(R.id.tv_sms_usage);
        tvVoiceUsage = findViewById(R.id.tv_voice_usage);
        pbData = findViewById(R.id.progress_data);
        pbSms = findViewById(R.id.progress_sms);
        pbVoice = findViewById(R.id.progress_voice);


        btnBuyAddi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Usages.this, Packages.class);
                startActivity(intent);
            }
        });

        NetworkUtils.fetchBalance(receivedNumber, receivedToken, new NetworkUtils.BalanceCallback() {
            @Override
            public void onSuccess(JSONObject balanceData) {
                runOnUiThread(() -> {
                    for (int i = 0; i < balanceData.length(); i++) {
                        try {
                            JSONObject pack = balanceData.getJSONObject("package");
                            String name = pack.getString("name");
                            int price = pack.getInt("price");
                            int dataAmount = pack.getInt("dataAmount");
                            int minAmount = pack.getInt("minAmount");
                            int smsAmount = pack.getInt("smsAmount");

                            JSONObject packUsed = balanceData.getJSONObject("balance");
                            int remData = packUsed.getInt("remData");
                            int remSms = packUsed.getInt("remSms");
                            int remMin = packUsed.getInt("remMin");
                            int remMoney = packUsed.getInt("remMoney");

                            tvDataUsage.setText("Remaining Data: " + remData + "GB");
                            tvVoiceUsage.setText("Remaining Voice: " + remMin + "Mins");
                            tvSmsUsage.setText("Remaining SMS: " + remSms);


                            pbData.setProgress(dataAmount/remData*100);
                            pbVoice.setProgress(minAmount/remMin*100);
                            pbSms.setProgress(smsAmount/remSms*100);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(Usages.this, "Failed to fetch balance", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}