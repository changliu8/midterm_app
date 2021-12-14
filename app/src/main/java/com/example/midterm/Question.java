package com.example.midterm;

public class Question {

    private static final String TAG = Question.class.getSimpleName();

    private String mQuestionString; //id of string resource representing the question
    private String mContributor; //author or contributor of the question
    private String mOptionA;
    private String mOptionB;
    private String mOptionC;
    private String mOptionD;
    private String mOptionE;
    private String ID;
    private Boolean flag;

    //Constructor used when the question has a contributor
    public Question(String aQuestion, String id, String optionA, String optionB, String optionC, String optionD, String optionE){
        mQuestionString = aQuestion;
        mContributor = "anonymous";
        ID = id;
        mOptionA = optionA;
        mOptionB = optionB;
        mOptionC = optionC;
        mOptionD = optionD;
        mOptionE = optionE;
        flag = false;
    }
    //Constructor used when the question doesn't have a contributor
    public Question(String aQuestion, String id, String optionA, String optionB, String optionC, String optionD, String optionE,String contributer){
        mQuestionString = aQuestion;
        if(contributer != null && !contributer.isEmpty())
            mContributor = contributer;
        else
            mContributor = "anonymous";
        ID = id;
        mOptionA = optionA;
        mOptionB = optionB;
        mOptionC = optionC;
        mOptionD = optionD;
        mOptionE = optionE;
        flag = false;
    }


    public String getQuestionString(){return mQuestionString;}
    public String getContributer(){return mContributor;}
    //format of the question output
    public String toString(){
        String toReturn = "";
        if(mContributor != null && !mContributor.isEmpty())
            toReturn += "[" + mContributor + "] ";
        toReturn += mQuestionString;
        return toReturn;
    }

    public String getmOptionA() {
        return mOptionA;
    }

    public String getmOptionB() {
        return mOptionB;
    }

    public String getmOptionC() {
        return mOptionC;
    }

    public String getmOptionD() {
        return mOptionD;
    }

    public String getmOptionE() {
        return mOptionE;
    }

    public char getIndex(){
        return mQuestionString.charAt(0);
    }

    public String getID() {
        return ID;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public Boolean getFlag() {
        return flag;
    }
}
