package br.pro.hashi.ensino.desagil.projeto1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

public class AddContacts extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contacts);

        String[] contacts = {
                "Pedro Paulo Telho",
                "Vitor Grando Eller",
                "Mohamed Ali",
                "Steve Jobs",
                "Marcelo Hashimoto",
                "Henry Rocha",
                "Bruno Cury",
                "Elon Musk",
                "Bill Gates",
                "Albert Einstein",
                "Nicola Tesla",
        };
        // Pegando o widget que faz a lista de mensagens.
        ListView contactList = (ListView) findViewById(R.id.contactList);

        // Criando um adaptador para a lista de mensagens.
        ListAdapter contactListAdapter = new ArrayAdapter<String>(contactList.getContext(), android.R.layout.simple_list_item_1, contacts);

        // Aplicando o adaptador no widget da lista de mensagens.
        contactList.setAdapter(contactListAdapter);
    }




}
