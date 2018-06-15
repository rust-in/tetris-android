package com.example.asus.tetris;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

/**
 * Created by ASUS on 2017/7/8.
 */

public class NextBlockView extends View {

    private Paint paint = null; //方块画笔

    private float viewWidth; //View的真是宽度
    private float unit; //绘制方块的宽度
    private float margin; //绘制方块内边框的宽度

    public int type; //方块类型

    /*用2*4的方格来展示下一个将要出现的方块*/
    private int[][] blocks = new int[2][4];

    /*方块的颜色*/
    public static final int[] color = {Color.parseColor("#F9F0B1"), Color.parseColor("#996633"), Color.parseColor("#663300"), Color.parseColor("#CCCC66"), Color.parseColor("#CCCC99"), Color.parseColor("#CCCC33"),Color.LTGRAY};

    public NextBlockView(Context context) {
        this(context, null);
    }

    public NextBlockView(Context context, AttributeSet attr) {
        this(context, attr, 0);
    }

    public NextBlockView(Context context, AttributeSet attr, int defStyleAttr) {
        super(context, attr, defStyleAttr);
        isInEditMode();
        paint = new Paint();

        //将方块数组全部置为空（0）
        for(int i = 0; i < 2; i++) {
            for (int j = 0; j < 4; j++) {
                this.blocks[i][j] = 0;
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.viewWidth = this.getMeasuredWidth(); //获得View宽度

        //根据viewWidth定义绘制方块的宽度与内边框宽度
        this.unit = this.viewWidth / 4;
        this.margin = this.viewWidth / 40;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        canvas.drawColor(getResources().getColor(R.color.color_main_view_background)); //设置画布背景色
        canvas.drawColor(Color.parseColor("#E8DEA5"));
        for (int i = 0; i < 2; i++){
            for(int j = 0; j < 4; j++){
                paint.setColor(color[blocks[i][j]]); //根据blocks数组设置画笔颜色
                this.drawRect(i, j, canvas, paint); //绘制一个方块
            }
        }
    }

    /**
     * 根据坐标绘制方格
     * @param i 方块y轴坐标
     * @param j 方块x轴坐标
     * @param canvas 当前画布
     * @param paint 当前画笔
     */
    private void drawRect(int i, int j, Canvas canvas, Paint paint){
        //绘制方块
        RectF rectf = new RectF(unit * j + margin, unit * i + margin, unit * (j + 1) - margin, unit * (i + 1) - margin);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(rectf, paint);

        //绘制方块边框
        paint.setColor(color[0]);
        Path path = new Path();
        path.moveTo(unit * j + margin * 1 / 3, unit * i + margin * 1 / 3);
        path.lineTo(unit * (j + 1) - margin * 1 / 3, unit * i + margin * 1 / 3);
        path.lineTo(unit * (j + 1) - margin * 1 / 3, unit * (i + 1) - margin * 1 / 3);
        path.lineTo(unit * j + margin * 1 / 3, unit * (i + 1) - margin * 1 / 3);
        path.lineTo(unit * j + margin * 1 / 3, unit * i + margin * 1 / 3);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path, paint);
    }

    /**
     * 绘制下一个将要出现的方块
     * @param type 方块类型
     */
    public void drawNextBlock(int type) {
        this.type = type;

        //重置blocks数组
        for(int i = 0; i < 2; i++) {
            for(int j = 0; j < 4; j++) {
                this.blocks[i][j] = 0;
            }
        }

        //获取下一个方块
        List<Coordinate> next = TetrisBlocks.CreateTetrisBlocks(type,0);

        //更新blocks数组
        for (Coordinate coordinate : next) {
            this.blocks[coordinate.y][coordinate.x] = coordinate.color;
        }

        invalidate();
    }
}

