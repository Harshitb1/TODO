package com.example.sagar.to_do;

/**
 * Created by SAGAR on 07-03-2018.
 */

public class todo {
  String title;
  String desc;
  int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public todo(String name, String desc,int id) {
        this.id=id;
        this.title= name;
        this.desc = desc;
    }
    public todo(String name, String desc) {
        this.title= name;
        this.desc = desc;
    }
    public String getName() {
        return title;
    }

    public void setName(String name) {
        this.title = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
