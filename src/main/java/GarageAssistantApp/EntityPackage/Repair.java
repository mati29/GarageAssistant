package main.java.GarageAssistantApp.EntityPackage;

import main.java.GarageAssistantApp.EntityPackage.Commission;
import main.java.GarageAssistantApp.EntityPackage.Employee;
import main.java.GarageAssistantApp.EntityPackage.Image;
import main.java.GarageAssistantApp.EntityPackage.Part;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Mati on 2016-12-03.
 */
@Entity(name="repair")
public class Repair {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="repair_id_seq")
    @SequenceGenerator(name="repair_id_seq", sequenceName="repair_id_seq", allocationSize=1)
    private Long id;
    private String description;
    private boolean accomplish;
    private int hours;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
    @ManyToOne
    @JoinColumn(name = "commission_id")
    private Commission commission;

    @OneToMany(mappedBy = "repair", cascade = CascadeType.ALL)
    private List<Part> partList;

    @OneToMany(mappedBy = "repair",cascade = CascadeType.ALL)
    private List<Image> imageList;

    public Repair(){}

    public Repair(Employee employee,Commission commission,String description){
        this.employee = employee;
        this.commission = commission;
        this.description = description;
    }

    public int getHours(){
        return this.hours;
    }
    public void setHours(int hours){
        this.hours = hours;
    }
    public Long getId(){
        return this.id;
    }
    public void setId(Long id){
        this.id = id;
    }
    public boolean getAccomplish(){
        return this.accomplish;
    }
    public void setAccomplish(boolean accomplish){
        this.accomplish = accomplish;
    }
    public Employee getEmployee(){
        return this.employee;
    }
    public void setEmployee(Employee employee){
        this.employee = employee;
    }
    public Commission getCommission(){
        return this.commission;
    }
    public void setCommission(Commission commission){
        this.commission = commission;
    }
    public List<Part> getPartList(){
        return this.partList;
    }
    public void setPartList(List<Part> partList){
        this.partList = partList;
    }
    public void setDescription(String description){
        this.description = description;
    }
    public String getDescription(){
        return this.description;
    }
    public void setImageList(List<Image> imageList){
        this.imageList = imageList;
    }
    public List<Image> getImageList(){
        return this.imageList;
    }
}
