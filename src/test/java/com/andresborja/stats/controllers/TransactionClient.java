package com.andresborja.stats.controllers;

import static com.andresborja.stats.statscollector.TimeUtils.getCurrentEpocTimestamp;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * Utility to test the server.
 * @author afborja
 */
public class TransactionClient {

    private static final int ELEMENTS = 1000;

    private static final double [] VALUES = new double [] {10.0, 20.0, 30.0, 40.0};
    private static final long [] DELTA_TIME = new long [] {100, 200, 300, 400};

    public void startTest() {
        for (int i = 0; i < ELEMENTS; i++) {
            long currentTimestamp = getCurrentEpocTimestamp();
            long timestamp = currentTimestamp - DELTA_TIME[i % DELTA_TIME.length];
            double nextValue = VALUES[i % VALUES.length];
            String tStr = "{    \"amount\": "+ nextValue + ",    \"timestamp\": " + timestamp + " }";

            HttpClient httpClient = HttpClientBuilder.create().build(); //Use this instead 

            try {
                HttpPost request = new HttpPost("http://localhost:8080/transactions");
                StringEntity params = new StringEntity(tStr);
                request.addHeader("content-type", "application/x-www-form-urlencoded");
                request.setEntity(params);
                HttpResponse response = httpClient.execute(request);

                System.out.println("Response: " + response.getStatusLine());

            }catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new TransactionClient().startTest();
    }
}
