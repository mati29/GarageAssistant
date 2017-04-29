package main.java.GarageAssistantApp.EntityPackage;

import javax.persistence.*;

/**
 * Created by Mati on 2016-11-05.
 */
@Entity(name="account")
public class Account {
    @Id
    //@GeneratedValue(strategy= GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="account_id_seq")
    @SequenceGenerator(name="account_id_seq", sequenceName="account_id_seq", allocationSize=1)
    private Long id;
    private String username;
    private String password;
    private boolean enabled;

    @OneToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @OneToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;

    public void setId(Long id){
        this.id = id;
    }

    public Long getId(){
        return this.id;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getUsername(){
        return this.username;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getPassword(){
        return this.password;
    }

    public void setClient(Client client){
        this.client = client;
    }

    public Client getClient(){
        return this.client;
    }

    public void setEmployee(Employee employee){
        this.employee = employee;
    }

    public Employee getEmployee(){
        return this.employee;
    }

    public void setAdmin(Admin admin){
        this.admin = admin;
    }

    public Admin getAdmin(){
        return this.admin;
    }

    public void setEnabled(boolean enabled){
        this.enabled = enabled;
    }

    public boolean getEnabled(){
        return this.enabled;
    }
}
