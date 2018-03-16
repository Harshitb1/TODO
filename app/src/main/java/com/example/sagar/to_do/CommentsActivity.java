package com.example.sagar.to_do;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class CommentsActivity extends AppCompatActivity {
    ArrayList<String> comments = new ArrayList<>();
    ArrayList<Integer> IDS = new ArrayList<>();
    Bundle bundle;
    int todoId;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        ListView listView = findViewById(R.id.commentsListView);
        Intent intent = getIntent();
        bundle = intent.getExtras();
        if(bundle != null){
            int id = bundle.getInt(Constants.ID_KEY,-1);
            todoId=id;
            comments = fetchCommentsFromDB(id);
        }

         adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,comments);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent detailIntent = new Intent(CommentsActivity.this,CommentsFormActivity.class);
//                Bundle bundle = new Bundle();
                String str= comments.get(position);
//                bundle.putInt(Constants.ID_KEY,(int)id);
                bundle.putInt(Constants.POSITION_KEY_COMMENT,position);
                bundle.putString(Constants.COMMENT_KEY,str);
                bundle.putBoolean("isAlreadyPresent",true);
                detailIntent.putExtras(bundle);
                startActivityForResult(detailIntent,Constants.EDIT_COMMENT_REQUEST);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, final long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(CommentsActivity.this);
                builder.setTitle("Delete Item?");
                builder.setMessage("Are you sure you want to delete this Item? You can not undo it.");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        todoOpenHelper openHelper = todoOpenHelper.getInstance(CommentsActivity.this);
                        SQLiteDatabase database = openHelper.getWritableDatabase();
                        int id=IDS.get(position);
                        String[] ids = {id +""};
                        database.delete(Contract.Comments.TABLE_NAME,Contract.Comments.ID + " = ?",ids);
                        comments.remove(position);
                        IDS.remove(position);
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
                builder.show();
                return true;
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.comments_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.ac) {
            Intent intent = new Intent(this,CommentsFormActivity.class);
            bundle.putBoolean("isAlreadyPresent",false);
            intent.putExtras(bundle);
            startActivityForResult(intent,Constants.ADD_COMMENT_REQUEST);
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       if(requestCode==Constants.ADD_COMMENT_REQUEST){
           if(resultCode==Constants.SAVE_SUCCESS_RESULT){
               Bundle b=data.getExtras();
                todoOpenHelper openHelper = todoOpenHelper.getInstance(this);
               String str = b.getString(Constants.COMMENT_KEY);
               int id1 = b.getInt(Constants.ID_KEY, -1);
               SQLiteDatabase database = openHelper.getWritableDatabase();
               ContentValues contentValues = new ContentValues();
               contentValues.put(Contract.Comments.COMMENT,str);
               contentValues.put(Contract.Comments.TODO_ID,id1);
               database.insert(Contract.Comments.TABLE_NAME,null,contentValues);
               ArrayList<String> comment1=fetchCommentsFromDB(id1);
               //may be error here
               comments.addAll(comment1);
               adapter.notifyDataSetChanged();
           }
       }
        if(requestCode==Constants.EDIT_COMMENT_REQUEST){
            if(resultCode==Constants.SAVE_SUCCESS_RESULT){
                Bundle b=data.getExtras();
                todoOpenHelper openHelper = todoOpenHelper.getInstance(this);
                String str = b.getString(Constants.COMMENT_KEY);
                int id1 = b.getInt(Constants.ID_KEY, -1);
                int position= b.getInt(Constants.POSITION_KEY_COMMENT);
                SQLiteDatabase database = openHelper.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put(Contract.Comments.COMMENT,str);
                contentValues.put(Contract.Comments.TODO_ID,todoId);
                int id =IDS.get(position);
                String[] args={id+""};
                database.update(Contract.Comments.TABLE_NAME,contentValues,Contract.Comments.ID +"=?",args);
                comments.set(position,str);
                adapter.notifyDataSetChanged();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private ArrayList<String> fetchCommentsFromDB(int id) {
        comments.clear();
        IDS.clear();
        ArrayList<String> comments = new ArrayList<>();
        todoOpenHelper openHelper = todoOpenHelper.getInstance(this);
        SQLiteDatabase database = openHelper.getReadableDatabase();
        String[] columnNames = {Contract.Comments.COMMENT,Contract.Comments.ID};
        String[] selectionArgs = {id +""};
        Cursor cursor = database.query(Contract.Comments.TABLE_NAME,columnNames,Contract.Comments.TODO_ID + " = ?",selectionArgs,null,null,null);
        while (cursor.moveToNext()){
            String comment = cursor.getString(cursor.getColumnIndex(Contract.Comments.COMMENT));
            int comment_id = cursor.getInt(cursor.getColumnIndex(Contract.Comments.ID));
            IDS.add(comment_id);
            comments.add(comment);
        }
        return comments;
    }
}
