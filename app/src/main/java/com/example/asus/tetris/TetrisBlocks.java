package com.example.asus.tetris;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASUS on 2017/7/7.
 */

public class TetrisBlocks {
    /**方块总类型数*/
    public static final int BlockType = 7;
    /**方块总方向数*/
    public static final int BlockDirection = 4;
    /**
     * 根据给定的类型与方向返回方块组
     * @param type 方块类型
     * @param direction 方块方向
     * @return 给定的方块
     */
    public static List<Coordinate> CreateTetrisBlocks(int type, int direction){
        /*方块的存储数组*/
        List<Coordinate> tetrisBlocks = new ArrayList<>();

        /*根据类型生成方块组*/
        switch (type){
            /*方四，无方向*/
            case 0:
                tetrisBlocks.add(new Coordinate(0,0,1));
                tetrisBlocks.add(new Coordinate(1,0,1));
                tetrisBlocks.add(new Coordinate(0,1,1));
                tetrisBlocks.add(new Coordinate(1,1,1));
                break;

            /*直四，需要两个方向*/
            case 1:
                switch (direction) {
                    case 0:case 2:
                        tetrisBlocks.add(new Coordinate(0,0,2));
                        tetrisBlocks.add(new Coordinate(1,0,2));
                        tetrisBlocks.add(new Coordinate(2,0,2));
                        tetrisBlocks.add(new Coordinate(3,0,2));
                        break;

                    case 1:case 3:
                        tetrisBlocks.add(new Coordinate(0,0,2));
                        tetrisBlocks.add(new Coordinate(0,1,2));
                        tetrisBlocks.add(new Coordinate(0,2,2));
                        tetrisBlocks.add(new Coordinate(0,3,2));
                        break;
                }
                break;

            /*弯四No.1，需要两个方向*/
            case 2:
                switch (direction){
                    case 0:case 2:
                        tetrisBlocks.add(new Coordinate(1,0,3));
                        tetrisBlocks.add(new Coordinate(2,0,3));
                        tetrisBlocks.add(new Coordinate(0,1,3));
                        tetrisBlocks.add(new Coordinate(1,1,3));
                        break;

                    case 1:case 3:
                        tetrisBlocks.add(new Coordinate(0,0,3));
                        tetrisBlocks.add(new Coordinate(0,1,3));
                        tetrisBlocks.add(new Coordinate(1,1,3));
                        tetrisBlocks.add(new Coordinate(1,2,3));
                        break;
                }
                break;

            /*弯四No.2，需要两个方向*/
            case 3:
                switch (direction) {
                    case 0:case 2:
                        tetrisBlocks.add(new Coordinate(0,0,3));
                        tetrisBlocks.add(new Coordinate(1,0,3));
                        tetrisBlocks.add(new Coordinate(1,1,3));
                        tetrisBlocks.add(new Coordinate(2,1,3));
                        break;

                    case 1:case 3:
                        tetrisBlocks.add(new Coordinate(1,0,3));
                        tetrisBlocks.add(new Coordinate(0,1,3));
                        tetrisBlocks.add(new Coordinate(1,1,3));
                        tetrisBlocks.add(new Coordinate(0,2,3));
                        break;
                }
                break;

            /*曲四No.1，四个方向*/
            case 4:
                switch (direction) {
                    case 0:
                        tetrisBlocks.add(new Coordinate(0,0,4));
                        tetrisBlocks.add(new Coordinate(0,1,4));
                        tetrisBlocks.add(new Coordinate(1,1,4));
                        tetrisBlocks.add(new Coordinate(2,1,4));
                        break;

                    case 1:
                        tetrisBlocks.add(new Coordinate(0,0,4));
                        tetrisBlocks.add(new Coordinate(1,0,4));
                        tetrisBlocks.add(new Coordinate(0,1,4));
                        tetrisBlocks.add(new Coordinate(0,2,4));
                        break;

                    case 2:
                        tetrisBlocks.add(new Coordinate(0,0,4));
                        tetrisBlocks.add(new Coordinate(1,0,4));
                        tetrisBlocks.add(new Coordinate(2,0,4));
                        tetrisBlocks.add(new Coordinate(2,1,4));
                        break;

                    case 3:
                        tetrisBlocks.add(new Coordinate(1,0,4));
                        tetrisBlocks.add(new Coordinate(1,1,4));
                        tetrisBlocks.add(new Coordinate(1,2,4));
                        tetrisBlocks.add(new Coordinate(0,2,4));
                        break;
                }
                break;

            /*曲四No.2，需要四个方向*/
            case 5:
                switch (direction) {
                    case 0:
                        tetrisBlocks.add(new Coordinate(2,0,4));
                        tetrisBlocks.add(new Coordinate(0,1,4));
                        tetrisBlocks.add(new Coordinate(1,1,4));
                        tetrisBlocks.add(new Coordinate(2,1,4));
                        break;

                    case 1:
                        tetrisBlocks.add(new Coordinate(0,0,4));
                        tetrisBlocks.add(new Coordinate(0,1,4));
                        tetrisBlocks.add(new Coordinate(0,2,4));
                        tetrisBlocks.add(new Coordinate(1,2,4));
                        break;

                    case 2:
                        tetrisBlocks.add(new Coordinate(0,0,4));
                        tetrisBlocks.add(new Coordinate(1,0,4));
                        tetrisBlocks.add(new Coordinate(2,0,4));
                        tetrisBlocks.add(new Coordinate(0,1,4));
                        break;

                    case 3:
                        tetrisBlocks.add(new Coordinate(0,0,4));
                        tetrisBlocks.add(new Coordinate(1,0,4));
                        tetrisBlocks.add(new Coordinate(1,1,4));
                        tetrisBlocks.add(new Coordinate(1,2,4));
                        break;
                }
                break;

            /*丁四，需要四个方向*/
            case 6:
                switch (direction) {
                    case 0:
                        tetrisBlocks.add(new Coordinate(1,0,5));
                        tetrisBlocks.add(new Coordinate(0,1,5));
                        tetrisBlocks.add(new Coordinate(1,1,5));
                        tetrisBlocks.add(new Coordinate(2,1,5));
                        break;

                    case 1:
                        tetrisBlocks.add(new Coordinate(0,0,5));
                        tetrisBlocks.add(new Coordinate(0,1,5));
                        tetrisBlocks.add(new Coordinate(1,1,5));
                        tetrisBlocks.add(new Coordinate(0,2,5));
                        break;

                    case 2:
                        tetrisBlocks.add(new Coordinate(0,0,5));
                        tetrisBlocks.add(new Coordinate(1,0,5));
                        tetrisBlocks.add(new Coordinate(2,0,5));
                        tetrisBlocks.add(new Coordinate(1,1,5));
                        break;

                    case 3:
                        tetrisBlocks.add(new Coordinate(1,0,5));
                        tetrisBlocks.add(new Coordinate(0,1,5));
                        tetrisBlocks.add(new Coordinate(1,1,5));
                        tetrisBlocks.add(new Coordinate(1,2,5));
                        break;
                }
                break;
        }

        return tetrisBlocks;
    }


}
