package com.example.min.weatherfore;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class MainActivity extends FragmentActivity
        implements WeatherFragment.Callbacks{
    private static final String WEATHER_L = "WEATHER_L";
    private static final String WEATHER_U = "WEATHER_U";
    private static final String WEATHER_N = "WEATHER_N";
    private static final String WEATHER_A = "WEATHER_A";
    private FragmentManager fm;
    //
    @Override
       public void onWeatherSelected(Weather weather) {
        //有条件的DetailFragment启动（区分平板和手机）
        if (findViewById(R.id.two_fragment_detail) == null) {
            //手机
            Intent intent = Details.newIntent(this,weather);
            startActivity(intent);
        } else {
            //平板
            Fragment fragment = fm.findFragmentById(R.id.two_fragment_detail);
            if (fragment == null) {
                 fragment = DetailFragment.newInstance(weather);
                fm.beginTransaction()
                        .add(R.id.two_fragment_detail, fragment)
                        .commit();
             }else{
                fragment = DetailFragment.newInstance(weather);
                fm.beginTransaction()
                        .replace(R.id.two_fragment_detail, fragment)
                        .commit();
            }
        }
    }
    //获取当前是平板还是手机状态
    public int getCon(){
        if(findViewById(R.id.two_fragment_detail) == null){
            return 0;
        }else{return 1;}
    }
    //
    public static Intent newIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //还原设置数据
        SharedPreferences pref = getSharedPreferences("Setting_data", MODE_PRIVATE);
        Setting_State.Location = pref.getString("WEATHER_L", "长沙");
        Setting_State.Unit = pref.getString("WEATHER_U", "摄氏度");
        Setting_State.Able = pref.getString("WEATHER_A", "Unabled");
        Setting_State.Note = pref.getBoolean("WEATHER_N", false);

        setContentView(R.layout.activity_masterdetail);

       fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_weather);
        if(fragment == null){
            fragment = new WeatherFragment();
            fm.beginTransaction().add(R.id.fragment_weather,fragment).commit();
        }

    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        //保存设置数据
        SharedPreferences.Editor editor = getSharedPreferences("Setting_data", MODE_PRIVATE).edit();
        editor.putString(WEATHER_L, Setting_State.Location);
        editor.putString(WEATHER_U, Setting_State.Unit);
        editor.putString(WEATHER_A, Setting_State.Able);
        editor.putBoolean(WEATHER_N, Setting_State.Note);
        editor.commit();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 为ActionBar扩展菜单项
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //响应菜单选择点击事件
        switch (item.getItemId()) {
            case R.id.Setting:
                //跳转设置activity
                Intent i = new Intent(MainActivity.this,Setting.class);
                startActivity(i);
                break;
            case R.id.Mlocation:
                //Toast.makeText(MainActivity.this, "Mlocation", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setPackage("com.autonavi.minimap");
                intent.setAction("android.intent.action.VIEW");
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse("androidamap://viewMap?sourceApplication=sunshine&poiname=学校&lat="
                                        +Setting_State.Lat+"&lon="+Setting_State.Lon+"&dev=0"));
                startActivity(intent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}


