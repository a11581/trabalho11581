package com.example.trabalho_v2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MenuActivity extends AppCompatActivity {

    private Button disciplineBtn, yearBtn, couseBtn, classBtn, mailBtn, contactBtn, loginBtn;
    private TextView usernameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        disciplineBtn=findViewById(R.id.buttonDiscipline);
        yearBtn=findViewById(R.id.buttonYear);
        couseBtn=findViewById(R.id.buttonCourse);
        classBtn=findViewById(R.id.buttonClasses);
        usernameText=findViewById(R.id.usernameId);
        mailBtn=findViewById(R.id.buttonMail);
        contactBtn=findViewById(R.id.buttonContact);
        loginBtn=findViewById(R.id.buttonLogin);
        Intent received = getIntent();
        usernameText.setText(received.getStringExtra("username"));

        disciplineBtn.setOnClickListener(v -> {
            Intent goToDisciplineActivity = new Intent(this, ReadDisciplineActivity.class);
            goToDisciplineActivity.putExtra("username", received.getStringExtra("username"));
            startActivity(goToDisciplineActivity);
        });
        yearBtn.setOnClickListener(v -> {
            Intent goToYearActivity = new Intent(this, ReadYearActivity.class);
            goToYearActivity.putExtra("username", received.getStringExtra("username"));
            startActivity(goToYearActivity);
        });
        couseBtn.setOnClickListener(v -> {
            Intent goToCourseActivity = new Intent(this, ReadCourseActivity.class);
            goToCourseActivity.putExtra("username", received.getStringExtra("username"));
            startActivity(goToCourseActivity);
        });
        classBtn.setOnClickListener(v -> {
            Intent goToClassActivity = new Intent(this, ReadClassActivity.class);
            goToClassActivity.putExtra("username", received.getStringExtra("username"));
            startActivity(goToClassActivity);
        });
        mailBtn.setOnClickListener(v -> {
            Intent goToMailActivity = new Intent(this, MailActivity.class);
            goToMailActivity.putExtra("username", received.getStringExtra("username"));
            startActivity(goToMailActivity);
        });
        contactBtn.setOnClickListener(v -> {
            Intent goToContactActivity = new Intent(this,ReadContactActivity.class);
            goToContactActivity.putExtra("username", received.getStringExtra("username"));
            startActivity(goToContactActivity);
        });
        loginBtn.setOnClickListener(v -> {
            Intent goToLoginActivity = new Intent(this,MainActivity.class);
            startActivity(goToLoginActivity);
        });
    }
}
