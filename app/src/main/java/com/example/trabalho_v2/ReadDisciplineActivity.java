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
import com.example.trabalho_v2.Model.Course;
import com.example.trabalho_v2.Model.Discipline;
import com.example.trabalho_v2.Model.Teacher;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class ReadDisciplineActivity extends AppCompatActivity {

    //Realm declaration
    private Realm realm;

    private String itemDiscipline="";

    // Views declaration
    private Button updateBtn, createBtn, deleteBtn, dateBtn, currentBtn, previousBtn;
    private RecyclerView recyclerView;
    private TextView usernameText;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_discipline);


        // Views initialization
        previousBtn=findViewById(R.id.buttonPreviousDiscipline);
        currentBtn=findViewById(R.id.buttonCurrentDiscipline);
        dateBtn=findViewById(R.id.buttonDatesDiscipline);
        deleteBtn=findViewById(R.id.buttonDeleteDiscipline);
        createBtn=findViewById(R.id.buttonCreateDiscipline);
        updateBtn=findViewById(R.id.buttonUpdateDiscipline);
        recyclerView = findViewById(R.id.disciplineListId);
        usernameText=findViewById(R.id.username4Id);
        Intent received = getIntent();
        usernameText.setText(received.getStringExtra("username"));


        dateBtn.setOnClickListener(v -> {
            if(itemDiscipline.equals("")){
                Toast.makeText(this,"Nothing selected",Toast.LENGTH_SHORT).show();
            }else {
                Intent goToReadDateActivity = new Intent(this, ReadDateActivity.class);
                goToReadDateActivity.putExtra("username", received.getStringExtra("username"));
                goToReadDateActivity.putExtra("discipline", itemDiscipline);
                startActivity(goToReadDateActivity);
            }
        });

        createBtn.setOnClickListener(v -> {
            Intent goToCreateDisciplineActivity = new Intent(this, CreateDisciplineActivity.class);
            goToCreateDisciplineActivity.putExtra("username", received.getStringExtra("username"));
            startActivity(goToCreateDisciplineActivity);
        });

        updateBtn.setOnClickListener(v -> {
            if(itemDiscipline.equals("")){
                Toast.makeText(this,"Nothing selected",Toast.LENGTH_SHORT).show();
            }else {
                Intent goToDisciplineDetailsActivity = new Intent(this, DisciplineDetailsActivity.class);
                goToDisciplineDetailsActivity.putExtra("discipline", itemDiscipline);
                goToDisciplineDetailsActivity.putExtra("username", received.getStringExtra("username"));
                startActivity(goToDisciplineDetailsActivity);
            }
        });
        deleteBtn.setOnClickListener(v -> {
            if(itemDiscipline.equals("")){
                Toast.makeText(this,"Nothing selected",Toast.LENGTH_SHORT).show();
            }else {
                realm.executeTransaction(r -> {

                    String[] separated = itemDiscipline.split(": ");

                    // Query
                    RealmQuery<Discipline> query = realm.where(Discipline.class);
                    // Query conditions
                    query.equalTo("name", separated[1]);
                    // Query results
                    RealmResults result = query.findAll();
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
        readDiscipline(result1);

        currentBtn.setOnClickListener(v -> {
            RealmList<Discipline> result2 = new RealmList<>();
            for(int i=0;i< result1.size();i++){
                if(result1.get(i).getYear().getYearDate()>=2019){
                    result2.add(result1.get(i));
                }
            }
            readDiscipline(result2);
        });
        previousBtn.setOnClickListener(v -> {
            RealmList<Discipline> result2 = new RealmList<>();
            for(int i=0;i< result1.size();i++){
                if(result1.get(i).getYear().getYearDate()<2019){
                    result2.add(result1.get(i));
                }
            }
            readDiscipline(result2);
        });
    }
    @Override
    protected void onPause() {
        super.onPause();
        realm.close();
    }
    private void readDiscipline(RealmList<Discipline> result){

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        DisciplineAdapter adapter = new DisciplineAdapter(result);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new DisciplineAdapter.ClickListener() {
            @Override
            public void onItemClick(DisciplineAdapter adapter, View v, int position) {
                TextView tv = v.findViewById(R.id.disciplineNameId);
                String name = tv.getText().toString();
                itemDiscipline=name;
                Toast.makeText(ReadDisciplineActivity.this, "Clicked on " + itemDiscipline, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(DisciplineAdapter adapter, View v, int position) {
                TextView tv = v.findViewById(R.id.disciplineNameId);
                String name = tv.getText().toString();
                itemDiscipline=name;
                Toast.makeText(ReadDisciplineActivity.this, "Clicked on " + itemDiscipline, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
