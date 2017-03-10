package main.java.CarPackage;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by Mati on 2017-03-10.
 */
@Entity(name="bill")
public class Bill {

    @Id
    //@GeneratedValue(strategy= GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="bill_id_seq")
    @SequenceGenerator(name="bill_id_seq", sequenceName="bill_id_seq", allocationSize=1)
    private Long id;
    private int price;

    @OneToOne
    @JoinColumn(name = "commission_id")
    private Commission commission;

    public void setId(Long id){
        this.id = id;
    }

    public Long getId(){
        return this.id;
    }

    public void setPrice(int price){
        this.price = price;
    }

    public int getPrice(){
        return this.price;
    }

    public void setCommission(Commission commission){
        this.commission = commission;
    }

    public Commission getCommission(){
        return this.commission;
    }

}
