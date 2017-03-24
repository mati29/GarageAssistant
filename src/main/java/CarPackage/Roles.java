package main.java.CarPackage;

import javax.persistence.*;

/**
 * Created by Mati on 2016-11-09.
 */
@Entity(name="roles")
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="roles_id_seq")
    @SequenceGenerator(name="roles_id_seq", sequenceName="roles_id_seq", allocationSize=1)
    private Long id;
    private String username;
    private String role;

    Roles(String username,String role){
        this.username = username;
        this.role = role;
    }

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

    public void setRole(String role){
        this.role = role;
    }

    public String getRole(){
        return this.role;
    }

}
