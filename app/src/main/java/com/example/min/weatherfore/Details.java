package com.example.min.weatherfore;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class Details extends FragmentActivity {
    private Weather weather;
    private static final String EXTRA_WEATHER = "sunshine_weather";

    public static Intent newIntent(Context packageContext, Weather weather) {
        Intent intent = new Intent(packageContext, Details.class);
        Bundle mBundle = new Bundle();
        mBundle.putSerializable(EXTRA_WEATHER, weather);
        intent.putExtras(mBundle);
        return intent;
    }
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.details);
            setActionBar();
            //接受WeatherFragment传来的Weather对象
            weather = (Weather) getIntent().getSerializableExtra(EXTRA_WEATHER);
            FragmentManager fm = getSupportFragmentManager();
           Fragment fragment = fm.findFragmentById(R.id.fragment_detail);
            if(fragment == null){
                //将Weather对象再转发给DetailFragment
              fragment = DetailFragment.newInstance(weather);
              fm.beginTransaction().add(R.id.fragment_detail,fragment).commit();
           }
        }
    private void setActionBar() {
        ActionBar actionBar = getActionBar();
        // 显示返回按钮
        actionBar.setDisplayHomeAsUpEnabled(true);
        // 去掉logo图标
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setTitle("Details");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 为ActionBar扩展菜单项
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.share, menu);
        return super.onCreateOptionsMenu(menu);
    }
    //
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:   //返回键的id
                this.finish();
                return false;
            case R.id.Share:    //分享天气详情
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT,weather.getmDate()+" "+Setting_State.Location+
                        "\n天气状况：" + weather.getmState()+"\n最低气温："+weather.getmL()+ "°"
                        +"\n最高气温："+weather.getmL()+ "°" +"\n相对湿度："+weather.getmHum()+"%\n"
                        +"大气压强："+weather.getmPre()+"hPa\n"+"风速："+weather.getmWin()+"km/h\n");
                i.putExtra(Intent.EXTRA_SUBJECT,"天气分享");
                startActivity(i);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    }

