package main.java.CarPackage;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

/**
 * Created by Mati on 2016-12-18.
 */
public interface RepairRepository extends CrudRepository<Repair, Long> {
    List<Repair> findByEmployeeId(Long id);
}
