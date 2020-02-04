package com.example.lab1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private EditText emailText;
    //private EditText email=(EditText)findViewById(R.id.editTextEmail);
    private ImageButton mImageButton;
    public static final String ACTIVITY_NAME = "PROFILE_ACTIVITY";
    private Button goToChatButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        emailText = (EditText) findViewById(R.id.emailTextID);
        mImageButton =(ImageButton) findViewById(R.id.imageButtonID);

        Intent intent = getIntent();
        emailText.setText(intent.getStringExtra("emailFromLastActivity"));

        mImageButton.setOnClickListener(e->{
                dispatchTakePictureIntent();
        });
        Log.e(ACTIVITY_NAME,"onCreate");

        goToChatButton=(Button)findViewById(R.id.buttonGoToChat);
        goToChatButton.setOnClickListener(e->
            {
                Intent goToChatIntent=new Intent(ProfileActivity.this,ChatRoomActivity.class);
                startActivity(goToChatIntent);
            });
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(ACTIVITY_NAME,"onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageButton.setImageBitmap(imageBitmap);
        }
    }

    @Override
    protected void onStart() {

        super.onStart();
        Log.e(ACTIVITY_NAME,"onStart");
    }

    @Override
    protected void onResume() {

        super.onResume();
        Log.e(ACTIVITY_NAME,"onResume");
    }

    @Override
    protected void onStop() {

        super.onStop();
        Log.e(ACTIVITY_NAME,"onStop");
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        Log.e(ACTIVITY_NAME,"onDestroy");
    }


}
