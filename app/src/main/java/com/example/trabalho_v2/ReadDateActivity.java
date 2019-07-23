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

import com.example.trabalho_v2.Model.Date;
import com.example.trabalho_v2.Model.Discipline;
import com.example.trabalho_v2.Model.Teacher;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class ReadDateActivity extends AppCompatActivity {

    //Realm declaration
    private Realm realm;

    private String discipline;

    private String itemDate="";

    private TextView usernameText;
    private Button createBtn, deleteBtn;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_date);

        // Views initialization
        deleteBtn=findViewById(R.id.buttonDeleteDate);
        createBtn=findViewById(R.id.buttonCreateDate);
        usernameText=findViewById(R.id.username15Id);
        recyclerView = findViewById(R.id.dateListId);

        Intent received = getIntent();
        usernameText.setText(received.getStringExtra("username"));

        String currentString = received.getStringExtra("discipline");
        String[] separated = currentString.split(": ");
        discipline=separated[1];

        createBtn.setOnClickListener(v -> {
            Intent goToCreateDateActivity = new Intent(this, CreateDateActivity.class);
            goToCreateDateActivity.putExtra("username", received.getStringExtra("username"));
            goToCreateDateActivity.putExtra("discipline", received.getStringExtra("discipline"));
            startActivity(goToCreateDateActivity);
        });

        deleteBtn.setOnClickListener(v -> {
            if(itemDate.equals("")){
                Toast.makeText(this,"Nothing selected",Toast.LENGTH_SHORT).show();
            }else {
                realm.executeTransaction(r -> {
                    // Query
                    RealmQuery<Date> query = realm.where(Date.class);
                    // Query conditions
                    query.equalTo("date", itemDate);
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
        readDate();
    }
    @Override
    protected void onPause() {
        super.onPause();
        realm.close();
    }

    private void readDate(){
        Intent received = getIntent();
        // Query
        RealmQuery<Discipline> query = realm.where(Discipline.class);
        // Query conditions
        query.equalTo("name", discipline);
        // Query results
        Discipline result = query.findFirst();

        RealmList<Date> result1 = result.getDates();

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        DateAdapter adapter = new DateAdapter(result1);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new DateAdapter.ClickListener() {
            @Override
            public void onItemClick(DateAdapter adapter, View v, int position) {
                TextView tv = v.findViewById(R.id.dateDateId);
                String name = tv.getText().toString();
                itemDate=name;
                Toast.makeText(ReadDateActivity.this, "Clicked on " + itemDate, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onItemLongClick(DateAdapter adapter, View v, int position) {
                TextView tv = v.findViewById(R.id.dateDateId);
                String name = tv.getText().toString();
                itemDate=name;
                Toast.makeText(ReadDateActivity.this, "Clicked on " + itemDate, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
