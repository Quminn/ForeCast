package com.example.min.weatherfore;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

public class Setting extends Activity{
    private RecyclerView mRecyclerView;
    private MyAdapter myAdapter;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.setting);
            setActionBar();
            mRecyclerView = (RecyclerView) findViewById(R.id.list_setting);
            //定位列表项
            mRecyclerView.setLayoutManager(new LinearLayoutManager(Setting.this));
            myAdapter = new MyAdapter(this);
            mRecyclerView.setAdapter(myAdapter);
        }
    //位置设置（输入城市名称）
    private class ViewHolder1 extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mloc;
        public ViewHolder1(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            mloc = itemView.findViewById(R.id.list_loc);
            mloc.setText(Setting_State.Location);
        }
        //响应列表项点击事件
        public void onClick(View v){
            final EditText inputServer = new EditText(Setting.this);
            AlertDialog.Builder builder = new AlertDialog.Builder(Setting.this);
            builder.setTitle("请输入城市名称：").setIcon(android.R.drawable.ic_dialog_map).setView(inputServer)
                    .setNegativeButton("取消", null);
            builder.setPositiveButton("确定" +
                    "", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Setting_State.Location = inputServer.getText().toString();
                    mloc.setText(Setting_State.Location);
                }
            });
            builder.show();
        }
    }
    //温度单位设置（选择华氏度或摄氏度）
    private class ViewHolder2 extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mtem;
        public ViewHolder2(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            mtem = itemView.findViewById(R.id.list_tem);
            mtem.setText(Setting_State.Unit);
        }
        //响应列表项点击事件
        public void onClick(View v){
            AlertDialog.Builder builder = new AlertDialog.Builder(Setting.this,android.R.style.Theme_Holo_Light_Dialog);
            builder.setTitle("选择温度显示单位：");
            // 指定下拉列表的显示数据
            final String[] units = {"摄氏度", "华氏度"};
            //    设置一个下拉的列表选择项
            builder.setItems(units, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    Setting_State.Unit = units[which];
                    mtem.setText(Setting_State.Unit);
                }
            });
            builder.show();
        }
    }
    //天气通知提醒（选择要或不要）
    private class ViewHolder3 extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mnote;
        private CheckBox mable;
        private View able;
        public ViewHolder3(View itemView){
            super(itemView);
            mnote = itemView.findViewById(R.id.list_note);
            mable = itemView.findViewById(R.id.list_checkBox);
            able = itemView.findViewById(R.id.list_checkBox);
            able.setOnClickListener(this);
            mnote.setText(Setting_State.Able);
            mable.setChecked(Setting_State.Note);
        }
        //响应CheckBox点击事件
        public void onClick(View v){
            if(mable.isChecked()){
                Setting_State.Able = "Enabled";
                Setting_State.Note = true;
                mnote.setText(Setting_State.Able);
                NotificationService.setServiceAlarm(Setting.this,true);
            }else{
                Setting_State.Able = "Unabled";
                Setting_State.Note = false;
                mnote.setText(Setting_State.Able);
                NotificationService.setServiceAlarm(Setting.this,false);
            }
        }
    }
    public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        private LayoutInflater layoutInflater;
        //构造函数
        public MyAdapter(Context context) {
            layoutInflater = LayoutInflater.from(context);
        }
        //创建View视图，根据不同的布局类型封装到相应的ViewHolder中
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            if(viewType == 1){
            View view = layoutInflater.inflate(R.layout.list_location,parent,false);
            return new ViewHolder1(view);
            }
            if(viewType == 2){
                View view = layoutInflater.inflate(R.layout.list_temper,parent,false);
                return new ViewHolder2(view);
            }
            if(viewType == 3){
                View view = layoutInflater.inflate(R.layout.list_note,parent,false);
                return new ViewHolder3(view);
            }
            else{ return null; }
        }
        //绑定ViewHolder的View视图和数据更新
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position){

        }
        //设置列表项数目为 3
        @Override
        public int getItemCount(){
            return 3;
        }
        //根据当前位置确定不同的布局类型
        public int getItemViewType(int position) {
            if (position == 0)  return 1;
            if (position == 1)  return 2;
            if (position == 2)  return 3;
            else return 0;
        }
    }
    //设置屏幕上方返回键和title
    private void setActionBar() {
        ActionBar actionBar = getActionBar();
        // 显示返回按钮
        actionBar.setDisplayHomeAsUpEnabled(true);
        // 去掉logo图标
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setTitle("Setting");
    }
    //响应屏幕上方返回键点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:   //返回键的id
                this.finish();
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    }

