package com.cpxiao.sudoku;

/**
 * Created by cpxiao on 2015/1/26.
 */

public class Game {
    int gameType;
    int gameNumbers;
    String gameDifficulty;

    //存放初始值
    private int sudoku_init[][];
    //存放当前9宫格中所有值
    private int sudoku[][];

    //用于存储每个单元格已使用的数据
    private int used[][][];

    public Game(int gameType, String gameDifficulty) {
        this.gameType = gameType;
        this.gameNumbers = gameType * gameType;
        this.gameDifficulty = gameDifficulty;
        sudoku_init = new int[this.gameNumbers][this.gameNumbers];
        sudoku = new int[this.gameNumbers][this.gameNumbers];
        used = new int[this.gameNumbers][this.gameNumbers][];
        InitSudoku initsudoku = new InitSudoku(gameType, gameDifficulty);
        sudoku_init = initsudoku.randomSudoku();
        sudoku = copyArray(sudoku_init);
        calculateAllUsedTiles();
    }
    public int[][] copyArray(int array[][]){
        for (int i = 0; i < gameNumbers; i++){
            for (int j = 0; j < gameNumbers; j++){
                sudoku[i][j] = array[i][j];
            }
        }
        return sudoku;
    }



    //根据坐标(x,y)获取方格中数字
    public int getTile(int x, int y, int flag) {
        if (flag == 0) {
            return sudoku_init[y][x];
        } else {
            return sudoku[y][x];
        }
    }

    //根据坐标(x,y)获得方格中显示字符
    public String getTileString(int x, int y, int flag) {
        int value = getTile(x, y, flag);
        if (value == 0) {
            return "";
        } else {
            return String.valueOf(value);
        }
    }

    public void calculateAllUsedTiles() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < gameNumbers; j++) {
                used[i][j] = calculateUsedTiles(i, j);
            }
        }
    }

    public int[] getUsedTilesByCoor(int x, int y) {
        return used[x][y];
    }

    //删除为0的数据
    private int[] rmzero(int[] array) {
        int count = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] != 0) {
                count++;
            }
        }
        int array1[] = new int[count];
        count = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] != 0) {
                array1[count++] = array[i];
            }
        }
        return array1;
    }

    //计算某单元格中已使用的数据
    public int[] calculateUsedWithZero(int x, int y) {
        int array[] = new int[gameNumbers];
        for (int i = 0; i < gameNumbers; i++) {
            if (i == y) {
                continue;
            }
            int t = getTile(x, i, 1);
            if (t != 0) {
                array[t - 1] = t;
            }
        }
        for (int i = 0; i < gameNumbers; i++) {
            if (i == x) {
                continue;
            }
            int t = getTile(i, y, 1);
            if (t != 0) {
                array[t - 1] = t;
            }
        }

        //判断小区域中数据
        int num_x = (x / gameType) * gameType;
        int num_y = (y / gameType) * gameType;
        for (int i = num_x; i < num_x + gameType; i++) {
            for (int j = num_y; j < num_y + gameType; j++) {
                if (i == x && j == y) {
                    continue;
                }
                int t = getTile(i, j, 1);
                if (t != 0) {
                    array[t - 1] = t;
                }
            }
        }
        return array;
    }

    //计算已使用过的数字
    public int[] calculateUsedTiles(int x, int y) {
        int array[];
        array = calculateUsedWithZero(x, y);
        //删除为0的数据
        array = rmzero(array);
        return array;
    }

    //计算未使用过的数字
    public int[] calculateUnusedTiles(int x, int y) {
        int array[];
        array = calculateUsedWithZero(x, y);

        int count = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] == 0) {
                count++;
            }
        }
        int array1[] = new int[count];
        count = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] == 0) {
                array1[count++] = i + 1;
            }
        }
        return array1;
    }

    public int[][] resetSudoku(int x, int y, int num_xy) {
        //当选定区域不是游戏初始值，则更新该区域值
        if (sudoku_init[y][x] == 0) {
            sudoku[y][x] = num_xy;
        }
        return sudoku;
    }

    //判断游戏是否成功(0:unsuccessful,1:success)
    public int ifsuccess() {
        int c1[] = new int[gameNumbers];
        //判断所有行是否成功
        for (int y = 0; y < gameNumbers; y++) {
            c1 = new int[gameNumbers];
            for (int x = 0; x < gameNumbers; x++) {
                int t = getTile(x, y, 1);
                if (t != 0) {
                    c1[t - 1] = t;
                } else {
                    return 0;
                }
            }
            c1 = rmzero(c1);
            if (c1.length != gameNumbers) {
                return 0;
            }
        }
        //判断所有列是否成功
        for (int x = 0; x < gameNumbers; x++) {
            c1 = new int[gameNumbers];
            for (int y = 0; y < gameNumbers; y++) {
                int t = getTile(x, y, 1);
                if (t != 0) {
                    c1[t - 1] = t;
                } else {
                    return 0;
                }
            }
            c1 = rmzero(c1);
            if (c1.length != gameNumbers) {
                return 0;
            }
        }
        //判断所有小区域是否成功
        for (int i = 0; i < gameType; i++) {
            for (int j = 0; j < gameType; j++) {
                for (int x = i * gameType; x < (i + 1) * gameType; x++) {
                    for (int y = j * gameType; y < (j + 1) * gameType; y++) {
                        int t = getTile(x, y, 1);
                        if (t != 0) {
                            c1[t - 1] = t;
                        } else {
                            return 0;
                        }
                    }
                }
                c1 = rmzero(c1);
                if (c1.length != gameNumbers) {
                    return 0;
                }
            }
        }
        //游戏成功，数据不能修改了
//        sudoku_init = sudoku;
        return 1;
    }
}
