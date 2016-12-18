package main.java.CarPackage;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

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
    private String description;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToOne
    @JoinColumn(name = "car_id")
    private Car car;

    @OneToMany(mappedBy = "commission",cascade = CascadeType.ALL)
    private Set<Repair> repairSet;

    public Commission(){
    }
    public Commission(Client client,Car car,Date term,String description){
        this.client = client;
        this.car = car;
        this.term = term;
        this.description = description;
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

    public void setDescription(String description){
        this.description = description;
    }

    public String getDescription(){
        return this.description;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Set<Repair> getRepairSet() {
        return this.repairSet;
    }

    public void setRepairSet(Set<Repair> repairSet) {
        this.repairSet = repairSet;
    }

    public Car getCar() {
        return this.car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

}
