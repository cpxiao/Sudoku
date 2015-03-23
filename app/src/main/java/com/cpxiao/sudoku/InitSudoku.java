package com.cpxiao.sudoku;

/**
 * Created by cpxiao on 2015/1/26.
 */

import java.util.Random;

public class InitSudoku {
    private int gameType;
    private int gameNumbers;
    private String gameDifficulty;
    private int initArray[];
    private int initNumbers;
    private int initSudoku[][];
    Random random1 = new Random();

    public InitSudoku(int gameType, String gameDifficulty) {
        this.gameType = gameType;
        this.gameNumbers = gameType * gameType;
        this.gameDifficulty = gameDifficulty;
        this.initArray = new int[this.gameNumbers];
        this.initSudoku = new int[this.gameNumbers][this.gameNumbers];
        initSudoku = randomSudoku();
    }

    //随机生成数独库
    public int[][] randomSudoku() {
        initArray = getRandomArray(gameNumbers);
        int array[];
        for (int i = 0; i < gameType; i++) {
            for (int j = 0; j < gameType; j++) {
                for (int k = 0; k < gameNumbers; k++) {
                    array = arrayDisplace(initArray, i + j * gameType);
                    initSudoku[i * gameType + j][k] = array[k];
                }
            }
        }

        int i = 0;
        while (i <= gameNumbers) {
            initSudoku = changeRow(initSudoku);
            initSudoku = changeRows(initSudoku);
            initSudoku = changeColumn(initSudoku);
            initSudoku = changeColumns(initSudoku);
            initSudoku = changeNumbers(initSudoku);
            i++;
        }
        initSudoku = keepInitNumbersByGameDifficulty(gameDifficulty);
        return initSudoku;
    }

    //删除数组中为0的数据
    private int[] rmNumberFromArray(int[] array, int num) {
        int count = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] != num) {
                count++;
            }
        }
        int array1[] = new int[count];
        count = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] != num) {
                array1[count++] = array[i];
            }
        }
        return array1;
    }

    //获得长度为length的无重复数字的随机数组
    private int[] getRandomArray(int length) {
        int array[] = new int[length];
        for (int i = 0; i < length; i++) {
            array[i] = i + 1;
        }
        int array2[] = new int[length];
        for (int i = 0; i < length; i++) {
            int t = random1.nextInt(array.length);
            array2[i] = array[t];
            array[t] = 0;
            array = rmNumberFromArray(array, 0);
        }
        return array2;
    }

    //随机换行
    private int[][] changeRow(int[][] array) {
        int tmp = random1.nextInt(gameType);
        int row1 = tmp * gameType + random1.nextInt(gameType);
        int row2 = tmp * gameType + random1.nextInt(gameType);
        int[][] array1 = new int[gameNumbers][gameNumbers];
        for (int i = 0; i < gameNumbers; i++) {
            for (int j = 0; j < gameNumbers; j++) {
                if (i == row1) {
                    array1[row2][j] = array[i][j];
                } else if (i == row2) {
                    array1[row1][j] = array[i][j];
                } else {
                    array1[i][j] = array[i][j];
                }
            }
        }
        return array1;
    }

    //随机换列
    private int[][] changeColumn(int[][] array) {
        int tmp = random1.nextInt(gameType);
        int column1 = tmp * gameType + random1.nextInt(gameType);
        int column2 = tmp * gameType + random1.nextInt(gameType);
        int[][] array1 = new int[gameNumbers][gameNumbers];
        for (int i = 0; i < gameNumbers; i++) {
            for (int j = 0; j < gameNumbers; j++) {
                if (j == column1) {
                    array1[i][column2] = array[i][j];
                } else if (j == column2) {
                    array1[i][column1] = array[i][j];
                } else {
                    array1[i][j] = array[i][j];
                }
            }
        }
        return array1;
    }

    //随机换横区域
    private int[][] changeRows(int[][] array) {
        int rows1 = random1.nextInt(gameType);
        int rows2 = random1.nextInt(gameType);
        int[][] array1 = new int[gameNumbers][gameNumbers];
        for (int i = 0; i < gameNumbers; i++) {
            for (int j = 0; j < gameNumbers; j++) {
                if (i / gameType == rows1) {
                    array1[i][j] = array[rows2 * gameType + i % gameType][j];
                } else if (i / gameType == rows2) {
                    array1[i][j] = array[rows1 * gameType + i % gameType][j];
                } else {
                    array1[i][j] = array[i][j];
                }
            }
        }
        return array1;
    }

    //随机换列区域
    private int[][] changeColumns(int[][] array) {
        int columns1 = random1.nextInt(gameType);
        int columns2 = random1.nextInt(gameType);
        int[][] array1 = new int[gameNumbers][gameNumbers];
        for (int i = 0; i < gameNumbers; i++) {
            for (int j = 0; j < gameNumbers; j++) {
                if (j / gameType == columns1) {
                    array1[i][j] = array[i][columns2 * gameType + j % gameType];
                } else if (j / gameType == columns2) {
                    array1[i][j] = array[i][columns1 * gameType + j % gameType];
                } else {
                    array1[i][j] = array[i][j];
                }
            }
        }
        return array1;
    }

    //数组位移
    private int[] arrayDisplace(int[] array, int n) {
        if (n <= 0 || n >= gameNumbers) {
            return array;
        }
        int[] array1 = new int[gameNumbers];
        for (int i = 0; i < gameNumbers; i++) {
            array1[i] = array[(i + n) % gameNumbers];
        }
        return array1;
    }

    //换数字
    private int[][] changeNumbers(int[][] array) {
        int num1 = random1.nextInt(gameNumbers);
        int num2 = random1.nextInt(gameNumbers);
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array.length; j++) {
                if (array[i][j] == num1) {
                    array[i][j] = num2;
                } else if (array[i][j] == num2) {
                    array[i][j] = num1;
                }
            }
        }
        return array;
    }

    //根据难度保留游戏初始值
    private int[][] keepInitNumbersByGameDifficulty(String gameDifficulty) {
        initNumbers = getInitNumbersByGameDifficulty(gameDifficulty);
        int tmp, count = 0, i, j;
        int tmpArray[] = new int[gameNumbers * gameNumbers];
        for (i = 0; i < tmpArray.length; i++) {
            tmpArray[i] = i;
        }
        int delNumbers = gameNumbers * gameNumbers - initNumbers;
        while (count < delNumbers) {
            tmp = tmpArray[random1.nextInt(tmpArray.length)];
            i = tmp / gameNumbers;
            j = tmp % gameNumbers;
            initSudoku[i][j] = 0;
            count++;
            tmpArray = rmNumberFromArray(tmpArray, tmp);
        }
        return initSudoku;
    }

    //根据难度获取保留数字的个数
    private int getInitNumbersByGameDifficulty(String gameDifficulty) {
        Double tmp;
        switch (gameDifficulty) {
            case "专家级":
                tmp = 0.3;
                break;
            case "高级":
                tmp = 0.35;
                break;
            case "中级":
                tmp = 0.4;
                break;
            default:
                tmp = 0.45;
                break;
        }
        initNumbers = (int)(tmp * gameNumbers * gameNumbers) + random1.nextInt(gameType);
        return initNumbers;
    }
}
