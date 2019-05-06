package br.pro.hashi.ensino.desagil.projeto1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class AddContact extends AppCompatActivity {
    private Button buttonAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        EditText textName = findViewById(R.id.Contact);
        EditText textPhone = findViewById(R.id.Phone);

        // Botão que adiciona um numero novo
        this.buttonAdd = (Button) findViewById(R.id.buttonAdd);

        // Cria um listener para quando esse botão é apertado.
        this.buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Intent changeScreen = new Intent(AddContact.this, AddContacts.class);
                Bundle bundle = new Bundle();
                bundle.putString(textName.toString(), textPhone.toString());
                changeScreen.putExtras(bundle);
                */
                Intent i = new Intent(AddContact.this, AddContact.class);
                i.putExtra("name", textName.toString());
                i.putExtra("phone", textPhone.toString());
                startActivity(i);
                startActivity(new Intent(AddContact.this, AddContact.class));
            }
        });
    }
}
