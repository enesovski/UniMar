package com.x_force.unimar.Item;

import java.util.ArrayList;

public class Tutoring extends Item{
    public Tutoring(String name, String desc, String UserName, ArrayList<String> category, int cost) { //user olması lazım
        name = name.toUpperCase();
        this.setName(name);
        this.setDesc(desc);
        this.setCost(cost);
        this.setCategory(category);
        this.setUserName(UserName);
        ItemManager.addToList(this);
    }

    public Tutoring(){}
}