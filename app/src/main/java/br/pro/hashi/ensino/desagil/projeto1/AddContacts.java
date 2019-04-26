package br.pro.hashi.ensino.desagil.projeto1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import java.util.HashMap;
import java.util.LinkedList;



public class AddContacts extends AppCompatActivity {
    private HashMap<String, String> contacts = new HashMap<String, String>();
    private Button add;
    private LinkedList<String> names = new LinkedList<>();
    private LinkedList<String> phone = new LinkedList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contacts);

        for(String i: contacts.keySet()){
            names.add(i);
            phone.add(contacts.get(i));
        }

        Intent intent = getIntent();


        String name = intent.getStringExtra("name");
        String phone1 = intent.getStringExtra("phone");
        System.out.println(name);
        System.out.println(phone1);
        names.add(name);
        phone.add(phone1);

        /*
        if(!extrasBundle.isEmpty()){
            boolean hasStringName = extrasBundle.containsKey(names.toString());
            boolean hasStringPhone = extrasBundle.containsKey(phone.toString());
            System.out.println(hasStringName);
            System.out.println(hasStringPhone);
        }
*/
        System.out.println(names);
        System.out.println(phone);
        // Pegando o widget que faz a lista de mensagens.
        ListView contactList = (ListView) findViewById(R.id.contactList);

        // Criando um adaptador para a lista de mensagens.
        ListAdapter contactListAdapter = new ArrayAdapter<String>(contactList.getContext(), android.R.layout.simple_list_item_1, names);

        // Aplicando o adaptador no widget da lista de mensagens.
        contactList.setAdapter(contactListAdapter);


        this.add = (Button) findViewById(R.id.Add_Contacts_Button);

        // Cria um listener para quando esse botão é apertado.
        this.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Muda de tela.
                startActivity(new Intent(AddContacts.this, AddContact.class));
            }
        });
    }

}
