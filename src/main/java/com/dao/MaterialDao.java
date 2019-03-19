package com.dao;

import com.company.Constants;
import com.models.Material;

/**
 * Created by James on 11/3/2016.
 */
public interface MaterialDao {

    boolean createMaterial(Material material);

    boolean materialExists(Material material);

    Material getMaterial(String value, Constants.QUERYID isId);

    Material getMaterial(Material material);
}
