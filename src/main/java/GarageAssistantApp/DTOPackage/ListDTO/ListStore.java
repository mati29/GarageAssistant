package main.java.GarageAssistantApp.DTOPackage.ListDTO;

import main.java.GarageAssistantApp.EntityPackage.Store;

import java.util.List;

/**
 * Created by Mati on 2017-03-25.
 */
public class ListStore {
    private List<Store> storeList;

    public List<Store> getStoreList() {
        return this.storeList;
    }

    public void setStoreList(List<Store> storeList) {
        this.storeList = storeList;
    }

    public ListStore() {}

    public ListStore(List<Store> storeList){
        this.storeList = storeList;
    }
}
