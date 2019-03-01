package com.example.min.weatherfore;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.min.weatherfore.database.WeatherDbSchema.WeatherTable;
import com.example.min.weatherfore.database.WeatherBaseHelper;
import com.example.min.weatherfore.database.WeatherCursorWrapper;
import com.example.min.weatherfore.database.WeatherDbSchema;

import java.util.ArrayList;
import java.util.List;

public class WeatherLab {
    private static WeatherLab sWeatherLab;
    private Context mContext;
    public SQLiteDatabase mDatabase;
    public static WeatherLab get(Context context) {
        if (sWeatherLab == null) {
            sWeatherLab = new WeatherLab(context);
        }
        return sWeatherLab;
    }
    private WeatherLab(Context context) {
        mContext = context.getApplicationContext();
        //创建数据库
        mDatabase = new WeatherBaseHelper(mContext)
                .getWritableDatabase();

    }
    //添加数据库数据
    public void addWeather(List<Weather> weather) {
        for(int i=0;i<weather.size();i++){
            ContentValues values = getContentValues(weather.get(i));
            values.put(WeatherDbSchema.WeatherTable.Cols.ID , String.valueOf(i));
            mDatabase.insert(WeatherDbSchema.WeatherTable.NAME, null, values);
        }
    }
    //更新数据库数据
    public void updateWeather(List<Weather> weather) {
        for(int i=0;i<weather.size();i++) {
            String id = String.valueOf(i);
            ContentValues values = getContentValues(weather.get(i));
            values.put(WeatherDbSchema.WeatherTable.Cols.ID , id);
            mDatabase.update(WeatherDbSchema.WeatherTable.NAME, values,
                    WeatherDbSchema.WeatherTable.Cols.ID + " = ?", new String[]{id});
        }
    }
    //得到一条插入数据
    private static ContentValues getContentValues(Weather weather) {
        ContentValues values = new ContentValues();
        values.put(WeatherDbSchema.WeatherTable.Cols.DATE , weather.getmDate());
        values.put(WeatherDbSchema.WeatherTable.Cols.CODE , weather.getmCode());
        values.put(WeatherDbSchema.WeatherTable.Cols.LOW , weather.getmL());
        values.put(WeatherDbSchema.WeatherTable.Cols.HIGH , weather.getmH());
        values.put(WeatherDbSchema.WeatherTable.Cols.STATE , weather.getmState());
        values.put(WeatherDbSchema.WeatherTable.Cols.HUM , weather.getmHum());
        values.put(WeatherDbSchema.WeatherTable.Cols.PRE , weather.getmPre());
        values.put(WeatherDbSchema.WeatherTable.Cols.WIN , weather.getmWin());
        return values;
    }
    //得到一条查询数据cursor
    private WeatherCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                WeatherTable.NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null  // orderBy
        );
        return new WeatherCursorWrapper(cursor);
    }
    //判断数据库是否为空
    public int Database(){
        WeatherCursorWrapper cursor = queryCrimes(null, null);
        return cursor.getCount();
    }
    //得到数据库中天气ArrayList
    public List<Weather> getWeather() {
        List<Weather> weathers = new ArrayList<>();
        WeatherCursorWrapper cursor = queryCrimes(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                weathers.add(cursor.getWeather());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return weathers;
    }

}
