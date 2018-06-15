package com.example.asus.tetris;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static MediaPlayer mediaPlayer; //背景音乐播放器
    public static boolean isMusicOn; //判断背景音乐是否在播放

    public static int highest; //历史最高分
    private TextView textView_highest; //显示最高分

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //定义开始游戏按钮，跳转到游戏界面
        Button start = (Button) findViewById(R.id.ingame);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TetrisActivity.class);
                startActivityForResult(intent, 1000);
            }
        });

        //定义设置按钮，跳转到设置界面
        Button edit = (Button) findViewById(R.id.setting);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

        //定义退出按钮，退出
        Button exit = (Button) findViewById(R.id.main_exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mediaPlayer = MediaPlayer.create(this, R.raw.backgroundmusic); //定义媒体播放器
        mediaPlayer.setLooping(true); //设置循环

        getMusic(); //取出历史背景音乐设置

        //若设置为开，播放背景音乐
        if(isMusicOn) {
            mediaPlayer.start();
        }

        //获得最高分
        textView_highest = (TextView) findViewById(R.id.main_highest);
        SharedPreferences sharedPreferences = getSharedPreferences("edit", 0);
        highest = sharedPreferences.getInt("highestScore", 0);
        textView_highest.setText(Integer.toString(highest)); //显示最高分
    }

    /**
     * 获取背景音乐的历史设置
     */
    private void getMusic() {
        SharedPreferences sharedPreferences = getSharedPreferences("edit",0);
        isMusicOn = sharedPreferences.getBoolean("isMusicOn", true);
    }

    /**
     * 接受从TetrisActivity获取的最高分更新
     * @param requestCode 1000
     * @param resultCode RESULT_OK
     * @param data 最高分
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            //获取返回的最高分，在主页面更新
            case 1000:
                if (resultCode == RESULT_OK) {
                    int tmp = highest;
                    highest = data.getIntExtra("highest", tmp);
                    textView_highest.setText(Integer.toString(highest));
                }
        }
    }

    /**
     * 重写退出函数，若播放器开启，则停止。并释放。
     */
    @Override
    public void onBackPressed() {
//        SettingActivity.exit();
        if (isMusicOn){
            mediaPlayer.stop();
        }
        mediaPlayer.release();
        System.exit(0);
    }

}
