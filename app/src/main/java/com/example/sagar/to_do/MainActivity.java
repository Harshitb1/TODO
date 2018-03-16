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
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<todo> todoList;
    ListView listView;
    todoOpenHelper openHelper;
    todoAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        openHelper = todoOpenHelper.getInstance(this);
        listView=findViewById(R.id.lv);
        todoList= fetchTodoFromDB();
         adapter= new todoAdapter(todoList,this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent detailIntent = new Intent(MainActivity.this,TodoFormActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.ID_KEY,(int)id);
                bundle.putInt(Constants.POSITION_KEY,position);
                Log.d("Editpos",id+"");
                Log.d("Editkey",position+"");
                detailIntent.putExtras(bundle);
                startActivityForResult(detailIntent,Constants.EDIT_TODO_REQUEST);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position,final long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Delete Item?");
                builder.setMessage("Are you sure you want to delete this Item? You can not undo it.");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteDatabase database = openHelper.getWritableDatabase();
                        String[] ids = {id +""};
                        database.delete(Contract.todo.TABLE_NAME,Contract.todo.ID + " = ?",ids);
                        todoList.remove(position);
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
        getMenuInflater().inflate(R.menu.main_activity_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.add){
          Intent intent = new Intent(this,TodoFormActivity.class);
          startActivityForResult(intent,Constants.ADD_TODO_REQUEST);
          return  true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data != null){
            Bundle bundle = data.getExtras();
          if(requestCode== Constants.ADD_TODO_REQUEST){
            if(resultCode == Constants.SAVE_SUCCESS_RESULT){
                if(bundle != null) {
                    todo item = getTodoFromBundle(bundle);
                    SQLiteDatabase database = openHelper.getWritableDatabase();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(Contract.todo.TITLE,item.getName());
                    contentValues.put(Contract.todo.DESCRIPTION,item.getDesc());
                    long id = database.insert(Contract.todo.TABLE_NAME,null,contentValues);
                    item.setId((int) id);
                    todoList.add(item);
                    adapter.notifyDataSetChanged();
                }
            }
          }
          if(requestCode==Constants.EDIT_TODO_REQUEST){
              if(bundle != null) {
                  int position = bundle.getInt(Constants.POSITION_KEY, -1);
                  if (position >= 0) {
                      todo item = getTodoFromBundle(bundle);
                      Log.d("TAG",item.getDesc());
                      todoList.set(position,item);
                      adapter.notifyDataSetChanged();
                  }
              }
          }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private ArrayList<todo> fetchTodoFromDB() {
        ArrayList<todo> todoList = new ArrayList<>();
        SQLiteDatabase database = openHelper.getReadableDatabase();
        Cursor cursor = database.query(Contract.todo.TABLE_NAME,null,null,null,null,null,null);
        while (cursor.moveToNext()){
            int titleColumnIndex = cursor.getColumnIndex(Contract.todo.TITLE);
            String title = cursor.getString(titleColumnIndex);
            String desc = cursor.getString(cursor.getColumnIndex(Contract.todo.DESCRIPTION));
            int id = cursor.getInt(cursor.getColumnIndex(Contract.todo.ID));
            todo item = new todo(title,desc,id);
           todoList.add(item);
        }
        return  todoList;
    }
        private todo getTodoFromBundle(Bundle bundle){
            if(bundle != null){
                String title = bundle.getString(Constants.TITLE_KEY,"");
                String desc = bundle.getString(Constants.DESCRIPTION_KEY,"");
                return new todo(title,desc);

            }
            return null;
        }
}
