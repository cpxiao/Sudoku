package com.cpxiao.sudoku;

/**
 * Created by cpxiao on 2015/1/26.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import android.text.format.DateFormat;

public class SudokuView extends View {
    private Context myContext;
    private Intent myIntent;
    private String tmpString;
    private int tmpNumXY;
    private float length;
    Paint backgroundPaint = new Paint();
    Paint boldLinePaint = new Paint();
    Paint linePaint = new Paint();
    Paint numberPaint = new Paint();
    Paint numberPain_init = new Paint();
    Paint numberPaint_choose = new Paint();

    private Game game;

    public SudokuView(Context context, int gameType, String gameDifficulty) {
        super(context);
        myContext = context;
        myIntent = new Intent();
        game = new Game(gameType, gameDifficulty);
    }

    public void initPaints(){
        backgroundPaint.setColor(getResources().getColor(R.color.sudoku_background));

        boldLinePaint.setColor(getResources().getColor(R.color.sudoku_boldLine));
        boldLinePaint.setStyle(Paint.Style.STROKE);
        boldLinePaint.setStrokeWidth(3);

        linePaint.setColor(getResources().getColor(R.color.sudoku_boldLine));

        numberPaint.setColor(getResources().getColor(R.color.sudoku_number));
        numberPaint.setStyle(Paint.Style.STROKE);
        numberPaint.setTextSize(length * 0.75f);
        numberPaint.setTextAlign(Align.CENTER);

        numberPain_init.setColor(getResources().getColor(R.color.sudoku_number_init));
        numberPain_init.setStyle(Paint.Style.STROKE);
        numberPain_init.setTextSize(length * 0.75f);
        numberPain_init.setTextAlign(Align.CENTER);

        numberPaint_choose.setColor(getResources().getColor(R.color.sudoku_number_choose));
        numberPaint_choose.setStyle(Paint.Style.FILL);
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.length = (h < w ? h : w) / (game.gameNumbers + 2);
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        initPaints();

        canvas.drawRect(0, 0, getWidth(), getHeight(), backgroundPaint);
        canvas.drawRect(length, length, length * (game.gameNumbers + 1), length * (game.gameNumbers + 1), boldLinePaint);

        //绘制填写数字区域
        for (int i = 1; i < game.gameNumbers; i++) {
            //画横线
            canvas.drawLine(length, length * (i + 1), length * (game.gameNumbers + 1), length * (i + 1), linePaint);
            //画竖线
            canvas.drawLine(length * (i + 1), length, length * (i + 1), length * (game.gameNumbers + 1), linePaint);
            //中间粗线
            if (i % game.gameType == 0) {
                canvas.drawLine(length, length * (i + 1), length * (game.gameNumbers + 1), length * (i + 1), boldLinePaint);
                canvas.drawLine(length * (i + 1), length, length * (i + 1), length * (game.gameNumbers + 1), boldLinePaint);
            }
        }

        FontMetrics fm = numberPaint.getFontMetrics();
        float x = length / 2;
        float y = length / 2 - (fm.ascent - fm.descent) / 2;

        //填充数字
        for (int i = 0; i < game.gameNumbers; i++) {
            for (int j = 0; j < game.gameNumbers; j++) {
                tmpString = game.getTileString(i, j, 0);
                //判断是否为初始值
                if (tmpString.equals("")){
                    tmpString = game.getTileString(i, j, 1);
                    canvas.drawText(tmpString, length * (i + 1) + x, length * (j + 1) + y, numberPaint);
                }
                else {
                    canvas.drawText(tmpString, length * (i + 1) + x, length * (j + 1) + y, numberPain_init);
                }
            }
        }

        //绘制待选数字区域，填充待选数字
        canvas.drawRect(length, length * (game.gameNumbers + 2.0f), length * (game.gameNumbers + 2.0f), length * (game.gameNumbers + 3.0f), boldLinePaint);
        for (int i = 1; i <= (game.gameNumbers + 1); i++) {
            canvas.drawLine(length * i, length * (game.gameNumbers + 2.0f), length * i, length * (game.gameNumbers + 3.0f), boldLinePaint);
        }
        for (int i = 0; i < game.gameNumbers; i++) {
            //选中某一数字，该区域颜色与其他不一样
            if (tmpNumXY == (i + 1)){
                canvas.drawRect(length * (i + 1.0f), length * (game.gameNumbers + 2.0f), length * (i + 2.0f), length * (game.gameNumbers + 3.0f), numberPaint_choose);
            }
            canvas.drawText(String.valueOf(i + 1), length * (i + 1.0f) + x, length * (game.gameNumbers + 2.0f) + y, numberPain_init);
        }

        //绘制del区域
        if (tmpNumXY == game.gameNumbers + 1){
            int i = game.gameNumbers;
            canvas.drawRect(length * (i + 1.0f), length * (game.gameNumbers + 2.0f), length * (i + 2.0f), length * (game.gameNumbers + 3.0f), numberPaint_choose);
        }
        canvas.drawText("del", length * (game.gameNumbers + 1.0f) + x, length * (game.gameNumbers + 2.0f) + y, numberPain_init);

        //绘制游戏时间，delete
        long sysTime = System.currentTimeMillis();
        CharSequence sysTimeStr = DateFormat.format("hh:mm:ss", sysTime);
//        canvas.drawText((String)sysTimeStr, length * (2.0f) + x, length * (game.gameNumbers + 3.0f) + y, numberPain_init);

        //绘制新游戏按钮
        canvas.drawText("新游戏", length * (2.0f), length * (game.gameNumbers + 4.0f) + y, numberPain_init);
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() != MotionEvent.ACTION_DOWN) {
            return super.onTouchEvent(event);
        }
        int selectedX = (int) (event.getX() / length) - 1;
        int selectedY = (int) (event.getY() / length) - 1;
Log.d("BBBBBBBBBBBBBBBBBBBB", "X = " + selectedX + "Y = "+selectedY);
        //点击填数区域
        if (selectedX >= 0 && selectedX < game.gameNumbers && selectedY >= 0 && selectedY < game.gameNumbers) {
            int num_xy = game.getTile(selectedX, selectedY, 1);
            if (tmpNumXY == 0){
                num_xy = (num_xy + 1) % (game.gameNumbers + 1);
            }
            else if (tmpNumXY == game.gameNumbers + 1){
                num_xy = 0;
            }
            else {
                num_xy = tmpNumXY;
            }

            game.resetSudoku(selectedX, selectedY, num_xy);

            invalidate();
            game.calculateAllUsedTiles();
            int result = game.ifsuccess();
            Log.d("AAAAAAAAAAAAAAAAA", "result = " + result);
            if (result == 1) {
                System.out.println("success!");
            }
        }
        //点击待选数字以及del区域
        else if(selectedY == game.gameNumbers + 1){
            if (tmpNumXY != 0){
                if(tmpNumXY == selectedX + 1){
                    tmpNumXY = 0;
                }
                else{
                    tmpNumXY = selectedX + 1;
                }
            }
            else{
                tmpNumXY = selectedX + 1;
            }
            invalidate();
        }
        //点击“新游戏”
        else if (selectedY == game.gameNumbers + 3){
            myIntent.setClass(myContext, MainActivity.class);
            myContext.startActivity(myIntent);
        }
        return true;
    }
}
