package br.pro.hashi.ensino.desagil.projeto1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

public class MainActivity extends AppCompatActivity implements ActivityConstants {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Botão que leva a tela de mensagens padrão.
        // Declarando os botões:
        Button defMsgsBtn = findViewById(R.id.defMsgs_btn);

        // Cria um listener para quando esse botão é apertado.
        defMsgsBtn.setOnClickListener(v -> {
            // Muda de tela.
            startActivity(DefaultMessages.class);
        });


        // Botão que leva a tela de compor mensagens
        Button newMsgBtn = findViewById(R.id.newMsg_btn);

        // Cria um listener para quando esse botão é apertado.
        newMsgBtn.setOnClickListener(v -> {
            // Muda de tela.
            startActivity(Morse.class);
        });


        // Botão que leva a tela de contatos
        Button contactsBtn = findViewById(R.id.contacts_btn);

        // Cria um listener para quando esse botão é apertado.
        contactsBtn.setOnClickListener(v -> {
            // Muda de tela.
            startActivity(Contacts.class);
        });
    }

    private void startActivity(Class c) {
        Intent intent = new Intent(MainActivity.this, c);
        intent.putExtra("callingActivity", ActivityConstants.MAINACITVITY);
        startActivity(intent);
    }
}
