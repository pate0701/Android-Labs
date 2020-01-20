package com.example.lab1;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {


    protected Button myButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.activity_main_linear);
        //setContentView(R.layout.activity_main_relative);
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
