package br.pro.hashi.ensino.desagil.projeto1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;

public class Dictionary extends AppCompatActivity {

    // Declarando os widgets existem nessa página.
    private ListView list_AlphaToMorse;
    private ListView list_MorseToAlpha;

    // Criando o translator.
    private final Translator translator = new Translator();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        // Pegando o widgets.
        this.list_AlphaToMorse = findViewById(R.id.list_AlphaToMorse);
        this.list_MorseToAlpha = findViewById(R.id.list_MorseToAlpha);

        // Pegando os dicionários.
        ArrayList dictAlphaToMorse = translator.dictAlphaToMorse();
        ArrayList dictMorseToAlpha = translator.dictMorseToAlpha();

        Collections.sort(dictAlphaToMorse);

        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.
        ArrayAdapter<String> arrayAdapterAlphaToMorse = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                dictAlphaToMorse );

        // Colocando o adapter na ListView.
        this.list_AlphaToMorse.setAdapter(arrayAdapterAlphaToMorse);

        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.
        ArrayAdapter<String> arrayAdapterMorseToAlpha = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                dictMorseToAlpha );

        // Colocando o adapter na ListView.
        this.list_MorseToAlpha.setAdapter(arrayAdapterMorseToAlpha);
    }
}
