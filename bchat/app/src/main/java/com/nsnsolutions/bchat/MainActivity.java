package com.nsnsolutions.bchat;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

/**
 * Opening user activities are defined in this class
 */
public class MainActivity extends AppCompatActivity {


    CheckBox enable_bt, visible_bt ;
    ImageView pair_bt;
    TextView name_bt;
    ListView listView;
    ImageView refresh;

    private BluetoothAdapter BA;
    private Set<BluetoothDevice> PairedDevices;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enable_bt = findViewById(R.id.enable_bt);
        visible_bt = findViewById(R.id.visible_bt);
        name_bt = findViewById(R.id.name_bt);
        listView = findViewById(R.id.list_view);
        pair_bt = findViewById(R.id.pair_bt);
        refresh = findViewById(R.id.refresh);

        name_bt.setText("  "+getLocalBluetoothName()); //to get the local bluetooth user name
        BA = BluetoothAdapter.getDefaultAdapter();

        if (BA == null) { //if bluetooth is not supported
            Toast.makeText(this, "Bluetooth not supported", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (BA.isEnabled()) {
            enable_bt.setChecked(true);
        }

        list(); //to display paired devices

        enable_bt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { //event listener for the bluetooth enable button
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) { //if button was unchecked
                    BA.disable();
                    Toast.makeText(MainActivity.this, "Turned off", Toast.LENGTH_SHORT).show(); //display the message
                } else { //if button was checked
                    Intent intentOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(intentOn, 0);
                    Toast.makeText(MainActivity.this, "Turned On", Toast.LENGTH_SHORT).show();
                }
            }
        });

        visible_bt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { //event listener for the bluetooth visibility
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) { //if button checked
                    Intent getVisible = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                    startActivityForResult(getVisible, 0);
                    Toast.makeText(MainActivity.this, "Visible for 2 min", Toast.LENGTH_SHORT).show();
                }
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() { //event listener for the refresh button
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Refreshing the chat list", Toast.LENGTH_SHORT).show();
                list(); //display the paired devices list
            }
        });



    }

    /**
     * A function to use at a app resume
     */
    @Override
    public void onResume(){ //In case of the user open app after opening and running at the background
        super.onResume();
        list();
    }


    /**
     * A function to go to add new user activity
     */
    public void pairView(View view){ //if user click the add new user button
        Intent intent = new Intent(this,PairView.class);
        startActivity(intent); //go to the Pair View activity
    }


    /**
     * A function to display all the paired bluetooth users of the phone
     * */
    private void list() {
        PairedDevices = BA.getBondedDevices();
        final ArrayList list = new ArrayList();
        final ArrayList<BluetoothDevice> devices = new ArrayList<BluetoothDevice>();
        for (BluetoothDevice bt : PairedDevices) {
            list.add(bt.getName());
            devices.add(bt);
        }

        // to populate listview
        Toast.makeText(this, "Showing Devices", Toast.LENGTH_SHORT).show();
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter); //adding to the list view
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) { //if user click at a one of the user name
                Intent intent = new Intent(MainActivity.this,ChatView.class);
                Object obj = adapterView.getItemAtPosition(i);
                String val = obj.toString();
                BluetoothDevice blt = devices.get(i);
                intent.putExtra("chater", val);
                intent.putExtra("deviceDetail",blt);
                startActivity(intent); //go to the chat view

            }

        });
    }


    /**
     * A function to return the local bluetooth name of the user
     */
    public String getLocalBluetoothName() {
        if (BA == null) {
            BA = BluetoothAdapter.getDefaultAdapter();
        }
        String name = BA.getName();
        if (name == null) {
            name = BA.getAddress();
        }
        return name;
    }



}
