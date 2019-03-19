package com.models;

import org.ektorp.support.CouchDbDocument;

/**
 * Created by James on 10/29/2016.
 */
public class Material extends CouchDbDocument {
    public class JSON {
        public static final String itemIdField = "item_id";
        public static final String nameField = "name";
        public static final String worthField = "price";
    }
    public class DB{
        public static final String itemIdField = "itemId";
        public static final String nameField = "name";
        public static final String worthField = "worth";
    }

    private Money worth;
    private String name;
    private String itemId;
    private int qty;


    public Material(Money money, String name){
        this.worth = (money != null ? money : new Money(0,0,0));
        this.name = (!name.isEmpty() ? name : "NAME NOT FOUND");
    }

    public Material(){}

    public double getWorthInGoldWithQty() {return this.worth.getAmountInGold() * qty;}

    public Money getWorth(){return worth;}

    public void setWorth(Money worth) {this.worth = worth;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getItemId() {return itemId;}

    public void setItemId(String itemId) {this.itemId = itemId;}

    public int getQty() {return qty;}

    public void setQty(int qty) {this.qty = qty;}

}
