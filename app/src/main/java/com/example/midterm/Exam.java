package com.example.midterm;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Collections;

public class Exam {

    private static final String TAG = Exam.class.getSimpleName();
    private static String email_address = "";
    private static final int MAX = 10;
    private static ArrayList<Question> questions;
    private static ArrayList<Answer> answers;


    //XML tags used to define an exam of multiple choice questions.

    public static ArrayList<Question> pullParseFrom(BufferedReader reader){

        //ArrayList questions = Question.exampleSet1(); //for now
        questions = new ArrayList<Question>();
        String current_text = "";
        String optionA = "";
        String optionB = "";
        String optionC = "";
        String optionD = "";
        String optionE = "";
        String tmp_text = "";
        String contributor = "";
        String id = "";

        // Get our factory and create a PullParser
        XmlPullParserFactory factory = null;
        try {
            factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();

            xpp.setInput(reader); // set input file for parser
            int eventType = xpp.getEventType(); // get initial eventType

            // Loop through pull events until we reach END_DOCUMENT
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagname = xpp.getName();

                // handle the xml tags encountered
                switch (eventType) {
                    case XmlPullParser.START_TAG: //XML opening tags
                        if(tagname.equalsIgnoreCase("question")){
                            contributor = xpp.getAttributeValue(null,"contributor");
                        }
                        if(tagname.equalsIgnoreCase("question")){
                            id = xpp.getAttributeValue(null,"id");
                        }
                        break;

                    case XmlPullParser.TEXT:
                        current_text = xpp.getText();
                        break;

                    case XmlPullParser.END_TAG: //XML closing tags
                        if(tagname.equalsIgnoreCase("question_text")){
                            tmp_text = current_text;
                            tmp_text = tmp_text.trim();
                        }
                        //get text for each option
                        if(tagname.equalsIgnoreCase("option_A")){
                            optionA = current_text;
                        }
                        if(tagname.equalsIgnoreCase("option_B")){
                            optionB = current_text;
                        }
                        if(tagname.equalsIgnoreCase("option_C")){
                            optionC = current_text;
                        }
                        if(tagname.equalsIgnoreCase("option_D")){
                            optionD = current_text;
                        }
                        if(tagname.equalsIgnoreCase("option_E")){
                            optionE = current_text;
                        }
                        //adding question into the Arraylist,
                        //use different methods if the question have contributor
                        if(xpp.getName().equalsIgnoreCase("question")){
                            if (contributor.equals("")){
                                tmp_text = tmp_text.trim();
                                Question question = new Question(tmp_text,id,optionA,optionB,optionC,optionD,optionE);
                                questions.add(question);
                            }
                            else {
                                tmp_text = tmp_text.trim();
                                contributor = contributor.trim();
                                Question question = new Question(tmp_text,id,optionA,optionB,optionC,optionD,optionE,contributor);
                                questions.add(question);
                            }
                        }
                        //get the email that the students will send answers to
                        if (tagname.equalsIgnoreCase("email")){
                            email_address = current_text;
                        }
                        break;

                    default:
                        break;
                }
                //iterate
                eventType = xpp.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

        while(questions.size()>MAX){
            int random_remove = getRandomNumber(0,questions.size()-1);
            questions.remove(random_remove);
        }
        for(int i =0;i<3;i++) {
            Collections.shuffle(questions);
        }
        return questions;

    }

    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    //parser wrote to parse the answer_key
    //but I didn't invoke this since the answers should be unknown
    public static ArrayList<Answer> pullAnswerParseFrom(BufferedReader reader){

        answers = new ArrayList<Answer>();
        String answer_text = "";
        String id = "";
        String current_text = "";

        // Get our factory and create a PullParser
        XmlPullParserFactory factory = null;
        try {
            factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();

            xpp.setInput(reader); // set input file for parser
            int eventType = xpp.getEventType(); // get initial eventType

            // Loop through pull events until we reach END_DOCUMENT
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagname = xpp.getName();

                // handle the xml tags encountered
                switch (eventType) {
                    case XmlPullParser.START_TAG: //XML opening tags
                        if(tagname.equalsIgnoreCase("answer")){
                            id = xpp.getAttributeValue(null,"id");
                        }
                        break;

                    case XmlPullParser.TEXT:
                        current_text = xpp.getText();
                        break;

                    case XmlPullParser.END_TAG: //XML closing tags
                        if (tagname.equalsIgnoreCase("answer")){
                            answer_text = current_text.trim();
                            Answer answer = new Answer(answer_text,id);
                            answers.add(answer);
                        }

                        break;

                    default:
                        break;
                }
                //iterate
                eventType = xpp.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return answers;
    }

    public static String getEmail_address() {
        return email_address;
    }

}
