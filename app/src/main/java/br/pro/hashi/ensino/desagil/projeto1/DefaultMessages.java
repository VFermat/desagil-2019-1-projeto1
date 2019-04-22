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
import android.widget.Toast;

public class DefaultMessages extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "br.pro.hashi.ensino.desagil.projeto1.EXTRA_MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_messages);

        // Botão que leva a tela principal.
        Button returnBTN = (Button) findViewById(R.id.return_btn);

        // Cria um listener para quando esse botão é apertado.
        returnBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Muda de tela.
                startActivity(new Intent(DefaultMessages.this, MainActivity.class));
            }
        });

        // Lista de mensagens. Poderiamos usar um arquivo JSON
        // para guardar as mensagens ou o Firebase.
        String[] messages = {
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

        // Pegando o widget que faz a lista de mensagens.
        ListView messageList = (ListView) findViewById(R.id.messageList);

        // Criando um adaptador para a lista de mensagens.
        ListAdapter messageListAdapter = new ArrayAdapter<String>(messageList.getContext(), android.R.layout.simple_list_item_1, messages);

        // Aplicando o adaptador no widget da lista de mensagens.
        messageList.setAdapter(messageListAdapter);

        // Criando um listener para quando o usuário clicar em uma mensagem.
        messageList.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // Dentro dessa função mudariamos para a tela de mensagem com a mensagem
                        // selecionada já escrita.
                        // Por enquanto apenas printamos o que o usuário selecionou.

                        // Pega a mensagem que foi seleconada.
                        String message = String.valueOf(parent.getItemAtPosition(position));

                        // Muda de tela e passa como variável para a próxima tela
                        // a mensagem que foi selecionada.
                        Intent intent = new Intent(DefaultMessages.this, Morse.class);
                        intent.putExtra(EXTRA_MESSAGE, message);
                        startActivity(intent);
                    }
                }
        );
    }
}
