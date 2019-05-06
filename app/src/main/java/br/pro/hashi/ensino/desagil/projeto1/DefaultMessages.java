package br.pro.hashi.ensino.desagil.projeto1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    // Lista de mensagens.
    private LinkedList<String> messages = new LinkedList<>();

    // Criando o objeto que contém a base de dados do Firebase.
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseRoot = this.database.getReference();

    // Usado para passar informação para a próxima tela.
    public static final String EXTRA_MESSAGE = "br.pro.hashi.ensino.desagil.projeto1.EXTRA_MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_messages);

        // Para ler a base de dados toda vez que ela sofrer mudanças.
        databaseRoot.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Este método é chamado uma vez durante a chamada
                // de addValueEventListener e depois sempre
                // que algum valor na referência sofrer alguma mudança.

                try {
                    buildMsgList(dataSnapshot.child("messages"));

                } catch (DatabaseException exception) {
                    System.out.println("\n\n\n[FIREBASE] Failed to parse value.\n\n\n");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


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

                // Pegando a mensagem selecionada:
                String selectedMsg = getSelectedMsg();

                // Muda de tela e passa como variável para a próxima tela
                // a mensagem que foi selecionada.
                Intent intent = new Intent(DefaultMessages.this, Morse.class);
                intent.putExtra(EXTRA_MESSAGE, selectedMsg);
                startActivity(intent);
            }
        });


        // Botão que sobe a lista de mensagens.
        this.upDefMsg_btn = (Button) findViewById(R.id.upDefMsg_btn);

        // Cria um listener para quando esse botão é apertado.
        this.upDefMsg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveUpMsgList();
            }
        });


        // Botão que desce a lista de mensagens.
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
        // Adicionando à lista de caixas de texto e à lista de indexes.
        msgList.add(listItem1_box);
        msgListIndex.add(0);

        // Caixa de texto que exibe as mensagens padrão.
        TextView listItem2_box = (TextView) findViewById(R.id.listItem2_box);
        // Mudando a mensagem mostrada.
        // Adicionando à lista de caixas de texto e à lista de indexes.
        msgList.add(listItem2_box);
        msgListIndex.add(1);

        // Caixa de texto que exibe as mensagens padrão.
        TextView listItem3_box = (TextView) findViewById(R.id.listItem3_box);
        // Mudando a mensagem mostrada.
        // Adicionando à lista de caixas de texto e à lista de indexes.
        msgList.add(listItem3_box);
        msgListIndex.add(2);

        // Caixa de texto que exibe as mensagens padrão.
        TextView listItem4_box = (TextView) findViewById(R.id.listItem4_box);
        // Mudando a mensagem mostrada.
        // Adicionando à lista de caixas de texto e à lista de indexes.
        msgList.add(listItem4_box);
        msgListIndex.add(3);

        // Caixa de texto que exibe as mensagens padrão.
        TextView listItem5_box = (TextView) findViewById(R.id.listItem5_box);
        // Mudando a mensagem mostrada.
        // Adicionando à lista de caixas de texto e à lista de indexes.
        msgList.add(listItem5_box);
        msgListIndex.add(4);
    }

    private String getSelectedMsg() {
        return this.messages.get(this.msgListIndex.get(2));
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
                textBox.setText(this.messages.get(msgIndex - 1));
                this.msgListIndex.set(i, msgIndex - 1);
            } else {
                textBox.setText(this.messages.get(this.messages.size() - 1));
                this.msgListIndex.set(i, this.messages.size() - 1);
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
            if (msgIndex + 1 <= this.messages.size() - 1) {
                textBox.setText(this.messages.get(msgIndex + 1));
                this.msgListIndex.set(i, msgIndex + 1);
            } else {
                textBox.setText(this.messages.get(0));
                this.msgListIndex.set(i, 0);
            }
        }
    }

    private void buildMsgList(DataSnapshot dataSnapshot) {
        // Essa função cria a lista de mensagens padrão.

        // Percorremos todas as mensagens salvas e as adicionamos à lista de mensagens.
        for (DataSnapshot dataSnap : dataSnapshot.getChildren()) {
            String msg = dataSnap.getValue(String.class);
            this.messages.add(msg);
        }

        this.msgList.get(0).setText(this.messages.get(0));
        this.msgList.get(1).setText(this.messages.get(1));
        this.msgList.get(2).setText(this.messages.get(2));
        this.msgList.get(3).setText(this.messages.get(3));
        this.msgList.get(4).setText(this.messages.get(4));
    }
}
