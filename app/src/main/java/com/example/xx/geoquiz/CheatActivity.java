package com.example.xx.geoquiz;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    private static final String EXTRA_ANSWER="com.example.xx.geoquiz.answer";
    private static final String EXTRA_CHEATED="com.example.xx.geoquiz.cheated";

    private boolean mAnswer,mCheated=false,mShown=false;
    private TextView mAnswerTextView;
    private Button mShowAnswerButton;

    public static Intent newIntent(Context context,boolean answer)
    {
        Intent i=new Intent(context,CheatActivity.class);
        i.putExtra(EXTRA_ANSWER,answer);
        return i;
    }

    public static boolean wasCheated(Intent result)
    {
        return  result.getBooleanExtra(EXTRA_CHEATED,false);
    }

    private void setCheatedResult()
    {
        Intent data=new Intent();
        data.putExtra(EXTRA_CHEATED,true);
        setResult(RESULT_OK,data);
    }

    private void showAnswer()
    {
        if(mAnswer)
            mAnswerTextView.setText(R.string.true_button);
        else
            mAnswerTextView.setText(R.string.false_button);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(EXTRA_CHEATED,mCheated);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        mAnswerTextView=(TextView)findViewById(R.id.answerTextView);

        mAnswer=getIntent().getBooleanExtra(EXTRA_ANSWER,false);

        if(savedInstanceState!=null)
        {
            mCheated=savedInstanceState.getBoolean(EXTRA_CHEATED,false);
            if(mCheated)
            {
                setCheatedResult();
                showAnswer();
            }
        }

        mShowAnswerButton=(Button)findViewById(R.id.showAnswerButton);
        mShowAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mCheated)
                {
                    showAnswer();
                    setCheatedResult();
                    mCheated=true;
                }
            }
        });
    }
}
