package com.example.roll;

import android.content.Intent;
import android.os.AsyncTask;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.roll.R;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

public class prediction extends AppCompatActivity {

    private TextView weatherTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction);

        weatherTextView = findViewById(R.id.weatherTextView);


        new FetchWeatherTask().execute();
    }

    private class FetchWeatherTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            String apiKey = "d12446aa3f4bfd2c31234432a82de2cc";
//            @Override
//            protected void onResume();
//            {
//                cityfinder city = new cityfinder();
//
//               String city = city.getStringExtra("City");
//            }
//            final String city = String.valueOf("city");
//            private void FetchWeatherTask(String.valueOf("city"));


            String city = "california";
                String apiUrl = "https://api.openweathermap.org/data/2.5/forecast?q=" + city + "&appid=" + apiKey;

                try {
                    URL url = new URL(apiUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.connect();


                    InputStream inputStream = connection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    return stringBuilder.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("list");


                    Calendar calendar = Calendar.getInstance();
                    int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

                    StringBuilder weatherData = new StringBuilder();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject listItem = jsonArray.getJSONObject(i);
                        long timestamp = listItem.getLong("dt");
                        calendar.setTimeInMillis(timestamp * 1000);
                        int day = calendar.get(Calendar.DAY_OF_MONTH);


                        if (day != currentDay && day <= currentDay + 4) {

                            JSONArray weatherArray = listItem.getJSONArray("weather");
                            JSONObject weatherObject = weatherArray.getJSONObject(0);
                            String weatherDescription = weatherObject.getString("description");
                            double temperature = listItem.getJSONObject("main").getDouble("temp");

                            weatherData.append("Date: ").append(calendar.getTime()).append("\n");
                            weatherData.append("Weather: ").append(weatherDescription).append("\n");
                            weatherData.append("Temperature: ").append(temperature).append("\n\n");
                        }
                    }


                    weatherTextView.setText(weatherData.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}