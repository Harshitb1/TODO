package com.example.sagar.to_do;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by SAGAR on 07-03-2018.
 */

public class todoOpenHelper extends SQLiteOpenHelper {
    public static todoOpenHelper openHelper;
    public static todoOpenHelper getInstance(Context context){
        if(openHelper == null){
            openHelper = new todoOpenHelper(context.getApplicationContext());
        }
        return  openHelper;
    }
    public todoOpenHelper(Context context) {

        super(context,Contract.DATABASE_NAME, null, Contract.VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String expensesSql = "CREATE TABLE " + Contract.todo.TABLE_NAME  + " ( " +
                Contract.todo.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Contract.todo.TITLE + " TEXT, " +
                Contract.todo.DESCRIPTION + " TEXT)";
        db.execSQL(expensesSql);
        String commentsSql = "CREATE TABLE " + Contract.Comments.TABLE_NAME + " ( " +
                Contract.Comments.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Contract.Comments.COMMENT + " TEXT, " +
                Contract.Comments.TODO_ID + " INTEGER,  " +
                "FOREIGN KEY (" + Contract.Comments.TODO_ID + ") REFERENCES " + Contract.todo.TABLE_NAME + " (" + Contract.todo.ID + ") ON DELETE CASCADE)";
        db.execSQL(commentsSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
