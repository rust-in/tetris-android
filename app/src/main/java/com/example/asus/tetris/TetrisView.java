package com.example.asus.tetris;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by ASUS on 2017/7/7.
 */

public class TetrisView extends View{
    /*绘制空白的画笔*/
    private Paint paintPre = null;
    /*绘制被填充的画笔*/
    private Paint paintBlock = null;
    /*方块背景的宽、高方向上个数*/
    public static final int WIDTH_NUM = 10;
    public static final int HEIGHT_NUM = 20;
    /*View的实际宽、高*/ //可以省略？
    private float viewWidth;
    private float viewHeight;
    /*方块的具体边长*/
    private float unit;
    /*方块间距*/
    private float margin;
    /*记录方块情况的矩阵（颜色？）*/
    private int[][] blocks = new int[HEIGHT_NUM][WIDTH_NUM];
    /*记录已固定位置的方块的矩阵*/
    private boolean[][] blocks_for_pre = new boolean[HEIGHT_NUM][WIDTH_NUM];
    /*颜色数组*/
    public static final int[] color = {Color.parseColor("#F9F0B1"), Color.parseColor("#996633"), Color.parseColor("#663300"), Color.parseColor("#CCCC66"), Color.parseColor("#CCCC99"), Color.parseColor("#CCCC33"),Color.parseColor("#999999"), Color.LTGRAY};
    /*记录当前下落方块的坐标、种类、方向、颜色*/
    private int currentX;
    private int currentY;
    private int currentType;
    private int currentDirection;
    private int currentColor;
    /*当前正在下落的方块坐标（颜色）数组*/
    private List<Coordinate> currentBlocks = new ArrayList<>();
    /*预判落点位置的方块数组*/
    private List<Coordinate> preBlocks = new ArrayList<>();

    public TetrisView(Context context){
        this(context, null);
    }

    public TetrisView(Context context, AttributeSet attr){
        this(context, attr, 0);
    }

    public TetrisView(Context context, AttributeSet attr, int defStyleAttr) {

        super(context, attr, defStyleAttr);
        isInEditMode();
        paintPre = new Paint();
        paintBlock = new Paint();

        //将记录位置的矩阵全部置空
        for (int i = 0; i < HEIGHT_NUM; i++) {
            for (int j = 0; j < WIDTH_NUM; j++) {
                this.blocks[i][j] = 0;
                this.blocks_for_pre[i][j] = false;
            }
        }

    }

    /**
     * 测量出view实际宽、高，进而得出方块的实际宽高以及间距的宽高
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        this.viewWidth = this.getMeasuredWidth();
        this.viewHeight = this.getMeasuredHeight();
        if(this.viewHeight > this.viewWidth * 2){
            this.unit = this.viewWidth / WIDTH_NUM;
            this.margin = this.viewWidth / (WIDTH_NUM * 10);
        } else {
            this.unit = this.viewHeight / HEIGHT_NUM;
            this.margin = this.viewHeight / (HEIGHT_NUM * 10);
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //设置画布背景颜色
//        canvas.drawColor(getResources().getColor(R.color.color_main_view_background));
        canvas.drawColor(Color.parseColor("#E8DEA5"));

        //根据颜色绘制所有方块
        for(int i = 0; i < HEIGHT_NUM; i++) {
            for(int j = 0; j < WIDTH_NUM; j++) {
                this.paintBlock.setColor(color[blocks[i][j]]);
                this.drawRect(i, j, canvas, paintBlock);
            }
        }

        //绘制预测落点的方块
        int test;
        for (test = 0; test < preBlocks.size(); test++) {
            this.paintPre.setColor(color[7]);
            this.drawRect(preBlocks.get(test).y, preBlocks.get(test).x, canvas, paintPre);
            Log.d("test", preBlocks.get(test).x+" "+preBlocks.get(test).y );
        }

        //重新绘制正在下落的方块，若预测落点与正在下落的方块重合，则应覆盖预测
        for (Coordinate coordinate : currentBlocks) {
            this.paintBlock.setColor(color[coordinate.color]);
            this.drawRect(coordinate.y, coordinate.x, canvas, paintBlock);
        }

    }

    /**
     * 根据坐标绘制方格
     * @param i 方块y轴坐标
     * @param j 方块x轴坐标
     * @param canvas 当前画布
     * @param paint 当前画笔
     */
    private void drawRect(int i, int j, Canvas canvas, Paint paint){ //changed
        RectF rectf = new RectF(unit * j + margin, unit * i + margin, unit * (j + 1) - margin, unit * (i + 1) - margin);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(rectf, paint);

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
     * 判断是否与正在下落的块重合
     */
    public boolean isCoincide(Coordinate coordinate) {
        for(Coordinate coordinate1 : this.currentBlocks) {
            if(coordinate1.x == coordinate.x && coordinate1.y == coordinate.y)
                return true;
        }
        return false;
    }

    /**
     * 判断能否下落
     */
    public boolean canDown(){
        for(Coordinate coordinate : this.currentBlocks) {
            if(isCoincide(new Coordinate(coordinate.x, coordinate.y + 1, coordinate.color))){
                continue; //可删？
            } else if (coordinate.y == HEIGHT_NUM - 1){
                return false;
            } else if (this.blocks[coordinate.y + 1][coordinate.x] != 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断能否向左移动
     */
    public boolean canLeft(){
        for (Coordinate coordinate : this.currentBlocks) {
            if(isCoincide(new Coordinate(coordinate.x - 1, coordinate.y, coordinate.color))){
                continue;
            } else if (coordinate.x == 0){
                return false;
            } else if (this.blocks[coordinate.y][coordinate.x -1] != 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断能否向右移动
     */
    public boolean canRight(){
        for (Coordinate coordinate : this.currentBlocks) {
            if(isCoincide(new Coordinate(coordinate.x + 1, coordinate.y, coordinate.color))){
                continue;
            } else if (coordinate.x == WIDTH_NUM - 1){
                return false;
            } else if (this.blocks[coordinate.y][coordinate.x + 1] != 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断能否旋转
     */
    public boolean canRoute(){

        List<Coordinate> tmp = TetrisBlocks.CreateTetrisBlocks(this.currentType,
                (this.currentDirection + 1) % 4);

        for(Coordinate coordinate : tmp) {
            if(this.currentType == 1) {
                if(this.currentDirection == 0 || this.currentDirection == 2) {
                    coordinate.x = coordinate.x + this.currentX + 1;
                    coordinate.y = coordinate.y + this.currentY - 1;
                } else {
                    coordinate.x = coordinate.x + this.currentX - 1;
                    coordinate.y = coordinate.y + this.currentY + 1;
                }
            } else if (this.currentType == 4) {
                if (this.currentDirection == 1) {
                    coordinate.x = coordinate.x + this.currentX - 1;
                    coordinate.y = coordinate.y + this.currentY;
                } else if (this.currentDirection == 2) {
                    coordinate.x = coordinate.x + this.currentX;
                    coordinate.y = coordinate.y + this.currentY - 1;
                } else {
                    coordinate.x = coordinate.x + this.currentX;
                    coordinate.y = coordinate.y + this.currentY;
                }
            } else if (this.currentType == 5) {
                if (this.currentDirection == 0) {
                    coordinate.x = coordinate.x + this.currentX + 1;
                    coordinate.y = coordinate.y + this.currentY - 1;
                } else if (this.currentDirection == 1) {
                    coordinate.x = coordinate.x + this.currentX + 1;
                    coordinate.y = coordinate.y + this.currentY;
                } else if (this.currentDirection == 2) {
                    coordinate.x = coordinate.x + this.currentX + 1;
                    coordinate.y = coordinate.y + this.currentY;
                } else {
                    coordinate.x = coordinate.x + this.currentX;
                    coordinate.y = coordinate.y + this.currentY;
                }
            } else if (this.currentType == 6) {
                if (this.currentDirection == 0) {
                    coordinate.x = coordinate.x + this.currentX + 1;
                    coordinate.y = coordinate.y + this.currentY;
                } else if (this.currentDirection == 1) {
                    coordinate.x = coordinate.x + this.currentX;
                    coordinate.y = coordinate.y + this.currentY + 1;
                } else {
                    coordinate.x = coordinate.x + this.currentX;
                    coordinate.y = coordinate.y + this.currentY;
                }
            } else { //可改
                coordinate.x = coordinate.x + this.currentX;
                coordinate.y = coordinate.y + this.currentY;
            }
        }

        for (Coordinate coordinate : tmp){
            if(this.isCoincide(coordinate)){
                continue;
            } else if (coordinate.x < 0 || coordinate.x > WIDTH_NUM - 1 ||
                    coordinate.y < 0 || coordinate.y > HEIGHT_NUM - 1){
                return false;
            } else if (this.blocks[coordinate.y][coordinate.x] != 0){
                return false;
            }
        }

        return true;
    }

    private boolean tran(List<Coordinate> coordinates){
        boolean left = false;
        boolean right = false;
        for(Coordinate coordinate : coordinates) {
            if (blocks[coordinate.y][coordinate.x] != 0 || coordinate.y < 0 || coordinate.y > HEIGHT_NUM - 1){
                return false;
            }
            if(coordinate.x < 0 ){
                left = true;
            } else if (coordinate.x > WIDTH_NUM - 1){
                right = true;
            }
        }

        if(left || right){
            if(left){
                for (Coordinate coordinate : coordinates) {
                    coordinate.x += 1;
                    tran(coordinates);
                }
            } else {
                for (Coordinate coordinate : coordinates) {
                    coordinate.x -= 1;
                    tran(coordinates);
                }
            }
        }

        return true;
    }

    /**
     * 将方块向下移动一格
     */
    public void toDown(){
        this.currentY += 1;
        for (Coordinate coordinate : this.currentBlocks){
            this.blocks[coordinate.y][coordinate.x] = 0;
        }
        for (Coordinate coordinate : this.currentBlocks) {
            coordinate.y += 1;
            this.blocks[coordinate.y][coordinate.x] = coordinate.color;
        }

        initPreBlocks();

        this.invalidate();
    }

    /**
     * 将方块向左移动一格
     */
    public void toLeft() {
        this.currentX -= 1;
        for (Coordinate coordinate : this.currentBlocks) {
            this.blocks[coordinate.y][coordinate.x] = 0;
        }
        for (Coordinate coordinate : this.currentBlocks) {
            coordinate.x -= 1;
            this.blocks[coordinate.y][coordinate.x] = coordinate.color;
        }

        initPreBlocks();

        this.invalidate();
    }

    /**
     * 将方块向右移动一格
     */
    public void toRight() {
        this.currentX += 1;
        for (Coordinate coordinate : this.currentBlocks) {
            this.blocks[coordinate.y][coordinate.x] = 0;
        }
        for (Coordinate coordinate : this.currentBlocks) {
            coordinate.x += 1;
            this.blocks[coordinate.y][coordinate.x] = coordinate.color;
        }

        initPreBlocks();

        this.invalidate();
    }

    /**
     * 将方块旋转
     */
    public void toRotate() {

        List<Coordinate> tmp = TetrisBlocks.CreateTetrisBlocks(this.currentType,
                (this.currentDirection + 1) % 4);

        for (Coordinate coordinate : tmp) {
            if (this.currentType == 1) {
                if (this.currentDirection == 0 || this.currentDirection == 2) {
                    coordinate.x = coordinate.x + this.currentX + 1;
                    coordinate.y = coordinate.y + this.currentY - 1;
                } else {
                    coordinate.x = coordinate.x + this.currentX - 1;
                    coordinate.y = coordinate.y + this.currentY + 1;
                }
            } else if (this.currentType == 4) {
                if (this.currentDirection == 1) {
                    coordinate.x = coordinate.x + this.currentX - 1;
                    coordinate.y = coordinate.y + this.currentY;
                } else if (this.currentDirection == 2) {
                    coordinate.x = coordinate.x + this.currentX;
                    coordinate.y = coordinate.y + this.currentY - 1;
                } else {
                    coordinate.x = coordinate.x + this.currentX;
                    coordinate.y = coordinate.y + this.currentY;
                }
            } else if (this.currentType == 5) {
                if (this.currentDirection == 0) {
                    coordinate.x = coordinate.x + this.currentX + 1;
                    coordinate.y = coordinate.y + this.currentY - 1;
                } else if (this.currentDirection == 1) {
                    coordinate.x = coordinate.x + this.currentX + 1;
                    coordinate.y = coordinate.y + this.currentY;
                } else if (this.currentDirection == 2) {
                    coordinate.x = coordinate.x + this.currentX + 1;
                    coordinate.y = coordinate.y + this.currentY;
                } else {
                    coordinate.x = coordinate.x + this.currentX;
                    coordinate.y = coordinate.y + this.currentY;
                }
            } else if (this.currentType == 6) {
                if (this.currentDirection == 0) {
                    coordinate.x = coordinate.x + this.currentX + 1;
                    coordinate.y = coordinate.y + this.currentY;
                } else if (this.currentDirection == 1) {
                    coordinate.x = coordinate.x + this.currentX;
                    coordinate.y = coordinate.y + this.currentY + 1;
                } else {
                    coordinate.x = coordinate.x + this.currentX;
                    coordinate.y = coordinate.y + this.currentY;
                }
            } else { //可改
                coordinate.x = coordinate.x + this.currentX;
                coordinate.y = coordinate.y + this.currentY;
            }
        }

        for (Coordinate coordinate : this.currentBlocks) {
            this.blocks[coordinate.y][coordinate.x] = 0;
        }
        for (Coordinate coordinate : tmp) {
            this.blocks[coordinate.y][coordinate.x] = coordinate.color;
        }

        this.currentBlocks = tmp;

        if (this.currentType == 1) {
            if (this.currentDirection == 0 || this.currentDirection == 2) {
                this.currentX++;
                this.currentY--;
            } else {
                this.currentX--;
                this.currentY++;
            }
        }

        this.currentDirection = (this.currentDirection + 1) % 4;

        initPreBlocks();

        this.invalidate();
    }

    /**
     * 检查是否存在可被删除的行
     * @return 可删除的总行数
     */
    public int checkRows() {

        int rows = 0;
        while (eliminateRow()) {
            rows ++;
        }


        for(int i = 0; i < HEIGHT_NUM; i++) {
            for(int j = 0; j < WIDTH_NUM; j++) {
                if (this.blocks[i][j] != 0) {
                    this.blocks_for_pre[i][j] = true;
                } else {
                    this.blocks_for_pre[i][j] = false;
                }
            }
        }

        return rows;
    }

    /**
     * 从下往上检查是否存在可删除的行
     * @return 若存在，则删除检测到的第一行并返回true，反之返回false
     */
    private boolean eliminateRow(){

        for (int i = HEIGHT_NUM - 1; i >= 0; i--) {

            boolean canDel = true;
            for (int j = 0; j < WIDTH_NUM; j++) {
                canDel = canDel && (this.blocks[i][j] != 0);
            }

            //若存在可删除的行
            if(canDel){
                for (int row = i; row >= 1; row--) {
                    for (int column = 0; column < WIDTH_NUM; column++) {
                        this.blocks[row][column] = this.blocks[row - 1][column];
                    }
                }
                for (int column = 0; column < WIDTH_NUM; column++) {
                    this.blocks[0][column] = 0;
                }

                return true;
            }
        }

        return false;
    }

    /**
     * 检测当下一个方块降落时，游戏是否结束
     * @param type 下一个方块的类型
     * @return 若游戏结束，返回true，反之返回false
     */
    public boolean gameOVer(int type) {
        for (Coordinate coordinate : TetrisBlocks.CreateTetrisBlocks(type, 0)){
            if(this.blocks[coordinate.y][coordinate.x + WIDTH_NUM / 2 - 2] != 0) {
                return true;
            }
        }

        return false;
    }

    /**
     * 重置当前界面，让游戏重新开始
     */
    public void reset() {

        for (int i = 0; i < HEIGHT_NUM; i++) {
            for (int j = 0; j < WIDTH_NUM; j++) {
                this.blocks[i][j] = 0;
                this.blocks_for_pre[i][j] = false;
            }
        }

        this.currentBlocks.clear();
        this.preBlocks.clear();
        this.currentX = WIDTH_NUM / 2 - 2;
        this.currentY = 0;
        this.currentType = 0;
        this.currentDirection = 0;

        this.invalidate();
    }

    /**
     * 向view中加入一个新的方块，并置于起始位置
     * @param type 方块类型
     */
    public void pushBlocks(int type) {
        //刷新状态
        this.currentType = type;
        this.currentDirection = 0;
        this.currentX = WIDTH_NUM / 2 - 2;
        this.currentY = 0;
        this.currentBlocks.clear();
        this.preBlocks.clear();

        for(int i = 0; i < HEIGHT_NUM; i++) {
            for(int j = 0; j < WIDTH_NUM; j++) {
                if (this.blocks[i][j] != 0) {
                    this.blocks_for_pre[i][j] = true;
                } else {
                    this.blocks_for_pre[i][j] = false;
                }
            }
        }

        this.currentBlocks = TetrisBlocks.CreateTetrisBlocks(type,0);
        for (Coordinate coordinate : this.currentBlocks) {
            coordinate.x += this.currentX;
            coordinate.y += this.currentY;
            this.blocks[coordinate.y][coordinate.x] = coordinate.color;
        }

        initPreBlocks();

        invalidate();
    }

    /**
     * 为游戏设置难度：从下至上n行随机出现已存在的方块
     * @param difficulty 难度等级，从下至上的行数
     */
    public void setDifficulty(int difficulty) {

        Random random = new Random();
        for(int row = HEIGHT_NUM - 1; row >= HEIGHT_NUM - difficulty; row--) {
            for(int column = 0; column < WIDTH_NUM; column++) {
                if(random.nextInt(2) == 1){
                    this.blocks[row][column] = 6;
                }
            }
        }

        for(int i = 0; i < HEIGHT_NUM; i++) {
            for(int j = 0; j < WIDTH_NUM; j++) {
                if (this.blocks[i][j] != 0) {
                    this.blocks_for_pre[i][j] = true;
                }
            }
        }
    }

    /**
     * 填满一个方格，用于game over时的动画效果
     * @param row 行数
     * @param column 列数
     */
    public void setFilled(int row, int column) {
        this.preBlocks.clear();
        this.currentBlocks.clear();

        this.blocks[row][column] = 6;

        this.invalidate();
    }

    /**
     * 生成预测落点的方块
     */
    private void initPreBlocks() {
        this.preBlocks.clear();
        int difference = HEIGHT_NUM;
        int exact = -1;
        for (Coordinate coordinate : currentBlocks) {
            if(difference > HEIGHT_NUM - 1 - coordinate.y) {
                difference = HEIGHT_NUM - 1 - coordinate.y;
            }
        }
        for (int i = 1; i <= difference; i++) {
            boolean flag = true;
            for (Coordinate coordinate : currentBlocks) {
                if (blocks_for_pre[coordinate.y + i][coordinate.x]) {
                    flag = false;
                }
            }
            if(!flag) {
                exact = i - 1;
                break;
            } else if (i == difference) {
                exact = difference;
            }
        }

        if(exact != -1) {
            for (int j = 0; j < 4; j++) {
                preBlocks.add(new Coordinate(currentBlocks.get(j).x, currentBlocks.get(j).y + exact, 7));
            }
        }


    }
}



