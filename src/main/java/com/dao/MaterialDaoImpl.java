package com.dao;

import com.company.Constants;
import com.models.Material;
import com.models.Money;
import org.apache.log4j.Logger;
import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.ViewResult;
import org.ektorp.ViewResult.Row;
import org.json.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.List;

/**
 * Created by James on 11/3/2016.
 */
@Component
public class MaterialDaoImpl implements MaterialDao{
    final static Logger logger = Logger.getLogger(MaterialDaoImpl.class);

    @Autowired
    CouchManagerDao couchManagerDao;

    private final String designDoc = "_design/valueDoc";
    private final String viewName = "by_name";

    public boolean createMaterial(Material material) {
        boolean success = false;
        CouchDbConnector connector = couchManagerDao.getConnector();
        connector.create(material);
        String id = material.getId();
        if(id != null && !id.equals(""))
            success = false;
        return success;
    }

    public boolean materialExists(Material material){
        boolean exists = false;
        Material matInDB = getMaterial(material.getName(), Constants.QUERYID.FALSE);
        if(matInDB != null && matInDB.getName() != null && !matInDB.getName().isEmpty())
            exists = true;
        return exists;
    }

    public Material getMaterial(String value, Constants.QUERYID isId){
        CouchDbConnector connector = couchManagerDao.getConnector();
        if(isId == Constants.QUERYID.TRUE){
            Material material = connector.find(Material.class, value);
            return material;
        }
        else {
            Material material = new Material();
            ViewQuery view = new ViewQuery();
            view.designDocId(designDoc);
            view.viewName(viewName);
            view.key(value);

            ViewResult result = connector.queryView(view);
            List<Row> rows = result.getRows();
            for (Row row : rows) {
                material = parseMaterial(row.getValue());
            }

            return material;
        }
    }
    public Material getMaterial(Material material){
        Material mat = getMaterial(material.getName(),Constants.QUERYID.FALSE);

        return mat;
    }


    private Material parseMaterial(String doc){
        Material material = new Material();
        try {
            JSONObject obj = new JSONObject(doc);
            material.setName(obj.getString(Material.DB.nameField));
            material.setItemId(obj.getString(Material.DB.itemIdField));
            material.setWorth(parseMoney(obj.getString(Material.DB.worthField)));
        }catch(JSONException ex){
            logger.error("Could not parse material from JSON: " + ex.getMessage());
        }

        return material;
    }
    private Money parseMoney(String doc){
        Money money = new Money();
        try {
            JSONObject obj = new JSONObject(doc);
            money.setGold(Integer.parseInt(obj.getString(Money.DB.goldField)));
            money.setSilver(Integer.parseInt(obj.getString(Money.DB.silverField)));
            money.setCopper(Integer.parseInt(obj.getString(Money.DB.copperField)));

        }catch(JSONException ex){
            logger.error("Could not parse money from JSON: " + ex.getMessage());
        }
        return money;
    }




}
