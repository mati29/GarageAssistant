package main.java.CarPackage;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by Mati on 2016-12-23.
 */
@Entity(name="store")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="store_id_seq")
    @SequenceGenerator(name="store_id_seq", sequenceName="store_id_seq", allocationSize=1)

    private Long id;
    private String type;
    private String model;
    private String brand;
    int amount;
    double price;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private Set<Part> partSet;

    @OneToMany(mappedBy = "store",cascade=CascadeType.ALL)
    private Set<Image> imageSet;

    public Set<Part> getPartSet(){
        return this.partSet;
    }
    public void setPartSet(Set<Part> partSet){
        this.partSet = partSet;
    }
    public void setId(Long Id){
        this.id = id;
    }
    public Long getId(){
        return this.id;
    }
    public void setType(String type){
        this.type = type;
    }
    public String getType(){
        return this.type;
    }
    public void setModel(String model){
        this.model = model;
    }
    public String getModel(){
        return this.model;
    }
    public void setBrand(String brand){
        this.brand = brand;
    }
    public String getBrand(){
        return this.brand;
    }
    public void setAmount(int amount){
        this.amount = amount;
    }
    public int getAmount(){
        return this.amount;
    }
    public void setPrice(double price){
        this.price = price;
    }
    public double getPrice(){
        return this.price;
    }
    public void setImageSet(Set<Image> imageSet){
        this.imageSet = imageSet;
    }
    public Set<Image> getImageSet(){
        return this.imageSet;
    }
}
