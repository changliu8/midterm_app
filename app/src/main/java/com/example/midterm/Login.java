package com.example.midterm;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Login extends AppCompatActivity {

    private TextView mLoginName;
    private TextView mLoginId;
    private Button mLoginButton;


    private static final String STUDENT_LOGIN_NAME = "student_login_name";
    private static final String STUDENT_LOGIN_ID = "student_login_id";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mLoginName = (TextView) findViewById(R.id.text_student_name);
        mLoginId = (TextView) findViewById(R.id.text_student_id);
        mLoginButton = (Button) findViewById(R.id.start);

        final String default_id = getString(R.string.text_id);
        final String default_name = getString(R.string.text_name);

        //check if the student_name & student_id are entered
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the student name and student id
                String student_name = mLoginName.getText().toString();
                String student_id = mLoginId.getText().toString();
                Toast t;

                //check the student puts name & id or not
                if (student_name.equals("") || (student_name.equals(default_name))) {
                    if (student_id.equals("") || (student_id.equals(default_id))) {
                        t = Toast.makeText(Login.this, "Please enter your name and id", Toast.LENGTH_SHORT);
                        t.show();
                        return;
                    }
                    t = Toast.makeText(Login.this, "Please enter your name", Toast.LENGTH_SHORT);
                    t.show();
                    return;
                }
                if (student_id.equals("") || (student_id.equals(default_id))) {
                    t = Toast.makeText(Login.this, "Please enter your id", Toast.LENGTH_SHORT);
                    t.show();
                    return;
                }
                Intent intent = new Intent(Login.this, MainActivity.class);
                intent.putExtra(STUDENT_LOGIN_NAME, student_name);
                intent.putExtra(STUDENT_LOGIN_ID, student_id);
                startActivity(intent);
            }
        });

        //clear default text in student name field
        //so the student would not delete previous information in the textview
        mLoginName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (mLoginName.getText().toString().equals(default_name)){
                    mLoginName.setText("");
                }
                return false;
            }
        });

        //clear default text in student id field
        //so the student would not delete previous information in the textview
        mLoginId.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (mLoginId.getText().toString().equals(default_id)){
                    mLoginId.setText("");
                }
                return false;
            }
        });

    }
}