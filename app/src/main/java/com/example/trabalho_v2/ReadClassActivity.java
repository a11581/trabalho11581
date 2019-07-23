package com.example.trabalho_v2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trabalho_v2.Model.Class;
import com.example.trabalho_v2.Model.Discipline;
import com.example.trabalho_v2.Model.Teacher;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class ReadClassActivity extends AppCompatActivity {

    //Realm declaration
    private Realm realm;

    // Views declaration
    String itemClassRoom="";
    String itemClassDate="";
    String itemClassStartingHour="";

    private TextView usernameText;
    private Button updateBtn, createBtn, deleteBtn, topicBtn, currentBtn, previousBtn;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_class);

        // Views initialization
        currentBtn=findViewById(R.id.buttonCurrentClass);
        previousBtn=findViewById(R.id.buttonPreviousClass);
        topicBtn=findViewById(R.id.buttonClassTopic);
        deleteBtn=findViewById(R.id.buttonDeleteClass);
        createBtn=findViewById(R.id.buttonCreateClass);
        updateBtn=findViewById(R.id.buttonUpdateClass);
        usernameText=findViewById(R.id.username13Id);
        recyclerView=findViewById(R.id.classListId);
        Intent received = getIntent();
        usernameText.setText(received.getStringExtra("username"));

        topicBtn.setOnClickListener(v -> {
            if(itemClassRoom.equals("")){
                Toast.makeText(this,"Nothing selected",Toast.LENGTH_SHORT).show();
            }else {
                Intent goToReadTopicActivity = new Intent(this, ReadTopicActivity.class);
                goToReadTopicActivity.putExtra("username", received.getStringExtra("username"));
                goToReadTopicActivity.putExtra("room", itemClassRoom);
                goToReadTopicActivity.putExtra("starting", itemClassStartingHour);
                goToReadTopicActivity.putExtra("date", itemClassDate);
                startActivity(goToReadTopicActivity);
            }
        });

        updateBtn.setOnClickListener(v -> {
            if(itemClassRoom.equals("")){
                Toast.makeText(this,"Nothing selected",Toast.LENGTH_SHORT).show();
            }else {
                Intent goToClassDetailsActivity = new Intent(this, ClassDetailsActivity.class);
                goToClassDetailsActivity.putExtra("room", itemClassRoom);
                goToClassDetailsActivity.putExtra("starting", itemClassStartingHour);
                goToClassDetailsActivity.putExtra("date", itemClassDate);
                goToClassDetailsActivity.putExtra("username", received.getStringExtra("username"));
                startActivity(goToClassDetailsActivity);
            }
        });

        createBtn.setOnClickListener(v -> {
            Intent goToCreateClassActivity = new Intent(this, CreateClassActivity.class);
            goToCreateClassActivity.putExtra("username", received.getStringExtra("username"));
            startActivity(goToCreateClassActivity);
        });
        deleteBtn.setOnClickListener(v -> {
            if(itemClassRoom.equals("")){
                Toast.makeText(this,"Nothing selected",Toast.LENGTH_SHORT).show();
            }else {
                realm.executeTransaction(r -> {

                    String[] separated = itemClassRoom.split(": ");

                    String[] separated2 = itemClassStartingHour.split(": ");
                    String[] separated3 = separated2[1].split(" ");

                    RealmResults result = realm.where(Class.class)
                            .equalTo("room", separated[1])
                            .and()
                            .equalTo("date", itemClassDate)
                            .and()
                            .equalTo("startingHour", Integer.parseInt(separated3[0]))
                            .findAll();
                    result.deleteFirstFromRealm();

                });
                Intent goToMenuActivity = new Intent(this, MenuActivity.class);
                goToMenuActivity.putExtra("username", received.getStringExtra("username"));
                startActivity(goToMenuActivity);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        realm = Realm.getDefaultInstance();
        Intent received = getIntent();
        // Query
        RealmQuery<Teacher> query = realm.where(Teacher.class);
        // Query conditions
        query.equalTo("name", received.getStringExtra("username"));
        // Query results
        Teacher result = query.findFirst();

        RealmList<Discipline> result1 = result.getDisciplines();

        List<Class> listClasses = new ArrayList<>();
        //listClasses=result1.get(0).getClasses();

        for (int i = 0; i < result1.size(); i++) {
            List<Class> list = new ArrayList<>();
            list=result1.get(i).getClasses();
            for (int j = 0; j < list.size(); j++) {
                listClasses.add(list.get(j));
            }
        }
        readClass(listClasses);

        currentBtn.setOnClickListener(v -> {
            List<Class> list2 = new ArrayList<>();
            Date currentTime = Calendar.getInstance().getTime();
            for(int i=0;i< listClasses.size();i++){
                String fullDate=listClasses.get(i).getDate();
                String[] separated = fullDate.split(" ");
                if(Integer.parseInt(separated[1])>=23){
                    list2.add(listClasses.get(i));
                }
            }
            readClass(list2);
        });
        previousBtn.setOnClickListener(v -> {
            List<Class> list2 = new ArrayList<>();
            for(int i=0;i< listClasses.size();i++){
                String fullDate=listClasses.get(i).getDate();
                String[] separated = fullDate.split(" ");
                if(Integer.parseInt(separated[1])<23){
                    list2.add(listClasses.get(i));
                }
            }
            readClass(list2);
        });
    }
    @Override
    protected void onPause() {
        super.onPause();
        realm.close();
    }

    private void readClass(List<Class> listClasses){
        // Query
        //RealmQuery<Class> query = realm.where(Class.class);
        // Query results
        // Execute the query:
        //RealmResults<Class> result = query.findAll();

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ClassAdapter adapter = new ClassAdapter(listClasses);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new ClassAdapter.ClickListener() {
            @Override
            public void onItemClick(ClassAdapter adapter, View v, int position) {
                TextView tv = v.findViewById(R.id.classRoomId);
                String name = tv.getText().toString();
                itemClassRoom=name;
                TextView tv1 = v.findViewById(R.id.classDateId);
                String name1 = tv1.getText().toString();
                itemClassDate=name1;
                TextView tv2 = v.findViewById(R.id.classStartingHourId);
                String name2 = tv2.getText().toString();
                itemClassStartingHour=name2;
                Toast.makeText(ReadClassActivity.this, "Clicked on class " + itemClassRoom + " " + itemClassStartingHour + " " + itemClassDate, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(ClassAdapter adapter, View v, int position) {
                TextView tv = v.findViewById(R.id.classRoomId);
                String name = tv.getText().toString();
                itemClassRoom=name;
                TextView tv1 = v.findViewById(R.id.classDateId);
                String name1 = tv1.getText().toString();
                itemClassDate=name1;
                TextView tv2 = v.findViewById(R.id.classStartingHourId);
                String name2 = tv2.getText().toString();
                itemClassStartingHour=name2;
                Toast.makeText(ReadClassActivity.this, "Clicked on class " + itemClassRoom + " " + itemClassStartingHour + " " + itemClassDate, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
