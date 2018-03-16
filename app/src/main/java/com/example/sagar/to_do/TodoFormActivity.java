package com.example.sagar.to_do;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class TodoFormActivity extends AppCompatActivity {
    EditText title, desc;
    Bundle bundle;
    boolean addMode=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_form);
        title = findViewById(R.id.title);
        desc = findViewById(R.id.desc);
        Intent intent = getIntent();
        bundle = intent.getExtras();
        if (bundle != null) {
            addMode=false;
            populateDataFromBundle();
        } else {
            bundle = new Bundle();
            addMode=true;
        }
        if(addMode){

        }
    }


    public void SaveButton(View view) {
        String t = title.getText().toString();
        String d = desc.getText().toString();

        if (isNullOrEmpty(t)) {
            Toast.makeText(this, "Title can't be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isNullOrEmpty(d)) {
            Toast.makeText(this, "Description can't be empty", Toast.LENGTH_SHORT).show();
            return;
        }


        bundle.putString(Constants.TITLE_KEY, t);
        bundle.putString(Constants.DESCRIPTION_KEY, d);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(Constants.SAVE_SUCCESS_RESULT, intent);
        finish();
    }

    private boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    private void populateDataFromBundle() {

        int id = bundle.getInt(Constants.ID_KEY, -1);
        if (id >= 0) {
            todoOpenHelper openHelper = todoOpenHelper.getInstance(this);
            SQLiteDatabase database = openHelper.getReadableDatabase();

            String[] selectionArgs = {id + ""};
            Cursor cursor = database.query(Contract.todo.TABLE_NAME, null, Contract.todo.ID + " = ?", selectionArgs, null, null, null);
            if (cursor.moveToFirst()) {
                int titleColumnIndex = cursor.getColumnIndex(Contract.todo.TITLE);
                String t = cursor.getString(titleColumnIndex);
                String d = cursor.getString(cursor.getColumnIndex(Contract.todo.DESCRIPTION));
                title.setText(t);
                desc.setText(d);
            }
        }
    }

        public boolean onCreateOptionsMenu (Menu menu){
            getMenuInflater().inflate(R.menu.form_activity_menu, menu);

            return true;
        }

        @Override
        public boolean onOptionsItemSelected (MenuItem item){
            if(addMode){
                Toast.makeText(this,"Can't add comments to empty item",Toast.LENGTH_SHORT).show();
                return false;
            }
            if (item.getItemId() == R.id.vc) {
                Intent intent = new Intent(this, CommentsActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
            return super.onOptionsItemSelected(item);


    }
}
