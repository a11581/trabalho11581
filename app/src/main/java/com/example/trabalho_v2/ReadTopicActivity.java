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
import com.example.trabalho_v2.Model.Date;
import com.example.trabalho_v2.Model.Discipline;
import com.example.trabalho_v2.Model.Topic;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class ReadTopicActivity extends AppCompatActivity {

    //Realm declaration
    private Realm realm;

    private String className;

    private String itemTopic="";

    private String room, date, startingHour;
    private TextView usernameText;
    private Button createBtn, deleteBtn;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_topic);

        // Views initialization
        deleteBtn=findViewById(R.id.buttonDeleteTopic);
        createBtn=findViewById(R.id.buttonCreateTopic);
        usernameText=findViewById(R.id.username17Id);
        recyclerView = findViewById(R.id.topicListId);

        Intent received = getIntent();
        usernameText.setText(received.getStringExtra("username"));

        String currentString = received.getStringExtra("room");
        String[] separated = currentString.split(": ");
        room=separated[1];

        date=received.getStringExtra("date");

        String currentString2 = received.getStringExtra("starting");
        String[] separated2 = currentString2.split(": ");
        String[] separated3 = separated2[1].split(" ");
        startingHour=separated3[0];

        createBtn.setOnClickListener(v -> {
            Intent goToCreateTopicActivity = new Intent(this, CreateTopicActivity.class);
            goToCreateTopicActivity.putExtra("username", received.getStringExtra("username"));
            goToCreateTopicActivity.putExtra("room", room);
            goToCreateTopicActivity.putExtra("starting", startingHour);
            goToCreateTopicActivity.putExtra("date", date);
            startActivity(goToCreateTopicActivity);
        });

        deleteBtn.setOnClickListener(v -> {
            if(itemTopic.equals("")){
                Toast.makeText(this,"Nothing selected",Toast.LENGTH_SHORT).show();
            }else {
                realm.executeTransaction(r -> {
                    // Query
                    RealmQuery<Topic> query = realm.where(Topic.class);
                    // Query conditions
                    query.equalTo("name", itemTopic);
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
        readTopic();
    }
    @Override
    protected void onPause() {
        super.onPause();
        realm.close();
    }

    private void readTopic(){
        Intent received = getIntent();
        Class result = realm.where(Class.class)
                .equalTo("room", room)
                .and()
                .equalTo("date", date)
                .and()
                .equalTo("startingHour", Integer.parseInt(startingHour))
                .findFirst();

        RealmList<Topic> result1 = result.getTopics();

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        TopicAdapter adapter = new TopicAdapter(result1);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new TopicAdapter.ClickListener() {
            @Override
            public void onItemClick(TopicAdapter adapter, View v, int position) {
                TextView tv = v.findViewById(R.id.topicNameId);
                String name = tv.getText().toString();
                itemTopic=name;
                Toast.makeText(ReadTopicActivity.this, "Clicked on " + itemTopic, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(TopicAdapter adapter, View v, int position) {
                TextView tv = v.findViewById(R.id.topicNameId);
                String name = tv.getText().toString();
                itemTopic=name;
                Toast.makeText(ReadTopicActivity.this, "Clicked on " + itemTopic, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
