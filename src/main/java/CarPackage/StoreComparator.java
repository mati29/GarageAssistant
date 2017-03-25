package main.java.CarPackage;

import java.util.Comparator;

/**
 * Created by Mati on 2017-03-25.
 */
public class StoreComparator implements Comparator<Store> {
    @Override
    public int compare(Store store1, Store store2) {
        int storeTypeVal1,storeTypeVal2;
        switch(store1.getType()){
            case "Engine": storeTypeVal1 = 1;break;
            case "Transmission": storeTypeVal1 = 2;break;
            case "Tires": storeTypeVal1 = 3;break;
            case "Body": storeTypeVal1 = 4;break;
            case "Lights": storeTypeVal1 = 5;break;
            case "Equipment": storeTypeVal1 = 6;break;
            case "Brakes": storeTypeVal1 = 7;break;
            default:storeTypeVal1=0;
        }
        switch(store2.getType()){
            case "Engine": storeTypeVal2 = 1;break;
            case "Transmission": storeTypeVal2 = 2;break;
            case "Tires": storeTypeVal2 = 3;break;
            case "Body": storeTypeVal2 = 4;break;
            case "Lights": storeTypeVal2 = 5;break;
            case "Equipment": storeTypeVal2 = 6;break;
            case "Brakes": storeTypeVal2 = 7;break;
            default:storeTypeVal2=0;
        }
        if(storeTypeVal1>storeTypeVal2)
            return 1;
        if(storeTypeVal1==storeTypeVal2)
            return 0;
        else
            return -1;
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }
}
