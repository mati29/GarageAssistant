package main.java.CarPackage;

import javax.persistence.*;

/**
 * Created by Mati on 2016-12-23.
 */
@Entity(name="part")
public class Part {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="part_id_seq")
    @SequenceGenerator(name="part_id_seq", sequenceName="part_id_seq", allocationSize=1)

    private Long id;

    @ManyToOne
    @JoinColumn(name = "repair_id")
    private Repair repair;
    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    public Part(){}
    public Part(Repair repair,Store store){
        this.repair = repair;
        this.store = store;
    }

    public void setId(Long id){
        this.id = id;
    }

    public Long getId(){
        return this.id;
    }

    public Repair getRepair(){
        return this.repair;
    }
    public void setRepair(Repair repair){
        this.repair = repair;
    }

    public Store getStore(){
        return this.store;
    }
    public void setStore(Store store){
        this.store = store;
    }
}
