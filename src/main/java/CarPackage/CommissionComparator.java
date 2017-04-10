package main.java.CarPackage;

import java.util.Comparator;

/**
 * Created by Mati on 2017-04-10.
 */
public class CommissionComparator implements Comparator<Commission> {
    @Override
    public int compare(Commission commission1, Commission commission2) {

        if(commission1.getId()>commission2.getId())
            return 1;
        if(commission1.getId()==commission2.getId())
            return 0;
        else
            return -1;
    }

}
