package br.pro.hashi.ensino.desagil.projeto1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class AddContact extends AppCompatActivity implements ActivityConstants {
    private TextView contactNameTxt;
    private TextView contactPhoneTxt;
    private TextView morseTxt;
    private TextView focusTxt;
    private String message;
    private String morseMessage;
    private Translator translator;
    private boolean holdMode = false;
    private boolean spaceTimerRunning = false;
    private boolean longHoldTimerRunning = false;
    private boolean avoidDoubleClick = false;
    private boolean newLetterRunning = false;

    // Criando o objeto que contém a base de dados do Firebase.
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DatabaseReference databaseRoot = this.database.getReference();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        this.contactNameTxt = findViewById(R.id.contactName_txt);
        this.contactPhoneTxt = findViewById(R.id.contactPhone_txt);
        this.morseTxt = findViewById(R.id.morse_txt);
        this.translator = new Translator();
        this.focusTxt = contactNameTxt;
        contactNameTxt.setBackgroundColor(Color.CYAN);
        this.message = "";

        // Botão que adiciona um contato novo.
        // Declarando os widgets dessa tela.
        Button addContactBtn = findViewById(R.id.addContact_btn);

        // Cria um listener para quando esse botão é apertado.
        addContactBtn.setOnClickListener(addContactListener);

        // Botão que sobe para o campo de Nome.
        Button nameFieldMBtn = findViewById(R.id.nameFieldM_btn);

        nameFieldMBtn.setOnClickListener(v -> {
            message = "" + contactNameTxt.getText();
            morseMessage = "";
            focusTxt = contactNameTxt;
            contactNameTxt.setBackgroundColor(Color.CYAN);
            contactPhoneTxt.setBackgroundColor(Color.WHITE);
        });
        // Botão que sobe para o campo de telefone.
        Button phoneFieldMBtn = findViewById(R.id.phoneFieldM_btn);

        phoneFieldMBtn.setOnClickListener(v -> {
            message = "" + contactPhoneTxt.getText();
            morseMessage = "";
            focusTxt = contactPhoneTxt;
            contactPhoneTxt.setBackgroundColor(Color.CYAN);
            contactNameTxt.setBackgroundColor(Color.WHITE);
        });

        Button morseBtn = findViewById(R.id.morse_button);
        morseBtn.setOnClickListener(clickListener);
        morseBtn.setOnLongClickListener(longHoldListener);
        morseBtn.setOnTouchListener(longHoldTouchListener);

        Button deleteBtn = findViewById(R.id.delWordContact);
        deleteBtn.setOnClickListener(deleteCharListener);
    }

    private final View.OnClickListener deleteCharListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String nMessage = "" + focusTxt.getText();
            if (!nMessage.equals("")) {
                message = nMessage.substring(0, nMessage.length() - 1);
            } else {
                showToast("Nada Para Deletar");
            }
            updateFocusText();
        }
    };

    private final View.OnClickListener addContactListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Pegando o que o usuário escreveu:
            String contactName = contactNameTxt.getText().toString();
            String contactPhone = contactPhoneTxt.getText().toString();
            if (!PhoneNumberUtils.isGlobalPhoneNumber(contactPhone)) {
                showToast("Telefone Inválido");
                contactPhoneTxt.setText("");
                return;
            }
            if (contactName.equals("")) {
                showToast("Nome Inválido");
                contactNameTxt.setText("");
                return;
            }

            // Adicionando o contato à lista de contatos.
            databaseRoot.child("contacts").child(contactName).setValue(contactPhone);

            // Muda para a tela de contatos após o contato ser adicionado.
            startActivity(new Intent(AddContact.this, Contacts.class));
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
                morseMessage += ".";
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
            updateFocusText();
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
            updateFocusText();
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
            morseMessage += "-";
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

    private void updateFocusText() {
        focusTxt.setText(message);
    }

    private void updateMorseText() {
        morseTxt.setText(morseMessage);
    }

    private void translateMorse() {
        try {
            char translatedMorse = translator.morseToChar(morseMessage);
            message += translatedMorse;
        } catch (Exception exception) {
            showToast("Morse Invalido");
        }
        morseMessage = "";
    }

    // Método de conveniência para mostrar uma bolha de texto.
    private void showToast(String text) {

        // Constrói uma bolha de duração curta.
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 0);
        // Mostra essa bolha.
        toast.show();
    }

    private void vibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(VibrationEffect.createOneShot(50, 1));
    }

}