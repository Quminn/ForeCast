package com.example.min.weatherfore;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class DetailFragment extends Fragment {
    private TextView text_d_date;
    private TextView text_d_h;
    private TextView text_d_l;
    private TextView text_d_state;
    private TextView text_Hum;
    private TextView text_Pre;
    private TextView text_Win;
    private ImageView img_d;
    private SetImg ss;
    private Weather weather;
    private static final String DETAIL_WEATHER = "detail_weather";

    public static DetailFragment newInstance(Weather weather) {
        Bundle args = new Bundle();
        args.putSerializable(DETAIL_WEATHER, weather);
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获得转发的Weather对象
        weather = (Weather) getArguments().getSerializable(DETAIL_WEATHER);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.detail_fragment, container, false);
        ss = new SetImg();
        //绑定天气细节控件
        text_d_date = v.findViewById(R.id.text_d_date);
        text_d_h = v.findViewById(R.id.text_d_h);
        text_d_l = v.findViewById(R.id.text_d_l);
        text_d_state = v.findViewById(R.id.text_d_state);
        text_Hum = v.findViewById(R.id.text_Hum);
        text_Pre = v.findViewById(R.id.text_Pre);
        text_Win = v.findViewById(R.id.text_Win);
        img_d = v.findViewById(R.id.img_d);

        text_d_date.setText(weather.getmDate());
        if(Setting_State.Unit.equals("摄氏度")){
        text_d_h.setText(weather.getmH() + "°");
        text_d_l.setText(weather.getmL()  + "°");
        }else{
            text_d_h.setText(String.valueOf(Integer.parseInt(weather.getmH())*9/5 + 32)+ "°");
            text_d_l.setText(String.valueOf(Integer.parseInt(weather.getmL())*9/5 + 32)+ "°");
        }
        text_d_state.setText(weather.getmState());
        text_Hum.setText("Humidity: " + weather.getmHum() + "%");
        text_Pre.setText("Pressure: " + weather.getmPre() + "hPa");
        text_Win.setText("Wind: " + weather.getmWin() + "km/h");
        ss.setImg(img_d,weather.getmCode());
        return v;
    }

}
