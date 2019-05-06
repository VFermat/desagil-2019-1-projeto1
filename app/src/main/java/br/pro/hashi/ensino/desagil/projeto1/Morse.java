package br.pro.hashi.ensino.desagil.projeto1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Morse extends AppCompatActivity {

    // Declarando os botões:
    private EditText textMsg;
    private EditText textPhone;
    private Button sendMsg_btn;
    private Button addMsgToList_btn;

    // Criando o objeto que contém a base de dados do Firebase.
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseRoot = this.database.getReference();

    // Esta constante é um código que identifica o pedido de "mandar sms".
    private static final int REQUEST_SEND_SMS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_morse);

        // Pegando a mensagem/contato selecionado da última tela.
        Intent intent = getIntent();
        String IncomingMsg = intent.getStringExtra(DefaultMessages.EXTRA_MESSAGE);

        // Pegando o widgets.
        this.textMsg = (EditText) findViewById(R.id.text_message);
        this.textPhone = (EditText) findViewById(R.id.text_phone);
        this.sendMsg_btn = (Button) findViewById(R.id.button_send);
        this.addMsgToList_btn = (Button) findViewById(R.id.addMsgToList_btn);

        // Colocando a mensagem recebida na caixa de texto.
        this.textMsg.setText(IncomingMsg);

        // Cria um listener para quando esse botão é apertado.
        this.sendMsg_btn.setOnClickListener((view) -> {
                // Verifica se o aplicativo tem a permissão desejada.
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                    String message = this.textMsg.getText().toString();

                    if (message.isEmpty()) {
                        showToast("Mensagem inválida!");
                        return;
                    }

                    String phone = this.textPhone.getText().toString();

                    // Esta verificação do número de telefone é bem
                    // rígida, pois exige até mesmo o código do país.
                    if (!PhoneNumberUtils.isGlobalPhoneNumber(phone)) {
                        showToast("Número inválido!");
                        return;
                    }

                    // Enviar uma mensagem de SMS. Por simplicidade,
                    // não estou verificando se foi mesmo enviada,
                    // mas é possível fazer uma versão que verifica.
                    SmsManager manager = SmsManager.getDefault();
                    manager.sendTextMessage(phone, null, message, null, null);

                    // Limpar o campo para nenhum engraçadinho
                    // ficar apertando o botão várias vezes.
                    this.textPhone.setText("");

                } else {

                    // Senão, precisamos pedir essa permissão.

                    // Cria um vetor de permissões a pedir. Como queremos
                    // uma só, parece um pouco feio, mas é bem conveniente
                    // quando queremos pedir várias permissões de uma vez.
                    String[] permissions = new String[]{
                            Manifest.permission.SEND_SMS,
                    };

                    ActivityCompat.requestPermissions(this, permissions, this.REQUEST_SEND_SMS);
                }
        });

        // Cria um listener para quando esse botão é apertado.
        this.addMsgToList_btn.setOnClickListener((view) -> {
            // Pegando o que o usuário escreveu:
            String message = this.textMsg.getText().toString();

            // Adicionando o contato à lista de contatos.
            this.databaseRoot.child("messages").child(message).setValue(message);

            // Muda para a tela de contatos após o contato ser adicionado.
            startActivity(new Intent(Morse.this, DefaultMessages.class));
        });
    }

    // Método de conveniência para mostrar uma bolha de texto.
    private void showToast(String text) {

        // Constrói uma bolha de duração curta.
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);

        // Mostra essa bolha.
        toast.show();
    }
}
