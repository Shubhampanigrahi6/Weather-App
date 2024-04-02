package com.example.roll;

import org.json.JSONException;
import org.json.JSONObject;

public class weatherData {

    private String mTemprature,mIcon,mCity,mTYpe;
    private int mCondition;

    public static weatherData fromJson(JSONObject jsonObject)
    {
        try
        {
            weatherData weatherD= new weatherData();
            weatherD.mCity=jsonObject.getString("name");
            weatherD.mCondition=jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
            weatherD.mTYpe=jsonObject.getJSONArray("weather").getJSONObject(0).getString("main");
            weatherD.mIcon=updateWeatherIcon(weatherD.mCondition);
            double tempResult=jsonObject.getJSONObject("main").getDouble("temp")-273.15;
            int roundedValue=(int)Math.rint(tempResult);
            weatherD.mTemprature=Integer.toString(roundedValue);
            return weatherD;
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String updateWeatherIcon(int condition)
    {
        if (condition>=0 && condition<=300)
        {
            return "heavyrain";
        }
        else if (condition>=300 && condition<=500)
        {
            return "lightrain";
        }
        else if (condition>=500 && condition<=600)
        {
            return "snowfall";
        }
        else if (condition>=600 && condition<=700)
        {
            return "snowfall";
        }
        else if (condition>=801&& condition<=804)
        {
            return "cloud";
        }
       else if (condition==800)
        {
            return "sun";
        }

       return "sun";
    }

    public String getmTemprature() {
        return mTemprature+"°C";
    }

    public String getmIcon() {
        return mIcon;
    }

    public String getmCity() {
        return mCity;
    }

    public String getmTYpe() {
        return mTYpe;
    }
}
