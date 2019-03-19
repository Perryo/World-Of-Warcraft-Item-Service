package com.dao;

import com.company.Constants;
import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.net.MalformedURLException;


/**
 * Created by James on 11/3/2016.
 */
@Repository
public class CouchManagerDaoImpl implements CouchManagerDao {
    final static Logger logger = Logger.getLogger(CouchManagerDaoImpl.class);
    private static CouchDbConnector db;

    public CouchDbConnector getConnector() {
        if(db == null)
            createConnectorInstance();

        return db;
    }

    private void createConnectorInstance(){
        try {
            HttpClient httpClient = new StdHttpClient.Builder()
                    .url("http://127.0.0.1:5984")
                    .build();

            CouchDbInstance dbInstance = new StdCouchDbInstance(httpClient);
            db = new StdCouchDbConnector(Constants.DATABASE_NAME, dbInstance);
        }catch(MalformedURLException MalformedUrlEx){
            logger.error("Failed to get couch db instance: " + MalformedUrlEx.getMessage());
        }

    }
}
