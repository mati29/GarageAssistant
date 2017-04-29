package main.java.GarageAssistantApp.RepositoriesPackage;

/**
 * Created by Mati on 2016-10-23.
 */
import main.java.GarageAssistantApp.EntityPackage.Car;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends CrudRepository<Car, Long>{

    List<Car> findByBrand(String brand);
    List<Car> findAll();

}
