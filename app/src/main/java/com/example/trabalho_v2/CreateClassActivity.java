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
import com.example.trabalho_v2.Model.Course;
import com.example.trabalho_v2.Model.Discipline;
import com.example.trabalho_v2.Model.Teacher;
import com.example.trabalho_v2.Model.Year;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class CreateClassActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    //Realm declaration
    private Realm realm;

    String itemDiscipline;

    // Views declaration

    private Button createBtn;
    private EditText startingHourEt, roomEt, durationEt;
    private CheckBox specialClassCB;
    private TextView usernameText;
    private Spinner spnDiscipline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_class);

        // Views initialization
        spnDiscipline=findViewById(R.id.spinnerDisciplineId);
        createBtn = findViewById(R.id.buttonCreateClass);
        startingHourEt = findViewById(R.id.startingHourId);
        usernameText=findViewById(R.id.username12Id);
        roomEt=findViewById(R.id.roomId);
        durationEt=findViewById(R.id.durationId);
        specialClassCB=findViewById(R.id.specialClassId);
        TextView date = (TextView) findViewById(R.id.dateId);
        Intent received = getIntent();
        usernameText.setText(received.getStringExtra("username"));

        Button button = (Button) findViewById(R.id.buttonDatePicker);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker=new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"Date Picker");
            }
        });

        // Register button
        // Need more verifications
        createBtn.setOnClickListener(v -> {
            if (isStringInt(this.startingHourEt.getText().toString())) {
                if (isStringInt(this.durationEt.getText().toString())) {
                    Class result2 = realm.where(Class.class)
                            .equalTo("room", roomEt.getText().toString())
                            .and()
                            .equalTo("date", date.getText().toString())
                            .and()
                            .equalTo("startingHour", Integer.parseInt(startingHourEt.getText().toString()))
                            .findFirst();

                    String disciplineNameText = itemDiscipline;
                    boolean disciplineNameIsNotEmpty = !disciplineNameText.equals("Choose Discipline");
                    boolean disciplineDateIsNotEmpty = !date.getText().toString().equals("Date");
                    boolean disciplineRoomIsNotEmpty = !roomEt.getText().toString().equals("");

                    if(result2==null){
                        if(disciplineNameIsNotEmpty){
                            if(disciplineDateIsNotEmpty){
                                if(disciplineRoomIsNotEmpty){
                                    realm.executeTransaction(r -> {
                                        Class aClass= realm.createObject(Class.class, UUID.randomUUID().toString());
                                        //year.setYearDate(Integer.parseInt(yearEt.getText().toString()));
                                        aClass.setStartingHour(Integer.parseInt(startingHourEt.getText().toString()));
                                        aClass.setDate(date.getText().toString());
                                        aClass.setDuration(Integer.parseInt(durationEt.getText().toString()));
                                        aClass.setRoom(roomEt.getText().toString());
                                        aClass.setSpecial(specialClassCB.isChecked());
                                        aClass.setDisciplineName(itemDiscipline);

                            /*
                            // Query
                            RealmQuery<Teacher> query2 = realm.where(Teacher.class);
                            // Query conditions
                            query.equalTo("name", received.getStringExtra("username"));
                            // Query results
                            Teacher result2 = query2.findFirst();

                            result2.getDisciplines().add(discipline);
                            */
                                        // Query
                                        RealmQuery<Discipline> query = realm.where(Discipline.class);
                                        // Query conditions
                                        query.equalTo("name", itemDiscipline);
                                        // Query results
                                        Discipline result = query.findFirst();

                                        result.getClasses().add(aClass);

                                    });

                                    Intent goToMenuActivity = new Intent(this, MenuActivity.class);
                                    goToMenuActivity.putExtra("username", received.getStringExtra("username"));
                                    startActivity(goToMenuActivity);
                                }
                                else{
                                    Toast.makeText(this,"Room is empty",Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                Toast.makeText(this,"Choose Date",Toast.LENGTH_SHORT).show();
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
    protected void onResume() {
        super.onResume();
        realm = Realm.getDefaultInstance();
        readDiscipline();
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

    private void readDiscipline(){

        Intent received = getIntent();

        // Query
        RealmQuery<Teacher> query = realm.where(Teacher.class);
        // Query conditions
        query.equalTo("name", received.getStringExtra("username"));
        // Query results
        Teacher teacher= query.findFirst();

        RealmList<Discipline> result = teacher.getDisciplines();


        List<String> listDiscipline = new ArrayList<>();
        listDiscipline.add(0, "Choose Discipline");

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
        spnDiscipline.setAdapter(dataAdapter);
        spnDiscipline.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
