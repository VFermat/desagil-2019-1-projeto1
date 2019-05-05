package br.pro.hashi.ensino.desagil.projeto1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Morse extends AppCompatActivity {

    private final Translator translator = new Translator();
    private String message = "";
    private String morseWord = "";
    private boolean holdMode = false;
    private boolean spaceTimerRunning = false;
    private boolean longHoldTimerRunning = false;
    private boolean avoidDoubleClick = false;
    private boolean newLetterRunning = false;
    private EditText editText;
    private EditText morseMessageEditText;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_morse);

        Intent intent = getIntent();
        this.message += intent.getStringExtra(DefaultMessages.EXTRA_MESSAGE);

        this.editText = findViewById(R.id.editText3);
        updateText();

        this.morseMessageEditText = findViewById(R.id.editText);
        updateMorseText();

        Button morseButton = findViewById(R.id.button);
        morseButton.setOnLongClickListener(longHoldListener);
        morseButton.setOnTouchListener(longHoldTouchListener);
        morseButton.setOnClickListener(clickListener);
    }

    private final View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!avoidDoubleClick & !holdMode) {
                if (newLetterRunning) {
                    newLetterTimer.cancel();
                }
                if (spaceTimerRunning) {
                    spaceTimer.cancel();
                }
                morseWord += ".";
                vibrate();
                updateMorseText();
                avoidDoubleClickTimer.start();
                newLetterTimer.start();
            }
        }
    };

    private final View.OnLongClickListener longHoldListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            longHoldTimer.start();
            if (spaceTimerRunning) {
                spaceTimer.cancel();
                spaceTimerRunning = false;
            }
            if (newLetterRunning) {
                newLetterTimer.cancel();
                newLetterRunning = false;
            }
            holdMode = true;
            return true;
        }
    };

    private final View.OnTouchListener longHoldTouchListener = new View.OnTouchListener() {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            v.onTouchEvent(event);
            // Checa se o botão foi solto
            if (event.getAction() == MotionEvent.ACTION_UP) {
                // Checa se está no modo de Hold
                if (holdMode) {
                    if (longHoldTimerRunning) {
                        longHoldTimer.cancel();
                        longHoldTimerRunning = false;
                    }
                    if (newLetterRunning) {
                        newLetterTimer.cancel();
                        newLetterRunning = false;
                    }
                    holdMode = false;
                    newLetterTimer.start();
                }
            }
            return false;
        }
    };

    private final CountDownTimer spaceTimer = new CountDownTimer(2000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            spaceTimerRunning = true;
        }

        @Override
        public void onFinish() {
            spaceTimerRunning = false;
            vibrate();
            message += " ";
            updateText();
        }
    };

    private final CountDownTimer newLetterTimer = new CountDownTimer(5000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            newLetterRunning = true;
        }

        @Override
        public void onFinish() {
            newLetterRunning = false;
            translateMorse();
            vibrate();
            updateMorseText();
            updateText();
            spaceTimer.start();
        }
    };


    private final CountDownTimer longHoldTimer = new CountDownTimer(3000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            longHoldTimerRunning = true;
        }

        @Override
        public void onFinish() {
            longHoldTimerRunning = false;
            morseWord += "-";
            vibrate();
            updateMorseText();
        }
    };

    private final CountDownTimer avoidDoubleClickTimer = new CountDownTimer(1000, 100) {
        @Override
        public void onTick(long millisUntilFinished) {
            avoidDoubleClick = true;
        }

        @Override
        public void onFinish() {
            avoidDoubleClick = false;
        }
    };

    private void updateText() {
        this.editText.setText(this.message);
    }

    private void updateMorseText() {
        this.morseMessageEditText.setText(this.morseWord);
    }

    private void vibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(VibrationEffect.createOneShot(50, 1));
    }

    private void showToast() {
        Toast toast = Toast.makeText(this, "Morse Invalido", Toast.LENGTH_LONG);
        toast.show();
    }

    private void translateMorse() {
        try {
            char translatedMorse = translator.morseToChar(morseWord);
            message += translatedMorse;
        } catch (Exception exception) {
            showToast();
        }
        morseWord = "";
    }

}
