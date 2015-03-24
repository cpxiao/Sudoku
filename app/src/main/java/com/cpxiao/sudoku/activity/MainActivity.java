package com.cpxiao.sudoku.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.cpxiao.sudoku.ActivityCollector;
import com.cpxiao.sudoku.R;
import static java.lang.Math.sqrt;


public class MainActivity extends ActionBarActivity {
    //设置默认值
    private int gameType = 3;
    private String gameDifficulty = "easy";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏标题栏
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        ActivityCollector.addActivity(this);
        RadioGroup radioGroup1 = (RadioGroup) findViewById(R.id.radioGroup1);
        RadioGroup radioGroup2 = (RadioGroup) findViewById(R.id.radioGroup2);

        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radioButtonId = group.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton)MainActivity.this.findViewById(radioButtonId);
                String aa = (String)rb.getText();
                String[] bb = aa.split("\\*");
                gameType = (int)sqrt(Integer.valueOf(bb[0]).doubleValue());
            }
        });
        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radioButtonId = group.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton)MainActivity.this.findViewById(radioButtonId);
                gameDifficulty = (String)rb.getText();
            }
        });
        Button button1 = (Button)findViewById(R.id.button);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameActivity.actionStart(MainActivity.this, gameType, gameDifficulty);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
