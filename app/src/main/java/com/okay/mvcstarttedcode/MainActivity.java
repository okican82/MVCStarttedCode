package com.okay.mvcstarttedcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.gson.Gson;
import com.okay.mvcstarttedcode.Adapters.WeatherAdapter;
import com.okay.mvcstarttedcode.Entity.Result;
import com.okay.mvcstarttedcode.Entity.Weather;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView dataListRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataListRecyclerView = findViewById(R.id.dataListRecyclerView);

        getData();
    }

    private void getData()
    {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        Request request = new Request.Builder()
                .url("https://api.collectapi.com/weather/getWeather?data.lang=tr&data.city=ankara")
                .method("GET", null)
                .addHeader("authorization", "apikey 2Stxlk4jSx7jEwkTpUOGi9:2hTZLp49lZk7vzGNpf3ccy")
                .addHeader("content-type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()) {
                    final String myResponse = response.body().string();

                    Weather weather = new Gson().fromJson(myResponse,Weather.class);

                    ArrayList<Result> resultList = new ArrayList<Result>(weather.getResult());
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            diplayListData(resultList);
                        }
                    });

                }
            }
        });
    }

    private void diplayListData(ArrayList<Result> resultList)
    {
        WeatherAdapter adapter = new WeatherAdapter(resultList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        dataListRecyclerView.setLayoutManager(mLayoutManager);
        dataListRecyclerView.setAdapter(adapter);
    }
}