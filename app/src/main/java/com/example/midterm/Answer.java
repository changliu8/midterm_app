package com.example.midterm;

public class Answer {
    private String mAnswer;
    private String ID;

    public Answer(String answer,String id){
        mAnswer = answer;
        ID = id;
    }

    public String getID() {
        return ID;
    }

    public String getmAnswer() {
        return mAnswer;
    }
}
