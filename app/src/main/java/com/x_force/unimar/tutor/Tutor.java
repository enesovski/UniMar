package com.x_force.unimar.tutor;

import androidx.appcompat.app.AppCompatActivity;
import com.x_force.unimar.R;

public class Lesson extends AppCompatActivity {
    private String name;
    private String desc;
    private User user;
    private int cost;

    public Lesson(String name, String desc, User user, int cost) {
        this.setName(name);
        this.setDesc(desc);
        this.setUser(user);
        this.setCost(cost);
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

}
