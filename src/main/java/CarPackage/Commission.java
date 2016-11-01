package main.java.CarPackage;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Mati on 2016-11-01.
 */
@Entity(name="commission")
public class Commission {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private Date term;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    public Commission(){
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

}
