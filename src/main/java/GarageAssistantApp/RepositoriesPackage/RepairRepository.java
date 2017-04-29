package main.java.GarageAssistantApp.RepositoriesPackage;

import main.java.GarageAssistantApp.EntityPackage.Repair;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Mati on 2016-12-18.
 */
public interface RepairRepository extends CrudRepository<Repair, Long> {
    List<Repair> findByEmployeeId(Long id);
}
