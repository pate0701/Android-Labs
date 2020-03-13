
package com.example.lab1;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ChatRoomActivity extends AppCompatActivity {

    private static final String TAG = "ChatRoomActivity";

    public static final String MSG_SELECTED = "MSG";
    public static final String MSGDB_ID = "ID";
    public static final String MSG_POSITION = "POSITION";
    public static final String SENT = "SENT";
    public static final String RECEIVED = "RECEIVED";
    public static final int EMPTY_ACTIVITY = 345;

    MyOpener myDb;
    private ListView chatListView;
    private EditText chatText;
    private Button btnSend, btnReceive;
    private String typedMessage;
    long matchId;
    int savedPosition;


    private ArrayList<MessageModel> mMessageArrayList = new ArrayList<MessageModel>();
    ChatListAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        myDb = new MyOpener(this);


        btnSend = (Button) findViewById(R.id.BtnSend);
        btnReceive = (Button) findViewById(R.id.BtnReceive);
        chatText = (EditText) findViewById(R.id.chatText);
        chatListView = (ListView) findViewById(R.id.chatListView);
        boolean isTablet = findViewById(R.id.fragmentLocation) != null; //check if the FrameLayout is loaded

        chatAdapter = new ChatListAdapter();
        chatListView.setAdapter((ListAdapter) chatAdapter);

        //viewAll();

        Cursor res = myDb.getAllData();
        if (res.getCount() == 0) {
            Toast.makeText(ChatRoomActivity.this, "No data found", Toast.LENGTH_LONG).show();
        }

        while (res.moveToNext()) {
            if (res.getInt(2) == 1) {
                MessageModel chatMessage = new MessageModel(true, false, res.getString(1), res.getLong(0));
                //Log.d("message: ",res.getString(1));
                mMessageArrayList.add(chatMessage);
                chatAdapter = new ChatListAdapter();
                chatListView.setAdapter((ListAdapter) chatAdapter);
            } else if (res.getInt(2) == 0) {
                MessageModel chatMessage = new MessageModel(false, true, res.getString(1), res.getLong(0));
                mMessageArrayList.add(chatMessage);
                chatAdapter = new ChatListAdapter();
                chatListView.setAdapter(chatAdapter);
            }
        }

        chatListView.setOnItemLongClickListener((p, b, pos, id) -> {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Do you want to delete this?")
                    .setPositiveButton("Yes", (click, arg) -> {
                        ChatListAdapter myAdapter = new ChatListAdapter(mMessageArrayList, getApplicationContext());
                        chatListView.setAdapter(myAdapter);
                        mMessageArrayList.remove(pos);
                        myDb.deleteData(String.valueOf(id));
                        myAdapter.notifyDataSetChanged();

                    })
                    //What the No button does:
                    .setNegativeButton("No", (click, arg) -> {
                    })
                    .setMessage("The selected row is " + pos + "\nThe database id is " + id)
                    .setView(getLayoutInflater().inflate(R.layout.alert_dialog, null))

                    //Show the dialog
                    .create().show();

            return true;
        });

        ;

        //printCursor(res);

        //FRAGMENT block for Lab8
        chatListView.setOnItemClickListener((list, item, position, id) -> {

            Bundle dataToPass = new Bundle();
            savedPosition = position;
            dataToPass.putString(MSG_SELECTED, mMessageArrayList.get(position).getMessage());
            dataToPass.putInt(MSG_POSITION, position);

            Cursor res2 = myDb.getAllData();
            if (res2.getCount() != 0) {
                res2.moveToFirst();
                do {
                    if (res2.getString(1).equals(mMessageArrayList.get(position).getMessage()))
                        matchId = res2.getLong(0);
                } while (res2.moveToNext());
            }

            dataToPass.putLong(MSGDB_ID, matchId);
            if (mMessageArrayList.get(position).isLeft())
                dataToPass.putString(RECEIVED, "Received");
            else
                dataToPass.putString(SENT, "Sent");

            if (isTablet) {
                DetailsFragment dFragment = new DetailsFragment(); //add a DetailFragment
                dFragment.setArguments(dataToPass); //pass it a bundle for information
                dFragment.setTablet(true);  //tell the fragment if it's running on a tablet or not
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragmentLocation, dFragment) //Add the fragment in FrameLayout
                        .addToBackStack("Back") //make the back button undo the transaction
                        .commit(); //actually load the fragment.
            } else //isPhone
            {
                Intent nextActivity = new Intent(ChatRoomActivity.this, EmptyActivity.class);
                nextActivity.putExtras(dataToPass); //send data to next activity
                startActivityForResult(nextActivity, EMPTY_ACTIVITY); //make the transition
            }
        });


    }


    public void sendChatMessage(View args0) {
        Cursor res = myDb.getAllData();
        long id = res.getCount() + 1;
        typedMessage = chatText.getText().toString();
        //insert sent message to database
        boolean isInserted = myDb.insertData(id, typedMessage, 1, 0);
        if (isInserted)
            Toast.makeText(ChatRoomActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(ChatRoomActivity.this, "Data not Inserted", Toast.LENGTH_LONG).show();

        //insert sent message to arraylist
        MessageModel chatMessage = new MessageModel(true, false, typedMessage, id);
        mMessageArrayList.add(chatMessage);
        ChatListAdapter chatAdapter = new ChatListAdapter();
        chatListView.setAdapter(chatAdapter);
        chatText.setText("");
        chatAdapter.notifyDataSetChanged();
    }


    public void receiveChatMessage(View args0) {
        Cursor res = myDb.getAllData();
        long id = res.getCount() + 1;
        typedMessage = chatText.getText().toString();

        //insert received message to database
        boolean isInserted = myDb.insertData(id, typedMessage, 0, 1);
        if (isInserted)
            Toast.makeText(ChatRoomActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(ChatRoomActivity.this, "Data not Inserted", Toast.LENGTH_LONG).show();

        //insert received message to arraylist
        MessageModel chatMessage = new MessageModel(false, true, typedMessage, id);
        mMessageArrayList.add(chatMessage);
        ChatListAdapter chatAdapter = new ChatListAdapter();
        chatListView.setAdapter(chatAdapter);
        chatText.setText("");
        chatAdapter.notifyDataSetChanged();
    }


    private class ChatListAdapter extends BaseAdapter implements ListAdapter {

        private static final String TAG = "ChatListAdapter";
        private Context mContext;
        private String mMessage;
        LayoutInflater inflater;
        private int mResource;

        public ChatListAdapter(ArrayList<MessageModel> mMessageArrayList, Context applicationContext) {
            this.mContext = applicationContext;
            this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public ChatListAdapter() {

        }

        public int getCount() {
            return mMessageArrayList.size();
        } //This function tells how many objects to show

        public Object getItem(int position) {
            return mMessageArrayList.get(position);
        }  //This returns the string at position p

        public long getItemId(int p) {
            return p;
        } //This returns the database id of the item at position p

        public View getView(int p, View customView, ViewGroup parent) {
            View thisRow = customView;

            if (mMessageArrayList.get(p).isRight()) {
                thisRow = getLayoutInflater().inflate(R.layout.row_send, null);
                TextView sendTextView = (TextView) thisRow.findViewById(R.id.msgSent);
                sendTextView.setText(mMessageArrayList.get(p).getMessage());
            } else if (mMessageArrayList.get(p).isLeft()) {
                thisRow = getLayoutInflater().inflate(R.layout.row_receive, null);
                TextView receiveTextView = (TextView) thisRow.findViewById(R.id.msgReceived);
                receiveTextView.setText(mMessageArrayList.get(p).getMessage());
            }
            return thisRow;

        }
    }

    public void printCursor(Cursor c) {
        String strDatabaseVersion = "Database version number: " + myDb.DATABASE_VERSION;
        String strNumberOfColumns = "Number of columns = " + c.getColumnCount();
        String strNumberOfResults = "Number of results = " + c.getCount();
        String strColumnNames = "Name of the columns: "
                + c.getColumnName(0) + ", "
                + c.getColumnName(1) + ", "
                + c.getColumnName(2) + ", "
                + c.getColumnName(3);
        StringBuffer buffer = new StringBuffer();
        c.moveToFirst();
        do {
            buffer.append("Id: " + c.getString(0) + " ");
            buffer.append("Message: " + c.getString(1) + " ");
            buffer.append("Issent: " + c.getString(2) + " ");
            buffer.append("Isreceived: " + c.getString(3) + "\n");
        } while (c.moveToNext());

        Log.i(TAG, strDatabaseVersion + "\n" + strNumberOfColumns + "\n" + strColumnNames
                + "\n" + strNumberOfResults + "\n" + buffer.toString());

    }

    //This function only gets called on the phone. The tablet never goes to a new activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EMPTY_ACTIVITY) {
            if (resultCode == RESULT_OK) //if you hit the delete button instead of back button
            {
                long id = data.getLongExtra(MSGDB_ID, 0);
                deleteMessageId((int) id);
            }
        }
    }

    public void deleteMessageId(int id) {
        Log.i("Delete this message:", " id=" + id);
        mMessageArrayList.remove(mMessageArrayList.get(savedPosition).getId());
        myDb.deleteData(Long.toString(matchId));
        chatAdapter.notifyDataSetChanged();

        //Refreshing data in chat view and mMessageArrayList by loading from database
        Cursor res = myDb.getAllData();
        mMessageArrayList.clear();
        while (res.moveToNext()) {
            if (res.getInt(2) == 1) {
                MessageModel chatMessage = new MessageModel(true, false, res.getString(1), res.getLong(0));
                //Log.d("message: ",res.getString(1));
                mMessageArrayList.add(chatMessage);
                chatAdapter = new ChatListAdapter();
                chatListView.setAdapter(chatAdapter);
            } else if (res.getInt(2) == 0) {
                MessageModel chatMessage = new MessageModel(false, true, res.getString(1), res.getLong(0));
                mMessageArrayList.add(chatMessage);
                chatAdapter = new ChatListAdapter();
                chatListView.setAdapter(chatAdapter);
            }
        }
        ;
    }

}



/*public class ChatRoomActivity extends AppCompatActivity {

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
        boolean isTablet= findViewById(R.id.fragmentLocation) != null;

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


}*/
