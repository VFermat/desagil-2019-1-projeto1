package br.pro.hashi.ensino.desagil.projeto1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.LinkedList;

public class DefaultMessages extends AppCompatActivity {
    // Declarando os botões:
    private Button addDefMSg_btn;
    private Button sendMsg_btn;
    private Button upDefMsg_btn;
    private Button downDefMsg_btn;

    // Lista que guarda todas as caixas de texto que exibem as mensagens.
    private LinkedList<TextView> msgList = new LinkedList<>();

    // Lista que guarda o index da mensagem sendo mostrada.
    private LinkedList<Integer> msgListIndex = new LinkedList<>();

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
                startActivity(new Intent(DefaultMessages.this, Contacts.class));
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
        TextView listItem1_box = (TextView) findViewById(R.id.listItem1_box);
        // Mudando a mensagem mostrada.
        listItem1_box.setText(this.messages[0]);
        // Adicionando à lista de caixas de texto e à lista de indexes.
        msgList.add(listItem1_box);
        msgListIndex.add(0);

        // Caixa de texto que exibe as mensagens padrão.
        TextView listItem2_box = (TextView) findViewById(R.id.listItem2_box);
        // Mudando a mensagem mostrada.
        listItem2_box.setText(this.messages[1]);
        // Adicionando à lista de caixas de texto e à lista de indexes.
        msgList.add(listItem2_box);
        msgListIndex.add(1);

        // Caixa de texto que exibe as mensagens padrão.
        TextView listItem3_box = (TextView) findViewById(R.id.listItem3_box);
        // Mudando a mensagem mostrada.
        listItem3_box.setText(this.messages[2]);
        // Adicionando à lista de caixas de texto e à lista de indexes.
        msgList.add(listItem3_box);
        msgListIndex.add(2);

        // Caixa de texto que exibe as mensagens padrão.
        TextView listItem4_box = (TextView) findViewById(R.id.listItem4_box);
        // Mudando a mensagem mostrada.
        listItem4_box.setText(this.messages[3]);
        // Adicionando à lista de caixas de texto e à lista de indexes.
        msgList.add(listItem4_box);
        msgListIndex.add(3);

        // Caixa de texto que exibe as mensagens padrão.
        TextView listItem5_box = (TextView) findViewById(R.id.listItem5_box);
        // Mudando a mensagem mostrada.
        listItem5_box.setText(this.messages[4]);
        // Adicionando à lista de caixas de texto e à lista de indexes.
        msgList.add(listItem5_box);
        msgListIndex.add(4);
    }


    private void moveUpMsgList() {
        // Essa função sobe a lista de mensagens.

        // Loop que passa por cada caixa de texto e o index da mensagem que está mostrando.
        for (int i = 0; i <= this.msgList.size() - 1; i++) {
            // Pegando o index e a caixa de texto.
            TextView textBox = this.msgList.get(i);
            int msgIndex = this.msgListIndex.get(i);

            // Lógica que se encarrega de subir a lista.
            if (msgIndex - 1 >= 0) {
                textBox.setText(this.messages[msgIndex - 1]);
                this.msgListIndex.set(i, msgIndex - 1);
            } else {
                textBox.setText(this.messages[this.messages.length - 1]);
                this.msgListIndex.set(i, this.messages.length - 1);
            }
        }
    }


    private void moveDownMsgList() {
        // Essa função desce a lista de mensagens.

        // Loop que passa por cada caixa de texto e o index da mensagem que está mostrando.
        for (int i = 0; i <= this.msgList.size() - 1; i++) {
            // Pegando o index e a caixa de texto.
            TextView textBox = this.msgList.get(i);
            int msgIndex = this.msgListIndex.get(i);

            // Lógica que se encarrega de descer a lista.
            if (msgIndex + 1 <= this.messages.length - 1) {
                textBox.setText(this.messages[msgIndex + 1]);
                this.msgListIndex.set(i, msgIndex + 1);
            } else {
                textBox.setText(this.messages[0]);
                this.msgListIndex.set(i, 0);
            }
        }
    }
}
