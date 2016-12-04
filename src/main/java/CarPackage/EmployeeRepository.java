package main.java.CarPackage;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * Created by Mati on 2016-12-03.
 */
@Repository
interface EmployeeRepository extends CrudRepository<Employee, Long> {
    //@Query(value = "select e.id,e.firstName,e.lastName,e.phoneNumber,e.email,e.post from employee e where e.post = :post",nativeQuery = true)
    Set<Employee> findByPost(/*@Param("post")*/ String post);
}
