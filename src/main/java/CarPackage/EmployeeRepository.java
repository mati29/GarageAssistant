package main.java.CarPackage;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * Created by Mati on 2016-12-03.
 */
@Repository
interface EmployeeRepository extends CrudRepository<Employee, Long> {
    Set<Employee> findByPosition(String position);
}
