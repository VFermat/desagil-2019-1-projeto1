package br.pro.hashi.ensino.desagil.projeto1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DefaultMessages extends AppCompatActivity {

    // Declarando os botões:
    private Button addDefMSg_btn;
    private Button sendMsg_btn;
    private Button upDefMsg_btn;
    private Button downDefMsg_btn;

    // Declarando as caixas de texto que exibem a lista de mensagens:
    private TextView listItem1_box;
    private TextView listItem2_box;
    private TextView listItem3_box;
    private TextView listItem4_box;
    private TextView listItem5_box;

    // Lista de mensagens. Poderiamos usar um arquivo JSON
    // para guardar as mensagens ou o Firebase.
    private String[] messages = {
            "Olá, tudo bem?",
            "Como você vai?",
            "Obrigado.",
            "Ok.",
            "Vejo isso depois.",
            "Bom dia!",
            "Boa noite!",
            "Boa tarde!",
            "Vamos sair?",
            "O que vai fazer mais tarde?",
            "Está livre esse fim de semana?"};

    private int currentMsgSelected;

    // Usado para passar informação para a próxima tela.
    public static final String EXTRA_MESSAGE = "br.pro.hashi.ensino.desagil.projeto1.EXTRA_MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_messages);

        // Botão que adiciona uma nova mensagem padrão.
        this.addDefMSg_btn = (Button) findViewById(R.id.addDefMsg_btn);

        // Cria um listener para quando esse botão é apertado.
        this.addDefMSg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Muda de tela.
                startActivity(new Intent(DefaultMessages.this, Morse.class));
            }
        });


        // Botão que envia a mensagem selecionada.
        this.sendMsg_btn = (Button) findViewById(R.id.sendMsg_btn);

        // Cria um listener para quando esse botão é apertado.
        this.sendMsg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Muda de tela.
                startActivity(new Intent(DefaultMessages.this, AddContacts.class));
            }
        });


        // Botão que envia a mensagem selecionada.
        this.upDefMsg_btn = (Button) findViewById(R.id.upDefMsg_btn);

        // Cria um listener para quando esse botão é apertado.
        this.upDefMsg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveUpMsgList();
            }
        });


        // Botão que envia a mensagem selecionada.
        this.downDefMsg_btn = (Button) findViewById(R.id.downDefMsg_btn);

        // Cria um listener para quando esse botão é apertado.
        this.downDefMsg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveDownMsgList();
            }
        });


        // Caixa de texto que exibe as mensagens padrão.
        this.listItem1_box = (TextView) findViewById(R.id.listItem1_box);
        this.listItem1_box.setText(this.messages[0]);

        this.listItem2_box = (TextView) findViewById(R.id.listItem2_box);
        this.listItem2_box.setText(this.messages[1]);

        this.listItem3_box = (TextView) findViewById(R.id.listItem3_box);
        this.listItem3_box.setText(this.messages[2]);

        this.listItem4_box = (TextView) findViewById(R.id.listItem4_box);
        this.listItem4_box.setText(this.messages[3]);

        this.listItem5_box = (TextView) findViewById(R.id.listItem5_box);
        this.listItem5_box.setText(this.messages[4]);

        this.currentMsgSelected = 2;
    }


    private void moveUpMsgList() {
        this.listItem3_box.setText(this.messages[this.currentMsgSelected - 1]);

        this.currentMsgSelected -= 1;
    }


    private void moveDownMsgList() {
        this.listItem3_box.setText(this.messages[this.currentMsgSelected + 1]);

        this.currentMsgSelected += 1;
    }
}
