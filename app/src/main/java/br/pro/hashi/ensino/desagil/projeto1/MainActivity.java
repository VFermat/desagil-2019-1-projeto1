package br.pro.hashi.ensino.desagil.projeto1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Botão que leva a tela de mensagens padrão.
        Button defaultMessagesBTN = (Button) findViewById(R.id.default_messages_btn);

        // Cria um listener para quando esse botão é apertado.
        defaultMessagesBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Muda de tela.
                startActivity(new Intent(MainActivity.this, DefaultMessages.class));
            }
        });
    }
}
