package main.java.CarPackage;

import javax.persistence.*;

/**
 * Created by Mati on 2017-03-26.
 */
@Entity(name="Image")
public class Image {

    @Id
    //@GeneratedValue(strategy= GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="image_id_seq")
    @SequenceGenerator(name="image_id_seq", sequenceName="image_id_seq", allocationSize=1)
    private Long id;
    private String path;

    @ManyToOne
    @JoinColumn(name = "repair_id")
    private Repair repair;

    @JoinColumn(name = "store_id")
    private Store store;//what type of part

    public Image(){

    }

    public Image(Repair repair,Store store){
        this.repair = repair;
        this.store = store;
    }

    public void setId(Long id){
        this.id = id;
    }

    public Long getId(){
        return this.id;
    }

    public void setPath(String path){
        this.path = path;
    }

    public String getPath(){
        return this.path;
    }

    public void setRepair(Repair repair){
        this.repair= repair;
    }

    public Repair getRepair(){
        return this.repair;
    }

    public void setStore(Store store){
        this.store = store;
    }

    public Store getStore(){
        return this.store;
    }


}
