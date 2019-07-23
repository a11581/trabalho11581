package com.example.trabalho_v2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trabalho_v2.Model.Teacher;
import io.realm.Realm;
import io.realm.RealmQuery;

public class MainActivity extends AppCompatActivity {

    //Realm declaration
    private Realm realm;

    // Views declaration
    private Button loginBtn, registerBtn;
    private TextView messageTv;
    private EditText nameEt, passwordEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Views initialization
        loginBtn = findViewById(R.id.buttonLoginId);
        registerBtn = findViewById(R.id.buttonRegisterId);
        messageTv = findViewById(R.id.messageId);
        nameEt=findViewById(R.id.name2Id);
        passwordEt=findViewById(R.id.passwordId);


        registerBtn.setOnClickListener(v -> {
            Intent goToRegisterActivity = new Intent(this, RegisterActivity.class);
            startActivity(goToRegisterActivity);
        });

        addNecessaryHandlers();

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

    private void addNecessaryHandlers() {

        loginBtn.setOnClickListener(view -> {
            // Query
            RealmQuery<Teacher> query = realm.where(Teacher.class);
            // Query conditions
            query.equalTo("name", this.nameEt.getText().toString());
            // Query results
            Teacher result = query.findFirst();

            if(result==null){
                Toast.makeText(this,"This username does not exist",Toast.LENGTH_SHORT).show();
            }
            else{
                String usernameText = this.nameEt.getText().toString();
                boolean usernameIsNotEmpty = !usernameText.equals("");
                String psw1Text = this.passwordEt.getText().toString();
                String psw2Text = result.getPassword();
                boolean passwordFieldsAreNotEmpty = !psw1Text.equals("") && !psw2Text.equals("");
                boolean passwordsAreEqual = psw1Text.equals(psw2Text);

                if(usernameIsNotEmpty && passwordFieldsAreNotEmpty && passwordsAreEqual) {
                    Intent goToMenuActivity = new Intent(this,MenuActivity.class);
                    goToMenuActivity.putExtra("username", usernameText);
                    startActivity(goToMenuActivity);
                }
                else {
                    Toast.makeText(this,"Username is empty or passwords text is different",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
