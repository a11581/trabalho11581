package com.example.trabalho_v2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trabalho_v2.Model.Course;
import com.example.trabalho_v2.Model.Discipline;
import com.example.trabalho_v2.Model.Teacher;
import com.example.trabalho_v2.Model.Year;

import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmQuery;

public class CreateYearActivity extends AppCompatActivity {

    //Realm declaration
    private Realm realm;

    // Views declaration
    private Button createBtn;
    private EditText yearEt;
    private TextView usernameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_year);

        // Views initialization
        createBtn = findViewById(R.id.buttonCreateYear);
        yearEt = findViewById(R.id.yearId);
        usernameText=findViewById(R.id.username6Id);
        Intent received = getIntent();
        usernameText.setText(received.getStringExtra("username"));

        // Register button
        // Need more verifications
        createBtn.setOnClickListener(v -> {
            if (isStringInt(this.yearEt.getText().toString())) {
                // Query
                RealmQuery<Year> query = realm.where(Year.class);
                // Query conditions
                query.equalTo("yearDate", Integer.parseInt(this.yearEt.getText().toString()));
                // Query results
                Year result = query.findFirst();

                if(result==null){

                    Intent goToMenuActivity = new Intent(this, MenuActivity.class);

                    realm.executeTransaction(r -> {
                        Year year = realm.createObject(Year.class, UUID.randomUUID().toString());
                        //year.setYearDate(Integer.parseInt(yearEt.getText().toString()));
                        year.setYearDate(Integer.parseInt(yearEt.getText().toString()));
                        year.setUsername(received.getStringExtra("username"));
                    });


                    goToMenuActivity.putExtra("username", received.getStringExtra("username"));
                    startActivity(goToMenuActivity);
                }
                else{
                    Toast.makeText(this,"This year already exist",Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(this,"The year is not an integer",Toast.LENGTH_SHORT).show();
            }
        });

    }
    @Override
    protected void onResume() {
        super.onResume();
        realm = Realm.getDefaultInstance();
    }
    @Override
    protected void onPause() {
        super.onPause();
        realm.close();
    }
    private boolean isStringInt(String s)
    {
        try
        {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException ex)
        {
            return false;
        }
    }
}
