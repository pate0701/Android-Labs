package com.example.lab1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    //protected Button myButton;
    private EditText email;
    private EditText password;
    private Button login;
    public static final String ACTIVITY_NAME = "MainActivity";
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);

        email=(EditText)findViewById(R.id.editTextEmail);
        password=(EditText)findViewById(R.id.editTextPassword);
        login=(Button)findViewById(R.id.buttonLogin);
        sharedPreferences = getSharedPreferences("SharedPreferenceFile", Context.MODE_PRIVATE);
        String savedString = sharedPreferences.getString("emailAddress", "").toString();
        email.setText(savedString);



        login.setOnClickListener(e->
        {
            Intent intent = new Intent(MainActivity.this,ProfileActivity.class);
            intent.putExtra("emailFromLastActivity",/*savedString*/email.getText().toString());
            startActivity(intent);
        });

    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.e(ACTIVITY_NAME,"onPause");

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("emailAddress", email.getText().toString());
        Log.e(ACTIVITY_NAME,"onPause"+email.getText().toString());
        editor.commit();
    }




}
















        /* //These are Lab 2 Files.

        setContentView(R.layout.activity_main_linear);
        setContentView(R.layout.activity_main_relative);
        setContentView(R.layout.activity_main_grid);

        myButton=(Button)findViewById(R.id.button1);
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, getResources().getString(R.string.toast_message), Toast.LENGTH_LONG).show();
            }
        });


        final EditText theEdit = findViewById(R.id.editText1);
        CheckBox cb = findViewById(R.id.checkBox1);
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton compoundButton, final boolean b) {

               // Toast.makeText(MainActivity.this, "Checkbox is " + b, Toast.LENGTH_LONG).show();
                Snackbar.make(theEdit, "Checkbox is " + b, Snackbar.LENGTH_LONG)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View click) {
                                compoundButton.setChecked(!b);
                            }
                        })
                        .show();

            }
        });

        Switch sw = findViewById(R.id.switch1);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton compoundButton, final boolean b) {

                //Toast.makeText(MainActivity.this, "Checkbox is " + b, Toast.LENGTH_LONG).show();
                Snackbar.make(theEdit, "Switch is " +(b==false?"off":"on"), Snackbar.LENGTH_LONG)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View click) {
                                compoundButton.setChecked(!b);
                            }


                        })
                        .show();

            }
        });



    }
}
*/