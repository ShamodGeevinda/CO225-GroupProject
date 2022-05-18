package com.nsnsolutions.bchat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ChatDatabase extends SQLiteOpenHelper {

    public static final String COL_MAC = "MAC";
    public static final String CHATS = "CHATS";
    public static final String MESSAGE = "MESSAGE";
    //public static final String STIME = "STIME";
    public static final String USER = "USER";
    public static final String MSG_COUNT = "MSG_COUNT";

    public ChatDatabase(@Nullable Context context) {
        super(context, "chat.db", null , 1);
    }




    //when calling the method for the first time
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //String createChatTable = "CREATE TABLE " + CHATS + " (" + MSG_COUNT+ " INTEGER PRIMARY KEY AUTOINCREMENT, "+ COL_MAC + " VARCHAR(100), " + MESSAGE +" VARCHAR(1000), "+ STIME +" VARCHAR(100), "+USER +" VARCHAR(1))";
        String createChatTable = "CREATE TABLE " + CHATS + " (" + MSG_COUNT+ " INTEGER PRIMARY KEY AUTOINCREMENT, "+ COL_MAC + " VARCHAR(100), " + MESSAGE +" VARCHAR(1000), "+USER +" VARCHAR(1))";
        sqLiteDatabase.execSQL(createChatTable);

    }

    //when version of the database is changed
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean addChat(Message message){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues cVal = new ContentValues();
        cVal.put(COL_MAC, message.getMac().replaceAll("[^a-zA-Z0-9]", ""));
        cVal.put(MESSAGE, message.getMessage());
        //cVal.put(STIME, message.getTime());
        cVal.put(USER, message.getUser());

        long insert = sqLiteDatabase.insert(CHATS, null, cVal);
        return insert != -1;
    }


    public List<Message> getAllChats(String macAddress){
        List<Message> allChats = new ArrayList<>();

        //getting data from the database

        String usersQuery =  "SELECT * FROM "+ CHATS;//+" WHERE "+COL_MAC+" = "+macAddress.replaceAll("[^a-zA-Z0-9]", "");
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(usersQuery, null);

        if(cursor.moveToFirst()){
            do{
                String mac = cursor.getString(1);
                String message = cursor.getString(2);
                //String time = cursor.getString(3);
                String user = cursor.getString(3);

                if(mac.equals(macAddress.replaceAll("[^a-zA-Z0-9]", "")) ){
                    Message messages = new Message(message, mac, user);
                    allChats.add(messages);
                }
            }while(cursor.moveToNext());
        }else{
            //nothing added to the list
        }
        //closing cursor and the database
        cursor.close();
        sqLiteDatabase.close();
        //returning the array
        return allChats;
    }


}
