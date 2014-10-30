package com.example.clazell.bestfriends;

/**
 * Created by nrobatmeily on 30/10/2014.
 */
public class Contact {
    private String name;
    private String number;
    private float percent;

    public Contact(String name, String number, float percent) {
        this.name = name;
        this.number = number;
        this.percent = percent;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public float getPercent() {
        return percent;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }

}
