package com.example.sagar.to_do;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by SAGAR on 07-03-2018.
 */

public class todoAdapter extends BaseAdapter {
 ArrayList<todo> todoList;
 Context context;

    public todoAdapter(ArrayList<todo> todoList, Context context) {
        this.todoList = todoList;
        this.context = context;
    }

    @Override

    public int getCount() {
        return todoList.size();
    }

    @Override
    public Object getItem(int i) {
        return todoList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return todoList.get(i).getId();
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View output = convertView;
        if(output == null){
            output = inflater.inflate(R.layout.expense_row,parent,false);
            ExpenseViewHolder holder = new ExpenseViewHolder(output);
            output.setTag(holder);
        }
        ExpenseViewHolder holder = (ExpenseViewHolder) output.getTag();
        todo todoItem = (todo)getItem(i);
        holder.titleTextView.setText(todoItem.getName());
        holder.descTextView.setText(todoItem.getDesc());
        return output;
    }

    public void viewComments(View view){

    }

    class ExpenseViewHolder {

        TextView titleTextView;
        TextView descTextView;

        ExpenseViewHolder(View rowLayout) {
            titleTextView = rowLayout.findViewById(R.id.title);
            descTextView = rowLayout.findViewById(R.id.description);
        }
    }
}

