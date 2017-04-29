package main.java.GarageAssistantApp.RepositoriesPackage;

import main.java.GarageAssistantApp.EntityPackage.Employee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Mati on 2016-12-03.
 */
@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Long> {

    List<Employee> findByPost(/*@Param("post")*/ String post);
    List<Employee> findAll();
}
