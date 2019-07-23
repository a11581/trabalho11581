package com.example.trabalho_v2;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trabalho_v2.Model.Class;
import com.example.trabalho_v2.Model.Discipline;
import com.example.trabalho_v2.Model.Teacher;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class ClassDetailsActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    //Realm declaration
    private Realm realm;

    private String itemDiscipline;

    private CheckBox emailCb, specialCb;
    private TextView dateTv, usernameTv;
    private EditText startingHourEt, roomEt, durationEt;
    private String room, date, startingHour;
    private Button updateBtn;
    private Spinner disciplineSpn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_details);


        startingHourEt=findViewById(R.id.startingHourId);
        roomEt=findViewById(R.id.roomId);
        durationEt=findViewById(R.id.durationId);
        dateTv=findViewById(R.id.dateId);
        updateBtn=findViewById(R.id.buttonUpdateClass);
        disciplineSpn=findViewById(R.id.spinnerDisciplineId);
        emailCb=findViewById(R.id.emailCheckBox);
        specialCb=findViewById(R.id.specialCheckBox);
        usernameTv=findViewById(R.id.username15Id);

        Intent received = getIntent();

        usernameTv.setText(received.getStringExtra("username"));

        String currentString = received.getStringExtra("room");
        String[] separated = currentString.split(": ");
        room=separated[1];

        date=received.getStringExtra("date");

        String currentString2 = received.getStringExtra("starting");
        String[] separated2 = currentString2.split(": ");
        String[] separated3 = separated2[1].split(" ");
        startingHour=separated3[0];

        Button button = (Button) findViewById(R.id.buttonDatePicker);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker=new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"Date Picker");
            }
        });


    }
    @Override
    protected void onResume() {
        super.onResume();

        realm = Realm.getDefaultInstance();

        Intent received = getIntent();

        Class result = realm.where(Class.class)
                .equalTo("room", room)
                .and()
                .equalTo("date", date)
                .and()
                .equalTo("startingHour", Integer.parseInt(startingHour))
                .findFirst();

        readDiscipline(result.getDisciplineName());

        dateTv.setText(result.getDate());
        durationEt.setText(result.getDuration().toString());
        roomEt.setText(result.getRoom());
        startingHourEt.setText(result.getStartingHour().toString());
        specialCb.setChecked(result.getSpecial());

        String currentClassDate= dateTv.getText().toString();

        updateBtn.setOnClickListener(v -> {
            if (isStringInt(this.startingHourEt.getText().toString())) {
                if (isStringInt(this.durationEt.getText().toString())) {
                    Class result2 = realm.where(Class.class)
                            .equalTo("room", roomEt.getText().toString())
                            .and()
                            .equalTo("date", dateTv.getText().toString())
                            .and()
                            .equalTo("startingHour", Integer.parseInt(startingHourEt.getText().toString()))
                            .findFirst();

                    String disciplineNameText = itemDiscipline;
                    boolean disciplineNameIsNotEmpty = !disciplineNameText.equals("Choose Discipline");
                    boolean disciplineDateIsNotEmpty = !dateTv.getText().toString().equals("Date");
                    boolean disciplineRoomIsNotEmpty = !roomEt.getText().toString().equals("");

                    if(result2==null){
                        if(disciplineNameIsNotEmpty){
                            if(disciplineDateIsNotEmpty){
                                if(disciplineRoomIsNotEmpty){
                                    realm.executeTransaction(r -> {
                                        result.setDisciplineName(itemDiscipline);
                                        result.setDate(dateTv.getText().toString());
                                        result.setSpecial(specialCb.isChecked());
                                        result.setRoom(roomEt.getText().toString());
                                        result.setDuration(Integer.parseInt(durationEt.getText().toString()));
                                        result.setStartingHour(Integer.parseInt(startingHourEt.getText().toString()));

                                    });



                                    Intent goToMenuActivity = new Intent(this, MenuActivity.class);
                                    goToMenuActivity.putExtra("username", received.getStringExtra("username"));
                                    startActivity(goToMenuActivity);

                                    if(emailCb.isChecked()){
                                        Intent goToMailActivity = new Intent(this, MailActivity.class);
                                        //goToMailActivity.putExtra("username", received.getStringExtra("username"));
                                        goToMailActivity.putExtra("date", currentClassDate);
                                        goToMailActivity.putExtra("newDate", dateTv.getText().toString());
                                        goToMailActivity.putExtra("newRoom", roomEt.getText().toString());
                                        goToMailActivity.putExtra("newStarting", startingHourEt.getText().toString());
                                        startActivity(goToMailActivity);
                                    }
                                    else{
                                        startActivity(goToMenuActivity);
                                    }
                                }
                                else{
                                    Toast.makeText(this,"Room is empty",Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                Toast.makeText(this,"Select date",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(this,"Choose Discipline",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(this,"This class already exist",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(this,"The duration is not an integer",Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(this,"This starting hour is not an integer",Toast.LENGTH_SHORT).show();
            }
        });

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

    @Override
    protected void onPause() {
        super.onPause();
        realm.close();
    }

    //d=discipline name
    private void readDiscipline(String d){

        Intent received = getIntent();

        // Query
        RealmQuery<Teacher> query = realm.where(Teacher.class);
        // Query conditions
        query.equalTo("name", received.getStringExtra("username"));
        // Query results
        Teacher teacher= query.findFirst();

        RealmList<Discipline> result = teacher.getDisciplines();


        List<String> listDiscipline = new ArrayList<>();
        listDiscipline.add(0, d);

        for (int i = 0; i < result.size(); i++) {
            Discipline discipline = result.get(i);
            listDiscipline.add(discipline.getName());
        }

        // Style and populate the spinner
        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listDiscipline);

        // Drop down layout style
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Attaching data adapter to spinner
        disciplineSpn.setAdapter(dataAdapter);
        disciplineSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Choose Course")) {
                    //do nothing
                } else {
                    //on selecting
                    itemDiscipline = parent.getItemAtPosition(position).toString();

                    Toast.makeText(parent.getContext(), "Selected: " + itemDiscipline, Toast.LENGTH_SHORT).show();
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
