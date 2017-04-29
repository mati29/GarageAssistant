package main.java.GarageAssistantApp.EntityPackage;

import main.java.GarageAssistantApp.EntityPackage.Account;

import javax.persistence.*;

/**
 * Created by Mati on 2017-03-21.
 */
@Entity(name="admin")
public class Admin {

    @Id
    //@GeneratedValue(strategy= GenerationType.AUTO)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="client_id_seq")
    @SequenceGenerator(name="client_id_seq", sequenceName="client_id_seq", allocationSize=1)//create new seq and persin
    private Long id;
    private String firstName;
    private String lastName;
    private int phoneNumber;
    private String email;
    private String post;

    @OneToOne(mappedBy = "admin",cascade=CascadeType.ALL)
    private Account account;

    public Admin(){
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

    public void setPost(String post){
        this.post = post;
    }

    public String getPost(){
        return this.post;
    }

    public void setAccount(Account account){
        this.account = account;
    }

    public Account getAccount(){
        return this.account;
    }
}
