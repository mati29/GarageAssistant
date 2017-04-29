package main.java.GarageAssistantApp.RepositoriesPackage;

import main.java.GarageAssistantApp.EntityPackage.Bill;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Mati on 2017-04-02.
 */
@Repository
public interface BillRepository extends CrudRepository<Bill, Long> {
}
