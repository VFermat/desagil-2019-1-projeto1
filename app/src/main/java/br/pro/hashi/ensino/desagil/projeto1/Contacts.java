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

public class Contacts extends AppCompatActivity {
    // Declarando os botões:
    private Button addContact_btn;
    private Button sendToContact_btn;
    private Button upContact_btn;
    private Button downContact_btn;

    // Lista que guarda todas as caixas de texto que exibem as mensagens.
    private LinkedList<TextView> contactList = new LinkedList<>();

    // Lista que guarda o index da mensagem sendo mostrada.
    private LinkedList<Integer> contactListIndex = new LinkedList<>();

    // Lista de mensagens.
    private LinkedList<String> contacts = new LinkedList<>();

    // Criando o objeto que contém a base de dados do Firebase.
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseRoot = this.database.getReference();

    // Usado para passar informação para a próxima tela.
    public static final String EXTRA_MESSAGE = "br.pro.hashi.ensino.desagil.projeto1.EXTRA_MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        // Para ler a base de dados toda vez que ela sofrer mudanças.
        databaseRoot.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Este método é chamado uma vez durante a chamada
                // de addValueEventListener e depois sempre
                // que algum valor na referência sofrer alguma mudança.

                try {
                    buildContactList(dataSnapshot.child("contacts"));

                } catch (DatabaseException exception) {
                    System.out.println("\n\n\n[FIREBASE] Failed to parse value.\n\n\n");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        // Botão que adiciona um novo contato.
        this.addContact_btn = (Button) findViewById(R.id.addContact_btn);

        // Cria um listener para quando esse botão é apertado.
        this.addContact_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Muda de tela.
                startActivity(new Intent(Contacts.this, AddContact.class));
            }
        });

        // Botão para enviar uma mensagem para o contato selecionado.
        this.sendToContact_btn = (Button) findViewById(R.id.sendToContact_btn);

        // Cria um listener para quando esse botão é apertado.
        this.sendToContact_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pegando o contato selecionada:
                String selectedMsg = getSelectedContact();

                // Muda de tela e passa como variável para a próxima tela
                // a mensagem que foi selecionada.
                Intent intent = new Intent(Contacts.this, Morse.class);
                intent.putExtra(EXTRA_MESSAGE, selectedMsg);
                startActivity(intent);
            }
        });

        // Botão que sobe a lista de mensagens.
        this.upContact_btn = (Button) findViewById(R.id.upContact_btn);

        // Cria um listener para quando esse botão é apertado.
        this.upContact_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveUpContactList();
            }
        });

        // Botão que desce a lista de mensagens.
        this.downContact_btn = (Button) findViewById(R.id.downContact_btn);

        // Cria um listener para quando esse botão é apertado.
        this.downContact_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveDownContactList();
            }
        });

        // Caixa de texto que exibe as mensagens padrão.
        TextView listItem1_box = (TextView) findViewById(R.id.listItem1_box);
        // Mudando a mensagem mostrada.
        // Adicionando à lista de caixas de texto e à lista de indexes.
        contactList.add(listItem1_box);
        contactListIndex.add(0);

        // Caixa de texto que exibe as mensagens padrão.
        TextView listItem2_box = (TextView) findViewById(R.id.listItem2_box);
        // Mudando a mensagem mostrada.
        // Adicionando à lista de caixas de texto e à lista de indexes.
        contactList.add(listItem2_box);
        contactListIndex.add(1);

        // Caixa de texto que exibe as mensagens padrão.
        TextView listItem3_box = (TextView) findViewById(R.id.listItem3_box);
        // Mudando a mensagem mostrada.
        // Adicionando à lista de caixas de texto e à lista de indexes.
        contactList.add(listItem3_box);
        contactListIndex.add(2);

        // Caixa de texto que exibe as mensagens padrão.
        TextView listItem4_box = (TextView) findViewById(R.id.listItem4_box);
        // Mudando a mensagem mostrada.
        // Adicionando à lista de caixas de texto e à lista de indexes.
        contactList.add(listItem4_box);
        contactListIndex.add(3);

        // Caixa de texto que exibe as mensagens padrão.
        TextView listItem5_box = (TextView) findViewById(R.id.listItem5_box);
        // Mudando a mensagem mostrada.
        // Adicionando à lista de caixas de texto e à lista de indexes.
        contactList.add(listItem5_box);
        contactListIndex.add(4);
    }

    private void moveDownContactList() {
        // Essa função desce a lista de contatos.

        // Loop que passa por cada caixa de texto e o index do contato que está
        // mostrando.
        for (int i = 0; i <= this.contactList.size() - 1; i++) {
            // Pegando o index e a caixa de texto.
            TextView textBox = this.contactList.get(i);
            int msgIndex = this.contactListIndex.get(i);

            // Lógica que se encarrega de descer a lista.
            if (msgIndex + 1 <= this.contacts.size() - 1) {
                textBox.setText(this.contacts.get(msgIndex + 1));
                this.contactListIndex.set(i, msgIndex + 1);
            } else {
                textBox.setText(this.contacts.get(0));
                this.contactListIndex.set(i, 0);
            }
        }
    }

    private void moveUpContactList() {
        // Essa função sobe a lista de contatos.

        // Loop que passa por cada caixa de texto e o index do contato que está
        // mostrando.
        for (int i = 0; i <= this.contactList.size() - 1; i++) {
            // Pegando o index e a caixa de texto.
            TextView textBox = this.contactList.get(i);
            int msgIndex = this.contactListIndex.get(i);

            // Lógica que se encarrega de subir a lista.
            if (msgIndex - 1 >= 0) {
                textBox.setText(this.contacts.get(msgIndex - 1));
                this.contactListIndex.set(i, msgIndex - 1);
            } else {
                textBox.setText(this.contacts.get(this.contacts.size() - 1));
                this.contactListIndex.set(i, this.contacts.size() - 1);
            }
        }
    }

    private String getSelectedContact() {
        return this.contacts.get(this.contactListIndex.get(2));
    }

    private void buildContactList(DataSnapshot dataSnapshot) {
        // Essa função cria a lista de mensagens padrão.

        // Percorremos todas as mensagens salvas e as adicionamos à lista de mensagens.
        for (DataSnapshot dataSnap : dataSnapshot.getChildren()) {
            String msg = dataSnap.getKey();
            this.contacts.add(msg);
        }

        this.contactList.get(0).setText(this.contacts.get(0));
        this.contactList.get(1).setText(this.contacts.get(1));
        this.contactList.get(2).setText(this.contacts.get(2));
        this.contactList.get(3).setText(this.contacts.get(3));
        this.contactList.get(4).setText(this.contacts.get(4));
    }
}
