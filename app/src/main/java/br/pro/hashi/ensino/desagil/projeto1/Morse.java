package br.pro.hashi.ensino.desagil.projeto1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Morse extends AppCompatActivity {

    private final Translator translator = new Translator();
    private String message = "";
    private String morseWord = "";
    private boolean holdMode = false;
    private boolean spaceTimerRunning = false;
    private boolean longHoldTimerRunning = false;
    private boolean avoidDoubleClick = false;
    private boolean newLetterRunning = false;
    private EditText editTextMessage;
    private EditText editTextMorseMessage;

    @SuppressLint("ClickableViewAccessibility")
    // Declarando os botões:
    private EditText textPhone;
    private Button buttonSendMsg;
    private Button buttonAddMsgToList;

    // Criando o objeto que contém a base de dados do Firebase.
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseRoot = this.database.getReference();

    // Esta constante é um código que identifica o pedido de "mandar sms".
    private static final int REQUEST_SEND_SMS = 0;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_morse);

        // Pegando a mensagem/contato selecionado da última tela.
        Intent intent = getIntent();
        this.message += intent.getStringExtra(DefaultMessages.EXTRA_MESSAGE);

        this.editTextMessage = findViewById(R.id.text_message);
        updateText();

        this.editTextMorseMessage = findViewById(R.id.morse_message);
        updateMorseText();

        Button morseButton = findViewById(R.id.morse);
        morseButton.setOnLongClickListener(longHoldListener);
        morseButton.setOnTouchListener(longHoldTouchListener);
        morseButton.setOnClickListener(clickListener);String IncomingMsg = intent.getStringExtra(DefaultMessages.EXTRA_MESSAGE);

        // Pegando o widgets.
        this.buttonSendMsg = (Button) findViewById(R.id.button_send);
        this.buttonAddMsgToList = (Button) findViewById(R.id.button_add);

        // Colocando a mensagem recebida na caixa de texto.

        // Cria um listener para quando esse botão é apertado.
        this.buttonSendMsg.setOnClickListener((view) -> {
            // Verifica se o aplicativo tem a permissão desejada.
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                String message = this.editTextMessage.getText().toString();

                if (message.isEmpty()) {
                    showToast("Mensagem inválida!");
                    return;
                }

                String phone = "5511987448483";

                // Esta verificação do número de telefone é bem
                // rígida, pois exige até mesmo o código do país.
                if (!PhoneNumberUtils.isGlobalPhoneNumber(phone)) {
                    showToast("Número inválido!");
                    return;
                }

                // Enviar uma mensagem de SMS. Por simplicidade,
                // não estou verificando se foi mesmo enviada,
                // mas é possível fazer uma versão que verifica.
                SmsManager manager = SmsManager.getDefault();
                manager.sendTextMessage(phone, null, message, null, null);

                // Limpar o campo para nenhum engraçadinho
                // ficar apertando o botão várias vezes.
                this.textPhone.setText("");

            } else {

                // Senão, precisamos pedir essa permissão.

                // Cria um vetor de permissões a pedir. Como queremos
                // uma só, parece um pouco feio, mas é bem conveniente
                // quando queremos pedir várias permissões de uma vez.
                String[] permissions = new String[]{
                        Manifest.permission.SEND_SMS,
                };

                ActivityCompat.requestPermissions(this, permissions, this.REQUEST_SEND_SMS);
            }
        });

        // Cria um listener para quando esse botão é apertado.
        this.buttonAddMsgToList.setOnClickListener((view) -> {
            // Pegando o que o usuário escreveu:
            String message = this.editTextMessage.getText().toString();

            // Adicionando o contato à lista de contatos.
            this.databaseRoot.child("messages").child(message).setValue(message);

            // Muda para a tela de contatos após o contato ser adicionado.
            startActivity(new Intent(Morse.this, DefaultMessages.class));
        });
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
        this.editTextMessage.setText(this.message);
    }

    private void updateMorseText() {
        this.editTextMorseMessage.setText(this.morseWord);
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

    // Método de conveniência para mostrar uma bolha de texto.
    private void showToast(String text) {

        // Constrói uma bolha de duração curta.
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);

        // Mostra essa bolha.
        toast.show();
    }
}
