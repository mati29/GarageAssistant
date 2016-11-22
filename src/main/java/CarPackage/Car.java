package main.java.CarPackage;

import javax.persistence.*;

/**
 * Created by Mati on 2016-10-23.
 */

@Entity(name="carslist")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="car_id_seq")
    @SequenceGenerator(name="car_id_seq", sequenceName="car_id_seq", allocationSize=1)
    private Long id;
    private String numberPlate;
    private String brand;
    private String model;
    private short vintage;

    @OneToOne(mappedBy = "car",cascade=CascadeType.ALL)
    private Commission commission;
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

    public void setCommission(Commission commission){
        this.commission = commission;
    }
    public Commission getCommision(){
        return this.commission;
    }

    @Override
    public String toString() {
        return String.format(
                "Car[id=%d, numberPlate='%s', brand='%s', model='%s', vintage='%s']",
                id, numberPlate, brand, model, vintage);
    }

}
