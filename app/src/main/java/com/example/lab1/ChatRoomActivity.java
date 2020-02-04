
package com.example.lab1;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;


public class ChatRoomActivity extends AppCompatActivity {

    ListView listView;
    EditText editText;
    List<MessageModel> listMessage = new ArrayList<>();
    Button sendBtn;
    Button receiveBtn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        listView = (ListView)findViewById(R.id.listViewMsg);
        editText = (EditText)findViewById(R.id.editTextMessage);
        sendBtn = (Button)findViewById(R.id.buttonSend);
        receiveBtn = (Button)findViewById(R.id.buttonReceive);

        sendBtn.setOnClickListener(c -> {
            String message = editText.getText().toString();
            MessageModel model = new MessageModel(message, true);
            listMessage.add(model);
            editText.setText("");
            ChatAdapter adt = new ChatAdapter(listMessage, getApplicationContext());
            listView.setAdapter(adt);
        });

        receiveBtn.setOnClickListener(c -> {
            String message = editText.getText().toString();
            MessageModel model = new MessageModel(message, false);
            listMessage.add(model);
            editText.setText("");
            ChatAdapter adt = new ChatAdapter(listMessage, getApplicationContext());
            listView.setAdapter(adt);
        });



        Log.d("ChatRoomActivity","onCreate");

    }

}



/*

This is lab 4 which was not Working,so i started creating this again from scratch.


public class ChatRoomActivity extends AppCompatActivity {

    //  Declaring SendButton,ReceiveButton,Listview,editTextMessage,ListMessage;
    private Button sendButton;
    private Button receiveButton;
    private ListView listView;
    private EditText editTextMessage;
    private ArrayList<MessageModel> listMessage = new ArrayList<>( );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        sendButton= findViewById(R.id.buttonSend);
        receiveButton= findViewById(R.id.buttonReceive);
        listView= findViewById(R.id.listViewMsg);
        editTextMessage= findViewById(R.id.editTextMessage);

        sendButton.setOnClickListener(click ->{
            String message=editTextMessage.getText().toString();
            MessageModel model=new MessageModel(message,true);
            listMessage.add(model);
            editTextMessage.setText(null);
            ChatAdapter adp=new ChatAdapter(listMessage,getApplicationContext());
            listView.setAdapter(adp);
        });

        receiveButton.setOnClickListener(e->{
            String message=editTextMessage.getText().toString();
            MessageModel model=new MessageModel(message,false);
            listMessage.add(model);
            editTextMessage.setText(null);
            ChatAdapter adp=new ChatAdapter(listMessage,getApplicationContext());
            listView.setAdapter(adp);
        });
    }
}
class ChatAdapter extends BaseAdapter {
    private List<MessageModel> messageModels;
    private Context context;
    private LayoutInflater inflater;

    public ChatAdapter(ArrayList<MessageModel> messageModels, Context context) {
        this.messageModels = messageModels;
        this.context=context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        return (long) position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            if (messageModels.get(position).isSend()) {
                view = inflater.inflate(R.layout.row_send, null);
            } else {
                view = inflater.inflate(R.layout.row_receive, null);
            }
            TextView messageText = (TextView)view.findViewById(R.id.textViewMessage);
            messageText.setText(messageModels.get(position).message);
        }
        return view;
    }

}

 */