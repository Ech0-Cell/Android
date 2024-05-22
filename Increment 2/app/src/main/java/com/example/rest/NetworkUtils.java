package com.example.rest;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

public class NetworkUtils {

    private static final String BASE_URL = "https://aom-vmovtyc2ya-uc.a.run.app/api/balance";

    public static void fetchBalance(String receivedNumber, String receivedToken, final BalanceCallback callback) {
        OkHttpClient client = new OkHttpClient();

        String url = BASE_URL + "?MSISDN=" + receivedNumber;

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", receivedToken)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                callback.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    try {
                        JSONObject JSONObject = new JSONObject(responseData);
                        callback.onSuccess(JSONObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        callback.onFailure(e);
                    }
                } else {
                    callback.onFailure(new IOException("Unexpected code " + response));
                }
            }
        });
    }

    public interface BalanceCallback {
        void onSuccess(JSONObject balanceData);
        void onFailure(Exception e);
    }
}
