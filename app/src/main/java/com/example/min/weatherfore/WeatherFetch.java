package com.example.min.weatherfore;
import com.example.min.weatherfore.database.WeatherDbSchema.WeatherTable;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class WeatherFetch {
    private static final String TAG = "Weather";
    private static final String API_KEY = "ffd39abe3be14d8c9c369e1029faf6e3";
    public static String  ResultId ;
    //从指定URL获取原始数据，返回一个字节数组流
    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() +
                        ": with " +
                        urlSpec);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }
    //将getUrlBytes(String)方法返回的结果转换为String
    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }
    //
    public List<Weather> fetchItems() {
        List<Weather> items = new ArrayList<>();

        try {
            String url = Uri.parse("https://free-api.heweather.com/s6/weather/forecast")
                    .buildUpon()
                    .appendQueryParameter("location", Setting_State.Location)
                    .appendQueryParameter("key", API_KEY)
                    .build().toString();
            String jsonString = getUrlString(url);
            Log.i(TAG, "Received JSON: " + jsonString);
            JSONObject jsonBody = new JSONObject(jsonString);
            parseItems(items, jsonBody);

        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        }
        return items;
    }

    private void parseItems(List<Weather> items, JSONObject jsonBody)
            throws IOException, JSONException {
           JSONArray hJsonArray = jsonBody.getJSONArray("HeWeather6");
           JSONObject hJsonObject = hJsonArray.getJSONObject(0);
           JSONObject uJsonObject = hJsonObject.getJSONObject("update");
           //获取天气更新时间
           ResultId = uJsonObject.getString("loc");
           JSONObject bJsonObject = hJsonObject.getJSONObject("basic");
           //获取位置经纬度
            Setting_State.Lat = bJsonObject.getString("lat");
            Setting_State.Lon = bJsonObject.getString("lon");
           JSONArray fJsonArray = hJsonObject.getJSONArray("daily_forecast");
        for (int i = 0; i < fJsonArray.length(); i++) {
            JSONObject JsonObject = fJsonArray.getJSONObject(i);
            Weather weather = new Weather();
            weather.setmCode(JsonObject.getString("cond_code_d"));
            weather.setmDate(JsonObject.getString("date"));
            weather.setmL(JsonObject.getString("tmp_min"));
            weather.setmH(JsonObject.getString("tmp_max"));
            weather.setmState(JsonObject.getString("cond_txt_d"));
            weather.setmPre(JsonObject.getString("pres"));
            weather.setmHum(JsonObject.getString("hum"));
            weather.setmWin(JsonObject.getString("wind_spd"));
            items.add(weather);
        }
    }

}
