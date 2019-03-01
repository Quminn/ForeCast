package com.example.min.weatherfore;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.List;

public class NotificationService extends IntentService {
    private static final String TAG = "NotificationService";
    private static final String PREF_LAST_RESULT_ID = "lastResultId";
    private static final long POLL_INTERVAL_MS = 1000*60;
    private String newResultId;
    private String lastResultId;
    private List<Weather> weathers;
    public NotificationService() {
        super(TAG);
    }
    public static Intent newIntent(Context context) {
        return new Intent(context, NotificationService.class);
    }
    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG,"开启服务");
        //判断后台服务网络是否可用
        if (!isNetworkAvailableAndConnected()) {
            return;
        }
        //获取上次查询ID
       lastResultId = PreferenceManager.getDefaultSharedPreferences(NotificationService.this)
                .getString(PREF_LAST_RESULT_ID, "s");
        Log.i(TAG,"获取上次查询ID");
        if (lastResultId.equals("s")) {
            lastResultId = WeatherFetch.ResultId;
        } else {
            //获取新的查询ID
            weathers = new WeatherFetch().fetchItems();
            newResultId = WeatherFetch.ResultId;
        }
        Log.i(TAG,"判断最新数据和原数据是否相同");
       //判断最新数据和原数据是否相同
        if (newResultId.equals(lastResultId)) {
            Log.i(TAG, "Got an old result: " + newResultId);
            Intent i = MainActivity.newIntent(this);
            PendingIntent pi = PendingIntent
                   .getActivity(this, 0, i, 0);
            //
            Log.i(TAG,"创建通知");
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //创建消息通道对象
                NotificationChannel channel = new NotificationChannel("1",
                        "Channel1", NotificationManager.IMPORTANCE_DEFAULT);
                channel.enableLights(true); //是否在桌面icon右上角展示小红点
                channel.setLightColor(Color.RED); //小红点颜色
                channel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
                notificationManager.createNotificationChannel(channel);
                //创建消息对象
                Notification notification = new NotificationCompat.Builder(this,"1")
                        .setTicker("天气更新通知")
                        .setSmallIcon(android.R.drawable.ic_menu_report_image)
                        .setContentTitle("天气更新通知")
                        //.setVibrate(new long[] {0,1000,1000,1000})
                        .setContentText("今日"+Setting_State.Location+
                                "天气状况为"+weathers.get(0).getmState()+",最低气温为"+weathers.get(0).getmL()
                                +"°,最高气温为"+weathers.get(0).getmL()+"°")
                        .setContentIntent(pi)
                        .setAutoCancel(true)
                        .setChannelId("1")
                        .build();
                //发送消息通知
                notificationManager.notify(0x1234, notification);
            }else{
                //创建消息对象
                Notification notification = new Notification.Builder(this)
                        .setTicker("天气更新通知")
                        .setSmallIcon(android.R.drawable.ic_menu_report_image)
                        .setContentTitle("天气更新通知")
                        //.setVibrate(new long[] {0,1000,1000,1000})
                        .setContentText("今日"+Setting_State.Location+
                                "天气状况为"+weathers.get(0).getmState()+",最低气温为"+weathers.get(0).getmL()
                                +"°,最高气温为"+weathers.get(0).getmL()+"°")
                        .setContentIntent(pi)
                        .setAutoCancel(true)
                        .build();
                //发送消息通知
                notificationManager.notify(0x1234, notification);}
            Log.i(TAG,"发送通知成功");
        } else {
            Log.i(TAG, "Got a new result: " + newResultId);
            Intent i = MainActivity.newIntent(this);
            PendingIntent pi = PendingIntent
                    .getActivity(this, 0, i, 0);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //创建消息通道对象
            NotificationChannel channel = new NotificationChannel("1",
                        "Channel1", NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true); //是否在桌面icon右上角展示小红点
            channel.setLightColor(Color.RED); //小红点颜色
            channel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
            notificationManager.createNotificationChannel(channel);
            //创建消息对象
            Notification notification = new NotificationCompat.Builder(this,"1")
                    .setTicker("天气更新通知")
                    .setSmallIcon(android.R.drawable.ic_menu_report_image)
                    .setContentTitle("天气更新通知")
                    //.setVibrate(new long[] {0,1000,1000,1000})
                    .setContentText("今日"+Setting_State.Location+
                            "天气状况为"+weathers.get(0).getmState()+",最低气温为"+weathers.get(0).getmL()
                                    +"°,最高气温为"+weathers.get(0).getmL()+"°")
                   .setContentIntent(pi)
                    .setAutoCancel(true)
                    .build();
            //发送消息通知
            notificationManager.notify(0x1234, notification);
            }else{
            //创建消息对象
            Notification notification = new Notification.Builder(this)
                    .setTicker("天气更新通知")
                    .setSmallIcon(android.R.drawable.ic_menu_report_image)
                    .setContentTitle("天气更新通知")
                    //.setVibrate(new long[] {0,1000,1000,1000})
                    .setContentText("今日"+Setting_State.Location+
                            "天气状况为"+weathers.get(0).getmState()+",最低气温为"+weathers.get(0).getmL()
                            +"°,最高气温为"+weathers.get(0).getmL()+"°")
                    .setContentIntent(pi)
                    .setAutoCancel(true)
                    .build();
            //发送消息通知
            notificationManager.notify(0x1234, notification);}
            Log.i(TAG,"发送通知成功");
        }
            //存储最新查询ID
        PreferenceManager.getDefaultSharedPreferences(NotificationService.this)
                .edit()
                .putString(PREF_LAST_RESULT_ID, newResultId)
                .apply();
    }
    //添加定时方法
    public static void setServiceAlarm(Context context, boolean isOn) {
        Intent i = NotificationService.newIntent(context);
        //创建一个用来启动NotificationService的PendingIntent
        PendingIntent pi = PendingIntent.getService(context,0, i,0);

        AlarmManager alarmManager = (AlarmManager)
                context.getSystemService(Context.ALARM_SERVICE);

        if (isOn) {
            //设置定时器
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime(), POLL_INTERVAL_MS, pi);
        } else {
            //取消定时器
            alarmManager.cancel(pi);
            pi.cancel();
        }
    }

    //确认网络连接是否可用
    private boolean isNetworkAvailableAndConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
        boolean isNetworkConnected = isNetworkAvailable &&
                cm.getActiveNetworkInfo().isConnected();
        return isNetworkConnected;
    }
}
