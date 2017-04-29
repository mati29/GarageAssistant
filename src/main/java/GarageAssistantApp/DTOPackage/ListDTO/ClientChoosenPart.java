package main.java.GarageAssistantApp.DTOPackage.ListDTO;

import main.java.GarageAssistantApp.DTOPackage.ChangePart;

import java.util.ArrayList;

/**
 * Created by Mati on 2017-02-04.
 */
public class ClientChoosenPart {
    public ArrayList<ChangePart> chosenPart;
    public void setChosenPart(ArrayList<ChangePart> chosenPart) {
        this.chosenPart = chosenPart;
    }
    public ArrayList<ChangePart> getChosenPart() {
        return chosenPart;
    }

    public ClientChoosenPart() {}
    public ClientChoosenPart(ArrayList<ChangePart> chosenPart){
        this.chosenPart = chosenPart;
    }
}
