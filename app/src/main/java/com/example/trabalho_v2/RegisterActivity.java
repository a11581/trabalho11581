package com.example.trabalho_v2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trabalho_v2.Model.Teacher;
import java.util.UUID;
import io.realm.Realm;
import io.realm.RealmQuery;

public class RegisterActivity extends AppCompatActivity {

    //Realm declaration
    private Realm realm;

    // Views declaration
    private Button backToLoginBtn, registerBtn;
    private EditText nameEt, password1Et, password2Et;
    private TextView messageTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        // Views initialization
        backToLoginBtn = findViewById(R.id.buttonBackId);
        registerBtn = findViewById(R.id.buttonLoginId);
        nameEt = findViewById(R.id.name2Id);
        password1Et = findViewById(R.id.pw1Id);
        password2Et = findViewById(R.id.pw2Id);
        messageTv = findViewById(R.id.message2Id);

        // Back to login button
        backToLoginBtn.setOnClickListener(v -> {
            Intent goToLoginActivity = new Intent(this, MainActivity.class);
            startActivity(goToLoginActivity);
        });

        // Register button
        // Need more verifications
        registerBtn.setOnClickListener(v -> {
            String usernameText = this.nameEt.getText().toString();
            String psw1Text = this.password1Et.getText().toString();
            String psw2Text = this.password2Et.getText().toString();


            // Query
            RealmQuery<Teacher> query = realm.where(Teacher.class);
            // Query conditions
            query.equalTo("name", this.nameEt.getText().toString());
            // Query results
            Teacher result = query.findFirst();


            //|| usernameText.length() < 3 || usernameText.length() > 8
            if(!usernameText.equals("")){
                if(result==null){
                    if(psw1Text.equals(psw2Text) && !psw1Text.equals("")){
                        // Adding Teachers to Realm
                        realm.executeTransaction(r -> {
                            Teacher teacher = realm.createObject(Teacher.class, UUID.randomUUID().toString());
                            teacher.setName(nameEt.getText().toString());
                            teacher.setPassword(password1Et.getText().toString());
                        });
                        Intent goToLoginActivity = new Intent(this, MainActivity.class);
                        startActivity(goToLoginActivity);
                    }
                    else{
                        Toast.makeText(this,"Password is not the same or is empty",Toast.LENGTH_SHORT).show();
                        // Giving wrong values!!!
                        //messageTv.setText(""+nameEt.toString().trim().trim().length());
                    }
                }
                else {
                    Toast.makeText(this,"This username already exist",Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(this,"Your name is empty",Toast.LENGTH_SHORT).show();
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
