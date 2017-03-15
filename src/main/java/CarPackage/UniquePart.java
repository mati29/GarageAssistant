package main.java.CarPackage;

/**
 * Created by Mati on 2017-03-14.
 */
public class UniquePart {
    int typeOfStore;
    int partId;
    String brand;
    String model;

    public UniquePart(){}

    public String getBrand(){
        return this.brand;
    }

    public void setBrand(String brand){
        this.brand = brand;
    }

    public String getModel(){
        return this.model;
    }

    public void setModel(String model){
        this.model= model;
    }

    public int getPartId(){
        return this.partId;
    }

    public void setPartId(int partId){
        this.partId = partId;
    }

    public int getTypeOfStore(){
        return this.typeOfStore;
    }

    public void setTypeOfStore(int typeOfStore){
        this.typeOfStore = typeOfStore;
    }
}
