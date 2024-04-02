package com.example.roll;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import kotlin.text.UStringsKt;

public class MainActivity2 extends AppCompatActivity {

    final String App_ID = "d12446aa3f4bfd2c31234432a82de2cc";
    final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather";

    final long MIN_TIME = 5000;
    final float MIN_DISTANCE = 1000;
    final int REQUEST_CODE = 101;


    String Location_Provider = LocationManager.GPS_PROVIDER;

    TextView NameofCity, weatherState, Temprature;
    ImageView mweatherIcon;
    Button mCityFinder;

    LocationManager mLocationManager;
    LocationListener mLocationListner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        weatherState = findViewById(R.id.type);
        Temprature = findViewById(R.id.temprature);
        mweatherIcon = findViewById(R.id.weather);
        mCityFinder = findViewById(R.id.find);
        NameofCity = findViewById(R.id.cityname);


        mCityFinder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this, cityfinder.class);
                startActivity(intent);
            }
        });


    }


    /* @Override
    protected void onResume() {
        super.onResume();
        getweatherForCurrentLocation();
    }*/

    @Override
    protected void onResume(){
        super.onResume();
        Intent mIntent=getIntent();
        String city=mIntent.getStringExtra("City");
        if(city!=null)
        {
            getWeatherForNewCity(city);
        }
        else
        {
            getweatherForCurrentLocation();
        }


    }

    private void getWeatherForNewCity(String city)
    {
        RequestParams params=new RequestParams();
        params.put("q",city);
        params.put("appid",App_ID);
        letsdoSomeNetworking(params);
    }





    private void getweatherForCurrentLocation() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationListner = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {

                String Latitude = String.valueOf(location.getLatitude());
                String Longitude = String.valueOf(location.getLongitude());

                RequestParams params =new RequestParams();
                 params.put("lat",Latitude);
                 params.put("lon",Longitude);
                 params.put("appid",App_ID);
                 letsdoSomeNetworking(params);

            }
        };


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
            return;
        }
        mLocationManager.requestLocationUpdates(Location_Provider, MIN_TIME, MIN_DISTANCE,mLocationListner);



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if(requestCode==REQUEST_CODE)
        {
            if (grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(MainActivity2.this,"Location get Successfully",Toast.LENGTH_SHORT).show();
                getweatherForCurrentLocation();
            }
            else
            {
                //user denied the permission
                Toast.makeText(MainActivity2.this,"Location get failed",Toast.LENGTH_SHORT).show();
                getweatherForCurrentLocation();
            }
        }
    }



    private void letsdoSomeNetworking(RequestParams params)
    {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(WEATHER_URL,params,new JsonHttpResponseHandler()

        {
           @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject responce) {

               Toast.makeText(MainActivity2.this,"Data Get Success",Toast.LENGTH_SHORT).show();

               weatherData weatherD=weatherData.fromJson(responce);
               updateUI(weatherD);


           }

        });
    }
    private void updateUI(weatherData weather){

        Temprature.setText(weather.getmTemprature());
        NameofCity.setText(weather.getmCity());
        weatherState.setText(weather.getmTYpe());
        int resourceID=getResources().getIdentifier(weather.getmIcon(),"drawable",getPackageName());
        mweatherIcon.setImageResource(resourceID);
    }
    @Override
    protected void onPause(){
        super.onPause();
        if(mLocationManager != null)
        {
            mLocationManager.removeUpdates(mLocationListner);
        }
    }
}