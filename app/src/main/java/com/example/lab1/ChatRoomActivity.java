
package com.example.lab1;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;


public class ChatRoomActivity extends AppCompatActivity {

    public static final String ACTIVITY_NAME="ChatRoomActivity";
    public ListView listView;
    public EditText editText;
    public List<MessageModel> listMessage = new ArrayList<>();
    public Button sendBtn;
    public Button receiveBtn;


        private MyOpener myOpener=new MyOpener(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        listMessage=myOpener.read();

        listView = (ListView)findViewById(R.id.listViewMsg);
        editText = (EditText)findViewById(R.id.editTextMessage);
        sendBtn = (Button)findViewById(R.id.buttonSend);
        receiveBtn = (Button)findViewById(R.id.buttonReceive);


        ChatAdapter adt=new ChatAdapter(listMessage,getApplicationContext());
        listView.setAdapter(adt);

        sendBtn.setOnClickListener(c -> {

            if(!editText.getText().toString().equals("")) {

                String message = editText.getText().toString();
                MessageModel messageModel = new MessageModel(message, "sender");
                listMessage.add(messageModel);
                myOpener.create(messageModel);
                editText.setText("");
                adt.notifyDataSetChanged();

            }
        });

        receiveBtn.setOnClickListener(c -> {

            if(!editText.getText().toString().equals("")){

                String message = editText.getText().toString();
                MessageModel messageModel = new MessageModel(message,"receiver");
                listMessage.add(messageModel);
                myOpener.create(messageModel);
                editText.setText("");
                adt.notifyDataSetChanged();

            }
        });

        listView.setOnItemLongClickListener( (p, b, pos, id) -> {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Do you want to delete this?")
                    .setPositiveButton("Yes", (click, arg) -> {

                        listMessage.remove(pos);


                        myOpener.delete(id);
                        ChatAdapter myAdapter = new ChatAdapter(listMessage,getApplicationContext());
                        listView.setAdapter(myAdapter);
                        myAdapter.notifyDataSetChanged();

                    })
                    //What the No button does:
                    .setNegativeButton("No", (click, arg) -> { })
                    .setMessage("The selected row is "+pos +"\nThe database id is "+id)
                    .setView(getLayoutInflater().inflate(R.layout.alert_dialog, null) )

                    //Show the dialog
                    .create().show();

            return true;
        });

        Log.d("ChatRoomActivity","onCreate");

    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.e(ACTIVITY_NAME, "in onstart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(ACTIVITY_NAME, "in onresume");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(ACTIVITY_NAME, "in onpause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(ACTIVITY_NAME, "in onstop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(ACTIVITY_NAME, "in ondestroy");
    }

}

 class ChatAdapter extends BaseAdapter {

    private List<MessageModel> messageModels;
    private Context context;
    LayoutInflater inflater;


    public ChatAdapter(List<MessageModel> messageModels, Context context) {
        this.messageModels = messageModels;
        this.context = context;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return messageModels.size();
    }

    @Override
    public Object getItem(int position) {
        return messageModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;

        if(messageModels.get(position).getType().equalsIgnoreCase("sender")){
            row = inflater.inflate(R.layout.row_send, parent, false);
            TextView message;
            message = (TextView) row.findViewById(R.id.textViewMessageSend);
            message.setText(messageModels.get(position).getMessage());
        }

        else{
            row = inflater.inflate(R.layout.row_receive, parent, false);
            TextView message;
            message = (TextView) row.findViewById(R.id.textViewMessageReceive);
            message.setText(messageModels.get(position).getMessage());
        }

        return (row);

    }


}