package br.pro.hashi.ensino.desagil.projeto1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    // Declarando os botões:
    private Button defMsgs_btn;
    private Button newMsg_btn;
    private Button contacts_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Botão que leva a tela de mensagens padrão.
        this.defMsgs_btn = (Button) findViewById(R.id.defMsgs_btn);

        // Cria um listener para quando esse botão é apertado.
        this.defMsgs_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Muda de tela.
                startActivity(new Intent(MainActivity.this, DefaultMessages.class));
            }
        });


        // Botão que leva a tela de compor mensagens
        this.newMsg_btn = (Button) findViewById(R.id.newMsg_btn);

        // Cria um listener para quando esse botão é apertado.
        this.newMsg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Muda de tela.
                startActivity(new Intent(MainActivity.this, Morse.class));
            }
        });


        // Botão que leva a tela de contatos
        this.contacts_btn = (Button) findViewById(R.id.contacts_btn);

        // Cria um listener para quando esse botão é apertado.
        this.contacts_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Muda de tela.
                startActivity(new Intent(MainActivity.this, Contacts.class));
            }
        });
    }
}
