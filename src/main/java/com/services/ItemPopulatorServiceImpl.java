package com.services;

import com.company.Constants;

import com.dao.MaterialDao;
import com.models.Material;
import com.models.MaterialSet;
import com.models.Money;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;


/**
 * Created by James on 11/3/2016.
 */
@Service
public class ItemPopulatorServiceImpl implements ItemPopulatorService {
    @Autowired
    MaterialDao materialDao;

    final static Logger logger = Logger.getLogger(ItemPopulatorServiceImpl.class);
    final String idField = "id";
    final enum TYPE{ITEM,SET};

    public void updateItemsDatabase(){
        updateItemsDatabase(Constants.CREATE.FALSE, TYPE.ITEM);
    }
    public void createItemsDatabase(){
        updateItemsDatabase(Constants.CREATE.TRUE, TYPE.ITEM);
    }
    public void updateItemSetDatabase(){
        updateItemsDatabase(Constants.CREATE.FALSE,TYPE.SET);
    }
    public void createItemSetDatabase(){
        updateItemsDatabase(Constants.CREATE.TRUE,TYPE.SET);
    }
    /**
     * Check the current database and builds a list of valid item ids which are run against the wow api. The response from the wow api is parsed and stored in the database.
     * @return void
     */
    private void updateItemsDatabase(Constants.CREATE create, TYPE type){
        HttpClient httpClient = HttpClientBuilder.create().build();
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(6);
        for (int id = 0; id < 50000; id++) {
            Runnable worker = new workerThread(id, httpClient, create, type);
            final Future handler = executor.submit(worker);
            executor.schedule(new Runnable(){
                public void run(){
                    handler.cancel(true);
                }
            }, 5000, TimeUnit.MILLISECONDS);

        }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        logger.debug("Finished all threads.");

    }

    /**
     * Parses a response body from the wow api into a Material object.
     * @param body
     * @return Material - the object parsed from the response body.
     */
    public Material parseItemServiceJson(String body){
        Material material = new Material();
        try {
            JSONObject obj = new JSONObject(body);
            material.setName(obj.getString(Material.JSON.nameField));
            material.setItemId(obj.getString(Material.JSON.itemIdField));
            material.setWorth(new Money());

        }catch(JSONException ex){
            logger.error("Could not parse material from JSON: " + ex.getMessage());
        }
        return material;
    }

    /**
     * Parses a response body from the wow api into a MaterialSet object.
     * @param body
     * @return MaterialSet - the object parsed from the response body.
     */
    public MaterialSet parseItemSetServiceJson(String body){
        MaterialSet materialSet = new MaterialSet();
        try {
            JSONObject obj = new JSONObject(body);
            materialSet.setName(obj.getString(MaterialSet.JSON.nameField));
            materialSet.setSetId(obj.getString(MaterialSet.JSON.idField));
            JSONArray items = obj.getJSONArray(MaterialSet.JSON.itemsField));
            List<Integer> itemsArray = new ArrayList<Integer>();
            for(int i = 0; i < items.length(); i++)
                itemsArray.add(new Integer(items.get(i).toString()));
            materialSet.setItemIds(itemsArray);


        }catch(JSONException ex){
            logger.error("Could not parse materialSet from JSON: " + ex.getMessage());
        }
        return materialSet;
    }

    private String getServiceURI(TYPE type){
        switch(type){
            case ITEM:
                return Constants.ITEM_SERVICE_URI;
            case SET:
                return Constants.ITEM_SET_SERVICE_URI;
            default:
                return "";
        }
    }

    private Object parse(TYPE type, String body){
        switch(type){
            case ITEM:
                return parseItemServiceJson(body);
            case SET:
                return parseItemSetServiceJson(body);
            default:
                return null;
        }
    }

    /**
     * Runnable class to make restcalls and handle the response from wow api. Should not have any read transactions.
     */
    private class workerThread implements Runnable{
        int id;
        HttpClient httpClient;
        Constants.CREATE create;
        TYPE type;
        public workerThread(int id, HttpClient httpClient, Constants.CREATE create, TYPE type){
            this.id = id;
            this.httpClient = httpClient;
            this.create = create;
            this.type = type;
        }
        public void run(){
                HttpResponse response;
                ResponseHandler<String> handler = new BasicResponseHandler();
                String URI = String.format(getServiceURI(type), id);
                HttpGet getRequest = new HttpGet(URI);
                getRequest.addHeader("accept", "application/json");
                try {
                    logger.debug("Querying for id: " + id);
                    response = httpClient.execute(getRequest);
                    if (response.getStatusLine().getStatusCode() != 200) {
                        logger.debug("id: " + id + " does not exist.");
                        EntityUtils.consume(response.getEntity());
                        return;
                    }
                    else{
                        //add method to perform appropriate action based on type and create
                        Material material = (Material)parse(type,handler.handleResponse(response));
                        logger.debug("Creating item with itemId: " + id);
                        if(create == Constants.CREATE.TRUE)
                            materialDao.createMaterial(material);
                        else

                        EntityUtils.consume(response.getEntity());
                        return;
                    }
                }
                catch(Exception ex){
                    logger.error("Could not get itemId data for itemId: " + id + ". Reason: " + ex.getMessage());
                }
        }
    }
}
