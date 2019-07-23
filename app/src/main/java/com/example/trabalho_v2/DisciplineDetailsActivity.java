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

import com.example.trabalho_v2.Model.Course;
import com.example.trabalho_v2.Model.Discipline;
import com.example.trabalho_v2.Model.Teacher;
import com.example.trabalho_v2.Model.Year;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class DisciplineDetailsActivity extends AppCompatActivity {

    //Realm declaration
    private Realm realm;

    private String discipline;

    private String itemYear, itemCourse,itemPeriod;

    private EditText nameEt, acronymEt;
    private Button updateBtn;
    private Spinner spnYear, spnCourse,spnPeriod;
    private TextView usernameTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discipline_details);

        usernameTv=findViewById(R.id.username14Id);
        updateBtn=findViewById(R.id.buttonUpdateDiscipline);
        spnYear=findViewById(R.id.spinnerYearId);
        spnCourse=findViewById(R.id.spinnerCourseId);
        spnPeriod=findViewById(R.id.spinner);
        nameEt=findViewById(R.id.nameId);
        acronymEt=findViewById(R.id.acronymId);

        Intent received = getIntent();

        String username = received.getStringExtra("username");
        usernameTv.setText(username);

        String currentString = received.getStringExtra("discipline");
        String[] separated = currentString.split(": ");
        discipline=separated[1];

    }
    @Override
    protected void onResume() {
        super.onResume();

        realm = Realm.getDefaultInstance();

        Intent received = getIntent();


        // Query
        RealmQuery<Discipline> query = realm.where(Discipline.class);
        // Query conditions
        query.equalTo("name", discipline);
        // Query results
        Discipline result = query.findFirst();

        nameEt.setText(result.getName());
        acronymEt.setText(result.getAcronym());
        if(result.getYear()==null){
            readYear("Choose year");
        }
        else{
            readYear(result.getYear().getYearDate().toString());
        }
        if(result.getCourse()==null){
            readCourse("Choose course");
        }
        else{
            readCourse(result.getCourse().getName());
        }
        readPeriod(result.getPeriod().toString());

        updateBtn.setOnClickListener(v -> {
            String courseNameText = this.nameEt.getText().toString();
            boolean courseNameIsNotEmpty = !courseNameText.equals("");
            String courseAcronymText = this.acronymEt.getText().toString();
            boolean courseAcronymIsNotEmpty = !courseAcronymText.equals("");
            boolean disciplineCourseIsNotEmpty = !itemCourse.equals("");
            boolean disciplineYearNameIsNotEmpty = !itemYear.equals("");
            boolean disciplinePeriodIsNotEmpty = !itemPeriod.equals("");

            // Query
            RealmQuery<Discipline> query3 = realm.where(Discipline.class);
            // Query conditions
            query3.equalTo("name", nameEt.getText().toString());
            // Query results
            Discipline result3 = query3.findFirst();

            if(result3==null){
                if(courseNameIsNotEmpty){
                    if(courseAcronymIsNotEmpty){
                        if(disciplineCourseIsNotEmpty){
                            if(disciplineYearNameIsNotEmpty){
                                if(disciplinePeriodIsNotEmpty){
                                    realm.executeTransaction(r -> {
                                        result.setName(nameEt.getText().toString());
                                        result.setAcronym(acronymEt.getText().toString());
                                        result.setPeriod(itemPeriod);


                                        // Query
                                        RealmQuery<Year> query1 = realm.where(Year.class);
                                        // Query conditions
                                        query1.equalTo("yearDate", Integer.parseInt(itemYear));
                                        // Query results
                                        Year result1 = query1.findFirst();

                                        result.setYear(result1);


                                        // Query
                                        RealmQuery<Course> query2 = realm.where(Course.class);
                                        // Query conditions
                                        query2.equalTo("name", itemCourse);
                                        // Query results
                                        Course result2 = query2.findFirst();

                                        result.setCourse(result2);


                                        Intent goToMenuActivity = new Intent(this,MenuActivity.class);
                                        goToMenuActivity.putExtra("username", received.getStringExtra("username"));
                                        startActivity(goToMenuActivity);

                                    });
                                    Intent goToMenuActivity = new Intent(this, MenuActivity.class);
                                    goToMenuActivity.putExtra("username", received.getStringExtra("username"));
                                    startActivity(goToMenuActivity);
                                }else{
                                    Toast.makeText(this,"Select Period",Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(this,"Select Year",Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(this,"Select Course",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(this,"Discipline acronym is empty",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(this,"Discipline name is empty",Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(this,"This discipline already exist",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        realm.close();
    }

    private void readPeriod(String s){


        List<String> listDays = new ArrayList<>();
        listDays.add(0, s);
        listDays.add(1, "1st Semester");
        listDays.add(2, "2nd Semester");
        listDays.add(3, "1st Quarter");
        listDays.add(4, "2nd Quarter");
        listDays.add(5, "3rd Quarter");
        listDays.add(6, "4th Quarter");


        // Style and populate the spinner
        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listDays);

        // Drop down layout style
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Attaching data adapter to spinner
        spnPeriod.setAdapter(dataAdapter);
        spnPeriod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Choose Period")) {
                    //do nothing
                } else {
                    //on selecting
                    itemPeriod = parent.getItemAtPosition(position).toString();

                    Toast.makeText(parent.getContext(), "Selected: " + itemPeriod, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method
            }
        });
    }

    private void readYear(String s){
        // Query
        RealmQuery<Year> query = realm.where(Year.class);
        // Query results
        // Execute the query:
        RealmResults<Year> result = query.findAll();


        List<String> listYears = new ArrayList<>();
        listYears.add(0, s);

        for (int i = 0; i < result.size(); i++) {
            Year year = result.get(i);
            listYears.add(year.getYearDate().toString());
        }

        // Style and populate the spinner
        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listYears);

        // Drop down layout style
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Attaching data adapter to spinner
        spnYear.setAdapter(dataAdapter);
        spnYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Choose Year")) {
                    //do nothing
                } else {
                    //on selecting
                    itemYear = parent.getItemAtPosition(position).toString();

                    Toast.makeText(parent.getContext(), "Selected: " + itemYear, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method
            }
        });
    }

    private void readCourse(String s){
        // Query
        RealmQuery<Course> query = realm.where(Course.class);
        // Query results
        // Execute the query:
        RealmResults<Course> result = query.findAll();


        List<String> listCourses = new ArrayList<>();
        listCourses.add(0, s);

        for (int i = 0; i < result.size(); i++) {
            Course course = result.get(i);
            listCourses.add(course.getName());
        }

        // Style and populate the spinner
        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listCourses);

        // Drop down layout style
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Attaching data adapter to spinner
        spnCourse.setAdapter(dataAdapter);
        spnCourse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Choose Course")) {
                    //do nothing
                } else {
                    //on selecting
                    itemCourse = parent.getItemAtPosition(position).toString();

                    Toast.makeText(parent.getContext(), "Selected: " + itemCourse, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method
            }
        });
    }
}
