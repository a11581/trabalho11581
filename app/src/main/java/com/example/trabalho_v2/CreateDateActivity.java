package com.example.trabalho_v2;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trabalho_v2.Model.Class;
import com.example.trabalho_v2.Model.Date;
import com.example.trabalho_v2.Model.Discipline;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmQuery;

public class CreateDateActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    //Realm declaration
    private Realm realm;
    private Button createBtn;
    private EditText descriptionEt;
    private TextView usernameText;
    private String discipline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_date);

        // Views initialization
        createBtn = findViewById(R.id.buttonCreateDate);
        usernameText=findViewById(R.id.username16Id);
        descriptionEt=findViewById(R.id.descriptionId);
        TextView date = (TextView) findViewById(R.id.dateId);


        Intent received = getIntent();
        usernameText.setText(received.getStringExtra("username"));
        String currentString = received.getStringExtra("discipline");
        String[] separated = currentString.split(": ");
        discipline=separated[1];


        Button button = (Button) findViewById(R.id.buttonDatePicker);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker=new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"Date Picker");
            }
        });
        createBtn.setOnClickListener(v -> {

            boolean disciplineDateIsNotEmpty = !date.getText().toString().equals("Date");
            if(disciplineDateIsNotEmpty){
                realm.executeTransaction(r -> {
                    Date importantDate= realm.createObject(Date.class, UUID.randomUUID().toString());
                    importantDate.setDate(date.getText().toString());
                    importantDate.setDescription(descriptionEt.getText().toString());

                    // Query
                    RealmQuery<Discipline> query = realm.where(Discipline.class);
                    // Query conditions
                    query.equalTo("name", discipline);
                    // Query results
                    Discipline result = query.findFirst();

                    result.getDates().add(importantDate);

                });

                Intent goToMenuActivity = new Intent(this, MenuActivity.class);
                goToMenuActivity.putExtra("username", received.getStringExtra("username"));
                startActivity(goToMenuActivity);
            }else {
                Toast.makeText(this,"Choose Date",Toast.LENGTH_SHORT).show();
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
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        String currentDateString= DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        TextView textView = (TextView) findViewById(R.id.dateId);
        textView.setText(currentDateString);
    }
}
