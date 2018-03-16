package com.example.sagar.to_do;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CommentsFormActivity extends AppCompatActivity {
     EditText comment;
     Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments_form);
        comment = findViewById(R.id.comment);
        Intent intent = getIntent();
        bundle = intent.getExtras();
        if(bundle==null){
            bundle = new  Bundle();
        }
        else{
            if(bundle.getBoolean("isAlreadyPresent")) {
                String str = bundle.getString(Constants.COMMENT_KEY);
                comment.setText(str);
            }
        }
    }

    public void save(View view){
        String d = comment.getText().toString();

        if (isNullOrEmpty(d)) {
            Toast.makeText(this, "Comment can't be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        bundle.putString(Constants.COMMENT_KEY,d);

        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(Constants.SAVE_SUCCESS_RESULT,intent);
        finish();
    }
    private boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
}
