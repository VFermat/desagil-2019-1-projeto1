package br.pro.hashi.ensino.desagil.projeto1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Morse extends AppCompatActivity {
    private Button button3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_morse);

        Intent intent = getIntent();
        String message = intent.getStringExtra(DefaultMessages.EXTRA_MESSAGE);

        EditText editText = (EditText) findViewById(R.id.Phone);
        editText.setText(message);

        // Botão que leva a tela de compor mensagens
        this.button3 = (Button) findViewById(R.id.button3);

        // Cria um listener para quando esse botão é apertado.
        this.button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Muda de tela.
                startActivity(new Intent(Morse.this, AddContacts.class));
            }
        });
    }

}
