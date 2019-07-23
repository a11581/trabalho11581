package com.example.trabalho_v2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trabalho_v2.Model.Contact;
import com.example.trabalho_v2.Model.Date;
import com.example.trabalho_v2.Model.Discipline;
import com.example.trabalho_v2.Model.Teacher;
import com.example.trabalho_v2.Model.Year;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class CreateContactActivity extends AppCompatActivity {

    //Realm declaration
    private Realm realm;
    private Button createBtn;
    private EditText startingHourEt, endingHourEt;
    private TextView usernameText;
    private Spinner daySpn, typeSpn;
    private String itemDay="";
    private String itemType="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact);

        // Views initialization
        createBtn = findViewById(R.id.buttonCreateContact);
        usernameText=findViewById(R.id.username20Id);
        startingHourEt=findViewById(R.id.startingHourId);
        endingHourEt=findViewById(R.id.endingHourId);
        daySpn=findViewById(R.id.spinnerDayId);
        typeSpn=findViewById(R.id.spinnertypeId);


        Intent received = getIntent();
        usernameText.setText(received.getStringExtra("username"));

        createBtn.setOnClickListener(v -> {
            realm.executeTransaction(r -> {

                if (isStringInt(startingHourEt.getText().toString())) {
                    if (isStringInt(endingHourEt.getText().toString())) {
                        if(!itemDay.equals("")){
                            if(!itemType.equals("")){
                                Contact contact= realm.createObject(Contact.class, UUID.randomUUID().toString());
                                contact.setStartingHour(Integer.parseInt(startingHourEt.getText().toString()));
                                contact.setEndingHour(Integer.parseInt(endingHourEt.getText().toString()));
                                contact.setDay(itemDay);
                                contact.setType(itemType);

                                // Query
                                RealmQuery<Teacher> query = realm.where(Teacher.class);
                                // Query conditions
                                query.equalTo("name",received.getStringExtra("username"));
                                // Query results
                                Teacher result = query.findFirst();

                                result.getContacts().add(contact);

                                Intent goToMenuActivity = new Intent(this, MenuActivity.class);
                                goToMenuActivity.putExtra("username", received.getStringExtra("username"));
                                startActivity(goToMenuActivity);
                            }
                            else{
                                Toast.makeText(this,"Select type",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(this,"Select day",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(this,"The ending hour is not an integer",Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(this,"The starting hour is not an integer",Toast.LENGTH_SHORT).show();
                }
            });
        });


    }
    @Override
    protected void onResume() {
        super.onResume();
        realm = Realm.getDefaultInstance();
        readDay();
        readType();
    }
    @Override
    protected void onPause() {
        super.onPause();
        realm.close();
    }
    private void readDay(){


        List<String> listDays = new ArrayList<>();
        listDays.add(0, "Choose day");
        listDays.add(1, "Monday");
        listDays.add(2, "Tuesday");
        listDays.add(3, "Wednesday");
        listDays.add(4, "Thursday");
        listDays.add(5, "Friday");
        listDays.add(6, "Saturday");
        listDays.add(7, "Sunday");


        // Style and populate the spinner
        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listDays);

        // Drop down layout style
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Attaching data adapter to spinner
        daySpn.setAdapter(dataAdapter);
        daySpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Choose day")) {
                    //do nothing
                } else {
                    //on selecting
                    itemDay = parent.getItemAtPosition(position).toString();

                    Toast.makeText(parent.getContext(), "Selected: " + itemDay, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method
            }
        });
    }

    private void readType(){


        List<String> listTypes = new ArrayList<>();
        listTypes.add(0, "Choose type");
        listTypes.add(1, "Personally");
        listTypes.add(2, "Remotely ");



        // Style and populate the spinner
        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listTypes);

        // Drop down layout style
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Attaching data adapter to spinner
        typeSpn.setAdapter(dataAdapter);
        typeSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Choose type")) {
                    //do nothing
                } else {
                    //on selecting
                    itemType = parent.getItemAtPosition(position).toString();

                    Toast.makeText(parent.getContext(), "Selected: " + itemType, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method
            }
        });
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
