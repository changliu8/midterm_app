package com.example.midterm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Result extends AppCompatActivity {

    private final static String STUDENT_MARK = "student_mark";
    private final static String STUDENT_ANSWER = "student_answer";
    private final static String CORRECT_ANSWER = "correct_answer";
    private static final String STUDENT_LOGIN_NAME = "student_login_name";
    private static final String STUDENT_LOGIN_ID = "student_login_id";
    private TextView mMark;
    private TextView mName;
    private TextView mId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        LinearLayout resultLayout = findViewById(R.id.result_list);
        mMark = (TextView) findViewById(R.id.mark);
        mName = (TextView) findViewById(R.id.result_name);
        mId = (TextView) findViewById(R.id.result_id);

        String student_answer[] = getIntent().getStringArrayExtra(Result.STUDENT_ANSWER);
        String correct_answer[] = getIntent().getStringArrayExtra(Result.CORRECT_ANSWER);
        int mark = getIntent().getIntExtra(Result.STUDENT_MARK,0);
        String name = getIntent().getStringExtra(Result.STUDENT_LOGIN_NAME);
        String id = getIntent().getStringExtra(Result.STUDENT_LOGIN_ID);
        mMark.setText(mMark.getText()+" "+ mark + " / 10");
        mName.setText(name);
        mId.setText(id);

        //compare the correct answers and the student answers to calculate the mark.
        for(int i=0;i<correct_answer.length;i++){
            TextView result = new TextView(Result.this);
            result.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT,0));
            result.setGravity(Gravity.CENTER);
            result.setText("Question " + (i+1) + ". Your answer : "+student_answer[i]+ "      Correct answer : "+correct_answer[i]);
            if(!student_answer[i].equalsIgnoreCase(correct_answer[i])){
                result.setTextColor(Color.RED);
            }
            else{
                result.setTextColor(Color.GREEN);
            }
            resultLayout.addView(result);
        }
    }
    @Override
    public void onBackPressed() {
        return;
    }
}