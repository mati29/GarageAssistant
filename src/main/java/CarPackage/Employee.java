package main.java.CarPackage;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * Created by Mati on 2016-12-03.
 */
@Entity(name="employee")
public class Employee {

    @Id
    //@GeneratedValue(strategy= GenerationType.AUTO)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="client_id_seq")
    @SequenceGenerator(name="client_id_seq", sequenceName="client_id_seq", allocationSize=1)//create new seq and persin
    private Long id;
    private String firstName;
    private String lastName;
    private int phoneNumber;
    private int salary;
    private String email;
    private String post;
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<Repair> repairList;

    @OneToOne(mappedBy = "employee",cascade=CascadeType.ALL)
    private Account account;

    public Employee(){
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

    public int setSalary(int salary){
        return this.salary = salary;
    }

    public int getSalary(){
        return this.salary;
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

    public List<Repair> getRepairList() {
        return repairList;
    }

    public void setRepairList(List<Repair> repairList) {
        this.repairList = repairList;
    }

    public Account getAccount(){
        return this.account;
    }

    public void setAccount(Account account){
        this.account=account;
    }


}
