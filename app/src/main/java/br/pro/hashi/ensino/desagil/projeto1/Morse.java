package br.pro.hashi.ensino.desagil.projeto1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

public class Morse extends AppCompatActivity implements ActivityConstants {

    private final Translator translator = new Translator();
    private String message;
    private String morseWord = "";
    private boolean holdMode = false;
    private boolean spaceTimerRunning = false;
    private boolean longHoldTimerRunning = false;
    private boolean avoidDoubleClick = false;
    private boolean newLetterRunning = false;
    private TextView textViewMessage;
    private TextView textViewMorseMessage;
    private Class nextActivity;
    private String phoneNumber;
    private String contactName;

    @SuppressLint("ClickableViewAccessibility")

    // Criando o objeto que contém a base de dados do Firebase.
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DatabaseReference databaseRoot = this.database.getReference();

    // Esta constante é um código que identifica o pedido de "mandar sms".
    private static final int REQUEST_SEND_SMS = 0;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_morse);

        // Pegando a mensagem/contato selecionado da última tela.
        Intent intent = getIntent();
        int callingActivity = intent.getIntExtra("callingActivity", 0);
        switch (callingActivity) {
            case MAINACITVITY:
                message = "";
                nextActivity = Contacts.class;
                break;
            case DEFAULTMESSAGESACTIVITY:
                message = intent.getStringExtra("message");
                nextActivity = Contacts.class;
                break;
            case MORSEACTIVITY:
                throw new NullPointerException();
            case CONTACTACTIVITY:
                message = "";
                phoneNumber = intent.getStringExtra("phoneNumber");
                contactName = intent.getStringExtra("contactName");
                nextActivity = null;
                break;
            case ADDCONTACTACTIVITY:
                break;
            default:
                throw new NullPointerException();
        }

        this.textViewMessage = findViewById(R.id.text_message);
        updateText();

        this.textViewMorseMessage = findViewById(R.id.morse_message);
        updateMorseText();

        Button buttonMorse = findViewById(R.id.morse);
        buttonMorse.setOnLongClickListener(longHoldListener);
        buttonMorse.setOnTouchListener(longHoldTouchListener);
        buttonMorse.setOnClickListener(clickListener);

        // Pegando o widgets.
        Button buttonSendMsg = findViewById(R.id.button_send);
        Button buttonAddMsgToList = findViewById(R.id.button_add);
        Button buttonDeleteChar = findViewById(R.id.button_delete);
        Button dic_btn = findViewById(R.id.dic_btn);

        // Cria um listener para quando o botão do dicionário é apertado.
        dic_btn.setOnClickListener(dicListener);

        // Cria um listener para quando esse botão é apertado.
        buttonSendMsg.setOnClickListener(sendMessageListener);

        // Cria um listener para quando esse botão é apertado.
        buttonAddMsgToList.setOnClickListener(addMsgToListListener);

        buttonDeleteChar.setOnClickListener(deleteCharListener);
    }

    private final View.OnClickListener dicListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(Dictionary.class);
        }
    };

    private final View.OnClickListener addMsgToListListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            databaseRoot.child("messages").child(message).setValue(message);
            startActivity(DefaultMessages.class);
        }
    };

    private final View.OnClickListener sendMessageListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (nextActivity == null) {
                if (ContextCompat.checkSelfPermission(Morse.this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {

                    if (message.isEmpty()) {
                        showToast("Mensagem inválida!");
                        return;
                    }

                    // Esta verificação do número de telefone é bem
                    // rígida, pois exige até mesmo o código do país.
                    if (!PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)) {
                        showToast("Número inválido!");
                        return;
                    }

                    // Enviar uma mensagem de SMS. Por simplicidade,
                    // não estou verificando se foi mesmo enviada,
                    // mas é possível fazer uma versão que verifica.
                    SmsManager manager = SmsManager.getDefault();
                    manager.sendTextMessage(phoneNumber, null, message, null, null);

                    // Limpar o campo para nenhum engraçadinho
                    // ficar apertando o botão várias vezes.
                    message = "";
                    updateText();
                    startActivity(MainActivity.class);

                } else {

                    // Senão, precisamos pedir essa permissão.

                    // Cria um vetor de permissões a pedir. Como queremos
                    // uma só, parece um pouco feio, mas é bem conveniente
                    // quando queremos pedir várias permissões de uma vez.
                    String[] permissions = new String[]{
                            Manifest.permission.SEND_SMS,
                    };

                    ActivityCompat.requestPermissions(Morse.this, permissions, REQUEST_SEND_SMS);
                }
            } else {
                if (!message.equals("")) {
                    startActivity(nextActivity, message);
                } else {
                    showToast("Número inválido!");
                }
            }
        }
    };

    private final View.OnClickListener deleteCharListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!message.equals("")) {
                message = message.substring(0, message.length() - 1);
            } else {
                showToast("Nada Para Deletar");
            }
            updateText();
        }
    };

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
            return true;
        }
    };

    private final CountDownTimer spaceTimer = new CountDownTimer(600, 100) {
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

    private final CountDownTimer newLetterTimer = new CountDownTimer(1500, 100) {
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

    private final CountDownTimer longHoldTimer = new CountDownTimer(900, 100) {
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

    private final CountDownTimer avoidDoubleClickTimer = new CountDownTimer(300, 100) {
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
        this.textViewMessage.setText(this.message);
    }

    private void updateMorseText() {
        this.textViewMorseMessage.setText(this.morseWord);
    }

    private void vibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(VibrationEffect.createOneShot(50, 1));
    }

    private void translateMorse() {
        try {
            char translatedMorse = translator.morseToChar(morseWord);
            message += translatedMorse;
        } catch (Exception exception) {
            showToast("Morse Invalido");
        }
        morseWord = "";
    }

    // Método de conveniência para mostrar uma bolha de texto.
    private void showToast(String text) {

        // Constrói uma bolha de duração curta.
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 0);
        // Mostra essa bolha.
        toast.show();
    }

    private void startActivity(Class c) {
        Intent intent = new Intent(Morse.this, c);
        intent.putExtra("callingActivity", MORSEACTIVITY);
        startActivity(intent);
    }

    private void startActivity(Class c, String message) {
        Intent intent = new Intent(Morse.this, c);
        intent.putExtra("callingActivity", MORSEACTIVITY);
        intent.putExtra("message", message);
        startActivity(intent);
    }
}
