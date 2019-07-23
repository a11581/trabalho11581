package com.example.trabalho_v2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trabalho_v2.Model.Course;
import com.example.trabalho_v2.Model.Year;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class ReadCourseActivity extends AppCompatActivity {

    private String itemCourse="";

    //Realm declaration
    private Realm realm;

    // Views declaration
    private Button createBtn,deleteBtn;
    private TextView usernameTv;
    private ListView coursesViewLv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_course);

        // Views initialization
        createBtn=findViewById(R.id.buttonCreateCourse);
        deleteBtn=findViewById(R.id.buttonDeleteCourse);
        coursesViewLv=findViewById(R.id.coursesViewId);
        usernameTv=findViewById(R.id.username10Id);
        Intent received = getIntent();
        usernameTv.setText(received.getStringExtra("username"));

        createBtn.setOnClickListener(v -> {
            Intent goToCreateCourseActivity = new Intent(this, CreateCourseActivity.class);
            goToCreateCourseActivity.putExtra("username", received.getStringExtra("username"));
            startActivity(goToCreateCourseActivity);
        });
        deleteBtn.setOnClickListener(v -> {
            if(itemCourse.equals("")){
                Toast.makeText(this,"Nothing selected",Toast.LENGTH_SHORT).show();
            }else {
                // Query
                RealmQuery<Course> query = realm.where(Course.class);
                // Query conditions
                query.equalTo("name", itemCourse);
                // Query results
                Course result = query.findFirst();

                if(!result.getUsername().equals(received.getStringExtra("username"))){
                    Toast.makeText(this,"You didnt created this year",Toast.LENGTH_SHORT).show();
                }
                else{
                    realm.executeTransaction(r -> {
                        // Query
                        RealmQuery<Course> query2 = realm.where(Course.class);
                        // Query conditions
                        query2.equalTo("name", itemCourse);
                        // Query results
                        RealmResults result2 = query2.findAll();
                        result2.deleteFirstFromRealm();
                    });
                    Intent goToMenuActivity = new Intent(this, MenuActivity.class);
                    goToMenuActivity.putExtra("username", received.getStringExtra("username"));
                    startActivity(goToMenuActivity);
                }
            }
        });

    }
    @Override
    protected void onResume() {
        super.onResume();
        realm = Realm.getDefaultInstance();
        readCourse();
    }
    @Override
    protected void onPause() {
        super.onPause();
        realm.close();
    }

    private void readCourse(){
        // Query
        RealmQuery<Course> query = realm.where(Course.class);
        // Query results
        // Execute the query:
        RealmResults<Course> result = query.findAll();


        List<String> listCourses = new ArrayList<>();

        for (int i = 0; i < result.size(); i++) {
            Course course = result.get(i);
            listCourses.add(course.getName());
        }

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, listCourses);
        coursesViewLv.setAdapter(adapter);
        coursesViewLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemCourse = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), "Selected: " + itemCourse, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
