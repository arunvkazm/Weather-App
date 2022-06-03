package com.example.ClimWeather;

import android.icu.text.SimpleDateFormat;
import android.os.Build;

import androidx.annotation.RequiresApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Locale;

public class WeatherData {

    private String mTemperature, mcity, mWeatherType, mWind, mTempMin, mTempMax, mPressure, mHumidity
            , msunrise, msunset, mUpdatedateandtime;


    @RequiresApi(api = Build.VERSION_CODES.N)
    public static WeatherData fromJson(JSONObject jsonObject) {

        try {
            WeatherData weatherD = new WeatherData();
            //Cityname
            weatherD.mcity = jsonObject.getString("name");
            //Weather type
            weatherD.mWeatherType = jsonObject.getJSONArray("weather").getJSONObject(0).getString("main");
            // Temperature
            double tempResult = jsonObject.getJSONObject("main").getDouble("temp") - 273.15;
            int roundedValue = (int) Math.rint(tempResult);
            weatherD.mTemperature = Integer.toString(roundedValue);
            //Minimam Temperature
            double minTemp = jsonObject.getJSONObject("main").getDouble("temp_min") - 273.15;
            int roundV = (int) Math.rint(minTemp);
            weatherD.mTempMin = Integer.toString(roundV);
            //Maximam Temperature
            double maxTemp = jsonObject.getJSONObject("main").getDouble("temp_max") - 273.15;
            int rV = (int) Math.rint(maxTemp);
            weatherD.mTempMax = Integer.toString(rV);
            //Wind
            int windspeed = jsonObject.getJSONObject("wind").getInt("speed");
            int roV = (int) Math.rint(windspeed);
            weatherD.mWind = Integer.toString(roV);
            //Pressure
            double pressure = jsonObject.getJSONObject("main").getDouble("pressure");
            int rv = (int) Math.rint(pressure);
            weatherD.mPressure = Integer.toString(rv);

            //Humidity
            int humidity = jsonObject.getJSONObject("main").getInt("humidity");
            int round = (int) Math.rint(humidity);
            weatherD.mHumidity = Integer.toString(round);

            //Sunrise
            Long sunrise = jsonObject.getJSONObject("sys").getLong("sunrise");
//            //int ro = (int) Math.rint(sunrise);
            SimpleDateFormat HMM = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
            final Date date = new Date(sunrise * 1000);
            weatherD.msunrise = HMM.format(date);
            //weatherD.msunrise = Integer.toString(date);

            //Sunset
            Long sunset = jsonObject.getJSONObject("sys").getLong("sunset");
//            //int roundd = (int) Math.rint()
            SimpleDateFormat time = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
            final Date d = new Date(sunset * 1000);
            weatherD.msunset = time.format(d);

            //Date and Time
            Long dateNtime = jsonObject.getLong("dt");
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a",Locale.ENGLISH);
            weatherD.mUpdatedateandtime = dateFormat.format(dateNtime*1000);


            return weatherD;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }


    }


    public String getmTemperature() {
        return mTemperature + "°C";
    }


    public String getMcity() {
        return mcity;
    }

    public String getmWeatherType() {
        return mWeatherType;
    }

    public String getmTempMin() {
        return "Min Temp:"+ mTempMin + "°C";
    }

    public String getmTempMax() {
        return "Max Temp:"+ mTempMax + "°C";
    }

    public String getmWind() {
        return mWind + " Km/h";
    }

    public String getmPressure() {
        return mPressure + " hPa";
    }

    public String getmHumidity() {
        return mHumidity + "%";
    }

    public String getMsunrise() {
        return msunrise;
    }

    public String getMsunset() {
        return msunset;
    }

    public String getmUpdatedateandtime() {
        return mUpdatedateandtime;
    }


}
