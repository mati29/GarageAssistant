package main.java.CarPackage;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by Mati on 2016-12-03.
 */
@Entity(name="employee")
@Inheritance(strategy= InheritanceType.TABLE_PER_CLASS)
public class Employee extends Client{

    String position;
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private Set<Repair> repairSet;

    public Employee(){
    }

    public void setPosition(String position){
        this.position = position;
    }

    public String getPosition(){
        return this.position;
    }

    public Set<Repair> getRepairSet() {
        return repairSet;
    }

    public void setRepairSet(Set<Repair> repairSet) {
        this.repairSet = repairSet;
    }


}
