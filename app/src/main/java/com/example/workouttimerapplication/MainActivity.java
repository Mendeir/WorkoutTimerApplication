package com.example.workouttimerapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.CountDownTimer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;
import java.util.Objects;

import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    private final long workTimeLeftInMillisReset = 0;
    private final long restTimeLeftInMillisReset = 0;
    private long workTimeLeftInMillis = 0;
    private long restTimeLeftInMillis = 0;

    private boolean workTimerRunning;
    private boolean restTimerRunning;

    private int setCounters = 1;
    private int sets = 0;
    private int workSec = 0;
    private int workMin = 0;
    private int restMin = 0;
    private int restSec = 0;

    private TextView workTextViewCountDown;
    private TextView restTextViewCountDown;
    private TextView setDisplayCounter;

    private EditText setText;

    private TextInputEditText workSecText;
    private TextInputEditText workMinText;
    private TextInputEditText restMinText;
    private TextInputEditText restSecText;

    private Button buttonStartPause;
    private Button buttonReset;
    private Button buttonSetTimer;

    private CountDownTimer workCountDownTimer;
    private CountDownTimer restCountDownTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setText = findViewById(R.id.numberOfSets);
        setDisplayCounter = findViewById(R.id.setCounter);
        workMinText = findViewById(R.id.workMinute);
        workSecText = findViewById(R.id.workSecs);
        restMinText = findViewById(R.id.restMinute);
        restSecText = findViewById(R.id.restSecs);
        workTextViewCountDown = findViewById(R.id.work_text_view_countdown);
        restTextViewCountDown = findViewById(R.id.rest_text_view_countdown);

        buttonStartPause = findViewById(R.id.button_start_pause);
        buttonReset = findViewById(R.id.button_reset);
        buttonSetTimer = findViewById(R.id.button_set);

        buttonSetTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sets = Integer.parseInt(setText.getText().toString());
                workMin = Integer.parseInt(Objects.requireNonNull(workMinText.getText()).toString());
                workSec = Integer.parseInt(Objects.requireNonNull(workSecText.getText()).toString());
                restMin = Integer.parseInt(Objects.requireNonNull(restMinText.getText()).toString());
                restSec = Integer.parseInt(Objects.requireNonNull(restSecText.getText()).toString());
                workTimeLeftInMillis = (workMin * 60000L) + (workSec * 1000L);
                restTimeLeftInMillis = (restMin * 60000L) + (restSec * 1000L);
                buttonSetTimer.setVisibility(View.INVISIBLE);
            }
        });

        buttonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (workTimerRunning) {
                    workPauseTimer();
                }
                else if(restTimerRunning){
                    restPauseTimer();
                }
                else {
                    setText.setVisibility(View.INVISIBLE);
                    setDisplayCounter.setVisibility(View.VISIBLE);
                    workMinText.setVisibility(View.INVISIBLE);
                    workSecText.setVisibility(View.INVISIBLE);
                    restMinText.setVisibility(View.INVISIBLE);
                    restSecText.setVisibility(View.INVISIBLE);

                    if (!workTimerRunning) {
                        startWorkTimer();

                    }else if (restTimerRunning) {
                        startRestTimer();
                    }
                }

            }
        });

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
                workTextViewCountDown.setVisibility(View.INVISIBLE);
                restTextViewCountDown.setVisibility(View.INVISIBLE);
            }
        });

    }

    private void startWorkTimer() {
        workCountDownTimer = new CountDownTimer(workTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                workTimeLeftInMillis = millisUntilFinished;
                workUpdateCountDownText();
            }

            @Override
            public void onFinish() {
                workTimerRunning = false;
                startRestTimer();
                buttonStartPause.setText("Pause");
                buttonStartPause.setVisibility(View.VISIBLE);
                buttonReset.setVisibility(View.VISIBLE);
            }
        }.start();
        workTimerRunning = true;
        workTextViewCountDown.setVisibility(View.VISIBLE);
        restTextViewCountDown.setVisibility(View.INVISIBLE);
        buttonStartPause.setText("pause");

    }

    private void startRestTimer() {
        restCountDownTimer = new CountDownTimer(restTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                restTimeLeftInMillis = millisUntilFinished;
                restUpdateCountDownText();
            }

            @Override
            public void onFinish() {
                restTimerRunning = false;
                buttonStartPause.setText("Pause");
                buttonStartPause.setVisibility(View.VISIBLE);
                buttonReset.setVisibility(View.VISIBLE);
                resetSetTimer();
                if(setCounters != sets){
                    setCounters++;
                    startWorkTimer();
                }
            }
        }.start();
        restTimerRunning = true;
        workTextViewCountDown.setVisibility(View.INVISIBLE);
        restTextViewCountDown.setVisibility(View.VISIBLE);
        buttonStartPause.setText("pause");
    }

    private void workPauseTimer() {
        workCountDownTimer.cancel();

        workTimerRunning = false;
        buttonStartPause.setText("Start");
        buttonReset.setVisibility(View.VISIBLE);
    }

    private void restPauseTimer() {
        restCountDownTimer.cancel();

        restTimerRunning = false;
        buttonStartPause.setText("Start");
        buttonReset.setVisibility(View.VISIBLE);
    }

    private void resetTimer() {
        sets = 0;
        workTimeLeftInMillis = workTimeLeftInMillisReset;
        restTimeLeftInMillis = restTimeLeftInMillisReset;

        setText.setVisibility(View.VISIBLE);
        buttonSetTimer.setVisibility(View.VISIBLE);
        setDisplayCounter.setVisibility(View.INVISIBLE);
        workMinText.setVisibility(View.VISIBLE);
        workSecText.setVisibility(View.VISIBLE);
        restMinText.setVisibility(View.VISIBLE);
        restSecText.setVisibility(View.VISIBLE);
        workTextViewCountDown.setVisibility(View.INVISIBLE);
        restTextViewCountDown.setVisibility(View.INVISIBLE);
        buttonStartPause.setText("Start");
        buttonStartPause.setVisibility(View.VISIBLE);
    }

    private void workUpdateCountDownText() {
        int workMinutes = (int) (workTimeLeftInMillis / 1000) / 60;
        int workSeconds = (int) (workTimeLeftInMillis / 1000) % 60;

        String workTimeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", workMinutes, workSeconds);
        setDisplayCounter.setText(String.valueOf(setCounters));
        workTextViewCountDown.setText(workTimeLeftFormatted);
    }

    private void restUpdateCountDownText() {
        int restMinutes = (int) (restTimeLeftInMillis / 1000) / 60;
        int restSeconds = (int) (restTimeLeftInMillis / 1000) % 60;

        String restTimeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", restMinutes, restSeconds);
        restTextViewCountDown.setText(restTimeLeftFormatted);
    }

    private void resetSetTimer(){
        workTimeLeftInMillis = (workMin * 60000L) + (workSec * 1000L);
        restTimeLeftInMillis = (restMin * 60000L) + (restSec * 1000L);
        buttonStartPause.setVisibility(View.VISIBLE);
    }
}