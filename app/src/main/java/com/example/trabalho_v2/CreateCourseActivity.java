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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class CreateCourseActivity extends AppCompatActivity {

    //Realm declaration
    private Realm realm;

    // Views declaration
    private Button createBtn;
    private EditText nameEt, acronymEt;
    private TextView usernameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_course);

        // Views initialization
        createBtn = findViewById(R.id.buttonCreateCourse);
        nameEt = findViewById(R.id.courseName2Id);
        acronymEt = findViewById(R.id.courseAcronymId);
        usernameText=findViewById(R.id.username9Id);
        Intent received = getIntent();
        usernameText.setText(received.getStringExtra("username"));


        // Register button
        // Need more verifications
        createBtn.setOnClickListener(v -> {
            // Query
            RealmQuery<Course> query = realm.where(Course.class);
            // Query conditions
            query.equalTo("name", this.nameEt.getText().toString());
            // Query results
            Course result = query.findFirst();

            String courseNameText = this.nameEt.getText().toString();
            boolean courseNameIsNotEmpty = !courseNameText.equals("");
            String courseAcronymText = this.acronymEt.getText().toString();
            boolean courseAcronymIsNotEmpty = !courseAcronymText.equals("");

            if(result==null){
                if(courseNameIsNotEmpty){
                    if(courseAcronymIsNotEmpty){
                        Intent goToMenuActivity = new Intent(this, MenuActivity.class);
                        realm.executeTransaction(r -> {
                            Course course = realm.createObject(Course.class, UUID.randomUUID().toString());
                            course.setName(nameEt.getText().toString());
                            course.setAcronym(acronymEt.getText().toString());
                            course.setUsername(received.getStringExtra("username"));
                        });
                        goToMenuActivity.putExtra("username", received.getStringExtra("username"));
                        startActivity(goToMenuActivity);
                    }
                    else{
                        Toast.makeText(this,"Course acronym is empty",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(this,"Course name is empty",Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(this,"This course already exist",Toast.LENGTH_SHORT).show();
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
}
