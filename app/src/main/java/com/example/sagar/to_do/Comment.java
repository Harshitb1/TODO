package com.example.sagar.to_do;

import java.util.Random;


public class Comment {

    private int id;
    private String comment;
    private int expenseId;

    public Comment(int id, String comment, int expenseId) {
        this.id = id;
        this.comment = comment;
        this.expenseId = expenseId;
    }

    public Comment(String comment, int expenseId) {
        this.comment = comment;
        this.expenseId = expenseId;
        this.id = -1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(int expenseId) {
        this.expenseId = expenseId;
    }

    static Comment getDummyComment(int expenseId){
        Random random = new Random();
        int randomInt = random.nextInt();
        return new Comment( expenseId + ".) Comment " + randomInt,expenseId);

    }
}
