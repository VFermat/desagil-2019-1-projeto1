package br.pro.hashi.ensino.desagil.projeto1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class AddContact extends AppCompatActivity {
    // Declarando os widgets dessa tela.
    private Button addContact_btn;
    private EditText contactName_txt;
    private EditText contactPhone_txt;

    // Criando o objeto que contém a base de dados do Firebase.
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseRoot = this.database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        this.contactName_txt = findViewById(R.id.contactName_txt);
        this.contactPhone_txt = findViewById(R.id.contactPhone_txt);

        // Botão que adiciona um contato novo.
        this.addContact_btn = (Button) findViewById(R.id.addContact_btn);

        // Cria um listener para quando esse botão é apertado.
        this.addContact_btn.setOnClickListener((view) -> {
            // Pegando o que o usuário escreveu:
            String contactName = this.contactName_txt.getText().toString();
            String contactPhone = this.contactPhone_txt.getText().toString();

            // Adicionando o contato à lista de contatos.
            this.databaseRoot.child("contacts").child(contactName).setValue(contactPhone);

            // Muda para a tela de contatos após o contato ser adicionado.
            startActivity(new Intent(AddContact.this, Contacts.class));
        });
    }
}
