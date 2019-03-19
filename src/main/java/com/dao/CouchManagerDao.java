package com.dao;

import org.ektorp.CouchDbConnector;

/**
 * Created by James on 11/3/2016.
 */
public interface CouchManagerDao {

     CouchDbConnector getConnector();
}
