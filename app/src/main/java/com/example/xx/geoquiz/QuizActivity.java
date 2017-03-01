package com.example.xx.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

public class QuizActivity extends AppCompatActivity {
    private Button mCheatButton,mTrueButton,mFalseButton,mNextButton,mPrevButton;
    private TextView mQuestionTextView;

    private static  final String KEY_INDEX="mCurrentIndex",KEY_CHEATED="mCheated";
    private static int REQ_CODE_CHEAT=0;

    private int mCurrentIndex=0;
    private boolean mCheated[];

    private Question[] mQuestionBank=new Question[]{
            new Question(R.string.question,true),
            new Question(R.string.question1,false),
            new Question(R.string.question2,false),
            new Question(R.string.question3,false)
    };

    private void updateQuestion(){
        int question=mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    private void checkAnswer(boolean userPressed)
    {
        boolean answer=mQuestionBank[mCurrentIndex].isAnswerTrue();

        int messageResId;
        if(!mCheated[mCurrentIndex])
            if(userPressed==answer)
                messageResId=R.string.correct_toast;
            else
                messageResId=R.string.incorrect_toast;
        else
            messageResId=R.string.warning_toast;

        Toast.makeText(QuizActivity.this,messageResId,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode!= Activity.RESULT_OK )
            return;
        if(requestCode==REQ_CODE_CHEAT&&data!=null)
            mCheated[mCurrentIndex]=CheatActivity.wasCheated(data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(KEY_INDEX,mCurrentIndex);
        outState.putBooleanArray(KEY_CHEATED,mCheated);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        if (savedInstanceState!=null)
        {
            mCurrentIndex=savedInstanceState.getInt(KEY_INDEX,0);
            mCheated=savedInstanceState.getBooleanArray(KEY_CHEATED);
        }
        else
        {
            mCheated=new boolean[mQuestionBank.length];
            Arrays.fill(mCheated,false);
        }

        mQuestionTextView=(TextView)findViewById(R.id.question_text_view);
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex=(mCurrentIndex+1)%mQuestionBank.length;
                updateQuestion();
            }
        });

        mCheatButton=(Button)findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean answer=mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent i=CheatActivity.newIntent(QuizActivity.this,answer);
                startActivityForResult(i,REQ_CODE_CHEAT);
            }
        });

        mTrueButton=(Button)findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(true);
            }
        });

        mFalseButton=(Button)findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(false);
            }
        });

        mNextButton=(Button)findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex=(mCurrentIndex+1)%mQuestionBank.length;
                updateQuestion();
            }
        });

        mPrevButton=(Button) findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex=(mCurrentIndex-1+mQuestionBank.length)%mQuestionBank.length;
                updateQuestion();
            }
        });

        updateQuestion();
    }
}
