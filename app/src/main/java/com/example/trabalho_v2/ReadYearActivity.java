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

import com.example.trabalho_v2.Model.Class;
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

public class ReadYearActivity extends AppCompatActivity {

    private String itemYear="";

    //Realm declaration
    private Realm realm;

    // Views declaration
    private TextView usernameText;
    private ListView yearsViewLv;
    private Button createBtn, deleteBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_year);


        // Views initialization
        usernameText=findViewById(R.id.username7Id);
        yearsViewLv=findViewById(R.id.yearsViewId);
        createBtn=findViewById(R.id.buttonCreateYear);
        deleteBtn=findViewById(R.id.buttonDeleteYear);
        Intent received = getIntent();
        usernameText.setText(received.getStringExtra("username"));

        createBtn.setOnClickListener(v -> {
            Intent goToCreateYearActivity = new Intent(this, CreateYearActivity.class);
            goToCreateYearActivity.putExtra("username", received.getStringExtra("username"));
            startActivity(goToCreateYearActivity);
        });
        deleteBtn.setOnClickListener(v -> {

                if(itemYear.equals("")){
                    Toast.makeText(this,"Nothing selected",Toast.LENGTH_SHORT).show();
                }else{
                    // Query
                    RealmQuery<Year> query = realm.where(Year.class);
                    // Query conditions
                    query.equalTo("yearDate", Integer.parseInt(itemYear));
                    // Query results
                    Year result = query.findFirst();

                    if(!result.getUsername().equals(received.getStringExtra("username"))){
                        Toast.makeText(this,"You didnt created this year",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        realm.executeTransaction(r -> {
                            // Query
                            RealmQuery<Year> query2 = realm.where(Year.class);
                            // Query conditions
                            query2.equalTo("yearDate", Integer.parseInt(itemYear));
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
        readYear();
    }
    @Override
    protected void onPause() {
        super.onPause();
        realm.close();
    }
    private void readYear(){
        // Query
        RealmQuery<Year> query = realm.where(Year.class);
        // Query results
        // Execute the query:
        RealmResults<Year> result = query.findAll();

        List<String> listYears = new ArrayList<>();

        for (int i = 0; i < result.size(); i++) {
            Year year = result.get(i);
            listYears.add(year.getYearDate().toString());
        }

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, listYears);
        yearsViewLv.setAdapter(adapter);
        yearsViewLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemYear = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), "Selected: " + itemYear, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
