package com.example.hp.gotoevent.bean;

/**
 * Created by hp on 18/12/2018.
 */

public class Role {

    private int type;
    private String userID;

    public Role() {
    }

    public Role(int type, String userID) {
        this.type = type;
        this.userID = userID;
    }

    @Override
    public String toString() {
        return "Role{" +
                ", type=" + type +
                '}';
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
