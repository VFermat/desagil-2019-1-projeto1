package br.pro.hashi.ensino.desagil.projeto1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Morse extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_morse);

        Intent intent = getIntent();
        String message = intent.getStringExtra(DefaultMessages.EXTRA_MESSAGE);

        EditText editText = (EditText) findViewById(R.id.editText3);
        editText.setText(message);
    }

}
