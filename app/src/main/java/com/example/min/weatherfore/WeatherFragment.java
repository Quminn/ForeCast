package com.example.min.weatherfore;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class WeatherFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private WeatherAdapter mAdapter;
    private List<Weather> mWeathers;
    private TextView text_date;
    private TextView text_h;
    private TextView text_l;
    private TextView text_state;
    private ImageView img_t;
    private SetImg ss;
    private Callbacks mCallbacks;
    private final String TAG="DATA";

    /**
     * Required interface for hosting activities.
     */
    //添加MainActivity回调接口
    public interface Callbacks {
        void onWeatherSelected(Weather weather);
        int getCon();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.weather_list, container, false);
        ss = new SetImg();
        if (mCallbacks.getCon()==0) {
            text_date = v.findViewById(R.id.text_date);
            text_h = v.findViewById(R.id.text_h);
            text_l = v.findViewById(R.id.text_l);
            text_state = v.findViewById(R.id.text_state);
            img_t = v.findViewById(R.id.img_t);}
        mRecyclerView =  v.findViewById(R.id.weather_RecyclerView);
        //定位列表项
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return v;
    }
    private void updateUI(){
        if (mCallbacks.getCon()==0) {
            //手机
        text_date.setText("Today," + mWeathers.get(0).getmDate());
        if(Setting_State.Unit.equals("摄氏度")){
        text_h.setText(mWeathers.get(0).getmH() + "°");
        text_l.setText(mWeathers.get(0).getmL() + "°");
        }else{
            text_h.setText(String.valueOf(Integer.parseInt(mWeathers.get(0).getmH())*9/5 + 32)+ "°");
            text_l.setText(String.valueOf(Integer.parseInt(mWeathers.get(0).getmL())*9/5 + 32)+ "°");
        }
        text_state.setText(mWeathers.get(0).getmState());
        ss.setImg(img_t,mWeathers.get(0).getmCode());
        mWeathers.remove(0);
        //刷新天气列表
        mAdapter = new WeatherAdapter(mWeathers);
        mRecyclerView.setAdapter(mAdapter);
        }else{
            //平板
            mWeathers.get(0).setmDate("Today," + mWeathers.get(0).getmDate());
            //刷新天气列表
            mAdapter = new WeatherAdapter(mWeathers);
            mRecyclerView.setAdapter(mAdapter);
            mCallbacks.onWeatherSelected(mWeathers.get(0));
        }
    }
   @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        new FetchItemsTask().execute();  //执行后台线程
    }
    @Override
    public void onResume() {
        super.onResume();
        setRetainInstance(true);
        new FetchItemsTask().execute();  //执行后台线程
    }
    //后台线程进行网络连接
    private class FetchItemsTask extends AsyncTask<Void,Void,List<Weather>> {
        @Override
        protected List<Weather> doInBackground(Void... params) {
                //连网获取数据
                List<Weather> weathers = new WeatherFetch().fetchItems();
                if(weathers.size()!=0){
                //判断数据库中表是否为空
                if(WeatherLab.get(getActivity()).Database()==0){
                    //初次存入数据库
                WeatherLab.get(getActivity()).addWeather(weathers);
                    Log.i(TAG,"初次存入数据库成功");
                }else{
                    //更新数据库
                    WeatherLab.get(getActivity()).updateWeather(weathers);
                    Log.i(TAG,"更新数据库成功");
                }
                return weathers;
                }else{
                //网络连接异常，从数据库取天气数据
                Log.i(TAG,"数据库取天气数据");
                return WeatherLab.get(getActivity()).getWeather();
              }
        }
        //将获取的数据进行UI更新
        @Override
        protected void onPostExecute(List<Weather> items) {
            mWeathers = items;
            updateUI();
        }
    }
    //定义ViewHolder内部类
    private class WeatherHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mDate;
        private TextView mState;
        private TextView mL;
        private TextView mH;
        private ImageView mImg;
        private Weather mWeather;
        //关联天气列表视图控件
        public WeatherHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            mDate = itemView.findViewById(R.id.list_date);
            mState = itemView.findViewById(R.id.list_state);
            mL = itemView.findViewById(R.id.list_l);
            mH = itemView.findViewById(R.id.list_h);
            mImg = itemView.findViewById(R.id.list_img);
        }
        //响应列表项点击事件
        public void onClick(View v){
            //调用回调方法
            mCallbacks.onWeatherSelected(mWeather);

        }
        //刷新视图控件
        public void bindWeather(Weather weather){
            mWeather = weather;
            mDate.setText(mWeather.getmDate());
            mState.setText(mWeather.getmState());
            if(Setting_State.Unit.equals("摄氏度")){
            mH.setText(mWeather.getmH() + "°");
            mL.setText(mWeather.getmL() + "°");
            }else{
                mH.setText(String.valueOf(Integer.parseInt(mWeather.getmH())*9/5 + 32)+ "°");
                mL.setText(String.valueOf(Integer.parseInt(mWeather.getmL())*9/5 + 32)+ "°");
            }
            ss.setImg(mImg,mWeather.getmCode());
        }
    }
    private class WeatherAdapter extends RecyclerView.Adapter<WeatherHolder>{
        private List<Weather> tWeathers;

        public WeatherAdapter(List<Weather> weathers){
            tWeathers = weathers;
       }
        //创建View视图，封装到ViewHolder中
        @Override
        public WeatherHolder onCreateViewHolder(ViewGroup parent,int viewType){
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_weather,parent,false);
            return new WeatherHolder(view);
        }
        //绑定ViewHolder的View视图和数据
        @Override
        public void onBindViewHolder(WeatherHolder holder,int position){
           Weather weather = tWeathers.get(position);
           holder.bindWeather(weather);
        }
        @Override
        public int getItemCount(){
            return tWeathers.size();
        }
    }
}
