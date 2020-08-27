package com.example.germanvocclient;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.germanvocclient.models.Word;
import com.example.germanvocclient.serverCommunication.CallServerService;
import com.example.germanvocclient.serverCommunication.serverResponses.onGetServerMessageResponse;
import com.example.germanvocclient.serverCommunication.serverResponses.onGetWordsServerResponse;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity {
    CallServerService callServerService;
    int handle;
    int rightCount;
    List<Word> quizWords;
    private Button getQuizButton;
    private Button submitButton;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private EditText getAnswerText;
    private TextView questionText;
    private TextView answerStatusText;
    private TextView questionNumberText;
    private TextView signOutText;
    private TextView resultText;
    private int curWordNum;
    private String answerStatusObtained;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        Intent intent = getIntent();
        handle = intent.getIntExtra("userHandle", 0);
        Log.i("handle value: ", Integer.toString(handle));

        callServerService = CallServerService.getInstance();

        getQuizButton = findViewById(R.id.buttonCreateQuiz);
        submitButton = findViewById(R.id.buttonSubmit);
        submitButton.setEnabled(false);
        radioGroup = findViewById(R.id.radioGroup);
        getAnswerText = findViewById(R.id.editTextAnswer);
        questionNumberText = findViewById(R.id.textViewQuestionNumeration);
        questionText = findViewById(R.id.textViewQuestion);
        answerStatusText = findViewById(R.id.textViewAnswerStatus);
        signOutText = findViewById(R.id.textViewSignOut);
        resultText = findViewById(R.id.textViewResult);

        getQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rightCount = 0;
                resultText.setText("");
                submitButton.setEnabled(true);
                answerStatusText.setText("");
                questionText.setText("");
                getAnswerText.setText("");
                questionNumberText.setText("");
                curWordNum = 0;
                quizWords = new ArrayList<>();
                callServerService.callServerGetQuiz(Integer.parseInt(getRadioButtonClickedText()), handle, new onGetWordsServerResponse() {
                    @Override
                    public void onWordsResponse(List<Word> quiz) {
                        Log.i("respBodyQuiz", "here");
                        for (int i = 0; i < quiz.size(); i++){
                            Log.i("respBodyQuiz", quiz.get(i).toString());
                            quizWords.add(quiz.get(i));
                        }
                        questionText.setText(quizWords.get(curWordNum).getTranslation());
                        questionNumberText.setText((curWordNum+1)+ " out of " + quizWords.size());
                    }
                });
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userAnswer = getAnswerText.getText().toString();
                if(!userAnswer.isEmpty()){
                    callServerService.callServerSendAnswer(userAnswer, handle, new onGetServerMessageResponse() {
                        @Override
                        public void onMessageResponse(JsonObject answer) {
                            if (answer.has("answerStatus")) {
                                answerStatusObtained = answer.get("answerStatus").getAsString();
                            }

                            int answerColor;
                            if (answerStatusObtained.equals("right")) {
                                rightCount++;
                                answerColor =  Color.GREEN;
                            }
                            else   answerColor =  Color.RED;
                            answerStatusText.setTextColor(answerColor);
                            answerStatusText.setText(answerStatusObtained);
                        }
                    });
                    if (curWordNum == quizWords.size() - 1) {
                        submitButton.setEnabled(false);
                        resultText.setText("Result: " +  (int)((float) (rightCount)/quizWords.size()*100) + "%");
                        return;
                    }
                    curWordNum++;
                    if (!quizWords.isEmpty()) {
                        Word word = quizWords.get(curWordNum);
                        questionText.setText(word.getTranslation());
                        questionNumberText.setText((curWordNum+1)+ " out of " + quizWords.size());
                    }
                }
            }
        });

        signOutText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    String getRadioButtonClickedText(){
        int radioButtonId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioButtonId);
        return radioButton.getText().toString();
    }
}