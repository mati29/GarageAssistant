package main.java.CarPackage;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Mati on 2016-11-01.
 */
@Entity(name="commission")
public class Commission {
    @Id
    //@GeneratedValue(strategy= GenerationType.AUTO)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="commission_id_seq")
    @SequenceGenerator(name="commission_id_seq", sequenceName="commission_id_seq", allocationSize=1)
    private Long id;
    private Date term;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToOne
    @JoinColumn(name = "car_id")
    private Car car;

    public Commission(){
    }
    public Commission(Client client,Car car,Date term){
        this.client = client;
        this.car = car;
        this.term = term;
    }

    public void setId(Long id){
        this.id = id;
    }

    public Long getId(){
        return this.id;
    }

    public void setTerm(Date term){
        this.term = term;
    }

    public Date getTerm(){
        return this.term;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Car getCar() {
        return this.car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

}
