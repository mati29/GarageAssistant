package main.java.CarPackage;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Mati on 2016-11-01.
 */
@Entity(name="client")
public class Client {
    @Id
    //@GeneratedValue(strategy= GenerationType.AUTO)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="client_id_seq")
    @SequenceGenerator(name="client_id_seq", sequenceName="client_id_seq", allocationSize=1)
    private Long id;
    private String firstName;
    private String lastName;
    private int phoneNumber;
    private String email;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Commission> commissionList;

    public Client(){
    }

    public void setId(Long id){
        this.id = id;
    }

    public Long getId(){
        return this.id;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public String getFirstName(){
        return this.firstName;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    public String getLastName(){
        return this.lastName;
    }

    public int setPhoneNumber(int phoneNumber){
        return this.phoneNumber = phoneNumber;
    }

    public int getPhoneNumber(){
        return this.phoneNumber;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getEmail(){
        return this.email;
    }

    public List<Commission> getCommissionList() {
        return commissionList;
    }

    public void setCommissionList(List<Commission> commissionList) {
        this.commissionList = commissionList;
    }

    public void setAccount(Account account){
        this.account = account;
    }

    public Account getAccount(){
        return this.account;
    }
}
