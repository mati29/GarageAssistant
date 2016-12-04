package main.java.CarPackage;

import javax.persistence.*;

/**
 * Created by Mati on 2016-12-03.
 */
@Entity(name="repair")
public class Repair {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="client_id_seq")
    @SequenceGenerator(name="client_id_seq", sequenceName="client_id_seq", allocationSize=1)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
    @ManyToOne
    @JoinColumn(name = "commission_id")
    private Commission commission;

    public Long getId(){
        return this.id;
    }
    public void setId(Long id){
        this.id = id;
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
}
