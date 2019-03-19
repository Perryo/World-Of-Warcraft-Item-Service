package com.company;

/**
 * Created by James on 10/29/2016.
 */
public class Constants {
    public static final String API_KEY = "YOUR_KEY_HERE";
    public static final String SECRET = "YOUR_SECRET_HERE";

    //database
    public static final String DATABASE_NAME = "items";
    public enum QUERYID {FALSE,TRUE};
    public enum CREATE {TRUE,FALSE};

    //services
    public static final String ITEM_SERVICE_URI = "https://us.api.battle.net/wow/item/%1$s?locale=en_US&apikey=" + API_KEY;
    public static final String ITEM_SET_SERVICE_URI = "https://us.api.battle.net/wow/item/set/%1s?locale=en_US&apikey=" + API_KEY;
}
