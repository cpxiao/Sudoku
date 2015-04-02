package com.cpxiao.sudoku.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.cpxiao.sudoku.utility.ActivityCollector;
import com.cpxiao.sudoku.R;
import com.cpxiao.sudoku.view.SudokuView;

public class GameActivity extends ActionBarActivity {
    public static void actionStart(Context context, int gameType, String gameDifficulty){
        Intent intent = new Intent(context, GameActivity.class);
        intent.putExtra("gameType_int", gameType);
        intent.putExtra("gameDifficulty_String", gameDifficulty);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        Intent intent = getIntent();
        int gameType = intent.getIntExtra("gameType_int", 3);
        String gameDifficulty = intent.getStringExtra("gameDifficulty_String");
        setContentView(new SudokuView(this, gameType, gameDifficulty));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
