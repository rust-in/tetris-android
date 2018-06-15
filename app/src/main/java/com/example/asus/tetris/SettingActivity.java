package com.example.asus.tetris;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class SettingActivity extends AppCompatActivity {

    private CheckBox backgroundMusic_check; //背景音乐开关
    private CheckBox sound_check; //音效开关

    private boolean isMusicOn; //获取背景音乐状态
    private boolean isSoundOn; //获取音效状态

    //游戏模式选择
    private RadioGroup radioGroup;
    private RadioButton radio_practice; //练习模式
    private RadioButton radio_challenge; //挑战模式


    private LinearLayout linearLayout_setChallenge; //挑战模式设置
    private TextView textView_visible; //挑战模式备注
    private TextView textView_speed_set; //速度等级选择导航语句

    private TextView textView_speed_select; //速度等级选择
    private TextView textView_level_select; //挑战（难度）等级选择

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        backgroundMusic_check = (CheckBox) findViewById(R.id.checkbox_music); //定义背景音乐开关

        this.getMusic(); //获取历史背景音乐设置

        //根据历史设置设置开关显示
        if(isMusicOn) {
            backgroundMusic_check.setChecked(true);
        } else {
            backgroundMusic_check.setChecked(false);
        }

        //定义背景音乐开关监视器
        backgroundMusic_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //若开启
                if (b) {
                    //将操作保存到历史记录中
                    SharedPreferences sharedPreferences = getSharedPreferences("edit", 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isMusicOn", true);
                    editor.apply();

                    isMusicOn = true;

                    MainActivity.isMusicOn = true; //设置主界面背景音乐判断为真

                    MainActivity.mediaPlayer.start(); //播放背景音乐
                }
                //若关闭
                else {
                    //将操作保存到历史记录中
                    SharedPreferences sharedPreferences = getSharedPreferences("edit", 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isMusicOn", false);
                    editor.apply();

                    isMusicOn = false;

                    MainActivity.isMusicOn = false; //设置主界面背景音乐判断为假

                    //若背景音乐正在播放，将其暂停
                    if(MainActivity.mediaPlayer.isPlaying()) {
                        MainActivity.mediaPlayer.pause();
                    }
                }
            }
        });

        sound_check = (CheckBox) findViewById(R.id.checkbox_sound); //定义背景音乐开关

        this.getSound(); //获取历史背景音乐设置

        //根据历史设置设置开关显示
        if(isSoundOn) {
            sound_check.setChecked(true);
        } else {
            sound_check.setChecked(false);
        }

        //定义音效开关监视器
        sound_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //若开启
                if(b) {
                    //保存到历史设置
                    SharedPreferences sharedPreferences = getSharedPreferences("edit", 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isSoundOn", true);
                    editor.apply();

                    isSoundOn = true;
                }
                //若关闭
                else {
                    //保存到历史设置
                    SharedPreferences sharedPreferences = getSharedPreferences("edit", 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isSoundOn", false);
                    editor.apply();

                    isSoundOn = false;
                }
            }
        });

        textView_visible = (TextView) findViewById(R.id.visible); //定义挑战模式备注
        textView_speed_set = (TextView) findViewById(R.id.speed_set); //定义速度等级选择导航语句

        //模式选择
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radio_practice = (RadioButton) findViewById(R.id.radio_practice);
        radio_challenge = (RadioButton) findViewById(R.id.radio_challenge);

        linearLayout_setChallenge = (LinearLayout) findViewById(R.id.layout_difficulty); //定义挑战模式设置

        //根据历史记录来设置界面
        SharedPreferences sharedPreferences = getSharedPreferences("edit", 0);

        //若历史选择为练习模式
        if(sharedPreferences.getBoolean("model_practice", true)){
            radio_practice.setChecked(true); //默认选择练习模式
            TetrisActivity.practice_or_challenge = true;
            linearLayout_setChallenge.setVisibility(View.INVISIBLE); //练习模式下难度选择隐藏
            textView_visible.setVisibility(View.INVISIBLE); //隐藏挑战模式备注
            textView_speed_set.setText("请选择下落速度"); //设置速度等级选择导航语句
        }
        //若历史选择为挑战模式
        else {
            radio_challenge.setChecked(true); //默认选择练习模式
            TetrisActivity.practice_or_challenge = false;
            linearLayout_setChallenge.setVisibility(View.VISIBLE); //挑战模式下显示难度选择
            textView_visible.setVisibility(View.VISIBLE); //显示挑战模式备注
            textView_speed_set.setText("请选择初始速度"); //设置速度等级选择导航语句
        }

        //定义模式选择监听器
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                //若选择为练习模式
                if(radio_practice.getId() == i) {
                    linearLayout_setChallenge.setVisibility(View.INVISIBLE);
                    textView_visible.setVisibility(View.INVISIBLE);
                    textView_speed_set.setText("请选择下落速度");
                    TetrisActivity.practice_or_challenge = true;

                    //保存选择
                    SharedPreferences sharedPreferences = getSharedPreferences("edit", 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("model_practice", true);
                    editor.apply();
                }
                //若选择为挑战模式
                else if (radio_challenge.getId() == i) {
                    linearLayout_setChallenge.setVisibility(View.VISIBLE);
                    textView_visible.setVisibility(View.VISIBLE);
                    textView_speed_set.setText("请选择初始速度");
                    TetrisActivity.practice_or_challenge = false;

                    //保存选择
                    SharedPreferences sharedPreferences = getSharedPreferences("edit", 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("model_practice", false);
                    editor.apply();
                }
            }
        });



        //设置速度选择器
        textView_speed_select = (TextView) findViewById(R.id.textview_speed);
        textView_speed_select.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //设置下划线
        textView_speed_select.getPaint().setAntiAlias(true); //抗锯齿
        //根据历史选择设置速度选择器显示
        switch (sharedPreferences.getInt("TesSpeed", 0)) {
            case 0:
                textView_speed_select.setText("Lv.1");
                break;
            case 1:
                textView_speed_select.setText("Lv.2");
                break;
            case 2:
                textView_speed_select.setText("Lv.3");
                break;
        }
        //定义速度选择监视器
        textView_speed_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //设置弹出列表
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                builder.setTitle("速度等级");
                builder.setItems(R.array.speed, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //保存选择
                        SharedPreferences sharedPreferences = getSharedPreferences("edit", 0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("TesSpeed", i);
                        editor.apply();
                        //根据选择设置速度显示
                        switch (i) {
                            case 0:
                                textView_speed_select.setText("Lv.1");
                                break;
                            case 1:
                                textView_speed_select.setText("Lv.2");
                                break;
                            case 2:
                                textView_speed_select.setText("Lv.3");
                                break;
                        }
                    }
                });
                //设置弹出列表的取消按钮
                builder.setCancelable(true);
                builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                builder.show();
            }
        });

        //设置难度选择器
        textView_level_select = (TextView) findViewById(R.id.textview_level);
        textView_level_select.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //设置下划线
        textView_level_select.getPaint().setAntiAlias(true); //抗锯齿
        //根据历史选择设置难度选择显示
        switch (sharedPreferences.getInt("TesLevel", 0)) {
            case 0:
                textView_level_select.setText("无难度");
                break;
            case 1:
                textView_level_select.setText("Lv.1");
                break;
            case 2:
                textView_level_select.setText("Lv.2");
                break;
            case 3:
                textView_level_select.setText("Lv.3");
                break;
            case 4:
                textView_level_select.setText("随机");
                break;
        }

        //定义难度选择监听器
        textView_level_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //定义弹出列表
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                builder.setTitle("难度等级");
                builder.setItems(R.array.difficulty, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //保存选择
                        SharedPreferences sharedPreferences = getSharedPreferences("edit", 0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("TesLevel", i);
                        editor.apply();
                        //根据选择设置难度显示页面
                        switch (i) {
                            case 0:
                                textView_level_select.setText("无难度");
                                break;
                            case 1:
                                textView_level_select.setText("Lv.1");
                                break;
                            case 2:
                                textView_level_select.setText("Lv.2");
                                break;
                            case 3:
                                textView_level_select.setText("Lv.3");
                                break;
                            case 4:
                                textView_level_select.setText("随机");
                                break;
                        }
                    }
                });
                //设置弹出列表的取消按钮
                builder.setCancelable(true);
                builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                builder.show();
            }
        });

    }

    /**
     * 获取背景音乐开关与否的历史设置
     */
    private void getMusic() {
        SharedPreferences sharedPreferences = getSharedPreferences("edit",0);
        isMusicOn = sharedPreferences.getBoolean("isMusicOn", true);
    }

    /**
     * 获取音效开关与否的历史设置
     */
    private void getSound() {
        SharedPreferences sharedPreferences = getSharedPreferences("edit",0);
        isSoundOn = sharedPreferences.getBoolean("isSoundOn", true);
    }
}

