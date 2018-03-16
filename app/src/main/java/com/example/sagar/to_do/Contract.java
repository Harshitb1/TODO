package com.example.sagar.to_do;

/**
 * Created by SAGAR on 07-03-2018.
 */

public class Contract {
    public static final String DATABASE_NAME = "todo_db";
    public static final int VERSION = 1;

    public class todo{

        public static final String TABLE_NAME = "todo";
        public static final String ID = "id";
        public static final String TITLE = "title";
        public static final String DESCRIPTION = "desc";

    }

    static class Comments {

        public static final String TABLE_NAME = "comments";
        public static final String ID = "id";
        public static final String COMMENT = "comment";
        public static final String TODO_ID = "todo_id";


    }

}
