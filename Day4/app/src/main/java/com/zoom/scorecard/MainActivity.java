package com.zoom.scorecard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    int ScoreA = 0;
    int ScoreB = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void Score3A(View v) {
        ScoreA = ScoreA + 3;
        displayScore(ScoreA);

    }

    public void Score2A(View v) {
        ScoreA = ScoreA + 2;
        displayScore(ScoreA);

    }

    public void Score1A(View v) {
        ScoreA = ScoreA + 1;
        displayScore(ScoreA);

    }

    public void Score3B(View v) {
        ScoreB = ScoreB + 3;
        display(ScoreB);

    }

    public void Score2B(View v) {
        ScoreB = ScoreB + 2;
        display(ScoreB);

    }

    public void Score1B(View v) {
        ScoreB = ScoreB + 1;
        display(ScoreB);

    }

    public void Reset(View v) {
        ScoreA = 0;
        ScoreB = 0;
        displayScore(ScoreA);
        display(ScoreB);
    }


        public void displayScore(int score) {
            TextView ScoreA = (TextView) findViewById(R.id.zero_A);
            ScoreA.setText(String.valueOf(score));
    }
    public void display(int score) {
        TextView ScoreB = (TextView) findViewById(R.id.zero_B);
        ScoreB.setText(String.valueOf(score));
    }


}
