package main.java.CarPackage;

import java.util.ArrayList;

/**
 * Created by Mati on 2016-12-23.
 */
public class ListPartRepair {
    private ArrayList<SinglePart> partRepair;

    public ArrayList<SinglePart> getPartRepair() {
        return partRepair;
    }

    public void setPartRepair(ArrayList<SinglePart> partRepair) {
        this.partRepair = partRepair;
    }

    public ListPartRepair() {}

    public ListPartRepair(ArrayList<SinglePart> partRepair) {
        this.partRepair = partRepair;
    }
}