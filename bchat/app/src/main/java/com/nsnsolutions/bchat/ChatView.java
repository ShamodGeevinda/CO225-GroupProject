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

public class ChatView extends AppCompatActivity  {

    TextView chatName;
    BluetoothDevice device, mBTDevice;
    ImageView connectBt;
    ImageView send_bt, imageBack;
    EditText etSend;
    StringBuilder messages;
    ArrayAdapter messageArrayAdapter;
    List<String> message = new ArrayList<String >();
    ListView chat;
    ChatDatabase chatDatabase = new ChatDatabase(ChatView.this);
    List<Message> messageList = new ArrayList<>();

    private static final UUID MY_UUID_INSECURE =
            UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");
    // BluetoothAdapter mAdapter;
    //    private BluetoothAdapter BA;
    //    private Set<BluetoothDevice> PairedDevices;
    BluetoothConnection mBluetoothConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_view);

        chatName = findViewById(R.id.chatname);
        connectBt = findViewById(R.id.connect_bt);
        send_bt = findViewById(R.id.send_bt);
        //holder.imageView = (ImageButton) convertView.findViewById(R.id.iv_thumb);
        etSend = findViewById(R.id.data);
        chat = findViewById(R.id.incommingMessage);
        imageBack = findViewById(R.id.imageBack);

        // retrieving data to add chat details in the top(header)
        String chatername = getIntent().getStringExtra("chater");
        device = getIntent().getExtras().getParcelable("deviceDetail");

        chatName.setText(chatername);
        messageList = chatDatabase.getAllChats(device.getAddress());
        //Toast.makeText(this, messageList.toString(), Toast.LENGTH_SHORT).show();
        //try {
            //chatDatabase = new ChatDatabase(ChatView.this);

            //messageArrayAdapter = new ArrayAdapter<Message>(ChatView.this, android.R.layout.simple_list_item_1, messageList);
            for(Message msg: messageList){
                message.add("You: "+msg.getMessage());
           }
            //Toast.makeText(this, message.toString(), Toast.LENGTH_SHORT).show();
            messageArrayAdapter = new ArrayAdapter<String>(ChatView.this, android.R.layout.simple_list_item_1, message);
            chat.setAdapter(messageArrayAdapter);
        //}catch(NullPointerException n){

        //}

        //incommingMessages = (TextView)findViewById(R.id.incommingMessage) ;
        //messages = new StringBuilder();
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter("incommingMessage"));

        // to catch bond state changes
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(mBroadcastReciever, filter);



        connectBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ChatView.this, "Connecting to the user", Toast.LENGTH_SHORT).show();

                //BluetoothDevice newD = (BluetoothDevice) device;
                device.createBond();
                mBTDevice = device;
                mBluetoothConnection = new BluetoothConnection(ChatView.this);
                Log.d("Main Activity",device.getName() + device.getAddress() + device.getBondState());

                startConnection();


            }
        });


        send_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    byte[] bytes = etSend.getText().toString().getBytes(Charset.defaultCharset());
                    Message message1 = new Message("You : "+ etSend.getText().toString(), "S", device.getAddress());
                    mBluetoothConnection.write(bytes);
                    chatDatabase.addChat(message1);
                    messageList = chatDatabase.getAllChats(device.getAddress());
                    message.clear();
                    for(Message msg: messageList){
                        message.add(msg.getMessage());
                    }
                    messageArrayAdapter.notifyDataSetChanged();
                    //Toast.makeText(ChatView.this, device.getAddress(), Toast.LENGTH_SHORT).show();
                    //messageArrayAdapter = new ArrayAdapter<Message>(ChatView.this, android.R.layout.simple_list_item_1, chatDatabase.getAllChats(device.getAddress()));
                    //chat.setAdapter(messageArrayAdapter);

                    etSend.setText("");
                }catch (Exception e){
                    Toast.makeText(ChatView.this, "Cant connect to the user!\nTry Again!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String text = intent.getStringExtra("theMessage");
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
            Message message1 = new Message(device.getName()+" : "+text , "R", device.getAddress());
            chatDatabase.addChat(message1);
            messageList = chatDatabase.getAllChats(device.getAddress());
            message.clear();
            for(Message msg: messageList){
                message.add(msg.getMessage());
            }
            messageArrayAdapter.notifyDataSetChanged();
            //messages.append(text+ "\n");
            //incommingMessages.setText(messages);
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
        //Log.d(TAG, "startBTConnection: Initializing RFCOM Bluetooth Connection.");

        mBluetoothConnection.startClient(device,uuid);
    }


}