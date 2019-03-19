package com.models;

import org.ektorp.support.CouchDbDocument;

/**
 * Created by James on 10/29/2016.
 */
public class Money extends CouchDbDocument{
    public class JSON {
        public static final String goldField = "gold";
        public static final String silverField = "silver";
        public static final String copperField = "copper";
    }
    public class DB extends JSON{}

    private int gold;
    private int silver;
    private int copper;

    public Money(){
        this.gold = 0;
        this.silver = 0;
        this.copper = 0;
    }

    public Money(int gold, int silver, int copper){

        this.gold = gold;
        this.silver = silver;
        this.copper = copper;
    }

    public double getAmountInGold(){
        double total = gold;
        total += silver * .01;
        total += copper * .001;

        return total;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getSilver() {
        return silver;
    }

    public void setSilver(int silver) {
        this.silver = silver;
    }

    public int getCopper() {
        return copper;
    }

    public void setCopper(int copper) {
        this.copper = copper;
    }
}
