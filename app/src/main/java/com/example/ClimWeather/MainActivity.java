package com.example.ClimWeather;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    TextView tvcityname, tvweathercond, tvtemp, tvtempMin, tvtempMax, tvsunrise, tvsunset,
            tvwind, tvpressure, tvhumidity,dateandtimeupdate;
    final String API_ID = "2ceda79993b758fb357ec153868fcf6e";
    final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather";

    final long MIN_TIME = 5000;
    final float MIN_DISTANCE = 1000;
    final int REQUEST_CODE = 101;

    String Location_provider = LocationManager.GPS_PROVIDER;

    LocationManager mLocationManager;
    LocationListener mLocationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        tvcityname = findViewById(R.id.cityname);
        //tvdateandtime = findViewById(R.id.dateandtime);
        tvweathercond = findViewById(R.id.status);
        tvtemp = findViewById(R.id.temp);
        tvtempMin = findViewById(R.id.temp_min);
        tvtempMax = findViewById(R.id.temp_max);
        tvsunrise = findViewById(R.id.sunrise);
        tvsunset = findViewById(R.id.sunset);
        tvwind = findViewById(R.id.wind);
        tvpressure = findViewById(R.id.pressure);
        tvhumidity = findViewById(R.id.humidity);
        dateandtimeupdate = findViewById(R.id.dateandtime);


    }

    @Override
    protected void onResume() {
        super.onResume();
        getWeatherForCurrentLocation();
    }

    private void getWeatherForCurrentLocation() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {

                String Latitude = String.valueOf(location.getLatitude());
                String Longitude = String.valueOf(location.getLongitude());

                RequestParams params = new RequestParams();
                params.put("lat", Latitude);
                params.put("lon", Longitude);
                params.put("appid", API_ID);
                letsdoSomeNetworking(params);

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {

            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {
                Toast.makeText(MainActivity.this, "Please provide the location", Toast.LENGTH_SHORT).show();

            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        mLocationManager.requestLocationUpdates(Location_provider, MIN_TIME, MIN_DISTANCE, mLocationListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Location get successfully", Toast.LENGTH_SHORT).show();
                getWeatherForCurrentLocation();
            } else {
                //User denied the permission
            }

        }

    }

    private void letsdoSomeNetworking(RequestParams params) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(WEATHER_URL, params, new JsonHttpResponseHandler() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Toast.makeText(MainActivity.this, "Data Get Success", Toast.LENGTH_SHORT).show();

                WeatherData weatherD = WeatherData.fromJson(response);
                updateUI(weatherD);


                // super.onSuccess(statusCode, headers, response);
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                //super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });


    }

    private void updateUI(WeatherData weatherd) {
        tvtemp.setText(weatherd.getmTemperature());
        tvcityname.setText(weatherd.getMcity());
        tvweathercond.setText(weatherd.getmWeatherType());
        tvtempMin.setText(weatherd.getmTempMin());
        tvtempMax.setText(weatherd.getmTempMax());
        tvwind.setText(weatherd.getmWind());
        tvpressure.setText(weatherd.getmPressure());
        tvhumidity.setText(weatherd.getmHumidity());
        tvsunrise.setText(weatherd.getMsunrise());
        tvsunset.setText(weatherd.getMsunset());
        dateandtimeupdate.setText(weatherd.getmUpdatedateandtime());
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mLocationManager != null) {
            mLocationManager.removeUpdates(mLocationListener);
        }
    }
}