package CarPackage;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by Mati on 2016-10-23.
 */

@Entity
public class Car {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String numberPlate;
    private String brand;
    private String model;
    private short vintage;
    //next owner etc...
    //specify like engike, brakes bla, bla for mechanic

    public void setId(Long id){
        this.id = id;
    }

    public Long getId(){
        return this.id;
    }

    public void setNumberPlate(String numberPlate){
        this.numberPlate = numberPlate;
    }

    public String getNumberPlate(){
        return this.numberPlate;
    }

    public void setBrand(String brand){
        this.brand = brand;
    }

    public String getBrand(){
        return this.brand;
    }

    public void setModel(String model){
        this.model = model;
    }

    public String getModel(){
        return this.model;
    }

    public void setVintage(short vintage){
        this.vintage = vintage;
    }

    public short getVintage(){
        return this.vintage;
    }

    @Override
    public String toString() {
        return String.format(
                "Car[id=%d, numberPlate='%s', brand='%s', model='%s', vintage='%s']",
                id, numberPlate, brand, model, vintage);
    }

}
