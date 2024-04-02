package com.example.roll;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText username=(EditText) findViewById(R.id.editTextText);
        EditText password=(EditText) findViewById(R.id.editTextTextPassword);
        button= (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //shubham mca
                if(username.getText().toString().equals("shubham") && password.getText().toString().equals("mca")){
                    openActivity2();
                }else
                    Toast.makeText(MainActivity.this,"failed",Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void openActivity2(){
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
        /*intent is an android software mechanism that allow user to coordinate
         the function of different activities to achieve a task.
         it simply a messaging object.
         */

        }
}