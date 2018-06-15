package com.example.asus.tetris;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.os.Handler;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class TetrisActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnTouchListener {

    private TetrisView tetrisView;
    private NextBlockView nextBlockView;
    private ImageButton start_or_pause;
    private TextView textView_score;
    private TextView textView_speed;
    private TextView textView_highest;
    private TextView textView_model;

    private boolean isStarted = false;
    private boolean isRunning = false;

    public static boolean practice_or_challenge = true;

    private int currentScore = 0;
    public static int speed = 1;
    public static int speed_tmp = 1;
    private boolean speed_up_one = false;
    public static int difficulty = 0;
    private int difficulty_exact;

    private Timer timer;

    private MediaPlayer mediaPlayer;

    public static boolean isSoundOn = true;
    public int highestScore = 0;

    // The what values of the messages
    private static final int DOWN = 0x00;
    private static final int SHIFT_DOWN = 0x01;
    private static final int LEFT = 0x02;
    private static final int RIGHT = 0x03;
    private static final int ROTATE = 0x04;
    private static final int START = 0x05;
    private static final int PAUSE = 0x06;
    private static final int RESUME = 0x07;
    private static final int RESET = 0x08;
    private static final int SET_ROW_ALL_FILLED = 0x09;
    private static final int RESET_MAIN_VIEW = 0x0A;

    private Handler refreshHandler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case DOWN:
                    //若可以正常下降，则下落一格
                    if(TetrisActivity.this.tetrisView.canDown()){
                        TetrisActivity.this.tetrisView.toDown();
                    }
                    //若不能下降，则将新的方块置入View中
                    else {
                        //检查可消除的行数（并进行消除）
                        int count = TetrisActivity.this.tetrisView.checkRows();
                        if(count > 0 && isSoundOn) {
                            mediaPlayer.start();
                        }
                        //增加相应的分数
                        TetrisActivity.this.currentScore += count * 100;
                        if(TetrisActivity.this.currentScore > TetrisActivity.this.highestScore) {
                            textView_score.setTextColor(getResources().getColor(R.color.color_highest));
                            textView_highest.setTextColor(0x8A000000);
                        }
                        TetrisActivity.this.textView_score.setText(Integer.toString(TetrisActivity.this.currentScore));
                        if (!practice_or_challenge) {
                            if(speed == 1) {
                                if(currentScore >= 2000) {
                                    speed = 2;
                                    speed_up_one = true;
                                }
                            } else if (speed == 2) {
                                if(speed_up_one) {
                                    if (currentScore >= 4000) {
                                        speed = 3;
                                    }
                                } else {
                                    if (currentScore >= 2000) {
                                        speed = 3;
                                    }
                                }
                            }
                        }

                        TetrisActivity.this.timer.cancel();
                        TetrisActivity.this.timer.purge();

                        //检测游戏是否结束，若结束，则重置
                        if(TetrisActivity.this.tetrisView.gameOVer(TetrisActivity.this.nextBlockView.type)) {
                            TetrisActivity.this.reset();
                        }
                        //若尚未结束?
                        else {

                            TetrisActivity.this.pushNextBlock();

                            switch (TetrisActivity.speed) {
                                case 1:
                                    TetrisActivity.this.setNewTimer(1000);
                                    textView_speed.setText("Lv.1");
                                    break;

                                case 2:
                                    TetrisActivity.this.setNewTimer(700);
                                    textView_speed.setText("Lv.2");
                                    break;

                                case 3:
                                    TetrisActivity.this.setNewTimer(400);
                                    textView_speed.setText("Lv.3");
                                    break;
                            }
                        }
                    }

                    break;
                case SHIFT_DOWN:
                    if (TetrisActivity.this.tetrisView.canDown()) {
                        TetrisActivity.this.tetrisView.toDown();
                    } else {
                        TetrisActivity.this.tetrisView.checkRows();
                    }

                    break;

                case LEFT:
                    if (TetrisActivity.this.tetrisView.canLeft()) {
                        TetrisActivity.this.tetrisView.toLeft();
                    }

                    break;

                case RIGHT:
                    if (TetrisActivity.this.tetrisView.canRight()) {
                        TetrisActivity.this.tetrisView.toRight();
                    }

                    break;

                case ROTATE:
                    if(TetrisActivity.this.tetrisView.canRoute()) {
                        TetrisActivity.this.tetrisView.toRotate();
                    }

                    break;

                case START:
                    TetrisActivity.this.start_or_pause.setImageResource(R.drawable.pause);

                    TetrisActivity.this.isStarted = true;
                    TetrisActivity.this.isRunning = true;

                    //设置挑战
                    if(!practice_or_challenge) {
                        if(difficulty == 4){
                            switch (difficulty_exact) {
                                case 0:
                                    textView_model.setText("挑战模式 Lv.0");
                                    break;
                                case 1:
                                    textView_model.setText("挑战模式 Lv.1");
                                    break;
                                case 2:
                                    textView_model.setText("挑战模式 Lv.2");
                                    break;
                                case 3:
                                    textView_model.setText("挑战模式 Lv.3");
                                    break;
                            }
                            TetrisActivity.this.tetrisView.setDifficulty(difficulty_exact * 2);
                        } else {
                            TetrisActivity.this.tetrisView.setDifficulty(TetrisActivity.difficulty * 2);
                        }
                    }

                    //放入第一个方块
                    TetrisActivity.this.pushNextBlock();

                    //设置速度 - change
                    switch (TetrisActivity.speed) {
                        case 1:
                            TetrisActivity.this.setNewTimer(1000);
                            textView_speed.setText("Lv.1");
                            break;

                        case 2:
                            TetrisActivity.this.setNewTimer(700);
                            textView_speed.setText("Lv.2");
                            break;

                        case 3:
                            TetrisActivity.this.setNewTimer(400);
                            textView_speed.setText("Lv.3");
                            break;
                    }

                    break;

                case PAUSE:

                    TetrisActivity.this.start_or_pause.setImageResource(R.drawable.start);

                    TetrisActivity.this.isStarted = true;
                    TetrisActivity.this.isRunning = false;

                    TetrisActivity.this.timer.cancel();
                    TetrisActivity.this.timer.purge();

                    break;
                case RESUME:

                    TetrisActivity.this.start_or_pause.setImageResource(R.drawable.pause);

                    TetrisActivity.this.isStarted = true;
                    TetrisActivity.this.isRunning = true;

                    switch (TetrisActivity.speed) {
                        case 1:
                            TetrisActivity.this.setNewTimer(1000);
                            textView_speed.setText("Lv.1");
                            break;

                        case 2:
                            TetrisActivity.this.setNewTimer(700);
                            textView_speed.setText("Lv.2");
                            break;

                        case 3:
                            TetrisActivity.this.setNewTimer(400);
                            textView_speed.setText("Lv.3");
                            break;
                    }

                    break;

                case RESET:

                    TetrisActivity.this.isStarted = false;
                    TetrisActivity.this.isRunning = false;

                    TetrisActivity.this.start_or_pause.setImageResource(R.drawable.start);

                    TetrisActivity.this.timer.cancel();
                    TetrisActivity.this.timer.purge();

                    TetrisActivity.this.gameOverFilm();

                    if(TetrisActivity.this.currentScore > TetrisActivity.this.highestScore) {
                        TetrisActivity.this.highestScore = TetrisActivity.this.currentScore;
                        SharedPreferences sharedPreferences = getSharedPreferences("edit", 0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("highestScore", TetrisActivity.this.highestScore);
                        editor.apply();
                        TetrisActivity.this.textView_highest.
                                setText(Integer.toString(TetrisActivity.this.highestScore));
                    }

                    if(difficulty == 4) {
                        Random random = new Random();
                        difficulty_exact = random.nextInt(3) + 1;
                    }

                    TetrisActivity.this.currentScore = 0;
                    TetrisActivity.this.textView_score.setText("0");

                    TetrisActivity.speed = TetrisActivity.speed_tmp;
                    switch (TetrisActivity.speed) {
                        case 1:
                            textView_speed.setText("Lv.1");
                            break;
                        case 2:
                            textView_speed.setText("Lv.2");
                            break;
                        case 3:
                            textView_speed.setText("Lv.3");
                            break;
                    }


                    TetrisActivity.this.textView_highest.setTextColor(getResources().getColor(R.color.color_highest));
                    TetrisActivity.this.textView_score.setTextColor(0x8A000000);
//                    TetrisActivity.this.pushNextBlock();

                    break;

                case SET_ROW_ALL_FILLED:
                    TetrisActivity.this.tetrisView.setFilled(message.getData().getInt("row"),
                            message.getData().getInt("column"));

                    break;

                case RESET_MAIN_VIEW:
                    TetrisActivity.this.tetrisView.reset();

                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tetris);

        initViews();

        this.start_or_pause.setOnClickListener(this);
        this.findViewById(R.id.reset).setOnClickListener(this);
//        this.findViewById(R.id.exit).setOnClickListener(this);
        this.findViewById(R.id.left).setOnClickListener(this);
        this.findViewById(R.id.right).setOnClickListener(this);
        this.findViewById(R.id.rotate).setOnClickListener(this);
        this.findViewById(R.id.down).setOnTouchListener(this);

        //获取历史最高分
        this.getHighestScore();
        this.textView_highest.setText(Integer.toString(this.highestScore));

        //获取音效设置
        this.getSound();
        mediaPlayer = MediaPlayer.create(this, R.raw.sound);
        mediaPlayer.setLooping(false);

        //获取模式、速度、难度设置
        SharedPreferences sharedPreferences = getSharedPreferences("edit", 0);
        practice_or_challenge = sharedPreferences.getBoolean("model_practice", true);
        TetrisActivity.speed = sharedPreferences.getInt("TesSpeed", 0) + 1;
        TetrisActivity.speed_tmp = sharedPreferences.getInt("TesSpeed", 0) + 1;
        TetrisActivity.difficulty = sharedPreferences.getInt("TesLevel", 0);
        speed_up_one = false;

        //设置游戏模式显示
        if(practice_or_challenge) {
            textView_model.setText("练习模式");
        } else {
            if(difficulty == 4) {
                Random random = new Random();
                difficulty_exact = random.nextInt(3) + 1;
                switch (difficulty_exact) {
                    case 0:
                        textView_model.setText("挑战模式 Lv.0");
                        break;
                    case 1:
                        textView_model.setText("挑战模式 Lv.1");
                        break;
                    case 2:
                        textView_model.setText("挑战模式 Lv.2");
                        break;
                    case 3:
                        textView_model.setText("挑战模式 Lv.3");
                        break;
                }
            }
            else {
                switch (difficulty) {
                    case 0:
                        textView_model.setText("挑战模式 Lv.0");
                        break;
                    case 1:
                        textView_model.setText("挑战模式 Lv.1");
                        break;
                    case 2:
                        textView_model.setText("挑战模式 Lv.2");
                        break;
                    case 3:
                        textView_model.setText("挑战模式 Lv.3");
                        break;
                }
            }

        }
        //设置速度
        switch (speed) {
            case 1:
                textView_speed.setText("Lv.1");
                break;
            case 2:
                textView_speed.setText("Lv.2");
                break;
            case 3:
                textView_speed.setText("Lv.3");
        }

        //获取第一块方块
        TetrisActivity.this.generateNextBlock();

        textView_highest.setTextColor(getResources().getColor(R.color.color_highest));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (TetrisActivity.this.isRunning) {
                            TetrisActivity.this.refreshHandler.sendEmptyMessage(PAUSE);
                        } else if (TetrisActivity.this.isStarted) {
                            TetrisActivity.this.refreshHandler.sendEmptyMessage(RESUME);
                        } else {
                            TetrisActivity.this.refreshHandler.sendEmptyMessage(START);
                        }
                    }
                }).start();

                break;

            case R.id.reset:
                if (TetrisActivity.this.isStarted) {
                    TetrisActivity.this.reset();
                }

                break;

            case R.id.left:
                if (TetrisActivity.this.isRunning) {
                    TetrisActivity.this.moveLeft();
                }

                break;

            case R.id.right:
                if (TetrisActivity.this.isRunning) {
                    TetrisActivity.this.moveRight();
                }

                break;

            case R.id.rotate:
                if (TetrisActivity.this.isRunning) {
                    TetrisActivity.this.rotate();
                }

                break;

            case R.id.down:
                if (TetrisActivity.this.isRunning) {
                    TetrisActivity.this.moveDown();
                }

                break;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (!TetrisActivity.this.isRunning) {
            return true;
        }

        if (view.getId() == R.id.down) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    TetrisActivity.this.resetTimer(100);

                    break;

                case MotionEvent.ACTION_UP:
                    switch (speed) {
                        case 1:
                            TetrisActivity.this.resetTimer(1000);
                            break;
                        case 2:
                            TetrisActivity.this.resetTimer(700);
                            break;
                        case 3:
                            TetrisActivity.this.resetTimer(400);
                            break;
                    }

                    break;
            }
        }

        return true;
    }
    /**
     * 初始化两个view
     */
    private void initViews(){
        this.tetrisView = (TetrisView) findViewById(R.id.tetrisView);
        this.nextBlockView = (NextBlockView) findViewById(R.id.nextBlockView);

        this.start_or_pause = (ImageButton) findViewById(R.id.start);
        this.textView_highest = (TextView) findViewById(R.id.highest);
        this.textView_score = (TextView) findViewById(R.id.score);
        this.textView_speed = (TextView) findViewById(R.id.speed);
        this.textView_model = (TextView) findViewById(R.id.layout_model);
    }
    /**
     * 为NextBlockView放置新的方块
     */
    private void generateNextBlock() {
        Random random = new Random();
        this.nextBlockView.drawNextBlock(random.nextInt(7));
    }

    /**
     * 将新的方块置入TetrisView,并更新NextBlockView
     */
    private void pushNextBlock() {
        this.tetrisView.pushBlocks(TetrisActivity.this.nextBlockView.type);
        this.tetrisView.invalidate();
        Random random = new Random();
        this.nextBlockView.drawNextBlock(random.nextInt(7));
    }

    private void getSound() {
        //获取音效开关
        SharedPreferences sharedPreferences = getSharedPreferences("edit",0);
        TetrisActivity.isSoundOn = sharedPreferences.getBoolean("isSoundOn", true);
    }

    private void getHighestScore() {
        //共享最高分
        SharedPreferences sharedPreferences = getSharedPreferences("edit",0);
        this.highestScore = sharedPreferences.getInt("highestScore", 0);
    }

    private void moveLeft() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                TetrisActivity.this.refreshHandler.sendEmptyMessage(LEFT);
            }
        }).start();
    }

    private void moveDown() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                TetrisActivity.this.refreshHandler.sendEmptyMessage(DOWN);
            }
        }).start();
    }

    private void moveRight() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                TetrisActivity.this.refreshHandler.sendEmptyMessage(RIGHT);
            }
        }).start();
    }

    private void rotate() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                TetrisActivity.this.refreshHandler.sendEmptyMessage(ROTATE);
            }
        }).start();
    }

    private void reset() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                TetrisActivity.this.refreshHandler.sendEmptyMessage(RESET);
            }
        }).start();
    }

    private void exit() {
        //退出程序
        this.onBackPressed();
    }

    /**
     * 重置timer，以替换当前下落速度
     * @param second 时间间隔
     */
    private void resetTimer(int second) {

        this.timer.cancel();
        this.timer.purge();

        this.timer = new Timer();
        this.timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                TetrisActivity.this.refreshHandler.sendEmptyMessage(DOWN);

                System.gc();
            }
        }, 0, second);
    }

    /**
     * 设置新的timer,默认1000
     * @param second 时间间隔
     */
    private void setNewTimer(int second) {

        this.timer = new Timer();
        this.timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                TetrisActivity.this.refreshHandler.sendEmptyMessage(DOWN);

                System.gc();
            }
        }, 1000 - (speed - 1) * 300, second);
    }

    /**
     * 重写退出程序函数，弹出确认窗口，并关闭音乐，更新最高分
     */
    @Override
    public void onBackPressed() {
        //如果游戏尚在继续中
        if (this.isRunning) {
            //模拟点击去暂停游戏
            this.start_or_pause.performClick();
        }

        if(this.isStarted) {
            //弹出询问窗口
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            //alertDialog.setTitle("Alert");
            alertDialog.setMessage("确认要放弃本次游戏吗？");
            alertDialog.setCancelable(true);
            alertDialog.setPositiveButton("是的", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                //更新最高分
                if (TetrisActivity.this.currentScore > TetrisActivity.this.highestScore) {
                    TetrisActivity.this.highestScore = TetrisActivity.this.currentScore;
                    SharedPreferences sharedPreferences = getSharedPreferences("edit", 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("highestScore", TetrisActivity.this.highestScore);
                    editor.apply();
                    TetrisActivity.this.textView_highest.
                            setText(Integer.toString(TetrisActivity.this.highestScore));

                }

                //更新主页面最高分
                Intent intent = new Intent();
                intent.putExtra("highest",TetrisActivity.this.highestScore);
                setResult(RESULT_OK, intent);

                //退出程序
                TetrisActivity.this.finish();
                }
            });
            alertDialog.setNegativeButton("我后悔了", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alertDialog.show();
        } else if (TetrisActivity.this.highestScore > MainActivity.highest) {
            MainActivity.highest = TetrisActivity.this.highestScore;
            SharedPreferences sharedPreferences = getSharedPreferences("edit", 0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("highestScore", TetrisActivity.this.highestScore);
            editor.apply();
            TetrisActivity.this.textView_highest.
                    setText(Integer.toString(TetrisActivity.this.highestScore));

            //更新主页面最高分
            Intent intent = new Intent();
            intent.putExtra("highest",TetrisActivity.this.highestScore);
            setResult(RESULT_OK, intent);

            TetrisActivity.this.finish();
        }
        else {
            //退出程序
            TetrisActivity.this.finish();
        }



    }

    private void gameOverFilm() {

        this.timer = new Timer();
        this.timer.scheduleAtFixedRate(new TimerTask() {
            int row = TetrisView.HEIGHT_NUM - 1;
            int column = 0;

            @Override
            public void run() {
                if (this.row >= 0) {
                    if(this.column < TetrisView.WIDTH_NUM) {
                        Message message = new Message();
                        message.what = SET_ROW_ALL_FILLED;
                        Bundle bundle = new Bundle();
                        bundle.putInt("row", row);
                        bundle.putInt("column", column);
                        message.setData(bundle);
                        TetrisActivity.this.refreshHandler.sendMessage(message);

                        this.column++;
                    } else {
                        this.row--;
                        this.column = 0;
                    }
                } else {
                    //重置TetrisView
                    TetrisActivity.this.refreshHandler.sendEmptyMessage(RESET_MAIN_VIEW);

                    TetrisActivity.this.timer.cancel();
                    TetrisActivity.this.timer.purge();
                }
            }
        }, 0, 25);
    }

}

