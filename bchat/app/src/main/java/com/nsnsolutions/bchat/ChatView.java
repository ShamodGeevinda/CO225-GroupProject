package com.nsnsolutions.bchat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.nio.charset.Charset;
import java.util.UUID;

/**
 * ChatView is a class that holds all the funtion of the chat user interface
 */
public class ChatView extends AppCompatActivity  {

    TextView chatName;
    BluetoothDevice device, mBTDevice;
    ImageView connectBt;
    ImageView send_bt, imageBack;
    EditText etSend;
    ListView chat;

    ArrayAdapter messageArrayAdapter;
    List<String> message = new ArrayList<String >();
    List<Message> messageList = new ArrayList<>();
    ChatDatabase chatDatabase = new ChatDatabase(ChatView.this);

    private static final UUID MY_UUID_INSECURE =
            UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");
    BluetoothConnection mBluetoothConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_view);

        chatName = findViewById(R.id.chatname);
        connectBt = findViewById(R.id.connect_bt);
        send_bt = findViewById(R.id.send_bt);
        etSend = findViewById(R.id.data);
        chat = findViewById(R.id.incommingMessage);
        imageBack = findViewById(R.id.imageBack);

        // retrieving data to add chat details in the top(header)
        String chatername = getIntent().getStringExtra("chater");
        device = getIntent().getExtras().getParcelable("deviceDetail");
        chatName.setText(chatername);

        //to update the list view
        messageList = chatDatabase.getAllChats(device.getAddress()); //getting all the message list relevant chat
        message.clear(); //clear the message array
        for(Message msg: messageList){
                message.add(msg.getMessage());
           }
        messageArrayAdapter = new ArrayAdapter<String>(ChatView.this, android.R.layout.simple_list_item_1, message);
        chat.setAdapter(messageArrayAdapter);

        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter("incommingMessage"));

        // to catch bond state changes
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(mBroadcastReciever, filter);



        connectBt.setOnClickListener(new View.OnClickListener() { //event listener to connectButton
            @Override
            public void onClick(View view) {
                Toast.makeText(ChatView.this, "Connecting to the user", Toast.LENGTH_SHORT).show();
                device.createBond();
                mBTDevice = device;
                mBluetoothConnection = new BluetoothConnection(ChatView.this);
                Log.d("Main Activity",device.getName() + device.getAddress() + device.getBondState());

                startConnection();
            }
        });


        send_bt.setOnClickListener(new View.OnClickListener() { //Event listener to the send button
            @Override
            public void onClick(View view) {
                try {
                    byte[] bytes = etSend.getText().toString().getBytes(Charset.defaultCharset());
                    Message message1 = new Message("You : "+ etSend.getText().toString(), "S", device.getAddress());
                    mBluetoothConnection.write(bytes);

                    chatDatabase.addChat(message1);//update the database
                    messageList = chatDatabase.getAllChats(device.getAddress()); //retrieve data from the database
                    message.clear();
                    for(Message msg: messageList){
                        message.add(msg.getMessage());
                    }
                    messageArrayAdapter.notifyDataSetChanged();

                    etSend.setText(""); //setting the text box empty after successful send
                }catch (Exception e){
                    Toast.makeText(ChatView.this, "Cant connect to the user!\nTry Again!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        imageBack.setOnClickListener(new View.OnClickListener() { //event listner to the back button
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent); //go to the main activity interface
            }
        });
    }

    BroadcastReceiver mReceiver = new BroadcastReceiver() { //to receive message
        @Override
        public void onReceive(Context context, Intent intent) {
            String text = intent.getStringExtra("theMessage");
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
            Message message1 = new Message(device.getName()+" : "+text , "R", device.getAddress());
            chatDatabase.addChat(message1); //add message to the database
            messageList = chatDatabase.getAllChats(device.getAddress());
            message.clear(); // clear the message array
            for(Message msg: messageList){
                message.add(msg.getMessage());
            }
            messageArrayAdapter.notifyDataSetChanged(); //update the list view

        }
    };

    private final BroadcastReceiver mBroadcastReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
                BluetoothDevice mDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // 3 cases
                // bonded already
                if (mDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
                        mBTDevice = mDevice;
                }
                // creating a bond
                if (mDevice.getBondState() == BluetoothDevice.BOND_BONDING) {

                }
                // breaking a bond
                if (mDevice.getBondState() == BluetoothDevice.BOND_NONE) {

                }
            }
        }
    };



    //create method for starting connection
    //***remember the conncction will fail and app will crash if you haven't paired first
    public void startConnection(){
        startBTConnection(mBTDevice,MY_UUID_INSECURE);
    }

    /**
     * starting chat service method
     */
    public void startBTConnection(BluetoothDevice device, UUID uuid){
        mBluetoothConnection.startClient(device,uuid);
    }


}