package com.example.midterm;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private final int MAX_QUESTIONS = 10;
    private final int MAX_QUESTIONS_PER_LINE = 5;
    private ArrayList<Button> button_list;

    private Button mButton_A;
    private Button mButton_B;
    private Button mButton_C;
    private Button mButton_D;
    private Button mButton_E;

    private Button mNextButton;
    private Button mSubButton;
    private Button mPrevButton;


    private TextView mQuestionTextView;
    private TextView mStudentNameTextView;
    private TextView mStudentIdTextView;
    private TextView mOptionA;
    private TextView mOptionB;
    private TextView mOptionC;
    private TextView mOptionD;
    private TextView mOptionE;

    private LinearLayout buttonsLayout;

    private TextView mCountingDown;

    private ProgressBar mProgressbar;


    private ImageButton mFlag;


    private ArrayList<Question> questions;
    private ArrayList<Answer> answers;

    private String[] user_answers;
    private String[] correct_answers_in_order;

    private String email_address= "";
    private Map<String, String> correct_answers = new HashMap<String, String>();
    private Boolean needAdditionRow = false;


    private int questionIndex;

    //For rotations
    private final String State = "STATE";
    private final String State_key = "STATE_KEY";
    private final String TEXT_VIEW_KEY = "TEXT_KEY";
    private final String QUES_INDEX = "QUES_INDEX";
    private final String ANSWERS_KEY = "ANSWERS_KEY";
    private final String NAME_KEY = "NAME_KEY";
    private final String ID_KEY = "ID_KEY";
    private static final String STUDENT_LOGIN_NAME = "student_login_name";
    private static final String STUDENT_LOGIN_ID = "student_login_id";
    private final String STUDENT_MARK = "student_mark";
    private final String STUDENT_ANSWER = "student_answer";
    private final String CORRECT_ANSWER = "correct_answer";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //set and inflate UI to manage

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        mNextButton = (Button) findViewById(R.id.next_id);
        mSubButton = (Button) findViewById(R.id.submit_id);
        mPrevButton = (Button) findViewById(R.id.prev_id);

        mButton_A = (Button) findViewById(R.id.ButtonA_id);
        mButton_B = (Button) findViewById(R.id.ButtonB_id);
        mButton_C = (Button) findViewById(R.id.ButtonC_id);
        mButton_D = (Button) findViewById(R.id.ButtonD_id);
        mButton_E = (Button) findViewById(R.id.ButtonE_id);


        mQuestionTextView = (TextView) findViewById(R.id.question_text_id);
        mOptionA = (TextView) findViewById(R.id.optionA_id);
        mOptionB = (TextView) findViewById(R.id.optionB_id);
        mOptionC = (TextView) findViewById(R.id.optionC_id);
        mOptionD = (TextView) findViewById(R.id.optionD_id);
        mOptionE = (TextView) findViewById(R.id.optionE_id);

        mFlag = (ImageButton) findViewById(R.id.flag);

        mStudentNameTextView = (TextView) findViewById(R.id.inner_name);
        mStudentIdTextView = (TextView) findViewById(R.id.inner_id);
        mQuestionTextView.setTextColor(Color.BLUE);

        mProgressbar = (ProgressBar) findViewById(R.id.midterm_progress);
        buttonsLayout = findViewById(R.id.buttonsLayout);

        mCountingDown = (TextView) findViewById(R.id.counting_down);

        new CountDownTimer(3600000, 1000) {

            public void onTick(long millisUntilFinished) {
                long millis = millisUntilFinished;
                //Convert milliseconds into hour,minute and seconds
                String hms = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                mCountingDown.setText(hms);
            }
            public void onFinish() {
                int mark = 0;
                correct_answers_in_order = new String[questions.size()];
                //get the student name and student id
                String student_name = mStudentNameTextView.getText().toString();
                String student_id = mStudentIdTextView.getText().toString();
                Toast t;
                for (int i =0;i<questions.size();i++) {
                    if (user_answers[i]==null){
                        user_answers[i]= "N/A";
                    }
                }
                for(int i =0;i<questions.size();i++){
                    if(user_answers[i].equalsIgnoreCase(correct_answers.get(questions.get(i).getID()))){
                        mark++;
                    }
                }
                for(int i =0;i<questions.size();i++){
                    correct_answers_in_order[i] = correct_answers.get(questions.get(i).getID());
                }
                System.out.println();
                System.out.println(mark);
                Intent intent = new Intent(MainActivity.this, Result.class);
                intent.putExtra(STUDENT_MARK, mark);
                intent.putExtra(STUDENT_ANSWER, user_answers);
                intent.putExtra(CORRECT_ANSWER,correct_answers_in_order);
                intent.putExtra(STUDENT_LOGIN_NAME, student_name);
                intent.putExtra(STUDENT_LOGIN_ID, student_id);
                startActivity(intent);
            }
        }.start();

        button_list = new ArrayList<Button>();
        int numRow;

        //check if I need addition row for buttons
        if(MAX_QUESTIONS%MAX_QUESTIONS_PER_LINE==0){
            numRow = MAX_QUESTIONS/MAX_QUESTIONS_PER_LINE;
        }
        else{
            numRow = (MAX_QUESTIONS/MAX_QUESTIONS_PER_LINE)+1;
            needAdditionRow = true;
        }

        //create index buttons for each of questions
        for(int i =0;i<MAX_QUESTIONS/MAX_QUESTIONS_PER_LINE;i++){
            LinearLayout result = new LinearLayout(MainActivity.this);
            for(int j=0;j<MAX_QUESTIONS_PER_LINE;j++){
                Button button = new Button(MainActivity.this);
                //button.setText(MAX_QUESTIONS_PER_LINE*i+j);
                TableRow.LayoutParams params = new TableRow.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.MATCH_PARENT,
                        1.0f
                );
                params.setMargins(4,4,4,4);
                button.setLayoutParams(params);
                button.setPadding(2,2,2,2);
                button.setBackgroundColor(Color.WHITE);
                result.addView(button);
                int number = MAX_QUESTIONS_PER_LINE*i+j+1;
                button.setText(String.valueOf(number));
                button.setId(number);
                int finalI = MAX_QUESTIONS_PER_LINE*i+j;
                button_list.add(button);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        questionIndex = finalI;
                        setColor(questionIndex);
                        setText();
                        clearButtonList();
                        button_list.get(questionIndex).setBackgroundColor(Color.LTGRAY);
                        mProgressbar.setProgress((questionIndex+1)*10);
                    }
                });
            }
            buttonsLayout.addView(result);
        }
        if(needAdditionRow){
            int i=numRow-1;
            LinearLayout result = new LinearLayout(MainActivity.this);
            for(int j=0;j<MAX_QUESTIONS%MAX_QUESTIONS_PER_LINE;j++){
                Button button = new Button(MainActivity.this);
                //button.setText(MAX_QUESTIONS_PER_LINE*i+j);
                TableRow.LayoutParams params = new TableRow.LayoutParams(
                        TableLayout.LayoutParams.WRAP_CONTENT,
                        TableLayout.LayoutParams.WRAP_CONTENT,
                        1
                );
                params.setMargins(4,4,4,4);
                button.setLayoutParams(params);
                button.setPadding(2,2,2,2);
                button.setBackgroundColor(Color.WHITE);
                result.addView(button);
                int number = MAX_QUESTIONS_PER_LINE*i+j+1;
                button.setText(String.valueOf(number));
                button.setId(number);
                int finalI = MAX_QUESTIONS_PER_LINE*i+j;
                button_list.add(button);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        questionIndex = finalI;
                        setColor(questionIndex);
                        setText();
                        clearButtonList();
                        button_list.get(questionIndex).setBackgroundColor(Color.LTGRAY);
                        mProgressbar.setProgress((questionIndex+1)*10);
                    }
                });
            }
            buttonsLayout.addView(result);
        }


        questions = null;
        answers = null;
        questionIndex = 0;

        ArrayList<Question> parsedModel = null;
        ArrayList<Answer> parsedModel_two = null;
        //Parse exam xml file
        try {
            InputStream iStream = getResources().openRawResource(R.raw.comp2601exam);
            BufferedReader bReader = new BufferedReader(new InputStreamReader(iStream));
            parsedModel = Exam.pullParseFrom(bReader);
            email_address = Exam.getEmail_address();
            bReader.close();
        }
        catch (java.io.IOException e){
            e.printStackTrace();
        }
        String stu_name = getIntent().getStringExtra(MainActivity.STUDENT_LOGIN_NAME);
        String stu_id = getIntent().getStringExtra(MainActivity.STUDENT_LOGIN_ID);
        mStudentNameTextView.setText(mStudentNameTextView.getText()+stu_name);
        mStudentIdTextView.setText(mStudentIdTextView.getText()+stu_id);



        try {
            InputStream iStream = getResources().openRawResource(R.raw.answers);
            BufferedReader bReader = new BufferedReader(new InputStreamReader(iStream));
            parsedModel_two = Exam.pullAnswerParseFrom(bReader);
            bReader.close();
        }
        catch (java.io.IOException e){
            e.printStackTrace();

        }

        questions = parsedModel;
        answers = parsedModel_two;
        for(int i=0;i<answers.size();i++){
            correct_answers.put(answers.get(i).getID(),answers.get(i).getmAnswer());
        }

        for(int j=0;j<questions.size();j++){
            System.out.println("questions: " + questions.get(j).getID());
        }

        for(int j=0;j<answers.size();j++){
            System.out.println("answers: " + answers.get(j).getID());
        }

        //this is the user_answers
        user_answers = new String[questions.size()];

        //set question text and options text
        if(questions != null && questions.size() > 0) {
            setText();
        }
        button_list.get(0).setBackgroundColor(Color.LTGRAY);
        mProgressbar.setProgress((questionIndex+1)*10);

        //prev button, move to previous question
        mPrevButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mOptionA.setBackgroundColor(Color.WHITE);
                if(questionIndex > 0) questionIndex--;
                setColor(questionIndex);
                setText();
                clearButtonList();
                button_list.get(questionIndex).setBackgroundColor(Color.LTGRAY);
                mProgressbar.setProgress((questionIndex+1)*10);
            }
        });

        //next button, move to next question
        mNextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(questionIndex < questions.size()-1) questionIndex++;
                setColor(questionIndex);
                setText();
                clearButtonList();
                button_list.get(questionIndex).setBackgroundColor(Color.LTGRAY);
                mProgressbar.setProgress((questionIndex+1)*10);
            }
        });

        //color button A, clear colors on other buttons, update the user_answer.
        mButton_A.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                clearColor();
                user_answers[questionIndex] = "A";
                mButton_A.setBackgroundColor(Color.RED);
                setSelected(questionIndex);

            }

        });

        //color button B, clear colors on other buttons, update the user_answer.
        mButton_B.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                clearColor();
                user_answers[questionIndex] = "B";
                mButton_B.setBackgroundColor(Color.RED);
                setSelected(questionIndex);

            }

        });

        //color button C, clear colors on other buttons, update the user_answer.
        mButton_C.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                clearColor();
                user_answers[questionIndex] = "C";
                mButton_C.setBackgroundColor(Color.RED);
                setSelected(questionIndex);

            }

        });

        //color button D, clear colors on other buttons, update the user_answer.
        mButton_D.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                clearColor();
                user_answers[questionIndex] = "D";
                mButton_D.setBackgroundColor(Color.RED);
                setSelected(questionIndex);

            }

        });

        //color button E, clear colors on other buttons, update the user_answer.
        mButton_E.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                clearColor();
                user_answers[questionIndex] = "E";
                mButton_E.setBackgroundColor(Color.RED);
                setSelected(questionIndex);

            }

        });

        //submit button, submit answer when it is done;
        mSubButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int mark = 0;
                correct_answers_in_order = new String[questions.size()];
                //get the student name and student id
                String student_name = mStudentNameTextView.getText().toString();
                String student_id = mStudentIdTextView.getText().toString();
                Toast t;
                //check if the student has unselected question
                for (int i =0;i<questions.size();i++) {
                    if (user_answers[i]==null){
                        t = Toast.makeText(MainActivity.this, "You have unselected question, please double check", Toast.LENGTH_SHORT);
                        t.show();
                        return;
                    }
                }
                for(int i =0;i<questions.size();i++){
                    if(user_answers[i].equalsIgnoreCase(correct_answers.get(questions.get(i).getID()))){
                        mark++;
                    }
                }
                for(int i =0;i<questions.size();i++){
                    correct_answers_in_order[i] = correct_answers.get(questions.get(i).getID());
                }
                System.out.println();
                System.out.println(mark);
                Intent intent = new Intent(MainActivity.this, Result.class);
                intent.putExtra(STUDENT_MARK, mark);
                intent.putExtra(STUDENT_ANSWER, user_answers);
                intent.putExtra(CORRECT_ANSWER,correct_answers_in_order);
                intent.putExtra(STUDENT_LOGIN_NAME, student_name);
                intent.putExtra(STUDENT_LOGIN_ID, student_id);
                startActivity(intent);
            }

        });

        mFlag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFlag(questionIndex);
            }
        });

    }
    //set the question index, user_answers, student name and id
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        mQuestionTextView.setText(savedInstanceState.getString(TEXT_VIEW_KEY));
        questionIndex = savedInstanceState.getInt(QUES_INDEX);
        user_answers = savedInstanceState.getStringArray(ANSWERS_KEY);
        setColor(questionIndex);
        mStudentNameTextView.setText(savedInstanceState.getString(NAME_KEY));
        mStudentIdTextView.setText(savedInstanceState.getString(ID_KEY));

    }

    // invoked when the activity may be temporarily destroyed, save the instance state here
    //save the question index, user_answers, student name and id
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(State, State_key);
        outState.putInt(QUES_INDEX,questionIndex);
        String test = mQuestionTextView.getText().toString();
        outState.putString(TEXT_VIEW_KEY,test);
        outState.putStringArray(ANSWERS_KEY,user_answers);
        outState.putString(NAME_KEY,mStudentNameTextView.getText().toString());
        outState.putString(ID_KEY,mStudentIdTextView.getText().toString());
        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);
    }
    //helper function, get user's answer and color the correct button.
    public void setColor(int index){
        clearColor();
        final int color = Color.LTGRAY;
        if (user_answers[index]!=null){
            String tmp_case = user_answers[index];
            switch(tmp_case){
                case "A":
                    mButton_A.setBackgroundColor(Color.RED);
                    break;
                case "B":
                    mButton_B.setBackgroundColor(Color.RED);
                    break;
                case "C":
                    mButton_C.setBackgroundColor(Color.RED);
                    break;
                case "D":
                    mButton_D.setBackgroundColor(Color.RED);
                    break;
                case "E":
                    mButton_E.setBackgroundColor(Color.RED);
                    break;
            }
        }
    }
    //helper function, reset all buttons to unselected.
    public void clearColor(){
        mButton_A.setBackgroundColor(Color.WHITE);
        mButton_B.setBackgroundColor(Color.WHITE);
        mButton_C.setBackgroundColor(Color.WHITE);
        mButton_D.setBackgroundColor(Color.WHITE);
        mButton_E.setBackgroundColor(Color.WHITE);
    }
    //helper function, set text for options and questions
    public void setText(){
        mQuestionTextView.setText("" + (questionIndex + 1) + ") " +
                questions.get(questionIndex).toString());
        //mQuestionTextView.setText(questions.get(questionIndex).toString());
        mOptionA.setText("A: " + questions.get(questionIndex).getmOptionA());
        mOptionB.setText("B: " + questions.get(questionIndex).getmOptionB());
        mOptionC.setText("C: " + questions.get(questionIndex).getmOptionC());
        mOptionD.setText("D: " + questions.get(questionIndex).getmOptionD());
        mOptionE.setText("E: " + questions.get(questionIndex).getmOptionE());
    }
    //set the index buttons to default color
    public void clearButtonList(){
        for(int i =0;i<button_list.size();i++){
            button_list.get(i).setBackgroundColor(Color.WHITE);
        }
    }
    //set/unset flag
    public void setFlag(int index){
        if(!questions.get(index).getFlag()){
            questions.get(index).setFlag(true);
            button_list.get(index).setText("🚩"+(index+1));
        }
        else{
            questions.get(index).setFlag(false);
            button_list.get(index).setText(String.valueOf(index+1));
        }
    }
    //check if the question has been selected
    public void setSelected(int index){
        final int font_size = 15;
        final int color = Color.GREEN;
        button_list.get(index).setPaintFlags(Paint.FAKE_BOLD_TEXT_FLAG);
        button_list.get(index).setTextColor(color);
        button_list.get(index).setTextSize(font_size);
    }
}

