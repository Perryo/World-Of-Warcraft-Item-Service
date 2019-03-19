package com.services;
import com.models.Material;

/**
 * Created by James on 10/29/2016.
 */
public interface ItemService {
    public void getItemInfo(Material item);

    public Material getItemInfoByName(String name);

    public Material getItemInfoById(String id);
}

