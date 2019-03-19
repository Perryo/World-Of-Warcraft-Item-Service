package com.services;

import com.company.Constants;
import com.dao.MaterialDao;
import com.models.Material;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by James on 10/29/2016.
 */
@Service
public class ItemServiceImpl implements ItemService{
    @Autowired
    MaterialDao materialDao;

    @Override
    public void getItemInfo(Material item) {

    }
    @Override
    public Material getItemInfoByName(String name) {
        return materialDao.getMaterial(name, Constants.QUERYID.FALSE);
    }
    @Override
    public Material getItemInfoById(String id) {
       return materialDao.getMaterial(id, Constants.QUERYID.TRUE);
    }

    private String buildItemServiceURI(Material item){
        String URI = String.format(Constants.ITEM_SERVICE_URI,item.getId());
        return URI;
    }
}
