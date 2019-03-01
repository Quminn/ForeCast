package com.example.min.weatherfore.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.min.weatherfore.Weather;
import com.example.min.weatherfore.database.WeatherDbSchema.WeatherTable;

public class WeatherCursorWrapper extends CursorWrapper {
    public WeatherCursorWrapper(Cursor cursor) {
        super(cursor);
    }
    public Weather getWeather() {
        Weather weather = new Weather();
        weather.setmDate(getString(getColumnIndex(WeatherTable.Cols.DATE)));
        weather.setmCode(getString(getColumnIndex(WeatherTable.Cols.CODE)));
        weather.setmH(getString(getColumnIndex(WeatherTable.Cols.HIGH)));
        weather.setmL(getString(getColumnIndex(WeatherTable.Cols.LOW)));
        weather.setmState(getString(getColumnIndex(WeatherTable.Cols.STATE)));
        weather.setmHum(getString(getColumnIndex(WeatherTable.Cols.HUM)));
        weather.setmPre(getString(getColumnIndex(WeatherTable.Cols.PRE)));
        weather.setmWin(getString(getColumnIndex(WeatherTable.Cols.WIN)));
        return weather;
    }
}
