package com.example.lab1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.TextView;


public class DetailsFragment extends Fragment {

    private boolean isTablet;
    private Bundle dataFromActivity;
    private long id;
    private AppCompatActivity parentActivityHide;

    public void setTablet(boolean tablet) { isTablet = tablet; }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dataFromActivity = getArguments();
        id = dataFromActivity.getLong(ChatRoomActivity.MSGDB_ID );

        // Inflate the layout for this fragment
        View result =  inflater.inflate(R.layout.fragment_details, container, false);

        //show the message
        TextView message = (TextView)result.findViewById(R.id.message);
        message.setText(dataFromActivity.getString(ChatRoomActivity.MSG_SELECTED));

        //show the id:
        TextView idView = (TextView)result.findViewById(R.id.idText);
        idView.setText("ID=" + id);

        //show sent or received
        CheckBox sentReceived = (CheckBox) result.findViewById(R.id.sentReceived);
        if(dataFromActivity.getString(ChatRoomActivity.RECEIVED)!=null)sentReceived.setSelected(false);
            //sentReceived.setText(dataFromActivity.getString(ChatRoomActivity.RECEIVED));
        if(dataFromActivity.getString(ChatRoomActivity.SENT)!=null)
            //sentReceived.setText(dataFromActivity.getString(ChatRoomActivity.SENT));
            sentReceived.setSelected(true);

        // get the Hide button, and add a click listener:
        Button hideButton = (Button)result.findViewById(R.id.hideButton);
        hideButton.setOnClickListener( clk -> {

            if(isTablet) { //both the list and details are on the screen:
                ChatRoomActivity parent = (ChatRoomActivity)getActivity();
                parent.getSupportFragmentManager().beginTransaction().remove(this).commit();
                parent.finish();
            }

            else
            {
                EmptyActivity parent = (EmptyActivity) getActivity();
                Intent backToFragmentExample = new Intent();
                parent.getSupportFragmentManager().beginTransaction().remove(this).commit();
                parent.finish();
            }
        });
        return result;
    }
}