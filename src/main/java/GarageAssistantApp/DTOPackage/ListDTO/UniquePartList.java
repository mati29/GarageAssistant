package main.java.GarageAssistantApp.DTOPackage.ListDTO;

import main.java.GarageAssistantApp.DTOPackage.SimpleDTO.UniquePart;

import java.util.ArrayList;

/**
 * Created by Mati on 2017-03-14.
 */
public class UniquePartList {
    private ArrayList<UniquePart> uniqueParts;

    public ArrayList<UniquePart> getUniqueParts() {
        return uniqueParts;
    }

    public void setUniqueParts(ArrayList<UniquePart> uniqueParts) {
        this.uniqueParts = uniqueParts;
    }

    public UniquePartList() {}

    public UniquePartList(ArrayList<UniquePart> uniqueParts){
        this.uniqueParts = uniqueParts;
    }
}
