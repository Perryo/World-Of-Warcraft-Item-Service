package com.company;

import com.dao.MaterialDao;
import com.models.Material;
import com.models.Money;
import com.services.ItemPopulatorService;
import com.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.HashMap;
public class Main {
    @Autowired
    ItemService itemService;

    @Autowired
    ItemPopulatorService itemPopulatorService;

    @Autowired
    MaterialDao matDao;

    public Main(){
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        AutowireCapableBeanFactory acbFactory = context.getAutowireCapableBeanFactory();
        acbFactory.autowireBean(this);
    }
    public static void main(String[] args) {
        boolean shouldUpdate = false;
        Main m = new Main();
        if(shouldUpdate) {
            m.updateData();
        }
        m.start();
        HashMap<String, Material> materialsMap = new HashMap<String,Material>();
    }

    private void start(){
        Material mat = new Material(new Money(), "Nightborne Delicacy Platter");
        mat = matDao.getMaterial(mat);
        System.out.println(mat.getItemId());
    }

    private void createData(){
        itemPopulatorService.createDatabase();
    }
    private void updateData(){
        itemPopulatorService.updateDatabase();
    }

}
