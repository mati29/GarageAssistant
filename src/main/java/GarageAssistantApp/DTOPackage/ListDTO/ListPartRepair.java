package main.java.GarageAssistantApp.DTOPackage.ListDTO;

import main.java.GarageAssistantApp.DTOPackage.SimpleDTO.SinglePart;

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