package com.example.trabalho_v2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.trabalho_v2.Model.Discipline;

import java.util.ArrayList;
import java.util.List;

public class MailActivity extends AppCompatActivity {
    private EditText mEditTextTo;
    private EditText mEditTextSubject;
    private EditText mEditTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail);

        mEditTextTo = findViewById(R.id.edit_text_to);
        mEditTextSubject = findViewById(R.id.edit_text_subject);
        mEditTextMessage = findViewById(R.id.edit_text_message);

        Intent received = getIntent();

        if(received.getStringExtra("date")==null) {
            mEditTextMessage.setText("");
            List<String> listStudents = new ArrayList<>();
            listStudents.add(0, "student@gmail.com");
            listStudents.add(1, "student1@gmail.com");
            listStudents.add(2, "student2@gmail.com");
            listStudents.add(3, "student3@gmail.com");

            String emails ="";

            for (int i = 0; i < listStudents.size(); i++) {
                emails= emails + listStudents.get(i) + ",";
            }

            mEditTextTo.setText(emails);
        }
        else{
            List<String> listStudents = new ArrayList<>();
            listStudents.add(0, "student@gmail.com");
            listStudents.add(1, "student1@gmail.com");
            listStudents.add(2, "student2@gmail.com");
            listStudents.add(3, "student3@gmail.com");

            List<String> listAdministrative = new ArrayList<>();
            listAdministrative.add(0, "administrative@gmail.com");

            String emails ="";

            for (int i = 0; i < listStudents.size(); i++) {
                emails= emails + listStudents.get(i) + ",";
            }
            for (int i = 0; i < listAdministrative.size(); i++) {
                emails= emails + listAdministrative.get(i) + ",";
            }

            mEditTextTo.setText(emails);
            mEditTextMessage.setText("Old date: " + received.getStringExtra("date") + "\nNew date: " + received.getStringExtra("newDate") + " Room: " + received.getStringExtra("newRoom") + " Starting at " + received.getStringExtra("newStarting") + " hour");
        }

        Button buttonSend = findViewById(R.id.button_send);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMail();
            }
        });
    }

    private void sendMail() {

        String recipientList = mEditTextTo.getText().toString();
        String[] recipients = recipientList.split(",");


        String subject = mEditTextSubject.getText().toString();

        String message = mEditTextMessage.getText().toString();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, recipients);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);

        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Choose an email client"));
    }
}
