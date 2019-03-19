package com.models;

import java.util.List;

/**
 * Created by James on 11/9/2016.
 */
public class MaterialSet {
    public class JSON{
        public static final String idField = "id";
        public static final String nameField = "name";
        public static final String itemsField = "items";
    }

    public class DB extends JSON{
        public static final String idField = "setId";
        public static final String itemsField = "itemIds";
    }


    private String setId;
    private List<Integer> itemIds;
    private String name;

    public MaterialSet(){}

    public String getSetId() {
        return setId;
    }

    public void setSetId(String setId) {
        this.setId = setId;
    }

    public List<Integer> getItemIds() {
        return itemIds;
    }

    public void setItemIds(List<Integer> itemIds) {
        this.itemIds = itemIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
