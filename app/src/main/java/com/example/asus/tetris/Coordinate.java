package com.example.asus.tetris;

/**
 * Created by ASUS on 2017/7/10.
 */

/**
 * 记录每一个方块的坐标
 */
public class Coordinate {

    public int x; //横轴坐标

    public int y; //纵轴坐标

    public int color;

    public Coordinate(int x,int y,int color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }
}
