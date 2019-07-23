package com.example.trabalho_v2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trabalho_v2.Model.Class;
import com.example.trabalho_v2.Model.Contact;
import com.example.trabalho_v2.Model.Date;
import com.example.trabalho_v2.Model.Discipline;
import com.example.trabalho_v2.Model.Teacher;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class ReadContactActivity extends AppCompatActivity {
    //Realm declaration
    private Realm realm;
    private String itemDay="";
    private String itemStarting;
    private TextView usernameText;
    private Button createBtn, deleteBtn;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_contact);

        // Views initialization
        deleteBtn=findViewById(R.id.buttonDeleteContact);
        createBtn=findViewById(R.id.buttonCreateContact);
        usernameText=findViewById(R.id.username19Id);
        recyclerView = findViewById(R.id.contactListId);

        Intent received = getIntent();
        usernameText.setText(received.getStringExtra("username"));

        createBtn.setOnClickListener(v -> {
            Intent goToCreateContactActivity = new Intent(this, CreateContactActivity.class);
            goToCreateContactActivity.putExtra("username", received.getStringExtra("username"));
            startActivity(goToCreateContactActivity);
        });

        deleteBtn.setOnClickListener(v -> {
            if(itemDay.equals("")){
                Toast.makeText(this,"Nothing selected",Toast.LENGTH_SHORT).show();
            }else {
                realm.executeTransaction(r -> {

                    /*
                    // Query
                    RealmQuery<Contact> query = realm.where(Contact.class);
                    // Query conditions
                    query.equalTo("startingHour", Integer.parseInt(itemStarting));
                    // Query results
                    RealmResults result = query.findAll();
                    result.deleteFirstFromRealm();
                    */

                    String currentString = itemDay;
                    String[] separated = currentString.split(": ");
                    String currentString2 = itemStarting;
                    String[] separated2 = currentString2.split(": ");

                    RealmResults result = realm.where(Contact.class)
                            .equalTo("day", separated[1])
                            .and()
                            .equalTo("startingHour", Integer.parseInt(separated2[1]))
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
        readContact();
    }
    @Override
    protected void onPause() {
        super.onPause();
        realm.close();
    }

    private void readContact(){
        Intent received = getIntent();
        // Query
        RealmQuery<Teacher> query = realm.where(Teacher.class);
        // Query conditions
        query.equalTo("name",received.getStringExtra("username"));
        // Query results
        Teacher result = query.findFirst();

        RealmList<Contact> result1 = result.getContacts();

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ContactAdapter adapter = new ContactAdapter(result1);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new ContactAdapter.ClickListener() {
            @Override
            public void onItemClick(ContactAdapter adapter, View v, int position) {
                TextView tv = v.findViewById(R.id.contactDayId);
                String name = tv.getText().toString();
                itemDay=name;
                TextView tv1 = v.findViewById(R.id.contactStartingHourId);
                String name1 = tv1.getText().toString();
                itemStarting=name1;
                Toast.makeText(ReadContactActivity.this, "Clicked on " + itemDay + " starting at: " + itemStarting + " hours", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(ContactAdapter adapter, View v, int position) {
                TextView tv = v.findViewById(R.id.contactDayId);
                String name = tv.getText().toString();
                itemDay=name;
                TextView tv1 = v.findViewById(R.id.contactStartingHourId);
                String name1 = tv1.getText().toString();
                itemStarting=name1;
                Toast.makeText(ReadContactActivity.this, "Clicked on " + itemDay + " starting at: " + itemStarting + " hours", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
