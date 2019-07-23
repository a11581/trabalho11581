package com.example.trabalho_v2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trabalho_v2.Model.Class;
import com.example.trabalho_v2.Model.Date;
import com.example.trabalho_v2.Model.Discipline;
import com.example.trabalho_v2.Model.Topic;

import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmQuery;

public class CreateTopicActivity extends AppCompatActivity {

    //Realm declaration
    private Realm realm;
    private Button createBtn;
    private EditText nameEt, contentEt;
    private TextView usernameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_topic);

        // Views initialization
        createBtn = findViewById(R.id.buttonCreateTopic);
        usernameText=findViewById(R.id.username18Id);
        nameEt=findViewById(R.id.topicNameId);
        contentEt=findViewById(R.id.topicContentId);

        Intent received = getIntent();
        usernameText.setText(received.getStringExtra("username"));

        createBtn.setOnClickListener(v -> {
            boolean topicNameIsNotEmpty = !nameEt.getText().toString().equals("");
            boolean topicContentIsNotEmpty = !contentEt.getText().toString().equals("");

            if(topicNameIsNotEmpty){
                if(topicContentIsNotEmpty){
                    realm.executeTransaction(r -> {
                        Topic topic = realm.createObject(Topic.class, UUID.randomUUID().toString());
                        topic.setName(nameEt.getText().toString());
                        topic.setContent(contentEt.getText().toString());

                        Class result = realm.where(Class.class)
                                .equalTo("room", received.getStringExtra("room"))
                                .and()
                                .equalTo("date", received.getStringExtra("date"))
                                .and()
                                .equalTo("startingHour", Integer.parseInt(received.getStringExtra("starting")))
                                .findFirst();

                        result.getTopics().add(topic);

                    });

                    Intent goToMenuActivity = new Intent(this, MenuActivity.class);
                    goToMenuActivity.putExtra("username", received.getStringExtra("username"));
                    startActivity(goToMenuActivity);
                }else{
                    Toast.makeText(this,"Topic content is empty",Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this,"Topic name is empty",Toast.LENGTH_SHORT).show();
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
