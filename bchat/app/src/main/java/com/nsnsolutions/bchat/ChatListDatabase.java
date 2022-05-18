/*package com.nsnsolutions.test1;

import android.bluetooth.BluetoothDevice;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ChatListDatabase extends SQLiteOpenHelper {

    public static final String COL_RECEIVER_NAME = "RECEIVER_NAME";
    public static final String COL_MAC = "MAC";
    public static final String CHAT_LIST = "CHAT_LIST";
    public static final String ID = "ID";


    public ChatListDatabase(@Nullable Context context) {
        super(context, "chatList.db", null , 1);
    }

    //when calling the method for the first time
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createChatListTable = "CREATE TABLE " + CHAT_LIST + " (" +ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +COL_MAC + " VARCHAR(100), " + COL_RECEIVER_NAME + " VARCHAR(100))";
        sqLiteDatabase.execSQL(createChatListTable);


    }

    //when version of the database is changed
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean addUser(BluetoothDevice device) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues lVal = new ContentValues();
        lVal.put(COL_MAC, device.getAddress());
        lVal.put(COL_RECEIVER_NAME, device.getName());

        long insert = sqLiteDatabase.insert(CHAT_LIST, null, lVal);
        return insert != -1;
    }

    public List<BluetoothDevice> getAllUsers(){
        List<BluetoothDevice> allUsers = new ArrayList<>();

        //getting data from the database
        String usersQuery =  "SELECT DISTINCT * FROM "+ CHAT_LIST;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(usersQuery, null);

        if(cursor.moveToFirst()){
            do{
                String userMac = cursor.getString(1);
                String userName = cursor.getString(2);
                Receiver receiver = new Receiver(userMac, userName);
                allUsers.add(receiver);
            }while(cursor.moveToNext());
        }else{
                //nothing added to the list
        }
        //closing cursor and the database
        cursor.close();
        sqLiteDatabase.close();
        //returning the array
        return allUsers;
    }

}
*/